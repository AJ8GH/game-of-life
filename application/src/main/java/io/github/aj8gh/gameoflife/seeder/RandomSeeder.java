package io.github.aj8gh.gameoflife.seeder;

import static io.github.aj8gh.gameoflife.seeder.AbstractSeeder.SeederType.RANDOM;

import io.github.aj8gh.gameoflife.domain.Cell;
import java.util.List;
import lombok.Getter;

@Getter
public class RandomSeeder extends AbstractSeeder {

  private static final SeederType type = RANDOM;

  public RandomSeeder(int rows, int columns) {
    super(rows, columns);
  }

  @Override
  public List<List<Cell>> seed() {
    return generateSeed();
  }

  @Override
  public SeederType getType() {
    return type;
  }
}
