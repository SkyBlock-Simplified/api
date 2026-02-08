package dev.sbs.api.client.request;

import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface Request {

    @NotNull ConcurrentMap<String, ConcurrentList<String>> getHeaders();

    @NotNull HttpMethod getMethod();

    @NotNull Instant getTimestamp();

    @NotNull String getUrl();

    @Getter
    @RequiredArgsConstructor
    class Impl implements Request {

        private final long timestamp;
        private final @NotNull HttpMethod method;
        private final @NotNull String url;
        private final @NotNull ConcurrentMap<String, ConcurrentList<String>> headers;

        public @NotNull Instant getTimestamp() {
            return Instant.ofEpochMilli(this.timestamp);
        }

    }

}
