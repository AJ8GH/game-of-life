package org.jonasa;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class LauncherTest {
    private static final int EXPECTED_ROWS = 20;
    private static final int EXPECTED_COLUMNS = 20;

    @Test
    void main() {
        Game game = mock(Game.class);
        Launcher.setGame(game);

        Launcher.main(new String[] {});

        verify(game, times(1)).seed(EXPECTED_ROWS, EXPECTED_COLUMNS);
        verify(game, times(1)).run();
    }
}
