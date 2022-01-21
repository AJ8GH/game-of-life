package org.jonasa.gameoflife.config;

import org.jonasa.gameoflife.seeder.FileSeeder;
import org.jonasa.gameoflife.seeder.RandomSeeder;
import org.jonasa.gameoflife.seeder.Seeder;
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
    public Seeder seeder() {
        return seedFromFile ?
                new FileSeeder(rows, columns, resolveFilePath()) :
                new RandomSeeder(rows, columns);
    }

    private String resolveFilePath() {
        return USER_DIR.endsWith(seedModule) ?
                USER_DIR + seedFilePath + seedFile :
                USER_DIR + seedModule + seedFilePath + seedFile;
    }
}
