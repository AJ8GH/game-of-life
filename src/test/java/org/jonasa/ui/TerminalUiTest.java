package org.jonasa.ui;

import org.jonasa.application.Game;
import org.jonasa.domain.Grid;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class TerminalUiTest {
    private static final String ACTUAL_OUTPUT = "Mock Output";
    private static final String EXPECTED_OUTPUT = "Mock Output\n";

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
    void accept() {
        UI commandLine = new TerminalUi();
        when(game.getGrid()).thenReturn(grid);
        when(grid.toString()).thenReturn(ACTUAL_OUTPUT);

        commandLine.accept(game);

        verify(game).getGrid();
        assertEquals(EXPECTED_OUTPUT, out.toString());
    }
}
