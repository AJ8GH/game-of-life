package org.jonasa;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GameTest {
    @Test
    void run() throws InterruptedException {
        Grid grid = mock(Grid.class);
        when(grid.population()).thenReturn(99L).thenReturn(0L);
        Game game = new Game(grid);

        game.run();
        verify(grid).tick();
    }
}
