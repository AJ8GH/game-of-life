package org.jonasa.gameoflife;

import lombok.extern.slf4j.Slf4j;
import org.jonasa.gameoflife.application.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class GameOfLifeApplication {
    private static Game game;

    public static void main(String[] args) {
        log.info("*** STARTING API ***");
        var context = SpringApplication.run(GameOfLifeApplication.class, args);
        log.info("*** API STARTED ***");

        log.info("*** STARTING Game of Life ***");
        game = context.getBean(Game.class);
        game.run();
    }

    public static void stop() {
        game.stop();
    }
}
