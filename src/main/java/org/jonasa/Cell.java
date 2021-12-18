package org.jonasa;

public class Cell {
    private boolean alive = true;

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
    }

    public void revive() {
        this.alive = true;
    }
}
