package io.github.aj8gh.gameoflife.seeder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.aj8gh.gameoflife.domain.Cell;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileSeederTest {

  private FileSeeder fileSeeder;

  @BeforeEach
  void setUp() {
    String seedFilePath = "src/test/resources/";
    String seedFileName = "test_seed.csv";
    int rows = 3;
    int columns = 3;
    fileSeeder = new FileSeeder(rows, columns, seedFilePath, seedFileName);
  }

  @Test
  void seed() {
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

  @Test
  void setSeedFileName_WhenFileExists_SetsNewFileName() throws FileNotFoundException {
    String fileName = "test_seed.csv";
    fileSeeder.setSeedFileName(fileName);
    assertEquals(fileName, fileSeeder.getSeedFileName());
  }

  @Test
  void setSeedFileName_WhenNoFileExists_ThrowsException() {
    assertThrows(FileNotFoundException.class, () -> fileSeeder.setSeedFileName("bad_file.csv"));
  }
}
