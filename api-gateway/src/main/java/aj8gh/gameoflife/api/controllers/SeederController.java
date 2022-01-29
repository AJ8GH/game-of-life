package aj8gh.gameoflife.api.controllers;

import aj8gh.gameoflife.api.domain.FileRequest;
import aj8gh.gameoflife.api.domain.SeederType;
import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.seeder.FileSeeder;
import aj8gh.gameoflife.seeder.RandomSeeder;
import aj8gh.gameoflife.seeder.Seeder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static aj8gh.gameoflife.api.domain.SeederType.FILE;
import static aj8gh.gameoflife.api.domain.SeederType.RANDOM;

@RestController
public class SeederController {

    private static final Logger LOG = LogManager.getLogger(SeederController.class.getName());
    private static final String TYPE_ENDPOINT = "/seeder/type";
    private static final String FILE_ENDPOINT = "/seeder/file";
    private static final String APPLICATION_JSON = "application/json";

    private final FileSeeder fileSeeder;
    private final RandomSeeder randomSeeder;
    private final Game game;
    private final ObjectMapper mapper;
    private final Map<SeederType, Seeder> seederMap;

    public SeederController(RandomSeeder randomSeeder, FileSeeder fileSeeder,
                            Game game, ObjectMapper mapper) {
        this.fileSeeder = fileSeeder;
        this.randomSeeder = randomSeeder;
        this.game = game;
        this.mapper = mapper;
        this.seederMap = Map.of(RANDOM, randomSeeder, FILE, fileSeeder);
    }

    @CrossOrigin
    @PostMapping(value = TYPE_ENDPOINT)
    public ResponseEntity<String> type(@RequestHeader(name = "Type") String type) {
        LOG.info("Received request {} at {}", type, TYPE_ENDPOINT);
        try {
            SeederType seederType = SeederType.valueOf(type);
            game.setSeeder(seederMap.get(seederType));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            LOG.info("Bad Request - Invalid Type header: {} | {}", type, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = FILE_ENDPOINT, consumes = APPLICATION_JSON)
    public ResponseEntity<String> file(@RequestBody String requestBody) {
        LOG.info("Received request {} at {}", requestBody, FILE_ENDPOINT);
        try {
            FileRequest request = mapper.readValue(requestBody, FileRequest.class);
            fileSeeder.setSeedFileName(request.getFileName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            LOG.info("Exception processing JSON request body: {} | {}", requestBody, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            LOG.info("File not found: {} | {}", requestBody, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
