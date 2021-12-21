package org.jonasa.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jonasa.domain.Grid;
import org.jonasa.seeder.Seeder;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class Game {
    private final Grid grid;
    private final Seeder seeder;
    private final AtomicInteger generation = new AtomicInteger(0);

    private int tickDuration = Config.getInt("game.tickDuration");

    public void seed() {
        seeder.seed(grid);
    }

    public void run() {
        try {
            while (true) {
                grid.tick();
                logTick();
                if (grid.population() == 0) break;
                sleep(tickDuration);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void logTick() {
        log.info("\nGeneration: {}, Population: {}{}",
                generation.incrementAndGet(), grid.population(), grid);
    }
}
