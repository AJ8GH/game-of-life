package aj8gh.gameoflife.config;

import lombok.RequiredArgsConstructor;
import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.domain.Grid;
import aj8gh.gameoflife.ui.Terminal;
import aj8gh.gameoflife.ui.UI;
import aj8gh.gameoflife.ui.Web;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final ApiClientConfig apiClientConfig;
    private final SeederConfig seederConfig;

    @Value("${game.tickDuration}")
    private int tickDuration;

    @Value("${ui.terminal}")
    private boolean cli;

    @Value("${ui.info}")
    private boolean displayInfo;

    @Bean
    public Game game() {
        return new Game(ui(), grid(), tickDuration, seederConfig.seeder());
    }

    @Bean
    public UI ui() {
        return cli ?
                new Terminal(displayInfo) :
                new Web(apiClientConfig.apiClient());
    }

    @Bean
    public Grid grid() {
        return new Grid(seederConfig.seeder().seed());
    }
}
