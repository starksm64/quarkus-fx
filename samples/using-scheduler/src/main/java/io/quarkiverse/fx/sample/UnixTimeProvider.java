package io.quarkiverse.fx.sample;

import java.io.IOException;

public interface UnixTimeProvider {
    long getTime() throws IOException;
}
