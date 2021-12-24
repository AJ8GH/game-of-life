package org.jonasa.apiclient;

import org.jonasa.apiclient.domain.GameState;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class ApiClient {
    private static final String SCHEME = "http";
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String PATH = "/queue";

    public ResponseEntity<String> queue(GameState gameState) {
        UriComponents uri = buildUri();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<GameState> request = new HttpEntity<>(gameState);
        return restTemplate.postForEntity(uri.toString(), request, String.class);
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
