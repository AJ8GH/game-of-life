package org.jonasa;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final List<List<Cell>> grid;

    public Grid(GridInitializer initializer, int rows, int columns) {
        this.grid = new ArrayList<>(rows);
        initializer.initialize(grid, rows, columns);
    }

    public List<List<Cell>> getGrid() {
        return new ArrayList<>(grid);
    }

    public int rows() {
        return grid.size();
    }

    public int columns() {
        return grid.get(0).size();
    }
}
