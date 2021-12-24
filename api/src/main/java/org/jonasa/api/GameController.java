package org.jonasa.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.PriorityQueue;
import java.util.Queue;

@RestController
public class GameController {
    private static final Queue<String> queue = new PriorityQueue<>();

    @PostMapping("/send")
    public String send() {
        return queue.poll();
    }

    public static void queue(String state) {
        queue.add(state);
    }
}
