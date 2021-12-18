package org.jonasa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {
    @Test
    void Grid() {
        Grid grid = new Grid(10, 10);
        assertEquals(10, grid.rows());
        assertEquals(10, grid.columns());
    }
}
