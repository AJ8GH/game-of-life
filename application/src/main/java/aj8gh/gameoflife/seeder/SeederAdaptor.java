package aj8gh.gameoflife.seeder;

import aj8gh.gameoflife.domain.Cell;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static aj8gh.gameoflife.seeder.AbstractSeeder.SeederType;
import static aj8gh.gameoflife.seeder.AbstractSeeder.SeederType.FILE;
import static aj8gh.gameoflife.seeder.AbstractSeeder.SeederType.RANDOM;

public class SeederAdaptor implements Seeder {
    private final Map<SeederType, Seeder> seederMap;
    private Seeder active;
    private Seeder inactive;

    public SeederAdaptor(Map<SeederType, Seeder> seederMap, boolean fromFile) {
        this.seederMap = seederMap;
        this.active = seederMap.get(fromFile ? FILE : RANDOM);
        this.inactive = seederMap.get(fromFile ? RANDOM : FILE);
    }

    public List<List<Cell>> seed() {
        return active.seed();
    }

    public void setType(SeederType type) {
        this.active = seederMap.get(type);
    }

    public AbstractSeeder.SeederType getType() {
        return active.getType();
    }

    public void setFile(String fileName) throws FileNotFoundException {
        FileSeeder seeder = (FileSeeder) seederMap.get(FILE);
        seeder.setSeedFileName(fileName);
    }

    public void setRows(Integer rows) {
        AbstractSeeder activeSeeder = (AbstractSeeder) active;
        activeSeeder.setRows(rows);
        AbstractSeeder inactiveSeeder = (AbstractSeeder) inactive;
        inactiveSeeder.setRows(rows);
    }

    public void setColumns(Integer columns) {
        AbstractSeeder activeSeeder = (AbstractSeeder) active;
        activeSeeder.setColumns(columns);
        AbstractSeeder inactiveSeeder = (AbstractSeeder) inactive;
        inactiveSeeder.setColumns(columns);
    }

    public int getRows() {
        AbstractSeeder seeder = (AbstractSeeder) active;
        return seeder.getRows();
    }

    public int getColumns() {
        AbstractSeeder seeder = (AbstractSeeder) active;
        return seeder.getColumns();
    }

    public String getFile() {
        FileSeeder seeder = (FileSeeder) seederMap.get(FILE);
        return seeder.getSeedFileName();
    }
}
