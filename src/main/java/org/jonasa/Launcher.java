package org.jonasa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@AllArgsConstructor
public class Launcher {
    private static final int ROWS = 20;
    private static final int COLUMNS = 20;

    private static Game game = new Game(new Grid(new ArrayList<>()), new Seeder());

    public static void main(String[] args) {
        log.info("Seeding grid {} by {}...", ROWS, COLUMNS);
        game.seed(ROWS, COLUMNS);

        log.info("Running Game of Life...");
        game.run();
    }

    public static void setGame(Game game) {
        Launcher.game = game;
    }
}
