package aj8gh.gameoflife.consumer;

import lombok.AllArgsConstructor;
import aj8gh.gameoflife.application.Game;

@AllArgsConstructor
public class CliConsumer implements UiConsumer {
    private static final String LOG_MESSAGE = "Generation: %s, Population: %s\n";
    private boolean info;

    @Override
    public void accept(Game game) {
        System.out.println(game.getGrid());
        if (info) {
            System.out.printf(LOG_MESSAGE, game.getGeneration(), game.population());
        }
    }
}
