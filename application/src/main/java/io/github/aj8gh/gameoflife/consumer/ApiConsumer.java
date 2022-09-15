package io.github.aj8gh.gameoflife.consumer;

import io.github.aj8gh.gameoflife.api.client.ApiClient;
import io.github.aj8gh.gameoflife.api.client.domain.Cell;
import io.github.aj8gh.gameoflife.api.client.domain.GameState;
import io.github.aj8gh.gameoflife.application.Game;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiConsumer implements Consumer<Game> {

  private final ApiClient client;

  @Override
  public void accept(Game game) {
    GameState gameState = convert(game);
    client.enqueue(gameState);
  }

  private GameState convert(Game game) {
    List<List<Cell>> grid = game.getGrid().getCells()
        .stream()
        .map(row -> row.stream().map(cell -> new Cell(cell.isAlive())).toList())
        .toList();

    return GameState.builder()
        .population(game.population())
        .generation(game.getGeneration())
        .grid(grid)
        .build();
  }
}
