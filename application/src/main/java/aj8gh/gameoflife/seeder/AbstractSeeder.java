package aj8gh.gameoflife.seeder;

import aj8gh.gameoflife.domain.Cell;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSeeder implements Seeder {
    public enum SeederType {
        FILE("FILE"),
        RANDOM("RANDOM");

        private final String label;

        SeederType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private int rows;
    private int columns;

    protected AbstractSeeder(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    List<List<Cell>> generateSeed() {
        List<List<Cell>> seed = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>(columns);
            for (int j = 0; j < columns; j++) row.add(new Cell());
            seed.add(row);
        }
        return seed;
    }

    void setRows(int rows) {
        validatePositiveInt(rows);
        this.rows = rows;
    }

    void setColumns(int columns) {
        validatePositiveInt(columns);
        this.columns = columns;
    }

    int getRows() {
        return rows;
    }

    int getColumns() {
        return columns;
    }

    void validatePositiveInt(int argument) {
        if (argument <= 0) {
            throw new IllegalArgumentException(
                    String.format("Illegal Argument %s is not > 0", argument));
        }
    }
}
