package aj8gh.gameoflife.consumer;

import aj8gh.gameoflife.application.Game;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

@AllArgsConstructor
public class ConsumerAdaptor implements Consumer<Game> {
    private final Set<Consumer<Game>> consumers;

    @Override
    public void accept(Game game) {
        consumers.forEach(consumer -> consumer.accept(game));
    }

    public void add(Consumer<Game> consumer) {
        consumers.add(consumer);
    }

    public void clear() {
        consumers.clear();
    }

    public Set<Consumer<Game>> getConsumers() {
        return Collections.unmodifiableSet(consumers);
    }
}
