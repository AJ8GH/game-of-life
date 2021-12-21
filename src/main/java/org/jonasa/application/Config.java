package org.jonasa.application;

import org.jonasa.domain.Grid;
import org.jonasa.seeder.Deserializer;
import org.jonasa.seeder.Seeder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Config {
    private static final Properties PROPS = new Properties();
    private static final String FILE_PATH = "src/main/resources/overrides.properties";
    private static final Deserializer DESERIALIZER;
    private static final Seeder SEEDER;
    private static final Grid GRID;
    private static final Game GAME;

    static {
        loadProperties();
        GRID = new Grid(new ArrayList<>());
        DESERIALIZER = new Deserializer();
        SEEDER = new Seeder(DESERIALIZER);
        GAME = new Game(GRID, SEEDER);
    }

    public static Game game() {
        return GAME;
    }

    public static Seeder seeder() {
        return SEEDER;
    }

    public static Grid grid() {
        return GRID;
    }

    public static Deserializer deserializer() {
        return DESERIALIZER;
    }

    public static boolean seedFromFile() {
        return Boolean.parseBoolean((String) PROPS.get("seeder.seed.fromFile"));
    }

    public static String getString(String key) {
        return (String) PROPS.get(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    private static void loadProperties() {
        try (FileInputStream in = new FileInputStream(FILE_PATH)) {
            PROPS.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
