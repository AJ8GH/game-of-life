package aj8gh.gameoflife.api.controllers;

import aj8gh.gameoflife.apiclient.domain.Cell;
import aj8gh.gameoflife.apiclient.domain.GameState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QueueControllerTest {
    private static final MediaType APPLICATION_JSON = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    private ObjectMapper mapper;
    private QueueController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        controller = new QueueController(mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @Order(1)
    void dequeue_NotFound() throws Exception {
        mockMvc.perform(get("/queue/dequeue"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    void dequeue_Success() throws Exception {
        String requestBody = mapper.writeValueAsString(getRequestBody());

        mockMvc.perform(post("/queue/enqueue")
                .contentType(APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        mockMvc.perform(get("/queue/dequeue"))
                .andExpect(status().isOk())
                .andExpect(content().string(requestBody));
    }

    @Test
    @Order(3)
    void enqueue_Success() throws Exception {
        String requestBody = mapper.writeValueAsString(getRequestBody());

        mockMvc.perform(post("/queue/enqueue")
                .contentType(APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void enqueue_BadRequest() throws Exception {
        String requestBody = "{'BadJSON':{expecting:400}}";

        mockMvc.perform(post("/queue/enqueue")
                .contentType(APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void clear() throws Exception {
        String requestBody = mapper.writeValueAsString(getRequestBody());

        mockMvc.perform(post("/queue/enqueue")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk());

        mockMvc.perform(delete("/queue/clear")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        mockMvc.perform(get("/queue/dequeue"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void dequeueAll() throws Exception {
        String requestBody = mapper.writeValueAsString(getRequestBody());
        String expectedResponse = mapper.writeValueAsString(List.of(
                getRequestBody(), getRequestBody(), getRequestBody()));

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/queue/enqueue")
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/queue/dequeue/all")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        mockMvc.perform(get("/queue/dequeue"))
                .andExpect(status().isNotFound());
    }

    private GameState getRequestBody() {
        List<Cell> row = List.of(
                new Cell(false),
                new Cell(true),
                new Cell(true));
        List<List<Cell>> grid = List.of(row, row, row);
        return new GameState(6, 3, grid);
    }
}
