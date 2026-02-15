package dev.sbs.api.client.request;

import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

public interface Request {

    /**
     * Retrieves the headers of the request.
     *
     * @return a {@link ConcurrentMap} where the keys are the header names and the values
     *         are lists of the corresponding header values.
     */
    @NotNull ConcurrentMap<String, ConcurrentList<String>> getHeaders();

    /**
     * Retrieves the HTTP method associated with the request.
     *
     * @return the {@link HttpMethod} of the request
     */
    @NotNull HttpMethod getMethod();

    /**
     * Retrieves the URL associated with the request.
     *
     * @return the request URL
     */
    @NotNull String getUrl();

    @Getter
    @RequiredArgsConstructor
    class Impl implements Request {

        private final @NotNull HttpMethod method;
        private final @NotNull String url;
        private final @NotNull ConcurrentMap<String, ConcurrentList<String>> headers;

    }

}
