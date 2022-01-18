package org.jonasa.gameoflife.apiclient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameState {
    long population;
    int generation;
    List<List<Cell>> grid;
}
