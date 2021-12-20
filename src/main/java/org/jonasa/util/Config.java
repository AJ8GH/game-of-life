package org.jonasa.util;

import org.jonasa.application.Game;
import org.jonasa.domain.Grid;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Config {
    private static final Properties PROPS = new Properties();
    private static final String FILE_PATH = "src/main/resources/overrides.properties";
    private static final Seeder SEEDER;
    private static final Grid GRID;
    private static final Game GAME;

    static {
        loadProperties();
        GRID = new Grid(new ArrayList<>());
        SEEDER = new Seeder();
        GAME = new Game(GRID, SEEDER);
    }

    public static String getString(String key) {
        return (String) PROPS.get(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public static Game game() {
        return GAME;
    }

    public static boolean seedFromFile() {
        return (boolean) PROPS.get("seeder.seed.fromFile");
    }

    private static void loadProperties() {
        try (FileInputStream in = new FileInputStream(FILE_PATH)) {
            PROPS.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
