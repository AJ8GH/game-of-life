package aj8gh.gameoflife.api;

import aj8gh.gameoflife.api.controllers.QueueController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import aj8gh.gameoflife.apiclient.domain.Cell;
import aj8gh.gameoflife.apiclient.domain.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
public class ApiIntegrationTest {
    private GameState gameState;
    private ObjectMapper mapper;
    private QueueController queueController;

    @BeforeEach
    void setUp() {
        queueController = new QueueController(new ObjectMapper());
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

        queueController.enqueue(requestBody);

        GameState dequeued = queueController.dequeue("{}").getBody();
        assertEquals(gameState, dequeued);
    }
}
