package org.jonasa.gameoflife.seeder;

import org.jonasa.gameoflife.domain.Cell;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSeeder implements Seeder {
    protected int rows;
    protected int columns;

    public abstract List<List<Cell>> seed();

    protected List<List<Cell>> generateSeed() {
        List<List<Cell>> seed = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) row.add(new Cell());
            seed.add(row);
        }
        return seed;
    }
}
