package io.github.aj8gh.gameoflife.domain;

import java.util.Random;
import lombok.Data;
import lombok.Generated;

@Data
public class Cell {

  private static final int MIN_NEIGHBOURS = 2;
  private static final int MAX_NEIGHBOURS = 3;

  private boolean alive;
  private int neighbours;

  public Cell() {
    this.alive = new Random().nextBoolean();
  }

  public void tick() {
    if (neighbours < MIN_NEIGHBOURS || neighbours > MAX_NEIGHBOURS) {
      die();
    }
    if (neighbours == MAX_NEIGHBOURS) {
      live();
    }
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
    return isAlive() ? " ● " : "   ";
  }
}
