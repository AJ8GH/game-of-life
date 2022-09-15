package io.github.aj8gh.gameoflife.api.controllers;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.aj8gh.gameoflife.api.client.domain.Cell;
import io.github.aj8gh.gameoflife.api.client.domain.GameState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = 8080)
class ApiIntegrationTest {

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
  void queue_Dequeue_GameStateReturned() throws JsonProcessingException {
    stubFor(post(urlEqualTo("/queue")).willReturn(ok()));
    String requestBody = mapper.writeValueAsString(gameState);

    queueController.enqueue(requestBody);

    GameState dequeued = queueController.dequeue().getBody();
    assertEquals(gameState, dequeued);
  }
}
