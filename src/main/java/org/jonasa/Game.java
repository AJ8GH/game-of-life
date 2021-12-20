package org.jonasa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
@RequiredArgsConstructor
public class Game {
    private final int tickDuration = Config.getInt("game.tickDuration");
    private final int rows = Config.getInt("game.seeder.rows");
    private final int columns = Config.getInt("game.seeder.columns");
    private final Grid grid;
    private final Seeder seeder;

    private int generation;

    public void seed() {
        seeder.seed(grid, rows, columns);
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
                ++generation, grid.population(), grid);
    }
}
