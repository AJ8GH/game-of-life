package org.jonasa.gameoflife;

import org.jonasa.gameoflife.GameOfLifeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GameOfLifeApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void applicationStartsAndStops() throws InterruptedException {
        GameOfLifeApplication.main(new String[] {});
        Thread.sleep(500);
        GameOfLifeApplication.stop();
    }
}
