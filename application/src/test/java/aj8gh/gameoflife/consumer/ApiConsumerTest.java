package aj8gh.gameoflife.consumer;

import aj8gh.gameoflife.apiclient.ApiClient;
import aj8gh.gameoflife.apiclient.domain.GameState;
import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.domain.Cell;
import aj8gh.gameoflife.domain.Grid;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

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
        when(grid.getGrid()).thenReturn(List.of(
                List.of(liveCell),
                List.of(deadCell),
                List.of(liveCell)
        ));
        when(game.getGrid()).thenReturn(grid);

        UiConsumer uiConsumer = new ApiConsumer(apiClient);
        uiConsumer.accept(game);

        GameState expectedArgument = convert(game);
        verify(apiClient).enqueue(expectedArgument);
    }

    private GameState convert(Game game) {
        List<List<aj8gh.gameoflife.apiclient.domain.Cell>> grid = game.getGrid()
                .getGrid()
                .stream()
                .map(row -> row.stream()
                        .map(cell -> new aj8gh.gameoflife.apiclient.domain.Cell(cell.isAlive()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

     return GameState.builder()
                .population(game.population())
                .generation(game.getGeneration())
                .grid(grid)
                .build();
    }
}
