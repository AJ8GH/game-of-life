package io.github.aj8gh.gameoflife.seeder;

import io.github.aj8gh.gameoflife.domain.Cell;
import io.github.aj8gh.gameoflife.seeder.AbstractSeeder.SeederType;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class SeederAdaptor implements Seeder {

  private final Map<SeederType, Seeder> seederMap;
  private Seeder active;
  private Seeder inactive;

  public SeederAdaptor(Map<SeederType, Seeder> seederMap, boolean fromFile) {
    this.seederMap = seederMap;
    this.active = seederMap.get(fromFile ? SeederType.FILE : SeederType.RANDOM);
    this.inactive = seederMap.get(fromFile ? SeederType.RANDOM : SeederType.FILE);
  }

  public List<List<Cell>> seed() {
    return active.seed();
  }

  public AbstractSeeder.SeederType getType() {
    return active.getType();
  }

  public void setType(SeederType type) {
    if (!type.equals(active.getType())) {
      this.inactive = active;
      this.active = seederMap.get(type);
    }
  }

  public int getRows() {
    AbstractSeeder seeder = (AbstractSeeder) active;
    return seeder.getRows();
  }

  public void setRows(Integer rows) {
    AbstractSeeder activeSeeder = (AbstractSeeder) active;
    activeSeeder.setRows(rows);
    AbstractSeeder inactiveSeeder = (AbstractSeeder) inactive;
    inactiveSeeder.setRows(rows);
  }

  public int getColumns() {
    AbstractSeeder seeder = (AbstractSeeder) active;
    return seeder.getColumns();
  }

  public void setColumns(Integer columns) {
    AbstractSeeder activeSeeder = (AbstractSeeder) active;
    activeSeeder.setColumns(columns);
    AbstractSeeder inactiveSeeder = (AbstractSeeder) inactive;
    inactiveSeeder.setColumns(columns);
  }

  public String getFile() {
    FileSeeder seeder = (FileSeeder) seederMap.get(SeederType.FILE);
    return seeder.getSeedFileName();
  }

  public void setFile(String fileName) throws FileNotFoundException {
    FileSeeder seeder = (FileSeeder) seederMap.get(SeederType.FILE);
    seeder.setSeedFileName(fileName);
  }
}
