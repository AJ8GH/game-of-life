package io.github.aj8gh.gameoflife.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeederConfigRequest {

  private String fileName;

  private Integer columns;

  private Integer rows;
}
