package io.github.aj8gh.gameoflife.api.client;

import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.forbidden;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.aj8gh.gameoflife.api.client.domain.Cell;
import io.github.aj8gh.gameoflife.api.client.domain.GameState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@WireMockTest(httpPort = 8080)
class ApiClientTest {

  private static final String EXPECTED_SCHEME = "http";
  private static final String EXPECTED_HOST = "localhost";
  private static final String EXPECTED_PATH = "/enqueue";

  private ApiClient apiClient;

  private GameState gameState;
  private String expectedRequestBody;

  @BeforeEach
  void setUp() throws JsonProcessingException {
    apiClient = new ApiClient(
        "http", "localhost", 8080, "/enqueue", new RestTemplate());

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
