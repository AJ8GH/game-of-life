package org.jonasa;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GridInitializerTest {
    @Test
    void initialize() {
        GridInitializer victim = new GridInitializer();
        List<List<Cell>> grid = new ArrayList<>();
        victim.initialize(grid, 5, 8);

        assertEquals(5, grid.size());
        assertEquals(8, grid.get(0).size());
    }
}
