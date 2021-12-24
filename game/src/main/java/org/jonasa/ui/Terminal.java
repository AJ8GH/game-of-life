package org.jonasa.ui;

import lombok.AllArgsConstructor;
import org.jonasa.application.Game;

@AllArgsConstructor
public class Terminal implements UI {
    private static final String LOG_MESSAGE = "Generation: %s, Population: %s\n";
    private boolean info;

    @Override
    public void accept(Game game) {
        System.out.println(game.getGrid());
        if (info) {
            System.out.printf(LOG_MESSAGE, game.getGeneration(), game.population());
        }
    }
}
