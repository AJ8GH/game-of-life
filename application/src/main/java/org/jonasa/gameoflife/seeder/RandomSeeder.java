package org.jonasa.gameoflife.seeder;

import org.jonasa.gameoflife.domain.Cell;

import java.util.List;

public class RandomSeeder extends AbstractSeeder {
    public RandomSeeder(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public List<List<Cell>> seed() {
        return generateSeed();
    }
}
