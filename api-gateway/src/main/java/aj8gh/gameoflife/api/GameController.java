package aj8gh.gameoflife.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.seeder.Seeder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class GameController {
    private static final String START_ENDPOINT = "/start";
    private static final String STOP_ENDPOINT = "/stop";
    private static final String RESET_ENDPOINT = "/reset";

    private final Game game;
    private final Seeder seeder;

    @CrossOrigin
    @PostMapping(value = START_ENDPOINT)
    public ResponseEntity<String> start() {
        try {
            log.info("Request received at {}", START_ENDPOINT);
            game.run();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.error("Illegal call to {} - game already running", START_ENDPOINT);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = STOP_ENDPOINT)
    public ResponseEntity<String> stop() {
        try {
            log.info("Request received at {}", STOP_ENDPOINT);
            game.stop();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.error("Illegal call to {} - game already stopped", STOP_ENDPOINT);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = RESET_ENDPOINT)
    public ResponseEntity<String> reset() {
        log.info("Request received at {}", RESET_ENDPOINT);
        game.reset(seeder.seed());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
