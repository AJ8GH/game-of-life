package org.jonasa.gameoflife.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jonasa.gameoflife.domain.Cell;
import org.jonasa.gameoflife.domain.Grid;
import org.jonasa.gameoflife.ui.UI;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

@Slf4j
@RequiredArgsConstructor
public class Game {

    private final UI ui;
    private final Grid grid;
    private final int tickDuration;

    private final Executor executor = Executors.newFixedThreadPool(6);
    private final AtomicInteger generation = new AtomicInteger(0);
    private final AtomicBoolean running = new AtomicBoolean(false);

    public void run() {
        if (!running.get()) {
            this.running.getAndSet(true);
            log.info("*** Game Started ***");
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
            log.info("*** Game Stopped ***");
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
        log.info("*** Game Reset ***");
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
