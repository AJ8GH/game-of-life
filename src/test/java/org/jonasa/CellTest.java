package org.jonasa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    private Cell cell;

    @BeforeEach
    void setUp() {
        cell = new Cell();
    }

    @Test
    void live() {
        cell.live();
        assertTrue(cell.isAlive());

        cell.die();
        assertFalse(cell.isAlive());
    }

    @Test
    void die() {
        cell.die();
        assertFalse(cell.isAlive());

        cell.live();
        assertTrue(cell.isAlive());
    }
}
