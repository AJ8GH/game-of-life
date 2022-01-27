package aj8gh.gameoflife.config;

import aj8gh.gameoflife.seeder.FileSeeder;
import aj8gh.gameoflife.seeder.RandomSeeder;
import aj8gh.gameoflife.seeder.Seeder;
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
        return seedFromFile ?
                new FileSeeder(rows, columns, resolveFilePath()) :
                new RandomSeeder(rows, columns);
    }

    @Bean("randomSeeder")
    public Seeder randomSeeder() {
        return new RandomSeeder(rows, columns);
    }

    @Bean("fileSeeder")
    public Seeder fileSeeder() {
        return new FileSeeder(rows, columns, resolveFilePath());
    }

    private String resolveFilePath() {
        return USER_DIR.endsWith(seedModule) ?
                USER_DIR + seedFilePath + seedFile :
                USER_DIR + seedModule + seedFilePath + seedFile;
    }
}
