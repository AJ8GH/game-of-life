package org.jonasa.seeder;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeserializerTest {
    @Test
    void readSeedFile() {
        Deserializer deserializer = new Deserializer();
        deserializer.setSeedFilePath("src/test/resources/test_seed.csv");
        List<Integer> seed = deserializer.readSeedFile();
        List<Integer> expected = List.of(15, 20, 0, 1, 5, 6, 3, 4, 10, 12, 15, 18);
        assertEquals(expected, seed);
    }
}
