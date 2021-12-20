package org.jonasa.integration;

import org.jonasa.application.Game;
import org.jonasa.util.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {
    @Test
    void configLoadsCorrectly() {
        Game game = Config.game();
        assertTrue(game.getRows() > 0);
        assertTrue(game.getColumns() > 0);
        assertTrue(game.getTickDuration() > 0);
    }
}
