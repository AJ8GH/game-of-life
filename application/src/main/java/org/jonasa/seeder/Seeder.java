package org.jonasa.seeder;

import org.jonasa.domain.Cell;

import java.util.List;

public interface Seeder {
    List<List<Cell>> seed();
}
