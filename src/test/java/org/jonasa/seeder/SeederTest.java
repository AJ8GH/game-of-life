package org.jonasa.seeder;

import org.jonasa.domain.Cell;
import org.jonasa.domain.Grid;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SeederTest {
    @Test
    void seed_GridSize() {
        Deserializer deserializer = mock(Deserializer.class);
        Seeder seeder = new Seeder(deserializer);
        seeder.setRows(5);
        seeder.setColumns(8);
        Grid grid = new Grid(new ArrayList<>());
        seeder.seed(grid);
        assertEquals(5, grid.rows());
        assertEquals(8, grid.columns());
    }

    @Test
    void seedFromFile() {
        Deserializer deserializer = mock(Deserializer.class);
        List<Integer> seed = List.of(0, 1, 2, 2, 1, 0);
        when(deserializer.readSeedFile()).thenReturn(seed);

        Grid grid = mock(Grid.class);
        List<List<Cell>> cells = new ArrayList<>();
        when(grid.getGrid()).thenReturn(cells);

        Cell cell = mock(Cell.class);
        when(grid.get(0, 1)).thenReturn(cell);
        when(grid.get(2, 2)).thenReturn(cell);
        when(grid.get(1, 0)).thenReturn(cell);

        Seeder seeder = new Seeder(deserializer);
        seeder.setSeedFromFile(true);
        seeder.setRows(3);
        seeder.setColumns(3);
        seeder.seed(grid);

        assertEquals(3, cells.size());
        assertEquals(3, cells.get(0).size());

        verify(deserializer).readSeedFile();
        verify(cell, times(3)).live();
    }
}
