package org.jonasa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jonasa.apiclient.domain.Cell;
import org.jonasa.apiclient.domain.GameState;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiGatewayControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    @Order(1)
    void dequeue_NotFound() throws Exception {
        mockMvc.perform(post("/dequeue")
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    void dequeue_Success() throws Exception {
        String requestBody = mapper.writeValueAsString(getRequestBody());

        mockMvc.perform(post("/enqueue")
                        .contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8))
                        .content(requestBody))
                .andExpect(status().isOk());

        mockMvc.perform(post("/dequeue")
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void enqueue_Success() throws Exception {
        String requestBody = mapper.writeValueAsString(getRequestBody());

        mockMvc.perform(post("/enqueue")
                .contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8))
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void enqueue_BadRequest() throws Exception {
        String requestBody = "{'BadJSON':{expecting:400}}";

        mockMvc.perform(post("/enqueue")
                .contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8))
                .content(requestBody))
                .andExpect(status().isBadRequest());
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
