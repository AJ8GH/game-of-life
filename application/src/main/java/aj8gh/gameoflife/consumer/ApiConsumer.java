package aj8gh.gameoflife.consumer;

import lombok.RequiredArgsConstructor;
import aj8gh.gameoflife.apiclient.ApiClient;
import aj8gh.gameoflife.apiclient.domain.Cell;
import aj8gh.gameoflife.apiclient.domain.GameState;
import aj8gh.gameoflife.application.Game;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ApiConsumer implements UiConsumer {
    private final ApiClient client;

    public void accept(Game game) {
        GameState gameState = convert(game);
        client.enqueue(gameState);
    }

    private GameState convert(Game game) {
        List<List<Cell>> grid = game.getGrid().getGrid()
                .stream()
                .map(row -> row.stream().map(cell -> new Cell(cell.isAlive()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        return GameState.builder()
                .population(game.population())
                .generation(game.getGeneration())
                .grid(grid)
                .build();
    }
}
