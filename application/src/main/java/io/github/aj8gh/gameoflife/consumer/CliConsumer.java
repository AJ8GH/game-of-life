package io.github.aj8gh.gameoflife.consumer;

import io.github.aj8gh.gameoflife.application.Game;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CliConsumer implements Consumer<Game> {

  private static final String INFO_MESSAGE = "Generation: %s, Population: %s\n";
  private boolean info;

  @Override
  public void accept(Game game) {
    System.out.println(game.getGrid());
    if (info) {
      System.out.printf(INFO_MESSAGE, game.getGeneration(), game.population());
    }
  }
}
