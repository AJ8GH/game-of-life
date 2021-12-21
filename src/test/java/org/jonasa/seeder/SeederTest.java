package org.jonasa.seeder;

import org.jonasa.domain.Cell;
import org.jonasa.domain.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SeederTest {
    @Mock
    private Grid grid;
    @Mock
    private Cell cell;
    @Mock
    private Deserializer deserializer;
    private Seeder seeder;
    private List<List<Cell>> cells;

    @BeforeEach
    void setUp() {
        openMocks(this);
        cells = new ArrayList<>();
        when(grid.getGrid()).thenReturn(cells);
        seeder = new Seeder(deserializer);
    }

    @Test
    void seed_FromFileFalse() {
        seeder.setRows(5);
        seeder.setColumns(8);
        seeder.setSeedFromFile(false);

        seeder.seed(grid);

        verify(grid, times(5)).getGrid();
        assertEquals(5, cells.size());
        assertEquals(8, cells.get(0).size());
        assertTrue(grid.getGrid().stream()
                .flatMap(Collection::stream)
                .anyMatch(Cell::isAlive));
    }

    @Test
    void seed_FromFileTrue() {
        List<Integer> seed = List.of(0, 1, 2, 2, 1, 0);
        when(deserializer.readSeedFile()).thenReturn(seed);
        when(grid.get(0, 1)).thenReturn(cell);
        when(grid.get(2, 2)).thenReturn(cell);
        when(grid.get(1, 0)).thenReturn(cell);
        seeder.setSeedFromFile(true);
        seeder.setRows(3);
        seeder.setColumns(3);

        seeder.seed(grid);

        verify(grid, times(3)).getGrid();
        assertEquals(3, cells.size());
        assertEquals(3, cells.get(0).size());
        verify(deserializer).readSeedFile();
        verify(cell, times(3)).live();
        assertTrue(cells.stream()
                .flatMap(Collection::stream)
                .noneMatch(Cell::isAlive));
    }
}
