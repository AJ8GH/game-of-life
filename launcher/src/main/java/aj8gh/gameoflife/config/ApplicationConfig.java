package aj8gh.gameoflife.config;

import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.consumer.ApiConsumer;
import aj8gh.gameoflife.consumer.CliConsumer;
import aj8gh.gameoflife.consumer.ConsumerAdaptor;
import aj8gh.gameoflife.domain.Grid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private static final int TOTAL_CONSUMERS = 2;

    private final ApiClientConfig apiClientConfig;
    private final SeederConfig seederConfig;

    @Value("${game.tickDuration}")
    private int tickDuration;

    @Value("${consumer.cli}")
    private boolean cli;

    @Value("${consumer.api}")
    private boolean api;

    @Value("${consumer.cli.info}")
    private boolean displayInfo;

    @Bean
    public Game game() {
        return new Game(
                consumerAdaptor(),
                seederConfig.seederAdaptor(),
                grid(),
                tickDuration);
    }

    @Bean
    public ConsumerAdaptor consumerAdaptor() {
        Set<Consumer<Game>> consumers = new HashSet<>(TOTAL_CONSUMERS);
        if (cli) consumers.add(cliConsumer());
        if (api) consumers.add(apiConsumer());

        return new ConsumerAdaptor(consumers);
    }

    @Bean
    public CliConsumer cliConsumer() {
        return new CliConsumer(displayInfo);
    }

    @Bean
    public ApiConsumer apiConsumer() {
        return new ApiConsumer(apiClientConfig.apiClient());
    }

    @Bean
    public Grid grid() {
        return new Grid(seederConfig.seeder().seed());
    }
}
