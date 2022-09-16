package io.github.aj8gh.gameoflife.api.client;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.aj8gh.gameoflife.api.client.domain.Cell;
import io.github.aj8gh.gameoflife.api.client.domain.GameState;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

@WireMockTest(httpPort = 8080)
class ApiClientTest {

  private static final String ROOT_URI = "http://localhost:8080";
  private static final String EXPECTED_SCHEME = "http";
  private static final String EXPECTED_HOST = "localhost";
  private static final String EXPECTED_PATH = "/queue/enqueue";
  private static final int EXPECTED_PORT = 8080;

  private ApiClient apiClient;

  private GameState gameState;
  private String expectedRequestBody;

  @BeforeEach
  void setUp() throws JsonProcessingException {
    apiClient = new ApiClient(new RestTemplateBuilder()
        .rootUri(ROOT_URI)
        .defaultHeader(CONTENT_TYPE, APPLICATION_JSON.toString())
        .defaultHeader(ACCEPT, APPLICATION_JSON.toString())
        .build(),
        EXPECTED_PATH);

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
    var response = apiClient.enqueue(gameState);

    verify(postRequestedFor(urlEqualTo(EXPECTED_PATH))
        .withScheme(EXPECTED_SCHEME)
        .withHost(equalTo(EXPECTED_HOST))
        .withRequestBody(equalToJson(expectedRequestBody))
        .withPort(EXPECTED_PORT));

    assertTrue(response.isPresent(), "No response found");
    assertTrue(response.get().getStatusCode().is2xxSuccessful());
  }

  @Test
  void enqueue_NotFound() {
    stubFor(post(urlEqualTo(EXPECTED_PATH)).willReturn(notFound()));
    var response = apiClient.enqueue(gameState);

    assertTrue(response.isPresent(), "No response found");
    assertTrue(response.get().getStatusCode().is4xxClientError());
  }
}
