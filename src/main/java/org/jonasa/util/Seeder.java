package org.jonasa.util;

import org.jonasa.domain.Cell;
import org.jonasa.domain.Grid;

import java.util.ArrayList;

public class Seeder {
    public void seed(Grid grid, int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) row.add(new Cell());
            grid.getGrid().add(row);
        }
    }
}
