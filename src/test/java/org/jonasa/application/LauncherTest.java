package org.jonasa.application;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class LauncherTest {
    @Test
    void main() {
        Game game = mock(Game.class);
        Launcher.setGame(game);

        Launcher.main(new String[] {});

        verify(game, times(1)).seed();
        verify(game, times(1)).run();
    }
}
