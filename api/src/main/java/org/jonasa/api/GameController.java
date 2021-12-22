package org.jonasa.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @PostMapping("/send")
    public String send() {
        return "{\"API\":\"Test response\"}";
    }
}
