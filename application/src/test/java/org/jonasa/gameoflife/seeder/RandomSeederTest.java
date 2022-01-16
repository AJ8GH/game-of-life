package org.jonasa.gameoflife.seeder;

import org.jonasa.gameoflife.domain.Cell;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomSeederTest {
    @Test
    void seed() {
        Seeder randomSeeder = new RandomSeeder(5, 8);

        List<List<Cell>> seed = randomSeeder.seed();

        assertEquals(5, seed.size());
        assertEquals(8, seed.get(0).size());

        assertTrue(seed.stream()
                .flatMap(Collection::stream)
                .anyMatch(Cell::isAlive));

        assertTrue(seed.stream()
                .flatMap(Collection::stream)
                .anyMatch(cell -> !cell.isAlive()));
    }
}
