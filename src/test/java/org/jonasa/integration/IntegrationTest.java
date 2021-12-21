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

        assertTrue(Config.getInt("game.seeder.columns") > 0);
        assertTrue(Config.getInt("game.seeder.rows") > 0);
        assertTrue(Config.getInt("game.tickDuration") > 0);
        assertNotNull(Config.getString("seeder.seed.filePath"));
    }
}
