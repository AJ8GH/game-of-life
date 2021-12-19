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
    void tick_TicksEveryCell() {
        Cell cell = mock(Cell.class);
        List<Cell> row = List.of(cell, cell, cell);
        List<List<Cell>> cells = List.of(row, row, row);

        Grid victim = new Grid(cells);
        victim.tick();

        verify(cell, times(9)).tick();
    }

    @Test
    void tick_SetsCorrectNumberOfNeighboursForEachCell() {
        Cell liveCell = mock(Cell.class, "liveCell");
        when(liveCell.isAlive()).thenReturn(true);
        when(liveCell.toString()).thenReturn("1");

        Cell deadCell = mock(Cell.class, "deadCell");
        when(deadCell.isAlive()).thenReturn(false);
        when(deadCell.toString()).thenReturn("0");

        List<Cell> deadRow = List.of(deadCell, deadCell, deadCell, deadCell, deadCell, deadCell, deadCell);
        List<Cell> liveRow = List.of(deadCell, deadCell, liveCell, liveCell, liveCell, deadCell, deadCell);
        List<List<Cell>> cells = List.of(deadRow, deadRow, liveRow, liveRow, liveRow, deadRow, deadRow);

        Grid victim = new Grid(cells);
        victim.tick();

        verify(deadCell, times(24)).setNeighbours(0);
        verify(deadCell, times(4)).setNeighbours(1);
        verify(deadCell, times(8)).setNeighbours(2);
        verify(deadCell, times(4)).setNeighbours(3);

        verify(liveCell, times(4)).setNeighbours(3);
        verify(liveCell, times(4)).setNeighbours(5);
        verify(liveCell, times(1)).setNeighbours(8);
    }

    @Test
    void tick_GridWrapsCorrectly() {
        Cell liveCell = mock(Cell.class, "liveCell");
        when(liveCell.isAlive()).thenReturn(true);
        when(liveCell.toString()).thenReturn("1");

        Cell deadCell = mock(Cell.class, "deadCell");
        when(deadCell.isAlive()).thenReturn(false);
        when(deadCell.toString()).thenReturn("0");

        Cell topLeft = mock(Cell.class, "topLeft");
        when(topLeft.isAlive()).thenReturn(true);
        when(topLeft.toString()).thenReturn("1");

        Cell topRight = mock(Cell.class, "topRight");
        when(topRight.isAlive()).thenReturn(true);
        when(topRight.toString()).thenReturn("1");

        Cell bottomLeft = mock(Cell.class, "bottomLeft");
        when(bottomLeft.isAlive()).thenReturn(true);
        when(bottomLeft.toString()).thenReturn("1");

        Cell bottomRight = mock(Cell.class, "bottomRight");
        when(bottomRight.isAlive()).thenReturn(true);
        when(bottomRight.toString()).thenReturn("1");

        List<Cell> topRow = List.of(topLeft, liveCell, deadCell, deadCell, topRight);
        List<Cell> row2 = List.of(liveCell, deadCell, deadCell, deadCell, deadCell);
        List<Cell> deadRow = List.of(deadCell, deadCell, deadCell, deadCell, deadCell);
        List<Cell> bottomRow = List.of(bottomLeft, deadCell, deadCell, deadCell, bottomRight);
        List<List<Cell>> cells = List.of(topRow, row2, deadRow, deadRow, bottomRow);

        Grid victim = new Grid(cells);
        victim.tick();

        verify(topLeft, times(1)).setNeighbours(5);
        verify(bottomRight, times(1)).setNeighbours(3);
        verify(topRight, times(1)).setNeighbours(4);
        verify(bottomLeft, times(1)).setNeighbours(4);
    }
}
