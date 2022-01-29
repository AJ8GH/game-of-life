package aj8gh.gameoflife.seeder;

import aj8gh.gameoflife.domain.Cell;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
public class FileSeeder extends AbstractSeeder {
    private final String seedFilePath;
    private String seedFileName;

    public FileSeeder(int rows, int columns,
                      String seedFilePath, String seedFileName) {
        this.rows = rows;
        this.columns = columns;
        this.seedFilePath = seedFilePath;
        this.seedFileName = seedFileName;
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

    private List<Integer> deserialize() {
        List<Integer> seed = new ArrayList<>();
        try (var reader = new BufferedReader(
                new FileReader(seedFilePath + seedFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(",");
                seed.add(Integer.valueOf(splitLine[0]));
                seed.add(Integer.valueOf(splitLine[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return seed;
    }
}
