package org.jonasa;

import lombok.Generated;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Setter
public class Grid {
    private final List<List<Cell>> grid;

    public Grid(int rows, int columns) {
        this.grid = new ArrayList<>(rows);
        initialize(rows, columns);
    }

    public Grid(List<List<Cell>> grid) {
        this.grid = grid;
    }

    public void tick() {
        log.info(this.toString());
        for (int i = 0; i < rows(); i++) {
            for (int j = 0; j < columns(); j++) {
                get(j, i).setNeighbours(getNeighbours(j, i));
            }
        }
        grid.forEach(row -> row.forEach(Cell::tick));
    }

    public int rows() {
        return grid.size();
    }

    public int columns() {
        return grid.get(0).size();
    }

    private Cell get(int x, int y) {
        return grid.get(y).get(x);
    }

    private int getNeighbours(int x, int y) {
        int nextX = (x == columns() - 1) ? 0 : x + 1;
        int prevX = (x == 0) ? columns() - 1 : x - 1;
        int nextY = (y == rows() - 1) ? 0 : y + 1;
        int prevY = (y == 0) ? rows() - 1 : y - 1;

        return (int) Stream.of(
                get(nextX, y),
                get(prevX, y),
                get(nextX, nextY),
                get(prevX, prevY),
                get(prevX, nextY),
                get(nextX, prevY),
                get(x, prevY),
                get(x, nextY)
        ).filter(Cell::isAlive).count();
    }

    private void initialize(int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) row.add(new Cell());
            grid.add(row);
        }
    }

    @Generated
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        grid.forEach(row -> sb.append(row).append("\n"));
        return sb.toString();
    }
}
