package org.jonasa.gameoflife.ui;

import lombok.RequiredArgsConstructor;
import org.jonasa.gameoflife.apiclient.ApiClient;
import org.jonasa.gameoflife.apiclient.domain.Cell;
import org.jonasa.gameoflife.apiclient.domain.GameState;
import org.jonasa.gameoflife.application.Game;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Web implements UI {
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
