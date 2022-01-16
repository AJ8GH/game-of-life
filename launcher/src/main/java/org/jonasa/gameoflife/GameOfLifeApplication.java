package org.jonasa.gameoflife;

import lombok.extern.slf4j.Slf4j;
import org.jonasa.gameoflife.application.Config;
import org.jonasa.gameoflife.application.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class GameOfLifeApplication {
    private static final Game GAME = Config.game();

    public static void main(String[] args) {
        log.info("*** STARTING API ***");
        SpringApplication.run(GameOfLifeApplication.class, args);
        log.info("*** API STARTED ***");

        log.info("*** STARTING Game of Life ***");
        GAME.run();
    }

    public static void stop() {
        GAME.stop();
    }
}
