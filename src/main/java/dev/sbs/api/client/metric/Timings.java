package dev.sbs.api.client.metric;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public class Timings {

    // HTTP
    private final long connectionTimeToLive;
    private final long connectionEvictIdleTimeout;
    private final long connectionKeepAliveTimeout;

    // Feign
    private final long connectionTimeout;
    private final long socketTimeout;

    // Concurrency
    private final int maxConnections;
    private final int maxConnectionsPerRoute;

    // Client
    private final long cacheDuration;

    public static @NotNull Timings createDefault() {
        return new Timings(
            120 * 1_000,
            45 * 1_000,
            30 * 1_000,
            5 * 1_000,
            10 * 1_000,
            200,
            50,
            Duration.ofHours(1).toMillis()
        );
    }

}
