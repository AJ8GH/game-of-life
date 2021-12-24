package org.jonasa.apiclient.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState {
    int population;
    int generation;
    List<List<Cell>> grid;
}
