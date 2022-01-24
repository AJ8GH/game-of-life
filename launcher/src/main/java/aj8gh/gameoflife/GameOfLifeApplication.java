package aj8gh.gameoflife;

import lombok.extern.slf4j.Slf4j;
import aj8gh.gameoflife.application.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@Slf4j
@SpringBootApplication
public class GameOfLifeApplication {
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
        log.info("*** STARTING Game of Life ***");
        game.run();
    }

    private static String getActiveProfile() {
        return System.getProperty("spring.profiles.active");
    }

    private static void apiLog(String message) {
        if (!Objects.equals(PROFILE, "local")) {
            log.info("*** {} ***", message);
        }
    }
}
