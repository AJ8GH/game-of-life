package org.jonasa.gameoflife.application;

import org.jonasa.gameoflife.apiclient.ApiClient;
import org.jonasa.gameoflife.domain.Grid;
import org.jonasa.gameoflife.seeder.FileSeeder;
import org.jonasa.gameoflife.seeder.RandomSeeder;
import org.jonasa.gameoflife.seeder.Seeder;
import org.jonasa.gameoflife.ui.Terminal;
import org.jonasa.gameoflife.ui.UI;
import org.jonasa.gameoflife.ui.Web;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties PROPS = new Properties();
    private static final String PROPS_PATH = "src/main/resources/conf/overrides.properties";

    private static final String GAME_TICK_DURATION_CONFIG = "game.tickDuration";

    private static final String UI_IMPL_CONFIG = "ui.terminal";
    private static final String UI_INFO_CONFIG = "ui.info";

    private static final String SEED_ROWS_CONFIG = "seeder.rows";
    private static final String SEED_COLUMNS_CONFIG = "seeder.columns";
    private static final String SEED_PATH_CONFIG = "seeder.filePath";
    private static final String SEED_FROM_FILE_CONFIG = "seeder.fromFile";

    static {
        try (FileInputStream in = new FileInputStream(getPath(PROPS_PATH))) {
            PROPS.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Game game() {
        return new Game(ui(), grid(), getInt(GAME_TICK_DURATION_CONFIG));
    }

    private static Seeder seeder() {
        int rows = getInt(SEED_ROWS_CONFIG);
        int columns = getInt(SEED_COLUMNS_CONFIG);
        String seedFilePath = getPath(getString(SEED_PATH_CONFIG));

        return getBoolean(SEED_FROM_FILE_CONFIG) ?
                new FileSeeder(rows, columns, seedFilePath) :
                new RandomSeeder(rows, columns);
    }

    private static UI ui() {
        return (getBoolean(UI_IMPL_CONFIG)) ?
                new Terminal(getBoolean(UI_INFO_CONFIG)) :
                new Web(new ApiClient(new RestTemplate()));
    }

    private static Grid grid() {
        return new Grid(seeder().seed());
    }

    private static String getString(String key) {
        return (String) PROPS.get(key);
    }

    private static boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    private static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    private static String getPath(String path) {
        String dir = System.getProperty("user.dir");
        String base = dir.split("game-of-life/")[0];
        return base + (base.endsWith("game-of-life") ?
                "/application/" : "game-of-life/application/") + path;
    }
}
