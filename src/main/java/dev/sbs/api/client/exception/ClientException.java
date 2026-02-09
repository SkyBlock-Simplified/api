package dev.sbs.api.client.exception;

import com.google.gson.JsonSyntaxException;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.metrics.ConnectionDetails;
import dev.sbs.api.client.request.HttpMethod;
import dev.sbs.api.client.request.Request;
import dev.sbs.api.client.response.HttpStatus;
import dev.sbs.api.client.response.Response;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.stream.pair.Pair;
import dev.sbs.api.util.StringUtil;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Getter
public class ClientException extends RuntimeException implements Response {

    private final boolean error = true;
    @Setter(AccessLevel.PROTECTED)
    protected @NotNull String name;
    protected @NotNull ClientErrorResponse response;
    private final @NotNull HttpStatus status;
    private final @NotNull Optional<String> body;
    private final @NotNull ConnectionDetails details;
    private final @NotNull ConcurrentMap<String, ConcurrentList<String>> headers;
    private final @NotNull Request request;
    @Setter(AccessLevel.PACKAGE)
    private int retryAttempts = 0;

    public ClientException(@NotNull String methodKey, @NotNull feign.Response response, @NotNull String name) {
        this(FeignException.errorStatus(methodKey, response), response, name);
    }

    public ClientException(@NotNull FeignException exception, @NotNull feign.Response response, @NotNull String name) {
        super(exception.getMessage(), exception.getCause(), false, true);
        this.name = name;
        this.response = exception::getMessage;
        this.status = HttpStatus.of(exception.status());
        this.body = exception.responseBody().map(byteBuffer -> StringUtil.toEncodedString(byteBuffer.array(), StandardCharsets.UTF_8));
        this.details = new ConnectionDetails(exception.request(), response);
        this.headers = filterHeaders(exception.responseHeaders());

        this.request = new Request.Impl(
            this.getDetails().getRequestStart().toEpochMilli(),
            HttpMethod.of(exception.request().httpMethod().name()),
            exception.request().url(),
            filterHeaders(exception.request().headers())
        );
    }

    private static ConcurrentMap<String, ConcurrentList<String>> filterHeaders(@NotNull Map<String, Collection<String>> headers) {
        return headers.entrySet()
            .stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .map(entry -> Pair.of(
                entry.getKey(),
                (ConcurrentList<String>) Concurrent.newUnmodifiableList(entry.getValue())
            ))
            .collect(Concurrent.toUnmodifiableMap());
    }

    protected final @Nullable <T> T fromJson(@Nullable String json, @NotNull Class<T> classOfT) throws JsonSyntaxException {
        try {
            return SimplifiedApi.getGson().fromJson(json, classOfT);
        } catch (JsonSyntaxException jsex) {
            return null;
        }
    }

}
