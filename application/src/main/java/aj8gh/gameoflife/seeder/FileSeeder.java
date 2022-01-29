package aj8gh.gameoflife.seeder;

import aj8gh.gameoflife.domain.Cell;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Getter
public class FileSeeder extends AbstractSeeder {
    private static final String COMMA_DELIMITER = ",";
    private final Set<String> files;
    private final String seedFilePath;
    private String seedFileName;

    public FileSeeder(int rows, int columns,
                      String seedFilePath, String seedFileName) {
        this.rows = rows;
        this.columns = columns;
        this.seedFilePath = seedFilePath;
        this.seedFileName = seedFileName;
        this.files = cacheFileNames();
    }

    public List<List<Cell>> seed() {
        List<Integer> seedData = deserialize();
        List<List<Cell>> seed = generateSeed();
        seed.stream().flatMap(Collection::stream).forEach(Cell::die);
        for (int i = 0; i < seedData.size(); i++) {
            seed.get(seedData.get(i)).get(seedData.get(++i)).live();
        }
        return seed;
    }

    public void setSeedFileName(String seedFileName) {
        if (!files.contains(seedFileName)) {
            throw new IllegalArgumentException("Non-existent file: " + seedFileName);
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
