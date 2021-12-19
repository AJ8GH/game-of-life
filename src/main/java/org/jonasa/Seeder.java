package org.jonasa;

import java.util.ArrayList;
import java.util.List;

public class Seeder {
    public List<List<Cell>> seed(int columns, int rows) {
        List<List<Cell>> grid = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) row.add(new Cell());
            grid.add(row);
        }
        return grid;
    }
}
