package org.jonasa.launcher;

import org.junit.jupiter.api.Test;

public class EndToEndTest {

    @Test
    void applicationStarts() throws InterruptedException {
        Launcher.main(new String[] {});
        Thread.sleep(500);
        Launcher.stop();
    }
}
