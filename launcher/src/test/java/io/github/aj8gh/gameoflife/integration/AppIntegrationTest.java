package io.github.aj8gh.gameoflife.integration;

import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import io.github.aj8gh.gameoflife.api.client.ApiClient;
import io.github.aj8gh.gameoflife.application.Game;
import io.github.aj8gh.gameoflife.consumer.ApiConsumer;
import io.github.aj8gh.gameoflife.consumer.CliConsumer;
import io.github.aj8gh.gameoflife.consumer.ConsumerAdaptor;
import io.github.aj8gh.gameoflife.domain.Grid;
import io.github.aj8gh.gameoflife.seeder.FileSeeder;
import io.github.aj8gh.gameoflife.seeder.RandomSeeder;
import io.github.aj8gh.gameoflife.seeder.Seeder;
import io.github.aj8gh.gameoflife.seeder.SeederAdaptor;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class AppIntegrationTest {

  private static final String ROOT_URI = "http://localhost:8080";
  private static final String PATH = "/queue/enqueue";

  private static final int ROWS = 10;
  private static final int COLUMNS = 10;
  private static final String SEED_FILE_PATH = "src/test/resources/";
  private static final String SEED_FILE_NAME = "test_seed.csv";
  private static final int TICK_DURATION = 50;

  @Test
  void seederAdaptor_setRowsAndColumns_ResetGameGridHasNewSize() {
    var apiClient = new ApiClient(new RestTemplateBuilder()
        .rootUri(ROOT_URI)
        .defaultHeader(CONTENT_TYPE, APPLICATION_JSON.toString())
        .defaultHeader(ACCEPT, APPLICATION_JSON.toString())
        .build(),
        PATH);

    var consumerAdaptor = new ConsumerAdaptor(Set.of(
        new CliConsumer(false),
        new ApiConsumer(apiClient)
    ));

    Seeder randomSeeder = new RandomSeeder(ROWS, COLUMNS);
    Seeder fileSeeder = new FileSeeder(ROWS, COLUMNS, SEED_FILE_PATH, SEED_FILE_NAME);

    var seederMap = Stream.of(randomSeeder, fileSeeder)
        .collect(toMap(Seeder::getType, Function.identity()));
    var seederAdaptor = new SeederAdaptor(seederMap, false);

    var grid = new Grid(randomSeeder.seed());
    var game = new Game(
        consumerAdaptor,
        seederAdaptor,
        grid,
        TICK_DURATION);

    game.run();
    assertEquals(10, game.getGrid().rows());
    assertEquals(10, game.getGrid().columns());

    seederAdaptor.setColumns(5);
    seederAdaptor.setRows(5);
    game.reset();

    assertEquals(5, game.getGrid().rows());
    assertEquals(5, game.getGrid().columns());
  }
}
