package aj8gh.gameoflife.application;

import aj8gh.gameoflife.domain.Grid;
import aj8gh.gameoflife.seeder.Seeder;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static java.lang.Thread.sleep;

@AllArgsConstructor
@Setter
public class Game {
    private static final Logger LOG = LogManager.getLogger(Game.class.getName());

    private final Executor executor = Executors.newFixedThreadPool(6);
    private final AtomicInteger generation = new AtomicInteger(0);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Consumer<Game> consumerAdaptor;
    private final Grid grid;

    private Seeder seeder;
    private int tickDuration;

    public void run() {
        if (!running.get()) {
            this.running.getAndSet(true);
            LOG.info("*** Game Started ***");
        } else {
            throw new IllegalStateException("Game already running");
        }
        executor.execute(() -> {
            while (running.get()) {
                consumerAdaptor.accept(this);
                if (extinct()) {
                    running.getAndSet(false);
                    break;
                }
                tick();
            }
        });
    }

    public void stop() {
        if (running.get()) {
            this.running.getAndSet(false);
            LOG.info("*** Game Stopped ***");
            return;
        }
        throw new IllegalStateException("Game already stopped");
    }

    private void tick() {
        grid.tick();
        generation.incrementAndGet();
        try {
            sleep(tickDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        synchronized (this) {
            if (isRunning()) stop();
        }
        grid.setGrid(seeder.seed());
        generation.getAndSet(0);
        LOG.info("*** Game Reset ***");
    }

    public void setTickDuration(int tickDuration) {
        if (tickDuration < 0) {
            throw new IllegalArgumentException("Tick Duration must be >= 0");
        }
        this.tickDuration = tickDuration;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getGeneration() {
        return generation.get();
    }

    public boolean isRunning() {
        return running.get();
    }

    public long population() {
        return grid.population();
    }

    private boolean extinct() {
        return population() == 0;
    }
}
