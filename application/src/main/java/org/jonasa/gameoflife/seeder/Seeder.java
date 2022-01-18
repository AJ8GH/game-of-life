package org.jonasa.gameoflife.seeder;

import org.jonasa.gameoflife.domain.Cell;

import java.util.List;

public interface Seeder {
    List<List<Cell>> seed();
}
