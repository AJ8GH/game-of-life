package io.github.aj8gh.gameoflife.api.controllers;

import io.github.aj8gh.gameoflife.api.domain.ConsumerType;
import io.github.aj8gh.gameoflife.api.domain.GameConfigRequest;
import io.github.aj8gh.gameoflife.api.domain.MetadataResponse;
import io.github.aj8gh.gameoflife.application.Game;
import io.github.aj8gh.gameoflife.consumer.CliConsumer;
import io.github.aj8gh.gameoflife.consumer.ConsumerAdaptor;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GameController {

  private static final Logger LOG = LoggerFactory.getLogger(GameController.class.getName());

  private static final String START_ENDPOINT = "/game/start";
  private static final String STOP_ENDPOINT = "/game/stop";
  private static final String RESET_ENDPOINT = "/game/reset";
  private static final String CONFIG_ENDPOINT = "/game/config";
  private static final String APPLICATION_JSON = "application/json";
  private static final String REQUEST_RECEIVED_MESSAGE = "Request received at {}";

  private final Map<ConsumerType, Consumer<Game>> consumerMap;
  private final QueueController queueController;
  private final SeederController seederController;
  private final ConsumerAdaptor consumerAdaptor;
  private final Game game;

  @CrossOrigin
  @PostMapping(value = START_ENDPOINT)
  ResponseEntity<Void> start() {
    try {
      LOG.info(REQUEST_RECEIVED_MESSAGE, START_ENDPOINT);
      game.run();
    } catch (IllegalStateException e) {
      LOG.error("Illegal call to {} - game already running", START_ENDPOINT);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @CrossOrigin
  @PostMapping(value = STOP_ENDPOINT)
  ResponseEntity<Void> stop() {
    try {
      LOG.info(REQUEST_RECEIVED_MESSAGE, STOP_ENDPOINT);
      game.stop();
    } catch (IllegalStateException e) {
      LOG.error("Illegal call to {} - game already stopped", STOP_ENDPOINT);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @CrossOrigin
  @PostMapping(value = RESET_ENDPOINT)
  ResponseEntity<Void> reset() {
    LOG.info(REQUEST_RECEIVED_MESSAGE, RESET_ENDPOINT);
    game.reset();
    queueController.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @CrossOrigin
  @PostMapping(value = CONFIG_ENDPOINT, consumes = APPLICATION_JSON)
  ResponseEntity<Void> config(@RequestBody GameConfigRequest request) {
    LOG.info("POST request received at {}: {}", CONFIG_ENDPOINT, request);
    try {
      updateGameConfig(request);
    } catch (IllegalArgumentException e) {
      LOG.error("Bad Request: {} | {}", request, e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @CrossOrigin
  @GetMapping(value = CONFIG_ENDPOINT)
  ResponseEntity<MetadataResponse> metadata() {
    LOG.info(REQUEST_RECEIVED_MESSAGE, CONFIG_ENDPOINT);

    MetadataResponse response = MetadataResponse.builder()
        .game(getGameMetadata())
        .seeder(seederController.getSeederMetadata())
        .build();

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private void updateGameConfig(GameConfigRequest request) {
    List<ConsumerType> consumerTypes = request.getConsumers();
    if (consumerTypes != null) {
      consumerAdaptor.clear();
      consumerTypes.forEach(consumerType -> consumerAdaptor
          .add(consumerMap.get(consumerType)));
    }
    game.setTickDuration(request.getTickDuration());
  }

  private MetadataResponse.Game getGameMetadata() {
    return MetadataResponse.Game.builder()
        .running(game.isRunning())
        .tickDuration(game.getTickDuration())
        .generation(game.getGeneration())
        .columns(game.getGrid().columns())
        .rows(game.getGrid().rows())
        .consumers(getConsumerTypes())
        .population(game.population())
        .build();
  }

  private List<ConsumerType> getConsumerTypes() {
    return consumerAdaptor.getConsumers().stream()
        .map(consumer -> consumer instanceof CliConsumer ? ConsumerType.CLI : ConsumerType.API)
        .toList();
  }
}
