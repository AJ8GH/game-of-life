package aj8gh.gameoflife.seeder;

import aj8gh.gameoflife.domain.Cell;
import lombok.Getter;

import java.util.List;

import static aj8gh.gameoflife.seeder.AbstractSeeder.SeederType.RANDOM;

@Getter
public class RandomSeeder extends AbstractSeeder {
    private final SeederType type = RANDOM;

    public RandomSeeder(int rows, int columns) {
        super(rows, columns);
    }

    @Override
    public List<List<Cell>> seed() {
        return generateSeed();
    }
}
