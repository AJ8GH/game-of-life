package org.jonasa.gameoflife.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jonasa.gameoflife.apiclient.domain.GameState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayDeque;
import java.util.Queue;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QueueController {
    private static final String ENQUEUE_ENDPOINT = "/enqueue";
    private static final String DEQUEUE_ENDPOINT = "/dequeue";
    private static final String CONTENT_TYPE = "application/json";

    private final Queue<GameState> queue = new ArrayDeque<>();
    private final ObjectMapper objectMapper;

    @PostMapping(value = ENQUEUE_ENDPOINT, consumes = CONTENT_TYPE)
    public ResponseEntity<String> enqueue(@RequestBody String body) {
        log.info("Request received at {}: {}", ENQUEUE_ENDPOINT, body);
        try {
            GameState gameState = objectMapper.readValue(body, GameState.class);
            queue.add(gameState);
        } catch (JsonProcessingException e) {
            log.error("Exception processing JSON request body: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = DEQUEUE_ENDPOINT, consumes = CONTENT_TYPE)
    public ResponseEntity<GameState> dequeue(@RequestBody String body) {
        log.info("Request received at {}: {}", DEQUEUE_ENDPOINT, body);
        if (queue.isEmpty()) {
            log.info("Queue is Empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(queue.remove(), HttpStatus.OK);
    }
}
