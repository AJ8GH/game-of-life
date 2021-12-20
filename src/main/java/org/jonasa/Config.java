package org.jonasa;

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

    public static String getString(String key) {
        return (String) PROPS.get(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }
}
