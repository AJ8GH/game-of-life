package org.jonasa;

import java.util.ArrayList;
import java.util.List;

public class GridInitializer {
    public void initialize(List<List<Cell>> grid, int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) row.add(new Cell());
            grid.add(row);
        }
    }
}
