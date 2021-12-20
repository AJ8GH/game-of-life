package org.jonasa;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GameTest {
    @Test
    void seed() {
        Seeder seeder = mock(Seeder.class);
        Grid grid = mock(Grid.class);

        Game game = new Game(grid, seeder);
        game.seed(5, 8);

        verify(seeder).seed(grid, 5, 8);
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
