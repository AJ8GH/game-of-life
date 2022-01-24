package aj8gh.gameoflife.api;

import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.seeder.Seeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTest {
    @Mock
    private Game game;
    @Mock
    private Seeder seeder;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        GameController controller = new GameController(game, seeder);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void start_WhenStopped_StartsGame() throws Exception {
        doThrow(new IllegalStateException()).when(game).run();

        mockMvc.perform(post("/start"))
                .andExpect(status().isBadRequest());

        verify(game).run();
    }

    @Test
    void start_WhenRunning_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/start"))
                .andExpect(status().isOk());

        verify(game).run();
    }

    @Test
    void reset() throws Exception {
        when(seeder.seed()).thenReturn(List.of(List.of()));
        mockMvc.perform(post("/reset"))
                .andExpect(status().isOk());

        verify(seeder).seed();
        verify(game).reset(List.of(List.of()));
    }

    @Test
    void stop_WhenRunning_StopsGame() throws Exception {
        mockMvc.perform(post("/stop"))
                .andExpect(status().isOk());

        verify(game).stop();
    }

    @Test
    void stop_WhenStopped_ReturnsBadRequest() throws Exception {
        doThrow(new IllegalStateException()).when(game).stop();

        mockMvc.perform(post("/stop"))
                .andExpect(status().isBadRequest());

        verify(game).stop();
    }
}
