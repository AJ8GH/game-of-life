package org.jonasa;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
public class Grid {
    private List<List<Cell>> grid;

    public Grid(int rows, int columns) {
        this.grid = new ArrayList<>(rows);
        initialize(rows, columns);
    }

    public void tick() {
        for (int i = 0; i < rows(); i++) {
            for (int j = 0; j < columns(); j++) {
                Cell cell = get(i, j);
                List<Cell> liveNeighbours = getNeighbours(j, i);
                if (liveNeighbours.size() < 2) {
                    cell.kill();
                } else if (liveNeighbours.size() > 3) {
                    cell.kill();
                } else {
                    cell.revive();
                }
            }
        }
    }

    private Cell get(int x, int y) {
        return grid.get(y).get(x);
    }

    private List<Cell> getNeighbours(int x, int y) {
        int prevX = (x == 0) ? x = columns() - 1 : x - 1;
        int prevY = (y == 0) ? y = rows() - 1 : y - 1;
        int nextX = (x == columns() - 1) ? 0 : x + 1;
        int nextY = (y == rows() - 1) ? 0 : y + 1;

        List<Cell> neighbours = new ArrayList<>();
        neighbours.add(get(nextX, y));
        neighbours.add(get(prevX, y));
        neighbours.add(get(nextX, nextY));
        neighbours.add(get(prevX, prevY));
        neighbours.add(get(prevX, nextY));
        neighbours.add(get(nextX, prevY));
        neighbours.add(get(x, prevY));
        neighbours.add(get(x, nextY));

        return neighbours.stream()
                .filter(Cell::isAlive)
                .collect(Collectors.toList());
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

    private void initialize(int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) row.add(new Cell());
            grid.add(row);
        }
    }
}
