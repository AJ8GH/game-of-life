package org.jonasa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Game {
    private final Grid grid;
    private final Seeder seeder;

    public void seed(int rows, int columns) {
        seeder.seed(grid, rows, columns);
    }

    public void run() {
        while (true) {
            grid.tick();
            log.info(grid.toString());
            if (grid.population() == 0) break;
        }
    }
}
