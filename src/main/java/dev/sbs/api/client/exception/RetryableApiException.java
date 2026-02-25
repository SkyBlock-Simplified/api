package dev.sbs.api.client.exception;

import dev.sbs.api.client.request.Request;
import feign.RetryableException;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ApiException wrapper that Feign can recognize for retries.
 *
 * @apiNote Only for internal use.
 */
@SuppressWarnings("deprecation")
public final class RetryableApiException extends RetryableException {

    private final @NotNull ApiException wrappedException;

    public RetryableApiException(@NotNull ApiException apiException, @NotNull Date retryAfter) {
        super(
            apiException.getStatus().getCode(),
            apiException.getMessage(),
            feign.Request.HttpMethod.valueOf(apiException.getRequest().getMethod().name()),
            apiException.getCause(),
            retryAfter,
            toFeignRequest(apiException.getRequest())
        );

        this.wrappedException = apiException;
    }

    public @NotNull ApiException getWrappedException() {
        return this.wrappedException;
    }

    private static @NotNull feign.Request toFeignRequest(@NotNull Request request) {
        return feign.Request.create(
            request.getMethod().name(),
            request.getUrl(),
            request.getHeaders()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue
                )),
            null,
            null
        );
    }

}
