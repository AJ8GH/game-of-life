package org.jonasa;

import lombok.Data;

import java.util.Random;

@Data
public class Cell {
    private boolean alive;

    public Cell() {
        this.alive = new Random().nextBoolean();
    }

    public void kill() {
        this.alive = false;
    }

    public void revive() {
        this.alive = true;
    }
}
