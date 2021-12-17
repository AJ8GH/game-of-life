import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void kill() {
        Cell cell = new Cell();
        assertTrue(cell.isAlive());

        cell.kill();
        assertFalse(cell.isAlive());
    }
}