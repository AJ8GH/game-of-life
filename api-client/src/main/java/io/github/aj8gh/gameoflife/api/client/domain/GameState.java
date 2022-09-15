package io.github.aj8gh.gameoflife.api.client.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "grid")
public class GameState {

  private long population;
  private int generation;
  private List<List<Cell>> grid;
}
