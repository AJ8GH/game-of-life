package aj8gh.gameoflife.seeder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.FileNotFoundException;
import java.util.Map;

import static aj8gh.gameoflife.seeder.AbstractSeeder.SeederType.FILE;
import static aj8gh.gameoflife.seeder.AbstractSeeder.SeederType.RANDOM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class SeederAdaptorTest {

    private SeederAdaptor seederAdaptor;
    @Mock
    private RandomSeeder randomSeeder;
    @Mock
    private FileSeeder fileSeeder;
    private Map<AbstractSeeder.SeederType, Seeder> seederMap;

    @BeforeEach
    void setUp() {
        openMocks(this);

        seederMap = Map.of(
                RANDOM, randomSeeder,
                FILE, fileSeeder
        );

        when(randomSeeder.getType()).thenReturn(RANDOM);
        when(fileSeeder.getType()).thenReturn(FILE);

        seederAdaptor = new SeederAdaptor(seederMap, false);
    }

    @Test
    void seed_SeedFromFileFalse_RandomSeederIsActive() {
        seederAdaptor = new SeederAdaptor(seederMap, false);
        seederAdaptor.seed();
        verify(randomSeeder).seed();
    }

    @Test
    void seed_SeedFromFileTrue_FileSeederIsActive() {
        seederAdaptor = new SeederAdaptor(seederMap, true);
        seederAdaptor.seed();
        verify(fileSeeder).seed();
    }

    @Test
    void setType() {
        seederAdaptor.setType(FILE);
        assertEquals(FILE, seederAdaptor.getType());

        seederAdaptor.setType(RANDOM);
        assertEquals(RANDOM, seederAdaptor.getType());
    }

    @Test
    void setFile() throws FileNotFoundException {
        seederAdaptor.setFile("FILE");
        verify(fileSeeder).setSeedFileName("FILE");
    }

    @Test
    void getFile() {
        seederAdaptor.getFile();
        verify(fileSeeder).getSeedFileName();
    }

    @Test
    void setRows() {
        seederAdaptor.setRows(99);
        verify(fileSeeder).setRows(99);
        verify(randomSeeder).setRows(99);
    }

    @Test
    void setColumns() {
        seederAdaptor.setColumns(99);
        verify(fileSeeder).setColumns(99);
        verify(randomSeeder).setColumns(99);
    }

    @Test
    void getColumns() {
        seederAdaptor.getColumns();
        verify(randomSeeder).getColumns();
    }

    @Test
    void getRows() {
        seederAdaptor.getRows();
        verify(randomSeeder).getRows();
    }
}
