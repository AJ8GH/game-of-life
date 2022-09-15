package io.github.aj8gh.gameoflife;

import static org.springframework.context.annotation.ComponentScan.Filter;

import io.github.aj8gh.gameoflife.application.Game;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan(excludeFilters = @Filter(RestController.class))
public class GameOfLifeApplication {

  private static final Logger LOG = LoggerFactory.getLogger(GameOfLifeApplication.class.getName());
  private static final String PROFILE = getActiveProfile();
  private static Game game;

  public static void main(String[] args) {
    apiLog("STARTING API");
    var context = SpringApplication.run(GameOfLifeApplication.class, args);
    game = context.getBean(Game.class);
    apiLog("API STARTED");

    if (!Objects.equals(PROFILE, "lazy")) {
      start();
    }
  }

  public static void stop() {
    game.stop();
  }

  private static void start() {
    LOG.info("*** STARTING Game of Life ***");
    game.run();
  }

  private static String getActiveProfile() {
    return System.getProperty("spring.profiles.active");
  }

  private static void apiLog(String message) {
    if (!Objects.equals(PROFILE, "local")) {
      LOG.info("*** {} ***", message);
    }
  }
}
