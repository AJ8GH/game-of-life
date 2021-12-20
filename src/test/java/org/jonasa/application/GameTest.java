package org.jonasa.application;

import org.jonasa.domain.Grid;
import org.jonasa.util.Config;
import org.jonasa.util.Seeder;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GameTest {
    private static final int EXPECTED_ROWS = Config.getInt("game.seeder.rows");
    private static final int EXPECTED_COLUMNS = Config.getInt("game.seeder.columns");

    @Test
    void seed() {
        Seeder seeder = mock(Seeder.class);
        Grid grid = mock(Grid.class);

        Game game = new Game(grid, seeder);
        game.seed();

        verify(seeder).seed(grid, EXPECTED_ROWS, EXPECTED_COLUMNS);
    }

    @Test
    void run_WhenPopulationIsZero_GameStops() {
        Seeder seeder = mock(Seeder.class);
        Grid grid = mock(Grid.class);

        when(grid.population())
                .thenReturn(10L).thenReturn(10L)
                .thenReturn(15L).thenReturn(15L)
                .thenReturn(8L).thenReturn(8L)
                .thenReturn(3L).thenReturn(3L)
                .thenReturn(0L).thenReturn(0L);

        Game game = new Game(grid, seeder);
        game.run();

        verify(grid, times(5)).tick();
    }
}
