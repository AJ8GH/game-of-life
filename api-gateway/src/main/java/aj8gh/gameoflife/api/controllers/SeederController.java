package aj8gh.gameoflife.api.controllers;

import aj8gh.gameoflife.api.domain.SeederType;
import aj8gh.gameoflife.application.Game;
import aj8gh.gameoflife.seeder.Seeder;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SeederController {
    private static final Logger LOG = LogManager.getLogger(SeederController.class.getName());

    private static final String TYPE_ENDPOINT = "/seeder/type";

    private final Seeder randomSeeder;
    private final Seeder fileSeeder;
    private final Game game;

    @CrossOrigin
    @PostMapping(value = TYPE_ENDPOINT)
    public ResponseEntity<String> type(@RequestHeader(name = "Type") String type) {
        LOG.info("Received request at {}", TYPE_ENDPOINT);

        if (!SeederType.getLabels().contains(type)) {
            LOG.info("Bad Request - Invalid Type: {}", type);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (type.equals(SeederType.RANDOM.name())) {
            game.setSeeder(randomSeeder);
        } else if (type.equals(SeederType.FILE.name())) {
            game.setSeeder(fileSeeder);
        }
        LOG.info("Seeder set to type {}", type);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
