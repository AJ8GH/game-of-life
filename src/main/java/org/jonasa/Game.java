package org.jonasa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Game {
    private static final String TICK_LOG = "Generation: {}\nPopulation: {}\n{}";
    private static final String END_LOG = "Game Over!\nScore: {} Generations";

    private final Grid grid;

    private int generation;
    private String snapShot;

    public void run() throws InterruptedException {
        while (true) {
            snapShot = grid.toString();
            grid.tick();
            log.info(TICK_LOG, generation++, grid.population(), grid);
            Thread.sleep(2000);
            if (isOver()) {
                log.info(END_LOG, generation);
                break;
            }
        }
    }

    private boolean isOver() {
        return grid.population() == 0 || grid.toString().equals(snapShot);
    }
}
