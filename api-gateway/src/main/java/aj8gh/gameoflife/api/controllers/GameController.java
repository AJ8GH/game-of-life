package aj8gh.gameoflife.api.controllers;

import aj8gh.gameoflife.application.Game;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GameController {
    private static final Logger LOG = LogManager.getLogger(GameController.class.getName());

    private static final String START_ENDPOINT = "/game/start";
    private static final String STOP_ENDPOINT = "/game/stop";
    private static final String RESET_ENDPOINT = "/game/reset";
    private static final String SPEED_ENDPOINT = "/game/speed";

    private final QueueController queueController;
    private final Game game;

    @CrossOrigin
    @PostMapping(value = START_ENDPOINT)
    public ResponseEntity<String> start() {
        try {
            LOG.info("Request received at {}", START_ENDPOINT);
            game.run();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            LOG.error("Illegal call to {} - game already running", START_ENDPOINT);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = STOP_ENDPOINT)
    public ResponseEntity<String> stop() {
        try {
            LOG.info("Request received at {}", STOP_ENDPOINT);
            game.stop();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            LOG.error("Illegal call to {} - game already stopped", STOP_ENDPOINT);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = RESET_ENDPOINT)
    public ResponseEntity<Void> reset() {
        LOG.info("Request received at {}", RESET_ENDPOINT);
        game.reset();
        synchronized (queueController) {
            queueController.clear();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = SPEED_ENDPOINT)
    public ResponseEntity<String> speed(@RequestParam("millis") int millis) {
        LOG.info("Value {} received at {}", millis, SPEED_ENDPOINT);
        try {
            game.setTickDuration(millis);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            LOG.error("Negative param {} passed to {}", millis, SPEED_ENDPOINT);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
