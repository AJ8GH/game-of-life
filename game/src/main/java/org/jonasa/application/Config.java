package org.jonasa.application;

import org.jonasa.domain.Grid;
import org.jonasa.seeder.FileSeeder;
import org.jonasa.seeder.RandomSeeder;
import org.jonasa.seeder.Seeder;
import org.jonasa.ui.TerminalUi;
import org.jonasa.ui.UI;

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

    public static org.jonasa.application.Game game() {
        return new org.jonasa.application.Game(ui(), grid(), getInt(GAME_TICK_DURATION_CONFIG));
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
                new TerminalUi(getBoolean(UI_INFO_CONFIG)) : null;
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
        return dir.endsWith("game") ? path : "game/" + path;
    }
}