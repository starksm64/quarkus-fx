package io.quarkiverse.fx;

import io.quarkus.runtime.QuarkusApplication;
import javafx.application.Application;

public class QuarkusFxApplication implements QuarkusApplication {

    @Override
    public int run(final String... args) throws InterruptedException {
        // Force the race condition between scheduler bean and FxApplication startup
        Thread.dumpStack();
        Thread.sleep(1000);
        Application.launch(FxApplication.class, args);
        return 0;
    }
}
