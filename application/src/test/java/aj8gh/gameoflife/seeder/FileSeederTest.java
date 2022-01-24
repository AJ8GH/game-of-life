package aj8gh.gameoflife.seeder;

import aj8gh.gameoflife.domain.Cell;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSeederTest {
    @Test
    void seed() {
        String seedFilePath = "src/test/resources/test_seed.csv";
        int rows = 3;
        int columns = 3;
        Seeder fileSeeder = new FileSeeder(rows, columns, seedFilePath);

        List<List<Cell>> seed = fileSeeder.seed();

        assertEquals(3, seed.size());
        assertEquals(3, seed.get(0).size());
        assertTrue(seed.get(0).get(1).isAlive());
        assertTrue(seed.get(2).get(2).isAlive());
        assertTrue(seed.get(1).get(0).isAlive());

        assertEquals(3, seed.stream()
                .flatMap(Collection::stream)
                .filter(Cell::isAlive).count());
    }
}
