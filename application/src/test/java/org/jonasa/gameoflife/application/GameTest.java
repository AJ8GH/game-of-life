package org.jonasa.gameoflife.application;

import org.jonasa.gameoflife.domain.Grid;
import org.jonasa.gameoflife.ui.UI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class GameTest {
    private static final int TICK_DURATION_MILLIS = 10;

    private Game game;
    @Mock
    private Grid grid;
    @Mock
    private UI ui;

    @BeforeEach
    void setUp() {
        openMocks(this);
        game = new Game(ui, grid, TICK_DURATION_MILLIS);
    }

    @Test
    void run_WhenPopulationIsZero_GameStops() throws InterruptedException {
        when(grid.population())
                .thenReturn(10L)
                .thenReturn(15L)
                .thenReturn(8L)
                .thenReturn(3L)
                .thenReturn(0L);

        game.run();

        Thread.sleep(TICK_DURATION_MILLIS * 10);
        verify(grid, times(5)).tick();
        verify(ui, times(5)).accept(game);
        assertEquals(5, game.getGeneration().get());
    }

    @Test
    void run_WhenRunning_ThrowsIllegalStateException() {
        game.run();
        assertTrue(game.isRunning());
        assertThrows(IllegalStateException.class, () -> game.run());
    }

    @Test
    void stop_WhenRunning_StopsGame() throws InterruptedException {
        game.run();
        Thread.sleep(100);
        assertTrue(game.isRunning());

        game.stop();
        Thread.sleep(100);
        assertFalse(game.isRunning());

        verify(grid, atMostOnce()).tick();
    }

    @Test
    void stop_WhenStop_ThrowsIllegalStateException() {
        assertFalse(game.isRunning());
        assertThrows(IllegalStateException.class, () -> game.stop());
    }

    @Test
    void population() {
        when(grid.population()).thenReturn(100L);
        assertEquals(100L, game.population());
        verify(grid).population();
    }
}
