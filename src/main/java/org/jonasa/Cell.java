package org.jonasa;

import lombok.Data;

import java.util.Random;

@Data
public class Cell {
    private boolean alive;
    private int neighbours;

    public Cell() {
        this.alive = new Random().nextBoolean();
    }

    public void tick() {
    }

    public void die() {
        this.alive = false;
    }

    public void live() {
        this.alive = true;
    }

    @Override
    public String toString() {
        return isAlive() ? "1" : "0";
    }
}
