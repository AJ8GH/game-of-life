package aj8gh.gameoflife.integration;

import aj8gh.gameoflife.apiclient.ApiClient;
import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.consumer.ApiConsumer;
import aj8gh.gameoflife.consumer.CliConsumer;
import aj8gh.gameoflife.consumer.ConsumerAdaptor;
import aj8gh.gameoflife.domain.Grid;
import aj8gh.gameoflife.seeder.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class AppIntegrationTest {
    private static final String SCHEME = "http";
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String PATH = "/queue/enqueue";

    private static final int ROWS = 10;
    private static final int COLUMNS = 10;
    private static final String SEED_FILE_PATH = "src/test/resources/";
    private static final String SEED_FILE_NAME = "test_seed.csv";
    private static final int TICK_DURATION = 50;

    private Game game;
    private SeederAdaptor seederAdaptor;
    private ConsumerAdaptor consumerAdaptor;

    @BeforeEach
    void setUp() {
       consumerAdaptor = new ConsumerAdaptor(Set.of(
                new CliConsumer(false),
                new ApiConsumer(new ApiClient(SCHEME, HOST, PORT, PATH, new RestTemplate()))
        ));

        Seeder randomSeeder = new RandomSeeder(ROWS, COLUMNS);
        Seeder fileSeeder = new FileSeeder(ROWS, COLUMNS, SEED_FILE_PATH, SEED_FILE_NAME);

        Map<AbstractSeeder.SeederType, Seeder> seederMap = new HashMap<>();
        seederMap.put(randomSeeder.getType(), randomSeeder);
        seederMap.put(fileSeeder.getType(), fileSeeder);
        seederAdaptor = new SeederAdaptor(seederMap, false);

        Grid grid = new Grid(randomSeeder.seed());
        game = new Game(
                consumerAdaptor,
                seederAdaptor,
                grid,
                TICK_DURATION);
    }

    @AfterEach
    void tearDown() {
        if (game.isRunning()) {
            game.stop();
        }
    }

    @Test
    void seederAdaptor_setRowsAndColumns_ResetGameGridHasNewSize() {
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
