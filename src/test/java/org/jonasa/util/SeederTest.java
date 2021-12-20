package org.jonasa.util;

import org.jonasa.domain.Grid;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SeederTest {
    @Test
    void seed_GridSize() {
        Seeder seeder = new Seeder();
        Grid grid = new Grid(new ArrayList<>());
        seeder.seed(grid, 5, 8);
        assertEquals(5, grid.rows());
        assertEquals(8, grid.columns());
    }
}
