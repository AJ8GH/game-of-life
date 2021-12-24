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
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest(httpPort = 8080)
public class ApiClientTest {
    private static final String EXPECTED_SCHEME = "http";
    private static final String EXPECTED_HOST = "localhost";
    private static final String EXPECTED_PATH = "/queue";
    private ApiClient apiClient;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        apiClient = new ApiClient(new RestTemplate());
        mapper = new ObjectMapper();
    }

    @Test
    void queue() throws JsonProcessingException {
        List<Cell> row = List.of(
                new Cell(false),
                new Cell(true),
                new Cell(true));
        List<List<Cell>> grid = List.of(row, row, row);
        GameState gameState = new GameState(6, 3, grid);
        String expectedRequestBody = mapper.writeValueAsString(gameState);

        stubFor(post(urlEqualTo(EXPECTED_PATH)).willReturn(ok()));
        ResponseEntity<String> response = apiClient.queue(gameState);

        verify(postRequestedFor(urlEqualTo(EXPECTED_PATH))
                .withScheme(EXPECTED_SCHEME)
                .withHost(equalTo(EXPECTED_HOST))
                .withRequestBody(equalToJson(expectedRequestBody))
                .withPort(8080));
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
