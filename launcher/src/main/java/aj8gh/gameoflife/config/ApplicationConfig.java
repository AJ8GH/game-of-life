package aj8gh.gameoflife.config;

import aj8gh.gameoflife.consumer.ConsumerAdaptor;
import aj8gh.gameoflife.consumer.UiConsumer;
import lombok.RequiredArgsConstructor;
import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.domain.Grid;
import aj8gh.gameoflife.consumer.CliConsumer;
import aj8gh.gameoflife.consumer.ApiConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                consumerAdaptor(TOTAL_CONSUMERS),
                grid(),
                seederConfig.seeder(),
                tickDuration
        );
    }

    @Bean
    public ConsumerAdaptor consumerAdaptor(int numberOfConsumers) {
        UiConsumer[] consumers = new UiConsumer[numberOfConsumers];
        if (cli) consumers[0] = cliConsumer();
        if (api) consumers[1] = apiConsumer();
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
