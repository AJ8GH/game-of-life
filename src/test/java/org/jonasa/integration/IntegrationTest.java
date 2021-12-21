package org.jonasa.integration;

import org.jonasa.application.Config;
import org.jonasa.application.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {
    @Test
    void configLoadsCorrectly() {
        Game game = Config.game();
        assertTrue(game.getTickDuration() > 0);
        assertNotNull(game.getGrid());
    }
}
