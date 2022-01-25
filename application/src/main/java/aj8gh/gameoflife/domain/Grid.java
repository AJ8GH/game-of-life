package aj8gh.gameoflife.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class Grid {
    private List<List<Cell>> grid;

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
