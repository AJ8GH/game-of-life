package org.jonasa.apiclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.jonasa.apiclient.domain.Cell;
import org.jonasa.apiclient.domain.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest(httpPort = 8080)
public class ApiClientTest {
    private ApiClient apiClient;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
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

        stubFor(post(urlEqualTo("/queue")).willReturn(ok()));
        ResponseEntity<String> response = apiClient.queue(gameState);

        verify(postRequestedFor(urlEqualTo("/queue"))
                .withScheme("http")
                .withHost(equalTo("localhost"))
                .withRequestBody(equalToJson(expectedRequestBody))
                .withPort(8080));
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
