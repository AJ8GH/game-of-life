package org.jonasa;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GridTest {
    @Test
    void Grid() {
        GridInitializer initializer = mock(GridInitializer.class);
        Grid victim = new Grid(initializer, 10, 10);

        verify(initializer).initialize(victim.getGrid(), 10, 10);
    }
}
