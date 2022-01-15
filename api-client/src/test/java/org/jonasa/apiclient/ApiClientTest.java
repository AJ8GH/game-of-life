package org.jonasa.apiclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.jonasa.apiclient.domain.Cell;
import org.jonasa.apiclient.domain.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 8080)
public class ApiClientTest {
    private static final String EXPECTED_SCHEME = "http";
    private static final String EXPECTED_HOST = "localhost";
    private static final String EXPECTED_PATH = "/enqueue";

    private ApiClient apiClient;

    private GameState gameState;
    private String expectedRequestBody;

    @BeforeEach
    void setUp()  throws JsonProcessingException {
        apiClient = new ApiClient(new RestTemplate());

        gameState = new GameState(6, 3, List.of(List.of(
                new Cell(false),
                new Cell(true),
                new Cell(true))));

        ObjectMapper mapper = new ObjectMapper();
        expectedRequestBody = mapper.writeValueAsString(gameState);
    }

    @Test
    void enqueue_Success() {
        stubFor(post(urlEqualTo(EXPECTED_PATH)).willReturn(ok()));
        ResponseEntity<String> response = apiClient.enqueue(gameState);

        verify(postRequestedFor(urlEqualTo(EXPECTED_PATH))
                .withScheme(EXPECTED_SCHEME)
                .withHost(equalTo(EXPECTED_HOST))
                .withRequestBody(equalToJson(expectedRequestBody))
                .withPort(8080));
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void enqueue_NotFound() {
        stubFor(post(urlEqualTo(EXPECTED_PATH)).willReturn(notFound()));
        ResponseEntity<String> response = apiClient.enqueue(gameState);
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void enqueue_ServerError() {
        stubFor(post(urlEqualTo(EXPECTED_PATH)).willReturn(serverError()));
        ResponseEntity<String> response = apiClient.enqueue(gameState);
        assertTrue(response.getStatusCode().is5xxServerError());
    }

    @Test
    void enqueue_BadRequest() {
        stubFor(post(urlEqualTo(EXPECTED_PATH)).willReturn(badRequest()));
        ResponseEntity<String> response = apiClient.enqueue(gameState);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void enqueue_OtherError() {
        stubFor(post(urlEqualTo(EXPECTED_PATH)).willReturn(forbidden()));
        ResponseEntity<String> response = apiClient.enqueue(gameState);
        assertNull(response);
    }
}
