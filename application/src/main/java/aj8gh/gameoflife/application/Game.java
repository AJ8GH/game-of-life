package aj8gh.gameoflife.application;

import aj8gh.gameoflife.domain.Cell;
import aj8gh.gameoflife.domain.Grid;
import aj8gh.gameoflife.ui.UI;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

@RequiredArgsConstructor
public class Game {
    private static final Logger LOG = LogManager.getLogger(Game.class.getName());

    private final UI ui;
    private final Grid grid;
    private final int tickDuration;

    private final Executor executor = Executors.newFixedThreadPool(6);
    private final AtomicInteger generation = new AtomicInteger(0);
    private final AtomicBoolean running = new AtomicBoolean(false);

    public void run() {
        if (!running.get()) {
            this.running.getAndSet(true);
            LOG.info("*** Game Started ***");
        } else {
            throw new IllegalStateException("Game already running");
        }
        executor.execute(() -> {
            while (running.get()) {
                ui.accept(this);
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
        } else {
            throw new IllegalStateException("Game already stopped");
        }
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

    public void reset(List<List<Cell>> seed) {
        if (isRunning()) {
            stop();
        }
        grid.setGrid(seed);
        LOG.info("*** Game Reset ***");
    }

    public Grid getGrid() {
        return grid;
    }

    public int getGeneration() {
        return generation.get();
    }

    public long population() {
        return grid.population();
    }

    public boolean isRunning() {
        return running.get();
    }

    private boolean extinct() {
        return population() == 0;
    }
}
