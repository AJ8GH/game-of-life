package io.github.aj8gh.gameoflife.seeder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.aj8gh.gameoflife.domain.Cell;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;

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
