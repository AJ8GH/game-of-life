package io.github.aj8gh.gameoflife.api.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.aj8gh.gameoflife.api.domain.SeederConfigRequest;
import io.github.aj8gh.gameoflife.seeder.AbstractSeeder;
import io.github.aj8gh.gameoflife.seeder.SeederAdaptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Slf4j
class SeederControllerTest {

  private static final MediaType APPLICATION_JSON = new MediaType(
      MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(),
      StandardCharsets.UTF_8);

  @Mock
  private SeederAdaptor seederAdaptor;
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    openMocks(this);
    SeederController controller = new SeederController(
        new ObjectMapper(),
        seederAdaptor);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void type_Random_SetsRandomSeeder() throws Exception {
    mockMvc.perform(post("/seeder/type")
            .header("Type", "RANDOM"))
        .andExpect(status().isOk());

    verify(seederAdaptor).setType(AbstractSeeder.SeederType.RANDOM);
  }

  @Test
  void type_File_SetsFileSeeder() throws Exception {
    mockMvc.perform(post("/seeder/type")
            .header("Type", "FILE"))
        .andExpect(status().isOk());

    verify(seederAdaptor).setType(AbstractSeeder.SeederType.FILE);
  }

  @Test
  void type_InvalidType_Returns400() throws Exception {
    mockMvc.perform(post("/seeder/type")
            .header("Type", "badHeader"))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(seederAdaptor);
  }

  @Test
  void type_MissingTypeHeader_Returns400() throws Exception {
    mockMvc.perform(post("/seeder/type")
            .header("badTypeHeader", "RANDOM"))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(seederAdaptor);
  }

  @Test
  void config_Success_SetsNewFileValue() throws Exception {
    String fileName = "hwss.csv";
    SeederConfigRequest seederConfigRequest = SeederConfigRequest.builder()
        .fileName(fileName)
        .build();

    ObjectMapper mapper = new ObjectMapper();
    String request = mapper.writeValueAsString(seederConfigRequest);

    mockMvc.perform(post("/seeder/config")
            .contentType(APPLICATION_JSON)
            .content(request))
        .andExpect(status().isOk());

    verify(seederAdaptor).setFile(fileName);
  }

  @Test
  void config_BadJsonBody_ReturnsBadRequest() throws Exception {
    String request = "{bad json: '400'}}";

    mockMvc.perform(post("/seeder/config")
            .contentType(APPLICATION_JSON)
            .content(request))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(seederAdaptor);
  }

  @Test
  void config_NonExistentFile_ReturnsBadRequest() throws Exception {
    String fileName = "nonExistentFile.csv";
    doThrow(new FileNotFoundException())
        .when(seederAdaptor)
        .setFile(fileName);

    SeederConfigRequest seederConfigRequest = SeederConfigRequest.builder()
        .fileName(fileName)
        .build();

    ObjectMapper mapper = new ObjectMapper();
    String request = mapper.writeValueAsString(seederConfigRequest);

    mockMvc.perform(post("/seeder/config")
            .contentType(APPLICATION_JSON)
            .content(request))
        .andExpect(status().isBadRequest());

    verify(seederAdaptor).setFile(fileName);
  }

  @Test
  void config_SuccessfulRowsAndColumns_SetsRowsAndColumns() throws Exception {
    SeederConfigRequest seederConfigRequest = SeederConfigRequest.builder()
        .rows(99)
        .columns(44)
        .build();

    ObjectMapper mapper = new ObjectMapper();
    String request = mapper.writeValueAsString(seederConfigRequest);

    mockMvc.perform(post("/seeder/config")
            .contentType(APPLICATION_JSON)
            .content(request))
        .andExpect(status().isOk());

    verify(seederAdaptor).setRows(99);
    verify(seederAdaptor).setColumns(44);
  }

  @Test
  void config_NegativeRowsAndColumns_ReturnsBadRequest() throws Exception {
    doThrow(new IllegalArgumentException())
        .when(seederAdaptor)
        .setRows(-99);
    doThrow(new IllegalArgumentException())
        .when(seederAdaptor)
        .setColumns(-44);

    SeederConfigRequest seederConfigRequest = SeederConfigRequest.builder()
        .rows(-99)
        .columns(-44)
        .build();

    ObjectMapper mapper = new ObjectMapper();
    String request = mapper.writeValueAsString(seederConfigRequest);

    mockMvc.perform(post("/seeder/config")
            .contentType(APPLICATION_JSON)
            .content(request))
        .andExpect(status().isBadRequest());
  }
}
