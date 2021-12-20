package org.jonasa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@AllArgsConstructor
public class Launcher {
    private static Game game = Config.game();

    public static void main(String[] args) {
        log.info("Seeding grid...");
        game.seed();

        log.info("Running Game of Life...");
        game.run();
    }

    public static void setGame(Game game) {
        Launcher.game = game;
    }
}
