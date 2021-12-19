package org.jonasa;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GridTest {
    @Test
    void Grid() {
        Grid victim = new Grid(10, 12);
        assertEquals(10, victim.rows());
        assertEquals(12, victim.columns());
    }

    @Test
    void tick() {
        Grid victim = new Grid(3, 3);

        Cell isolatedCell = mock(Cell.class);
        when(isolatedCell.isAlive()).thenReturn(true);

        Cell deadCell = mock(Cell.class);
        when(deadCell.isAlive()).thenReturn(false);

        List<Cell> deadCellRow = List.of(deadCell, deadCell, deadCell, deadCell, deadCell);
        List<Cell> isolatedCellRow = List.of(deadCell, deadCell, isolatedCell, deadCell, deadCell);
        List<List<Cell>> cells = List.of(deadCellRow, deadCellRow, isolatedCellRow, deadCellRow, deadCellRow);

        victim.setGrid(cells);

        victim.tick();
        verify(isolatedCell).kill();
        verify(deadCell, never()).revive();
    }
}
