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
    private static final String FILE_PATH = "src/main/resources/overrides.properties";

    static {
        try (FileInputStream in = new FileInputStream(FILE_PATH)) {
            PROPS.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Game game() {
        return new Game(ui(), grid(), getInt("game.tickDuration"));
    }

    private static Seeder seeder() {
        int rows = getInt("seeder.rows");
        int columns = getInt("seeder.columns");
        String seedFilePath = getString("seeder.filePath");

        return seedFromFile() ?
                new FileSeeder(rows, columns, seedFilePath) :
                new RandomSeeder(rows, columns);
    }

    private static UI ui() {
        return new TerminalUi();
    }

    private static Grid grid() {
        return new Grid(seeder().seed());
    }

    private static String getString(String key) {
        return (String) PROPS.get(key);
    }

    private static boolean seedFromFile() {
        return Boolean.parseBoolean(getString("seeder.fromFile"));
    }

    private static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }
}
