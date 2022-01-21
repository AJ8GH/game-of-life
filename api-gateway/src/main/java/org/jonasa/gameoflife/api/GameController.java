package org.jonasa.gameoflife.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jonasa.gameoflife.application.Game;
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
    private final Game game;

    @CrossOrigin
    @PostMapping(value = START_ENDPOINT)
    public ResponseEntity<String> start() {
        log.info("Request received at {}", START_ENDPOINT);
        if (game.isRunning()) {
            log.error("Illegal call to /start - game already running");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        game.run();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
