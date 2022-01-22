package org.jonasa.gameoflife.config;

import lombok.RequiredArgsConstructor;
import org.jonasa.gameoflife.application.Game;
import org.jonasa.gameoflife.domain.Grid;
import org.jonasa.gameoflife.ui.Terminal;
import org.jonasa.gameoflife.ui.UI;
import org.jonasa.gameoflife.ui.Web;
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
        return new Game(ui(), grid(), tickDuration);
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
