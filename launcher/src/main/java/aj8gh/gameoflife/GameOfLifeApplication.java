package aj8gh.gameoflife;

import aj8gh.gameoflife.application.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static org.springframework.context.annotation.ComponentScan.Filter;

@SpringBootApplication
@ComponentScan(excludeFilters = @Filter(RestController.class))
public class GameOfLifeApplication {
    private static final Logger LOG = LogManager.getLogger(GameOfLifeApplication.class.getName());
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
