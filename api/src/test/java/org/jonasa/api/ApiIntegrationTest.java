package org.jonasa.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.jonasa.apiclient.domain.Cell;
import org.jonasa.apiclient.domain.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
public class ApiIntegrationTest {
    private GameState gameState;
    private ObjectMapper mapper;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController = new GameController(new ObjectMapper());
        mapper = new ObjectMapper();
        List<Cell> row = List.of(
                new Cell(false),
                new Cell(true),
                new Cell(true));
        List<List<Cell>> grid = List.of(row, row, row);
        gameState = new GameState(6, 3, grid);
    }

    @Test
    void queuedGameStateReturnedBySend() throws JsonProcessingException {
        stubFor(post(urlEqualTo("/queue")).willReturn(ok()));
        String requestBody = mapper.writeValueAsString(gameState);

        gameController.queue(requestBody);

        GameState dequeued = gameController.send("{}").getBody();
        assertEquals(gameState, dequeued);
    }
}
