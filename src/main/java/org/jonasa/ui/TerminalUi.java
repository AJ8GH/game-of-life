package org.jonasa.ui;

import org.jonasa.application.Game;

public class TerminalUi implements UI {
    @Override
    public void accept(Game game) {
        System.out.println(game.getGrid().toString());
    }
}
