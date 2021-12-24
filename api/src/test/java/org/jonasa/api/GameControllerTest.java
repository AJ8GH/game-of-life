package org.jonasa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jonasa.apiclient.domain.Cell;
import org.jonasa.apiclient.domain.GameState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void send() throws Exception {
        mockMvc.perform(post("/send"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void queue() throws Exception {
        List<Cell> row = List.of(
                new Cell(false),
                new Cell(true),
                new Cell(true));
        List<List<Cell>> grid = List.of(row, row, row);
        GameState gameState = new GameState(6, 3, grid);

        String requestBody = mapper.writeValueAsString(gameState);

        mockMvc.perform(post("/queue")
                .contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8))
                .content(requestBody))
                .andExpect(status().isOk());
    }
}
