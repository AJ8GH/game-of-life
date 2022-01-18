package org.jonasa.gameoflife.ui;

import org.jonasa.gameoflife.application.Game;

import java.util.function.Consumer;

public interface UI extends Consumer<Game> {
    @Override
    void accept(Game game);
}
