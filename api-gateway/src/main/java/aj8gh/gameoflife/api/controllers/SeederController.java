package aj8gh.gameoflife.api.controllers;

import aj8gh.gameoflife.api.domain.MetadataResponse;
import aj8gh.gameoflife.api.domain.SeederConfigRequest;
import aj8gh.gameoflife.seeder.AbstractSeeder;
import aj8gh.gameoflife.seeder.SeederAdaptor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@AllArgsConstructor
public class SeederController {

    private static final Logger LOG = LogManager.getLogger(SeederController.class.getName());
    private static final String TYPE_ENDPOINT = "/seeder/type";
    private static final String CONFIG_ENDPOINT = "/seeder/config";
    private static final String APPLICATION_JSON = "application/json";

    private final ObjectMapper mapper;
    private final SeederAdaptor seederAdaptor;

    @CrossOrigin
    @PostMapping(value = TYPE_ENDPOINT)
    ResponseEntity<Void> type(@RequestHeader(name = "Type") String type) {
        LOG.info("Received request {} at {}", type, TYPE_ENDPOINT);
        try {
            AbstractSeeder.SeederType seederType = AbstractSeeder.SeederType.valueOf(type);
            seederAdaptor.setType(seederType);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            LOG.info("Unable to resolve seederType: {} | {}", type, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = CONFIG_ENDPOINT, consumes = APPLICATION_JSON)
    ResponseEntity<Void> config(@RequestBody String requestBody) {
        LOG.info("Received request {} at {}", requestBody, CONFIG_ENDPOINT);
        try {
            SeederConfigRequest request = mapper.readValue(requestBody, SeederConfigRequest.class);
            updateSeederConfig(request);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (JsonProcessingException e) {
            LOG.error("Exception processing JSON request body: {} | {}", requestBody, e.getMessage());
        } catch (FileNotFoundException e) {
            LOG.error("File not found: {} | {}", requestBody, e.getMessage());
        } catch (IllegalArgumentException e) {
            LOG.error("Bad request: {} | {}", requestBody, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private void updateSeederConfig(SeederConfigRequest request) throws FileNotFoundException {
        if (request.getFileName() != null) seederAdaptor.setFile(request.getFileName());
        if (request.getColumns() != null) seederAdaptor.setColumns(request.getColumns());
        if (request.getRows() != null) seederAdaptor.setRows(request.getRows());
    }

    MetadataResponse.Seeder getSeederMetadata() {
        return MetadataResponse.Seeder.builder()
                .seederType(seederAdaptor.getType())
                .rows(seederAdaptor.getRows())
                .columns(seederAdaptor.getColumns())
                .file(seederAdaptor.getFile())
                .build();
    }
}
