package io.github.aj8gh.gameoflife;

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
  void stop_applicationStartsAndStops() throws InterruptedException {
    GameOfLifeApplication.main(new String[]{});
    Thread.sleep(1000);
    GameOfLifeApplication.stop();
  }
}
