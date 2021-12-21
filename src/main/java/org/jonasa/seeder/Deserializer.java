package org.jonasa.seeder;

import lombok.Setter;
import org.jonasa.application.Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Setter
public class Deserializer {
    private String seedFilePath = Config.getString("seeder.seed.filePath");

    public List<Integer> readSeedFile() {
        List<Integer> seed = new ArrayList<>();
        try (var reader = new BufferedReader(new FileReader(seedFilePath))) {
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
