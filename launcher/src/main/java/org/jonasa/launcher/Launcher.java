package org.jonasa.launcher;

import lombok.extern.slf4j.Slf4j;
import org.jonasa.api.ApiApplication;
import org.jonasa.application.Config;
import org.jonasa.application.Game;

@Slf4j
public class Launcher {
    private static final Game GAME = Config.game();

    public static void main(String[] args) {
        log.info("*** Starting API ***");
        ApiApplication.main(new String[] {});
        log.info("*** API Started ***");

        log.info("*** Starting Game of Life ***");
        GAME.run();
    }
}
