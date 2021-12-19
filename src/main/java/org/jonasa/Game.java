package org.jonasa;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Game {
    public static void main(String[] args) {
        Grid grid = new Grid(10, 10);
        run(grid);
    }

    private static void run(Grid grid) {
        String snapShot;
        int generation = 0;
        try {
            while (true) {
                snapShot = grid.toString();
                grid.tick();
                log.info("Generation: {}\nPopulation: {}\n{}",
                        generation++, grid.population(), grid);
                Thread.sleep(2000);
                if (grid.population() == 0 || grid.toString().equals(snapShot)) {
                    log.info("Game Over!\nScore: {} Generations", generation);
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
