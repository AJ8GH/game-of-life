package aj8gh.gameoflife.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameConfigRequest {
    private Integer tickDuration;

    private List<ConsumerType> consumers;
}
