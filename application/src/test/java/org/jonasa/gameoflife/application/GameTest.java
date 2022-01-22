package org.jonasa.gameoflife.application;

import org.jonasa.gameoflife.domain.Cell;
import org.jonasa.gameoflife.domain.Grid;
import org.jonasa.gameoflife.ui.UI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

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
        verify(grid, times(4)).tick();
        verify(ui, times(5)).accept(game);
        assertEquals(4, game.getGeneration());

        assertFalse(game.isRunning());
    }

    @Test
    void run_WhenRunning_ThrowsIllegalStateException() {
        when(grid.population()).thenReturn(10L);
        game.run();

        assertTrue(game.isRunning());
        assertThrows(IllegalStateException.class, () -> game.run());
    }

    @Test
    void stop_WhenRunning_StopsGame() throws InterruptedException {
        when(grid.population()).thenReturn(10L);

        game.run();
        Thread.sleep(100);
        assertTrue(game.isRunning());

        game.stop();
        Thread.sleep(100);
        assertFalse(game.isRunning());
    }

    @Test
    void stop_WhenStopped_ThrowsIllegalStateException() {
        assertFalse(game.isRunning());
        assertThrows(IllegalStateException.class, () -> game.stop());
    }

    @Test
    void population() {
        when(grid.population()).thenReturn(100L);
        assertEquals(100L, game.population());
        verify(grid).population();
    }

    @Test
    void reset_whenStopped_ResetsGrid() {
        assertFalse(game.isRunning());
        List<List<Cell>> seed = List.of(List.of());
        game.reset(seed);

        verify(grid).setGrid(seed);
        assertFalse(game.isRunning());
    }

    @Test
    void reset_whenRunning_ResetsGridAndStopsGame() {
        when(grid.population()).thenReturn(100L);
        game.run();
        assertTrue(game.isRunning());

        List<List<Cell>> seed = List.of(List.of());
        game.reset(seed);

        verify(grid).setGrid(seed);
        assertFalse(game.isRunning());
    }
}
