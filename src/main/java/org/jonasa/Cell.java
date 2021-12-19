package org.jonasa;

import lombok.Data;
import lombok.Generated;

import java.util.Random;

@Data
public class Cell {
    private boolean alive;
    private int neighbours;

    public Cell() {
        this.alive = new Random().nextBoolean();
    }

    public void tick() {
        if (neighbours < 2 || neighbours > 3) die();
        if (neighbours == 3) live();
    }

    public void die() {
        this.alive = false;
    }

    public void live() {
        this.alive = true;
    }

    @Generated
    @Override
    public String toString() {
        return isAlive() ? "  *  " : "     ";
    }
}
