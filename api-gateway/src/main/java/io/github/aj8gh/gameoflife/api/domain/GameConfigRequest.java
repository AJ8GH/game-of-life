package io.github.aj8gh.gameoflife.api.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameConfigRequest {

  private Integer tickDuration;

  private List<ConsumerType> consumers;
}
