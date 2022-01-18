package org.jonasa.gameoflife.apiclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jonasa.gameoflife.apiclient.domain.GameState;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
public class ApiClient {
    private static final String SCHEME = "http";
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String PATH = "/enqueue";

    private final RestTemplate restTemplate;

    public ResponseEntity<String> enqueue(GameState gameState) {
        try {
            UriComponents uri = buildUri();
            HttpEntity<GameState> request = new HttpEntity<>(gameState);
            return restTemplate.postForEntity(uri.toString(), request, String.class);

        } catch (HttpClientErrorException.NotFound e) {
            log.error("Not Found: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException.BadRequest e) {
            log.error("Bad Request: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (HttpServerErrorException.InternalServerError e) {
            log.error("Internal Server Error: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error connecting to API Server: {}", e.getMessage());
            return null;
        }
    }

    private UriComponents buildUri() {
        return UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST)
                .port(PORT)
                .path(PATH)
                .build();
    }
}
