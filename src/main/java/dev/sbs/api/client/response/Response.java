package dev.sbs.api.client.response;

import dev.sbs.api.client.metrics.ConnectionDetails;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface Response {

    /**
     * Retrieves the Cloudflare cache status of the response by examining the {@code CF-Cache-Status} header
     * in the response's headers  or {@code CloudflareCacheStatus.UNKNOWN} if the header is unavailable or unrecognizable.
     *
     * @return the {@link CloudflareCacheStatus} indicating the caching status of the response.
     */
    default @NotNull CloudflareCacheStatus getCloudflareCacheStatus() {
        return this.getHeaders()
            .getOptional(CloudflareCacheStatus.HEADER_KEY)
            .flatMap(ConcurrentList::getFirst)
            .map(CloudflareCacheStatus::of)
            .orElse(CloudflareCacheStatus.UNKNOWN);
    }

    /**
     * Retrieves the headers of the response.
     *
     * @return a {@link ConcurrentMap} where the keys are the header names and the values
     *         are lists of the corresponding header values.
     */
    @NotNull ConcurrentMap<String, ConcurrentList<String>> getHeaders();

    /**
     * Retrieves the HTTP status of the response.
     *
     * @return the {@link HttpStatus} associated with the response, indicating the
     * specific HTTP response code and its classification (e.g., success, client error, server error).
     */
    @NotNull HttpStatus getStatus();

    /**
     * Retrieves the timestamp of when the response was received.
     *
     * @return a {@link Instant} representing the timestamp of the response.
     */
    default @NotNull Instant getTimestamp() {
        return this.getDetails().getResponseReceived();
    }

    /**
     * Retrieves the connection details for this response.
     *
     * @return the {@link ConnectionDetails} of the response.
     */
    @NotNull ConnectionDetails getDetails();

    /**
     * Determines if the response represents an error state.
     *
     * @return {@code true} if the response indicates an error; {@code false} otherwise.
     */
    default boolean isError() {
        return this.getStatus().getState().isError();
    }

    @Getter
    @RequiredArgsConstructor
    class Impl implements Response {

        private final @NotNull ConnectionDetails details;
        private final @NotNull HttpStatus status;
        private final @NotNull ConcurrentMap<String, ConcurrentList<String>> headers;

    }

}
