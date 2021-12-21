package org.jonasa.application;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jonasa.domain.Grid;
import org.jonasa.ui.UI;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

@Slf4j
@Data
public class Game {
    private final UI ui;
    private final Grid grid;
    private final int tickDuration;
    private final AtomicInteger generation = new AtomicInteger(0);

    public void run() {
        while (true) {
            ui.accept(this);
            tick();
            if (gameOver()) break;
        }
    }

    public long population() {
        return grid.population();
    }

    private void tick() {
        try {
            grid.tick();
            generation.incrementAndGet();
            sleep(tickDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean gameOver() {
        return population() == 0;
    }
}
