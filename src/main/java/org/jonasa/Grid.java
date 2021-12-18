package org.jonasa;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final List<List<Cell>> grid;

    public Grid(int rows, int columns) {
        this.grid  = new ArrayList<>(rows);
        initializeGrid(rows, columns);
    }

    public int rows() {
        return grid.size();
    }

    public int columns() {
        return grid.get(0).size();
    }

    private void initializeGrid(int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            List<Cell> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) row.add(new Cell());
            grid.add(row);
        }
    }
}
