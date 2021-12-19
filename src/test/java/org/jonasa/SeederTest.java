package org.jonasa;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeederTest {
    @Test
    void seed() {
        Seeder seeder = new Seeder();

        List<List<Cell>> seed = seeder.seed(5, 10);

        assertEquals(10, seed.size());
        assertEquals(5, seed.get(0).size());
    }
}
