package org.jonasa.gameoflife.api;

import org.jonasa.gameoflife.application.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTest {
    @Mock
    private Game game;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        GameController controller = new GameController(game);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void start() throws Exception {
        mockMvc.perform(post("/start"))
                .andExpect(status().isOk());

        verify(game).run();
    }
}
