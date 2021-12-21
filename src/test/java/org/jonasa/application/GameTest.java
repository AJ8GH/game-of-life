package org.jonasa.application;

import org.jonasa.domain.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class GameTest {
    private static final int TICK_DURATION_MILLIS = 10;
    @Mock
    private Grid grid;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void run_WhenPopulationIsZero_GameStops() {
        when(grid.population())
                .thenReturn(10L).thenReturn(10L)
                .thenReturn(15L).thenReturn(15L)
                .thenReturn(8L).thenReturn(8L)
                .thenReturn(3L).thenReturn(3L)
                .thenReturn(0L).thenReturn(0L);

        Game game = new Game(grid, TICK_DURATION_MILLIS);
        game.run();

        verify(grid, times(5)).tick();
    }
}
