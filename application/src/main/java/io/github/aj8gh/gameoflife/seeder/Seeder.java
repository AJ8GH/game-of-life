package io.github.aj8gh.gameoflife.seeder;

import io.github.aj8gh.gameoflife.domain.Cell;
import java.util.List;

public interface Seeder {

  List<List<Cell>> seed();

  AbstractSeeder.SeederType getType();
}
