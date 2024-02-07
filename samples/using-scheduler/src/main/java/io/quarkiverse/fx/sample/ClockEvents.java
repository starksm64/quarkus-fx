package io.quarkiverse.fx.sample;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import io.quarkiverse.fx.RunOnFxThread;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class ClockEvents {
    @Inject
    UnixTimeProvider timeProvider;
    @Inject
    Event<TimeEvent> timeEvent;

    @Scheduled(every = "1s")
    @RunOnFxThread
    void timeIncrement() {
        Log.info("timeIncrement");
        //Platform.runLater(this::sendTimeEvent);
        sendTimeEvent();
    }

    private void sendTimeEvent() {
        try {
            long unixTime = timeProvider.getTime();
            String timeString = String.format("%016x", unixTime);
            TimeEvent event = new TimeEvent(unixTime, timeString);
            Log.infof("%d/%s", unixTime, timeString);
            timeEvent.fireAsync(event);
        } catch (IOException e) {
            Log.error("Failed to send TimeEvent", e);
        }
    }
}
