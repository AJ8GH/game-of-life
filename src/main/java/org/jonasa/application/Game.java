package org.jonasa.application;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jonasa.domain.Grid;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

@Slf4j
@Data
public class Game {
    private final Grid grid;
    private final int tickDuration;
    private final AtomicInteger generation = new AtomicInteger(0);


    public void run() {
        try {
            while (true) {
                grid.tick();
//                logTick();
                System.out.println(grid);
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
