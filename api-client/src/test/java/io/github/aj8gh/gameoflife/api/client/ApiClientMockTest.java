package io.github.aj8gh.gameoflife.api.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import io.github.aj8gh.gameoflife.api.client.domain.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

class ApiClientMockTest {
  private static final String PATH = "/queue/enqueue";

  @Mock
  private RestTemplate restTemplate;
  private ApiClient apiClient;
  private GameState gameState;

  @BeforeEach
  void setUp() {
    openMocks(this);
    apiClient = new ApiClient(restTemplate, PATH);
    gameState = new GameState();
  }

  @Test
  void enqueue_Success() {
    apiClient.enqueue(gameState);
    verify(restTemplate).postForEntity(PATH, gameState, String.class);
  }

  @Test
  void enqueue_Failure() {
    when(restTemplate.postForEntity(PATH, gameState, String.class))
        .thenThrow(new RuntimeException());

    var response = apiClient.enqueue(gameState);
    verify(restTemplate).postForEntity(PATH, gameState, String.class);
    assertTrue(response.isEmpty());
  }
}
