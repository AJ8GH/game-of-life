package aj8gh.gameoflife.consumer;

import aj8gh.gameoflife.application.Game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ConsumerAdaptor implements Consumer<Game> {
    private final Set<Consumer<Game>> consumers;

    @SafeVarargs
    public ConsumerAdaptor(Consumer<Game>... consumers) {
        this.consumers = new HashSet<>(Arrays.asList(consumers));
    }

    @Override
    public void accept(Game game) {
        consumers.forEach(consumer -> consumer.accept(game));
    }
}
