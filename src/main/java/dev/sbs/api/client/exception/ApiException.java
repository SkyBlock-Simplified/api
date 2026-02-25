package dev.sbs.api.client.exception;

import com.google.gson.JsonSyntaxException;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.request.HttpMethod;
import dev.sbs.api.client.request.Request;
import dev.sbs.api.client.response.ConnectionDetails;
import dev.sbs.api.client.response.HttpStatus;
import dev.sbs.api.client.response.Response;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.StringUtil;
import feign.FeignException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Getter
public class ApiException extends RuntimeException implements Response<Optional<String>> {

    private final boolean error = true;
    private final @NotNull String name;
    private final @NotNull HttpStatus status;
    private final @NotNull Optional<String> body;
    private final @NotNull ConnectionDetails details;
    private final @NotNull ConcurrentMap<String, ConcurrentList<String>> headers;
    private final @NotNull Request request;
    protected @NotNull ApiErrorResponse response;
    private int retryAttempts = 0;

    public ApiException(@NotNull String methodKey, @NotNull feign.Response response, @NotNull String name) {
        this(FeignException.errorStatus(methodKey, response), response, name);
    }

    public ApiException(@NotNull FeignException exception, @NotNull feign.Response response, @NotNull String name) {
        super(exception.getMessage(), exception.getCause(), false, true);
        this.name = name;
        this.status = HttpStatus.of(exception.status());
        this.body = exception.responseBody().map(byteBuffer -> StringUtil.toEncodedString(byteBuffer.array(), StandardCharsets.UTF_8));
        this.details = new ConnectionDetails(response);
        this.headers = Response.getHeaders(exception.responseHeaders());
        this.response = exception::getMessage;

        this.request = new Request.Impl(
            HttpMethod.of(exception.request().httpMethod().name()),
            exception.request().url(),
            Response.getHeaders(exception.request().headers())
        );
    }

    protected final @Nullable <T> T fromJson(@Nullable String json, @NotNull Class<T> classOfT) throws JsonSyntaxException {
        try {
            return SimplifiedApi.getGson().fromJson(json, classOfT);
        } catch (JsonSyntaxException jsex) {
            return null;
        }
    }

}
