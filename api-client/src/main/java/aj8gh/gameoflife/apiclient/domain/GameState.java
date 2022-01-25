package aj8gh.gameoflife.apiclient.domain;

import lombok.*;

import java.util.List;

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
