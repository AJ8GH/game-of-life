package io.github.aj8gh.gameoflife.consumer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import io.github.aj8gh.gameoflife.api.client.ApiClient;
import io.github.aj8gh.gameoflife.api.client.domain.GameState;
import io.github.aj8gh.gameoflife.application.Game;
import io.github.aj8gh.gameoflife.domain.Cell;
import io.github.aj8gh.gameoflife.domain.Grid;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class ApiConsumerTest {

  @Mock
  ApiClient apiClient;
  @Mock
  Game game;
  @Mock
  Grid grid;
  @Mock
  Cell deadCell;
  @Mock
  Cell liveCell;

  @Test
  void accept() {
    openMocks(this);
    when(liveCell.isAlive()).thenReturn(true);
    when(deadCell.isAlive()).thenReturn(false);
    when(grid.getCells()).thenReturn(List.of(
        List.of(liveCell),
        List.of(deadCell),
        List.of(liveCell)
    ));
    when(game.getGrid()).thenReturn(grid);

    Consumer<Game> apiConsumer = new ApiConsumer(apiClient);
    apiConsumer.accept(game);

    GameState expectedArgument = convert(game);
    verify(apiClient).enqueue(expectedArgument);
  }

  private GameState convert(Game game) {
    List<List<io.github.aj8gh.gameoflife.api.client.domain.Cell>> grid = game.getGrid()
        .getCells()
        .stream()
        .map(row -> row.stream()
            .map(cell -> new io.github.aj8gh.gameoflife.api.client.domain.Cell(cell.isAlive()))
            .collect(Collectors.toList()))
        .collect(Collectors.toList());

    return GameState.builder()
        .population(game.population())
        .generation(game.getGeneration())
        .grid(grid)
        .build();
  }
}
