package aj8gh.gameoflife.consumer;

import aj8gh.gameoflife.application.Game;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class ConsumerAdaptorTest {
    @Mock
    CliConsumer cliConsumerUi;
    @Mock
    ApiConsumer apiConsumerUi;
    @Mock
    Game game;

    @Test
    void accept() {
        openMocks(this);
        ConsumerAdaptor consumerAdaptor = new ConsumerAdaptor(Set.of(cliConsumerUi, apiConsumerUi));
        consumerAdaptor.accept(game);

        verify(cliConsumerUi).accept(game);
        verify(apiConsumerUi).accept(game);
    }
}
