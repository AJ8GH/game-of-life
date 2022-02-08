package aj8gh.gameoflife.config;

import aj8gh.gameoflife.seeder.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
                new FileSeeder(rows, columns, resolveFilePath(), seedFile) :
                new RandomSeeder(rows, columns);
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
        Map<AbstractSeeder.SeederType, Seeder> seederMap = new HashMap<>();
        seeders().forEach(seeder -> seederMap.put(seeder.getType(), seeder));
        return new SeederAdaptor(seederMap, seedFromFile);
    }

    private String resolveFilePath() {
        return USER_DIR.endsWith(seedModule) ?
                USER_DIR + seedFilePath :
                USER_DIR + seedModule + seedFilePath;
    }

    private Collection<Seeder> seeders() {
        return Set.of(randomSeeder(), fileSeeder());
    }
}
