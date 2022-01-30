package aj8gh.gameoflife.consumer;

import aj8gh.gameoflife.application.Game;

import java.util.function.Consumer;

public interface UiConsumer extends Consumer<Game> {
    @Override
    void accept(Game game);
}
