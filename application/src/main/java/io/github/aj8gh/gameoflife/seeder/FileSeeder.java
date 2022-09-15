package io.github.aj8gh.gameoflife.seeder;

import static io.github.aj8gh.gameoflife.seeder.AbstractSeeder.SeederType.FILE;

import io.github.aj8gh.gameoflife.domain.Cell;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FileSeeder extends AbstractSeeder {

  private static final String COMMA_DELIMITER = ",";
  private static final SeederType type = FILE;

  private final Set<String> files;
  private final String seedFilePath;

  private String seedFileName;

  public FileSeeder(int rows, int columns, String seedFilePath,
      String seedFileName) {
    super(rows, columns);
    this.seedFilePath = seedFilePath;
    this.seedFileName = seedFileName;
    this.files = cacheFileNames();
  }

  @Override
  public List<List<Cell>> seed() {
    List<Integer> seedData = deserialize();
    List<List<Cell>> seed = generateSeed();
    seed.stream().flatMap(Collection::stream).forEach(Cell::die);
    for (int i = 0; i < seedData.size(); i++) {
      seed.get(seedData.get(i)).get(seedData.get(++i)).live();
    }
    return seed;
  }

  @Override
  public SeederType getType() {
    return type;
  }

  String getSeedFileName() {
    return seedFileName;
  }

  void setSeedFileName(String seedFileName) throws FileNotFoundException {
    if (!files.contains(seedFileName)) {
      throw new FileNotFoundException("Non-existent file: " + seedFileName);
    }
    this.seedFileName = seedFileName;
  }

  private List<Integer> deserialize() {
    List<Integer> seed = new ArrayList<>();
    try (var reader = new BufferedReader(
        new FileReader(seedFilePath + seedFileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] splitLine = line.split(COMMA_DELIMITER);
        seed.add(Integer.valueOf(splitLine[0]));
        seed.add(Integer.valueOf(splitLine[1]));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return seed;
  }

  private Set<String> cacheFileNames() {
    File directory = new File(seedFilePath);
    return new HashSet<>(Arrays.asList(
        Objects.requireNonNull(directory.list())));
  }
}
