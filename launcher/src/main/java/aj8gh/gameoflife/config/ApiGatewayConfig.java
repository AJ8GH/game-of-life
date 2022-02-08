package aj8gh.gameoflife.config;

import aj8gh.gameoflife.api.controllers.GameController;
import aj8gh.gameoflife.api.controllers.QueueController;
import aj8gh.gameoflife.api.controllers.SeederController;
import aj8gh.gameoflife.api.domain.ConsumerType;
import aj8gh.gameoflife.application.Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class ApiGatewayConfig {

    private final ApplicationConfig applicationConfig;
    private final SeederConfig seederConfig;

    @Bean
    public GameController gameController() {
        Map<ConsumerType, Consumer<Game>> consumerMap = Map.of(
                ConsumerType.API, applicationConfig.apiConsumer(),
                ConsumerType.CLI, applicationConfig.cliConsumer()
        );
        return new GameController(
                consumerMap,
                queueController(),
                seederController(),
                applicationConfig.consumerAdaptor(),
                applicationConfig.game());
    }

    @Bean
    public QueueController queueController() {
        return new QueueController(new ObjectMapper());
    }

    @Bean
    public SeederController seederController() {
        return new SeederController(
                new ObjectMapper(),
                seederConfig.seederAdaptor());
    }
}
