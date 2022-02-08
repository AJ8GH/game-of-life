package aj8gh.gameoflife.api.controllers;

import aj8gh.gameoflife.apiclient.domain.GameState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class QueueController {
    private static final Logger LOG = LogManager.getLogger(QueueController.class.getName());

    private static final String ENQUEUE_ENDPOINT = "/queue/enqueue";
    private static final String DEQUEUE_ENDPOINT = "/queue/dequeue";
    private static final String CLEAR_ENDPOINT = "/queue/clear";
    private static final String DEQUEUE_ALL_ENDPOINT = "/queue/dequeue/all";
    private static final String APPLICATION_JSON = "application/json";

    private final Queue<GameState> queue = new LinkedList<>();
    private final ObjectMapper objectMapper;

    @PostMapping(value = ENQUEUE_ENDPOINT, consumes = APPLICATION_JSON)
    ResponseEntity<Void> enqueue(@RequestBody String body) {
        try {
            GameState gameState = objectMapper.readValue(body, GameState.class);
            LOG.info("Request received at {}: {}", ENQUEUE_ENDPOINT, gameState);
            queue.add(gameState);
        } catch (JsonProcessingException e) {
            LOG.error("Exception processing JSON request body: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = DEQUEUE_ENDPOINT)
    ResponseEntity<GameState> dequeue() {
        LOG.info("Request received at {}", DEQUEUE_ENDPOINT);
        if (queue.isEmpty()) {
            LOG.info("Queue is Empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(queue.remove(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = DEQUEUE_ALL_ENDPOINT)
    ResponseEntity<Collection<GameState>> dequeueAll() {
        List<GameState> response;
        synchronized (queue) {
            response = new ArrayList<>(queue);
            queue.clear();
        }
        LOG.info("Request received at {}", DEQUEUE_ALL_ENDPOINT);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = CLEAR_ENDPOINT)
    ResponseEntity<Void> clear() {
        LOG.info("Request received at {}", CLEAR_ENDPOINT);
        synchronized (this) {
            queue.clear();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
