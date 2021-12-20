package org.jonasa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
@RequiredArgsConstructor
public class Game {
    private static final int TICK_DELAY = Config.getInt("game.tickDelay");
    private static final int ROWS = Config.getInt("game.rows");
    private static final int COLUMNS = Config.getInt("game.columns");

    private final Grid grid;
    private final Seeder seeder;

    private int generation;

    public void seed() {
        seeder.seed(grid, ROWS, COLUMNS);
    }

    public void run() {
        try {
            while (true) {
                grid.tick();
                logTick();
                if (grid.population() == 0) break;
                sleep(TICK_DELAY);
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
