package aj8gh.gameoflife.api.controllers;

import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.seeder.Seeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SeederControllerTest {
    private static final MediaType APPLICATION_JSON = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    @Mock
    private Seeder randomSeeder;
    @Mock
    private Seeder fileSeeder;
    @Mock
    private Game game;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        SeederController controller = new SeederController(randomSeeder, fileSeeder, game);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void type_Random_SetsRandomSeeder() throws Exception {
        mockMvc.perform(post("/seeder/type")
                        .contentType(APPLICATION_JSON)
                        .header("Type", "RANDOM"))
                .andExpect(status().isOk());

        verify(game).setSeeder(randomSeeder);
    }

    @Test
    void type_File_SetsFileSeeder() throws Exception {
        mockMvc.perform(post("/seeder/type")
                        .contentType(APPLICATION_JSON)
                        .header("Type", "FILE"))
                .andExpect(status().isOk());

        verify(game).setSeeder(fileSeeder);
    }

    @Test
    void type_InvalidType_Returns400() throws Exception {
        mockMvc.perform(post("/seeder/type")
                        .contentType(APPLICATION_JSON)
                        .header("Type", "badHeader"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(game);
    }

    @Test
    void type_MissingTypeHeader_Returns400() throws Exception {
        mockMvc.perform(post("/seeder/type")
                        .contentType(APPLICATION_JSON)
                        .header("badTypeHeader", "RANDOM"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(game);
    }
}
