package io.github.aj8gh.gameoflife.consumer;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import io.github.aj8gh.gameoflife.application.Game;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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
