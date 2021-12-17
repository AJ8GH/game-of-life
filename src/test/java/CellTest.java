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
    void kill() {
        assertTrue(cell.isAlive());

        cell.kill();
        assertFalse(cell.isAlive());
    }

    @Test
    void revive() {
        cell.kill();
        assertFalse(cell.isAlive());

        cell.revive();
        assertTrue(cell.isAlive());
    }
}