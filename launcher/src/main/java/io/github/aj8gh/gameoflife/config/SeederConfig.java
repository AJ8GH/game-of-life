package io.github.aj8gh.gameoflife.config;

import static java.util.stream.Collectors.toMap;

import io.github.aj8gh.gameoflife.seeder.FileSeeder;
import io.github.aj8gh.gameoflife.seeder.RandomSeeder;
import io.github.aj8gh.gameoflife.seeder.Seeder;
import io.github.aj8gh.gameoflife.seeder.SeederAdaptor;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Set;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeederConfig {

  private static final String USER_DIR = System.getProperty("user.dir");

  @Value("${seeder.seed.fromFile}")
  private boolean seedFromFile;

  @Value("${seeder.seed.path}")
  private String seedFilePath;

  @Value("${seeder.seed.file}")
  private String seedFile;

  @Value("${seeder.seed.module}")
  private String seedModule;

  @Value("${seeder.rows}")
  private int rows;

  @Value("${seeder.columns}")
  private int columns;

  @Bean
  @Qualifier("seeder")
  public Seeder seeder() {
    return seedFromFile ? new FileSeeder(rows, columns, resolveFilePath(), seedFile)
        : new RandomSeeder(rows, columns);
  }

  @Bean("randomSeeder")
  public Seeder randomSeeder() {
    return new RandomSeeder(rows, columns);
  }

  @Bean("fileSeeder")
  public Seeder fileSeeder() {
    return new FileSeeder(rows, columns, resolveFilePath(), seedFile);
  }

  @Bean("seederAdaptor")
  public SeederAdaptor seederAdaptor() {
    var seederMap = new EnumMap<>(seeders().stream()
        .collect(toMap(Seeder::getType, Function.identity())));
    return new SeederAdaptor(seederMap, seedFromFile);
  }

  private String resolveFilePath() {
    return USER_DIR.endsWith(seedModule) ? USER_DIR + seedFilePath
        : USER_DIR + seedModule + seedFilePath;
  }

  private Collection<Seeder> seeders() {
    return Set.of(randomSeeder(), fileSeeder());
  }
}
