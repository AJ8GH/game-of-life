package org.jonasa.seeder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jonasa.application.Config;
import org.jonasa.domain.Cell;
import org.jonasa.domain.Grid;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
public class Seeder {
    private final Deserializer deserializer;

    private boolean seedFromFile = Config.seedFromFile();
    private int rows = Config.getInt("game.seeder.rows");
    private int columns = Config.getInt("game.seeder.columns");

    public void seed(Grid grid) {
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) addCell(row);
            grid.getGrid().add(row);
        }
        if (seedFromFile) seedFromFile(grid);
    }

    private void seedFromFile(Grid grid) {
        List<Integer> seed = deserializer.readSeedFile();
        log.info(seed.toString());
        for (int i = 0; i < seed.size(); i++) {
            grid.get(seed.get(i), seed.get(++i)).live();
            log.info(grid.toString());
        }
    }

    private void addCell(List<Cell> row) {
        Cell cell = new Cell();
        row.add(cell);
        if (seedFromFile) cell.die();
    }
}
