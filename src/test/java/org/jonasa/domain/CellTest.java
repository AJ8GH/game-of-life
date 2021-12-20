package org.jonasa.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    private Cell liveCell;
    private Cell deadCell;

    @BeforeEach
    void setUp() {
        liveCell = new Cell();
        liveCell.live();

        deadCell = new Cell();
        deadCell.die();
    }

    @Test
    void live() {
        assertFalse(deadCell.isAlive());
        deadCell.live();
        assertTrue(deadCell.isAlive());
    }

    @Test
    void die() {
        assertTrue(liveCell.isAlive());
        liveCell.die();
        assertFalse(liveCell.isAlive());
    }

    @Test
    void tick_LiveCellWithUnderTwoNeighbours_Dies() {
        liveCell.setNeighbours(1);
        liveCell.tick();
        assertFalse(liveCell.isAlive());

        liveCell.live();
        liveCell.setNeighbours(0);
        liveCell.tick();
        assertFalse(liveCell.isAlive());
    }

    @Test
    void tick_LiveCellWithOverThreeNeighbours_Dies() {
        liveCell.setNeighbours(4);
        liveCell.tick();
        assertFalse(liveCell.isAlive());

        liveCell.live();
        liveCell.setNeighbours(8);
        liveCell.tick();
        assertFalse(liveCell.isAlive());
    }

    @Test
    void tick_LiveCellWithTwoOrThreeNeighbours_Survives() {
        liveCell.setNeighbours(2);
        liveCell.tick();
        assertTrue(liveCell.isAlive());

        liveCell.setNeighbours(3);
        liveCell.tick();
        assertTrue(liveCell.isAlive());
    }

    @Test
    void tick_DeadCellWithThreeNeighbours_Lives() {
        deadCell.setNeighbours(3);
        deadCell.tick();
        assertTrue(deadCell.isAlive());
    }

    @Test
    void tick_AllOtherCells_StayDead() {
        deadCell.setNeighbours(2);
        deadCell.tick();
        assertFalse(deadCell.isAlive());

        deadCell.setNeighbours(4);
        deadCell.tick();
        assertFalse(deadCell.isAlive());
    }
}
