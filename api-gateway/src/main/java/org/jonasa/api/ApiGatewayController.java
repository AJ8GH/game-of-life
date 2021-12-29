package org.jonasa.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jonasa.apiclient.domain.GameState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayDeque;
import java.util.Queue;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiGatewayController {
    private final ObjectMapper objectMapper;
    private final Queue<GameState> queue = new ArrayDeque<>();

    @PostMapping("/dequeue")
    public ResponseEntity<GameState> dequeue(@RequestBody String body) {
        log.info("Request received at /dequeue: {}", body);
        if (queue.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(queue.remove(), HttpStatus.OK);
    }

    @PostMapping("/enqueue")
    public ResponseEntity<String> enqueue(@RequestBody String body) {
        log.info("Request received at /enqueue: {}", body);
        try {
            GameState gameState = objectMapper.readValue(body, GameState.class);
            queue.add(gameState);
        } catch (JsonProcessingException e) {
            log.error("Exception processing JSON request body: {}", e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
