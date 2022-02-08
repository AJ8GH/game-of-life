package aj8gh.gameoflife.api.controllers;

import aj8gh.gameoflife.api.domain.ConsumerType;
import aj8gh.gameoflife.api.domain.GameConfigRequest;
import aj8gh.gameoflife.api.domain.MetadataResponse;
import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.consumer.ApiConsumer;
import aj8gh.gameoflife.consumer.CliConsumer;
import aj8gh.gameoflife.consumer.ConsumerAdaptor;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static aj8gh.gameoflife.api.domain.ConsumerType.API;
import static aj8gh.gameoflife.api.domain.ConsumerType.CLI;

@RequiredArgsConstructor
@RestController
public class GameController {
    private static final Logger LOG = LogManager.getLogger(GameController.class.getName());

    private static final String START_ENDPOINT = "/game/start";
    private static final String STOP_ENDPOINT = "/game/stop";
    private static final String RESET_ENDPOINT = "/game/reset";
    private static final String CONFIG_ENDPOINT = "/game/config";
    private static final String APPLICATION_JSON = "application/json";

    private final Map<ConsumerType, Consumer<Game>> consumerMap;
    private final QueueController queueController;
    private final SeederController seederController;
    private final ConsumerAdaptor consumerAdaptor;
    private final Game game;

    @CrossOrigin
    @PostMapping(value = START_ENDPOINT)
    ResponseEntity<Void> start() {
        try {
            LOG.info("Request received at {}", START_ENDPOINT);
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
            LOG.info("Request received at {}", STOP_ENDPOINT);
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
        LOG.info("Request received at {}", RESET_ENDPOINT);
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
        LOG.info("GET request received at {}", CONFIG_ENDPOINT);

        MetadataResponse response = MetadataResponse.builder()
                .game(getGameMetadata())
                .seeder(seederController.getSeederMetadata())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void updateGameConfig(GameConfigRequest request) {
        List<ConsumerType>  consumerTypes = request.getConsumers();
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
        return consumerAdaptor.getConsumers().stream().map(consumer -> {
            if (consumer instanceof CliConsumer) return CLI;
            if (consumer instanceof ApiConsumer) return API;
            return null;
        }).collect(Collectors.toList());
    }
}
