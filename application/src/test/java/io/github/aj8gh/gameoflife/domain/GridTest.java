package io.github.aj8gh.gameoflife.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;

class GridTest {

  @Test
  void population() {
    Cell liveCell = mock(Cell.class, "*");
    when(liveCell.isAlive()).thenReturn(true);

    Cell deadCell = mock(Cell.class, "*");
    when(deadCell.isAlive()).thenReturn(false);

    List<Cell> row1 = List.of(deadCell, liveCell, liveCell);
    List<Cell> row2 = List.of(deadCell, deadCell, liveCell);
    List<Cell> row3 = List.of(deadCell, deadCell, deadCell);
    List<List<Cell>> cells = List.of(row1, row2, row3);

    Grid grid = new Grid(cells);
    assertEquals(3, grid.population());
  }

  @Test
  void tick_TicksEveryCell() {
    Cell cell = mock(Cell.class, "*");
    List<Cell> row = List.of(cell, cell, cell);
    List<List<Cell>> cells = List.of(row, row, row);

    Grid grid = new Grid(cells);
    grid.tick();

    verify(cell, times(9)).tick();
  }

  @Test
  void tick_SetsCorrectNumberOfNeighboursForEachCell() {
    Cell liveCell = mock(Cell.class, "*");
    when(liveCell.isAlive()).thenReturn(true);

    Cell deadCell = mock(Cell.class, "-");
    when(deadCell.isAlive()).thenReturn(false);

    List<Cell> deadRow = List.of(deadCell, deadCell, deadCell, deadCell, deadCell, deadCell,
        deadCell);
    List<Cell> liveRow = List.of(deadCell, deadCell, liveCell, liveCell, liveCell, deadCell,
        deadCell);
    List<List<Cell>> cells = List.of(deadRow, deadRow, liveRow, liveRow, liveRow, deadRow, deadRow);

    Grid grid = new Grid(cells);
    grid.tick();

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
    Cell liveCell = mock(Cell.class, "*");
    when(liveCell.isAlive()).thenReturn(true);

    Cell deadCell = mock(Cell.class, "-");
    when(deadCell.isAlive()).thenReturn(false);

    Cell topLeft = mock(Cell.class, "L");
    when(topLeft.isAlive()).thenReturn(true);

    Cell topRight = mock(Cell.class, "R");
    when(topRight.isAlive()).thenReturn(true);

    Cell bottomLeft = mock(Cell.class, "l");
    when(bottomLeft.isAlive()).thenReturn(true);

    Cell bottomRight = mock(Cell.class, "r");
    when(bottomRight.isAlive()).thenReturn(true);

    List<Cell> topRow = List.of(topLeft, liveCell, deadCell, deadCell, topRight);
    List<Cell> row2 = List.of(liveCell, deadCell, deadCell, deadCell, deadCell);
    List<Cell> deadRow = List.of(deadCell, deadCell, deadCell, deadCell, deadCell);
    List<Cell> bottomRow = List.of(bottomLeft, deadCell, deadCell, deadCell, bottomRight);
    List<List<Cell>> cells = List.of(topRow, row2, deadRow, deadRow, bottomRow);

    Grid grid = new Grid(cells);
    grid.tick();

    verify(topLeft, times(1)).setNeighbours(5);
    verify(bottomRight, times(1)).setNeighbours(3);
    verify(topRight, times(1)).setNeighbours(4);
    verify(bottomLeft, times(1)).setNeighbours(4);
  }
}
