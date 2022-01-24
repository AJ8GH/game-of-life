package aj8gh.gameoflife.ui;

import aj8gh.gameoflife.application.Game;

import java.util.function.Consumer;

public interface UI extends Consumer<Game> {
    @Override
    void accept(Game game);
}
