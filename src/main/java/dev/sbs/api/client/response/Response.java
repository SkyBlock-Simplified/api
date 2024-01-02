package dev.sbs.api.client.response;

import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface Response {

    default @NotNull CFCacheStatus getCFCacheStatus() {
        return this.getHeaders()
            .getOptional(CFCacheStatus.HEADER_KEY)
            .flatMap(ConcurrentList::getFirst)
            .map(CFCacheStatus::of)
            .orElse(CFCacheStatus.UNKNOWN);
    }

    @NotNull ConcurrentMap<String, ConcurrentList<String>> getHeaders();

    @NotNull HttpStatus getStatus();

    @NotNull Instant getTimestamp();

    default boolean isError() {
        return false;
    }

    @Getter
    @RequiredArgsConstructor
    class Impl implements Response {

        private final long timestamp;
        private final @NotNull HttpStatus status;
        private final @NotNull ConcurrentMap<String, ConcurrentList<String>> headers;

        public @NotNull Instant getTimestamp() {
            return Instant.ofEpochMilli(this.timestamp);
        }

    }

}
