package aj8gh.gameoflife.api.controllers;

import aj8gh.gameoflife.application.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTest {
    @Mock
    private Game game;
    @Mock
    private QueueController queueController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        GameController controller = new GameController(queueController, game);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void start_WhenRunning_ReturnsBadRequest() throws Exception {
        doThrow(new IllegalStateException()).when(game).run();

        mockMvc.perform(post("/game/start"))
                .andExpect(status().isBadRequest());

        verify(game).run();
    }

    @Test
    void start_WhenStopped_StartsGame() throws Exception {
        mockMvc.perform(post("/game/start"))
                .andExpect(status().isOk());

        verify(game).run();
    }

    @Test
    void reset() throws Exception {
        mockMvc.perform(post("/game/reset"))
                .andExpect(status().isOk());

        verify(queueController).clear();
        verify(game).reset();
    }

    @Test
    void stop_WhenRunning_StopsGame() throws Exception {
        mockMvc.perform(post("/game/stop"))
                .andExpect(status().isOk());

        verify(game).stop();
    }

    @Test
    void stop_WhenStopped_ReturnsBadRequest() throws Exception {
        doThrow(new IllegalStateException()).when(game).stop();

        mockMvc.perform(post("/game/stop"))
                .andExpect(status().isBadRequest());

        verify(game).stop();
    }

    @Test
    void speed_Success_ChangesTickDuration() throws Exception {
        mockMvc.perform(post("/game/speed")
                        .param("millis", "2000"))
                .andExpect(status().isOk());

        verify(game).setTickDuration(2000);
    }

    @Test
    void speed_NonInt_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/game/speed")
                        .param("millis", "notAnInt"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(game);

        mockMvc.perform(post("/game/speed")
                        .param("millis", "1.34"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(game);
    }

    @Test
    void speed_NegativeInt_ReturnsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException()).when(game).setTickDuration(-500);

        mockMvc.perform(post("/game/speed")
                        .param("millis", "-500"))
                .andExpect(status().isBadRequest());

        verify(game).setTickDuration(-500);
    }
}
