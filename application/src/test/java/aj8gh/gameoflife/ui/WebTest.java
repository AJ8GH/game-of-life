package aj8gh.gameoflife.ui;

import aj8gh.gameoflife.apiclient.ApiClient;
import aj8gh.gameoflife.apiclient.domain.GameState;
import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.domain.Cell;
import aj8gh.gameoflife.domain.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class WebTest {
    @Mock
    ApiClient apiClient;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void accept() {
        UI ui = new Web(apiClient);
        Cell liveCell = new Cell();
        liveCell.live();
        Cell deadCell = new Cell();
        liveCell.die();
        Grid grid = new Grid(List.of(List.of(liveCell), List.of(deadCell), List.of(liveCell)));
        Game game = new Game(ui, grid, 1000);

        ui.accept(game);

        GameState expectedArgument = convert(game);
        verify(apiClient).enqueue(expectedArgument);
    }

    private GameState convert(Game game) {
        List<List<aj8gh.gameoflife.apiclient.domain.Cell>> grid = game.getGrid()
                .getGrid()
                .stream()
                .map(row -> row.stream()
                        .map(cell -> new aj8gh.gameoflife.apiclient.domain.Cell(cell.isAlive()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

     return GameState.builder()
                .population(game.population())
                .generation(game.getGeneration())
                .grid(grid)
                .build();
    }
}
