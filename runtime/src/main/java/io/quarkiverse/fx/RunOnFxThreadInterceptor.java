package io.quarkiverse.fx;

import java.util.concurrent.CountDownLatch;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.jboss.logging.Logger;

import javafx.application.Platform;

@Interceptor
@RunOnFxThread
public class RunOnFxThreadInterceptor {

    private static final Logger LOGGER = Logger.getLogger(RunOnFxThreadInterceptor.class);
    @PrimaryStage
    @Inject
    CountDownLatch hasStarted;

    @AroundInvoke
    public Object runOnFxThread(final InvocationContext ctx) throws Exception {
        LOGGER.tracef("intercepted %s on thread %s", ctx.getMethod(), Thread.currentThread());
        hasStarted.await();

        if (Platform.isFxApplicationThread()) {
            return ctx.proceed();
        } else {
            Platform.runLater(() -> {
                try {
                    ctx.proceed();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return null;
        }
    }

}
