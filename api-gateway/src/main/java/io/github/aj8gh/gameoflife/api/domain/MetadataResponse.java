package io.github.aj8gh.gameoflife.api.domain;

import io.github.aj8gh.gameoflife.seeder.AbstractSeeder;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetadataResponse {

  private Game game;

  private Seeder seeder;

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Game {

    private boolean running;

    private int generation;

    private long population;

    private int tickDuration;

    private int rows;

    private int columns;

    private List<ConsumerType> consumers;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Seeder {

    private AbstractSeeder.SeederType seederType;

    private String file;

    private int rows;

    private int columns;
  }
}
