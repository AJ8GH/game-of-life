package org.jonasa.ui;

import org.jonasa.application.Game;

import java.util.function.Consumer;

public interface UI extends Consumer<Game> {
    @Override
    void accept(Game game);
}
