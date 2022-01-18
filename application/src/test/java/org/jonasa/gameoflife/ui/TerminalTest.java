package org.jonasa.gameoflife.ui;

import org.jonasa.gameoflife.application.Game;
import org.jonasa.gameoflife.domain.Grid;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class TerminalTest {
    private static final String ACTUAL_OUTPUT = "Mock Output";
    private static final String EXPECTED_OUTPUT = "Mock Output\n";
    private static final String INFO_OUTPUT = "Generation: 198, Population: 50\n";

    private ByteArrayOutputStream out;
    private PrintStream originalOut;

    @Mock
    private Game game;
    @Mock
    private Grid grid;

    @BeforeEach
    void setUp() {
        openMocks(this);
        out = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void accept_LogInfoFalse() {
        UI ui = new Terminal(false);
        when(game.getGrid()).thenReturn(grid);
        when(grid.toString()).thenReturn(ACTUAL_OUTPUT);

        ui.accept(game);

        verify(game).getGrid();
        assertEquals(EXPECTED_OUTPUT, out.toString());
    }

    @Test
    void accept_LogInfoTrue() {
        UI ui = new Terminal(true);
        when(game.getGrid()).thenReturn(grid);
        when(grid.toString()).thenReturn(ACTUAL_OUTPUT);
        when(game.population()).thenReturn(50L);
        when(game.getGeneration()).thenReturn(new AtomicInteger(198));

        ui.accept(game);

        verify(game).getGrid();
        assertEquals(EXPECTED_OUTPUT + INFO_OUTPUT, out.toString());
    }
}
