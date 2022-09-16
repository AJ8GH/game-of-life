package io.github.aj8gh.gameoflife.api.client;

import io.github.aj8gh.gameoflife.api.client.domain.GameState;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class ApiClient {

  private static final Logger LOG = LoggerFactory.getLogger(ApiClient.class.getName());

  private final RestTemplate restTemplate;
  private final String path;

  public ApiClient(RestTemplate restTemplate, String path) {
    this.restTemplate = restTemplate;
    this.path = path;
  }

  public Optional<ResponseEntity<String>> enqueue(final GameState gameState) {
    try {
      var response = restTemplate.postForEntity(path, gameState, String.class);
      LOG.info("Sent Request: {}\nResponse received: {}", gameState, response);
      return Optional.of(response);
    } catch (HttpStatusCodeException e) {
      LOG.error("{} : {}", e.getMessage(), e.getStatusCode());
      return Optional.of(new ResponseEntity<>(e.getStatusCode()));
    } catch (Exception e) {
      LOG.error("Error connecting to server", e);
    }
    return Optional.empty();
  }
}
