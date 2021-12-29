package org.jonasa.ui;

import lombok.RequiredArgsConstructor;
import org.jonasa.apiclient.ApiClient;
import org.jonasa.apiclient.domain.Cell;
import org.jonasa.apiclient.domain.GameState;
import org.jonasa.application.Game;

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
                .generation(game.getGeneration().get())
                .grid(grid)
                .build();
    }
}
