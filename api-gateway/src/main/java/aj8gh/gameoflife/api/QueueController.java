package aj8gh.gameoflife.api;

import aj8gh.gameoflife.apiclient.domain.GameState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayDeque;
import java.util.Queue;

@RestController
@RequiredArgsConstructor
public class QueueController {

    private static final Logger LOG = LogManager.getLogger(QueueController.class.getName());
    private static final String ENQUEUE_ENDPOINT = "/enqueue";
    private static final String DEQUEUE_ENDPOINT = "/dequeue";
    private static final String CONTENT_TYPE = "application/json";

    private final Queue<GameState> queue = new ArrayDeque<>();
    private final ObjectMapper objectMapper;

    @PostMapping(value = ENQUEUE_ENDPOINT, consumes = CONTENT_TYPE)
    public ResponseEntity<String> enqueue(@RequestBody String body) {
        try {
            GameState gameState = objectMapper.readValue(body, GameState.class);
            LOG.info("Request received at {}: {}", ENQUEUE_ENDPOINT, gameState);
            queue.add(gameState);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            LOG.error("Exception processing JSON request body: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = DEQUEUE_ENDPOINT, consumes = CONTENT_TYPE)
    public ResponseEntity<GameState> dequeue(@RequestBody String body) {
        LOG.info("Request received at {}: {}", DEQUEUE_ENDPOINT, body);
        if (queue.isEmpty()) {
            LOG.info("Queue is Empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(queue.remove(), HttpStatus.OK);
    }
}
