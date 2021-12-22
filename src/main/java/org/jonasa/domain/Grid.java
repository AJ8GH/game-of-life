package org.jonasa.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Grid {
    private final List<List<Cell>> grid;
    private boolean wraps;

    public void tick() {
        for (int i = 0; i < rows(); i++) {
            for (int j = 0; j < columns(); j++) {
                get(i, j).setNeighbours(getNeighbours(i, j));
            }
        }
        grid.forEach(row -> row.forEach(Cell::tick));
    }

    public long population() {
        return grid.stream()
                .flatMap(Collection::stream)
                .filter(Cell::isAlive).count();
    }

    public Cell get(int y, int x) {
        return grid.get(y).get(x);
    }

    public int rows() {
        return grid.size();
    }

    public int columns() {
        return grid.get(0).size();
    }

    private int getNeighbours(int y, int x) {
        return wraps ? getNeighboursWithWrap(y, x) : getNeighboursNoWrap(y, x);
    }

    private int getNeighboursWithWrap(int y, int x) {
        int nextX = (x == columns() - 1) ? 0 : x + 1;
        int prevX = (x == 0) ? columns() - 1 : x - 1;
        int nextY = (y == rows() - 1) ? 0 : y + 1;
        int prevY = (y == 0) ? rows() - 1 : y - 1;

        return (int) Stream.of(
                get(nextY, nextX),
                get(prevY, prevX),
                get(nextY, prevX),
                get(prevY, nextX),
                get(y, nextX),
                get(y, prevX),
                get(prevY, x),
                get(nextY, x)
        ).filter(Cell::isAlive).count();
    }

    private int getNeighboursNoWrap(int y, int x) {
        List<Cell> neighbours = new ArrayList<>();
        if (y < rows() - 1 && x < columns() - 1) neighbours.add(get(y + 1, x + 1));
        if (y > 0 && x > 0) neighbours.add(get(y - 1, x - 1));
        if (y < rows() - 1 && x > 0) neighbours.add(get(y + 1, x - 1));
        if (y > 0 && x < columns() - 1) neighbours.add(get(y - 1, x + 1));
        if (x < columns() - 1) neighbours.add(get(y, x + 1));
        if (y < rows() - 1) neighbours.add(get(y + 1, x));
        if (x > 0) neighbours.add(get(y, x - 1));
        if (y > 0) neighbours.add(get(y - 1, x));
        return (int) neighbours.stream().filter(Cell::isAlive).count();
    }

    @Override
    @Generated
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        grid.forEach(row -> {
            row.forEach(sb::append);
            sb.append("\n");
        });
        return sb.toString();
    }
}
