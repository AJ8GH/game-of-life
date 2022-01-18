package org.jonasa.gameoflife;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
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
