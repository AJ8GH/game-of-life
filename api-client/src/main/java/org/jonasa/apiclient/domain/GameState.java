package org.jonasa.apiclient.domain;

import lombok.Value;

import java.util.List;

@Value
public class GameState {
    int population;
    int generation;
    List<List<Cell>> grid;
}
