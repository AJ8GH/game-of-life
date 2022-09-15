package io.github.aj8gh.gameoflife.api.client;

import io.github.aj8gh.gameoflife.api.client.domain.GameState;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public class ApiClient {

  private static final Logger LOG = LoggerFactory.getLogger(ApiClient.class.getName());

  private final String scheme;
  private final String host;
  private final int port;
  private final String path;
  private final RestTemplate restTemplate;

  public ResponseEntity<String> enqueue(final GameState gameState) {
    try {
      UriComponents uri = buildUri();
      HttpEntity<GameState> request = new HttpEntity<>(gameState);
      var response = restTemplate.postForEntity(uri.toString(), request, String.class);
      LOG.info("Sent Request: {}\nResponse received: {}", request, response);
      return response;

    } catch (HttpClientErrorException.NotFound e) {
      LOG.error("Not Found: {}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (HttpClientErrorException.BadRequest e) {
      LOG.error("Bad Request: {}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (HttpServerErrorException.InternalServerError e) {
      LOG.error("Internal Server Error: {}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      LOG.error("Error connecting to API Server: {}", e.getMessage());
      return null;
    }
  }

  private UriComponents buildUri() {
    return UriComponentsBuilder.newInstance()
        .scheme(scheme)
        .host(host)
        .port(port)
        .path(path)
        .build();
  }
}
