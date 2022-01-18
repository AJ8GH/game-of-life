package org.jonasa.gameoflife.config;

import org.jonasa.gameoflife.apiclient.ApiClient;
import org.jonasa.gameoflife.application.Game;
import org.jonasa.gameoflife.domain.Grid;
import org.jonasa.gameoflife.seeder.FileSeeder;
import org.jonasa.gameoflife.seeder.RandomSeeder;
import org.jonasa.gameoflife.seeder.Seeder;
import org.jonasa.gameoflife.ui.Terminal;
import org.jonasa.gameoflife.ui.UI;
import org.jonasa.gameoflife.ui.Web;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Value("${seeder.fromFile}")
    private boolean seedFromFile;
    @Value("${game.tickDuration:777}")
    private int tickDuration;
    @Value("${seeder.rows:7}")
    private int rows;
    @Value("${seeder.columns:7}")
    private int columns;
    @Value("${seeder.filePath}")
    private String seedFilePath;
    @Value("${ui.terminal}")
    private boolean cli;
    @Value("${ui.info}")
    private boolean displayInfo;

    @Bean
    public Game game() {
        return new Game(ui(), grid(), tickDuration);
    }

    @Bean
    public ApiClient apiClient() {
        RestTemplate restTemplate = new RestTemplate();
        return new ApiClient(restTemplate);
    }

    @Bean
    public UI ui() {
        return cli ? new Terminal(displayInfo) : new Web(apiClient());
    }

    @Bean
    public Grid grid() {
        return new Grid(seeder().seed());
    }

    @Bean
    public Seeder seeder() {
        return seedFromFile ?
                new FileSeeder(rows, columns, seedFilePath) :
                new RandomSeeder(rows, columns);
    }
}
