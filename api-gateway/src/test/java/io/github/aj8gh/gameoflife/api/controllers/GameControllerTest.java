package io.github.aj8gh.gameoflife.api.controllers;

import static io.github.aj8gh.gameoflife.api.domain.ConsumerType.API;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.aj8gh.gameoflife.api.domain.ConsumerType;
import io.github.aj8gh.gameoflife.api.domain.GameConfigRequest;
import io.github.aj8gh.gameoflife.api.domain.MetadataResponse;
import io.github.aj8gh.gameoflife.application.Game;
import io.github.aj8gh.gameoflife.consumer.ApiConsumer;
import io.github.aj8gh.gameoflife.consumer.CliConsumer;
import io.github.aj8gh.gameoflife.consumer.ConsumerAdaptor;
import io.github.aj8gh.gameoflife.domain.Grid;
import io.github.aj8gh.gameoflife.seeder.AbstractSeeder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class GameControllerTest {

  private static final MediaType APPLICATION_JSON = new MediaType(
      MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(),
      StandardCharsets.UTF_8);

  @Mock
  private Game game;
  @Mock
  private QueueController queueController;
  @Mock
  private SeederController seederController;
  @Mock
  private ConsumerAdaptor consumerAdaptor;
  @Mock
  private CliConsumer cliConsumer;
  @Mock
  private ApiConsumer apiConsumer;

  private MockMvc mockMvc;
  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    openMocks(this);
    mapper = new ObjectMapper();

    Map<ConsumerType, Consumer<Game>> consumerMap = Map.of(
        API, apiConsumer,
        ConsumerType.CLI, cliConsumer
    );
    GameController controller = new GameController(
        consumerMap, queueController, seederController, consumerAdaptor, game);

    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void start_WhenRunning_ReturnsBadRequest() throws Exception {
    doThrow(new IllegalStateException()).when(game).run();

    mockMvc.perform(post("/game/start"))
        .andExpect(status().isBadRequest());

    verify(game).run();
  }

  @Test
  void start_WhenStopped_StartsGame() throws Exception {
    mockMvc.perform(post("/game/start"))
        .andExpect(status().isOk());

    verify(game).run();
  }

  @Test
  void reset() throws Exception {
    mockMvc.perform(post("/game/reset"))
        .andExpect(status().isOk());

    verify(queueController).clear();
    verify(game).reset();
  }

  @Test
  void stop_WhenRunning_StopsGame() throws Exception {
    mockMvc.perform(post("/game/stop"))
        .andExpect(status().isOk());

    verify(game).stop();
  }

  @Test
  void stop_WhenStopped_ReturnsBadRequest() throws Exception {
    doThrow(new IllegalStateException()).when(game).stop();

    mockMvc.perform(post("/game/stop"))
        .andExpect(status().isBadRequest());

    verify(game).stop();
  }

  @Test
  void config_Success_ChangesTickDuration() throws Exception {
    GameConfigRequest request = GameConfigRequest.builder()
        .tickDuration(2000)
        .build();
    String requestBody = mapper.writeValueAsString(request);

    mockMvc.perform(post("/game/config")
            .contentType(APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk());

    verify(game).setTickDuration(2000);
  }

  @Test
  void config_NegativeInt_ReturnsBadRequest() throws Exception {
    GameConfigRequest request = GameConfigRequest.builder()
        .tickDuration(-500)
        .build();
    String requestBody = mapper.writeValueAsString(request);

    doThrow(new IllegalArgumentException()).when(game).setTickDuration(-500);

    mockMvc.perform(post("/game/config")
            .contentType(APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest());

    verify(game).setTickDuration(-500);
  }

  @Test
  void config_SuccessfulRequestBothConsumers_AddsBothConsumers() throws Exception {
    MetadataResponse.Seeder seederMetadata = MetadataResponse.Seeder.builder()
        .file("File")
        .columns(33)
        .rows(66)
        .seederType(AbstractSeeder.SeederType.RANDOM)
        .build();

    Grid grid = mock(Grid.class);
    ApiConsumer consumer = mock(ApiConsumer.class);

    when(game.isRunning()).thenReturn(true);
    when(game.getGrid()).thenReturn(grid);
    when(grid.columns()).thenReturn(20);
    when(grid.rows()).thenReturn(99);
    when(game.population()).thenReturn(53L);
    when(game.getGeneration()).thenReturn(17);
    when(game.getTickDuration()).thenReturn(578);
    when(consumerAdaptor.getConsumers()).thenReturn(Set.of(consumer));
    when(seederController.getSeederMetadata()).thenReturn(seederMetadata);

    MetadataResponse expectedResponse = MetadataResponse.builder()
        .game(MetadataResponse.Game.builder()
            .consumers(List.of(API))
            .generation(17)
            .running(true)
            .rows(99)
            .columns(20)
            .population(53)
            .tickDuration(578)
            .build())
        .seeder(MetadataResponse.Seeder.builder()
            .seederType(AbstractSeeder.SeederType.RANDOM)
            .file("File")
            .rows(66)
            .columns(33)
            .build())
        .build();

    String expectedJson = mapper.writeValueAsString(expectedResponse);

    mockMvc.perform(get("/game/config"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedJson));
  }
}
