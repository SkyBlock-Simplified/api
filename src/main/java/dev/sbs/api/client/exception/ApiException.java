package dev.sbs.api.client.exception;

import dev.sbs.api.client.request.HttpMethod;
import dev.sbs.api.client.request.Request;
import dev.sbs.api.client.response.HttpStatus;
import dev.sbs.api.client.response.Response;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.StringUtil;
import dev.sbs.api.util.mutable.pair.Pair;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

@Getter
public class ApiException extends RuntimeException implements Response {

    private final boolean error = true;
    private final @NotNull Instant timestamp;
    private final @NotNull HttpStatus status;
    private final @NotNull Request request;
    private final @NotNull Optional<String> body;
    private final @NotNull ConcurrentMap<String, ConcurrentList<String>> headers;
    @Setter(AccessLevel.PROTECTED)
    protected @NotNull String name = "Api";
    protected @NotNull ApiErrorResponse response;

    public ApiException(@NotNull FeignException exception) {
        super(exception.getMessage(), exception.getCause(), false, true);
        this.response = exception::getMessage;
        this.timestamp = Instant.now();
        this.status = HttpStatus.of(exception.status());
        this.body = exception.responseBody().map(byteBuffer -> StringUtil.toEncodedString(byteBuffer.array(), StandardCharsets.UTF_8));

        this.headers = exception.responseHeaders()
            .entrySet()
            .stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .map(entry -> Pair.of(
                entry.getKey(),
                (ConcurrentList<String>) Concurrent.newUnmodifiableList(entry.getValue())
            ))
            .collect(Concurrent.toUnmodifiableMap());

        this.request = new Request.Impl(
            System.currentTimeMillis(),
            HttpMethod.of(exception.request().httpMethod().name()),
            exception.request().url(),
            exception.request()
                .headers()
                .entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> Pair.of(
                    entry.getKey(),
                    (ConcurrentList<String>) Concurrent.newUnmodifiableList(entry.getValue())
                ))
                .collect(Concurrent.toUnmodifiableMap())
        );
    }

}
