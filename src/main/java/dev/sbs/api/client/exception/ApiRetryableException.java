package dev.sbs.api.client.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * This exception is raised when the {@link feign.Response} is deemed to be retryable, typically via an
 * {@link feign.codec.ErrorDecoder} when the {@link feign.Response#status() status} is 503.
 */
@Getter
@SuppressWarnings("all")
public class ApiRetryableException extends ApiException {

    private final @Nullable Long retryAfter;

    /**
    * @param retryAfter usually corresponds to the {@link feign.Util#RETRY_AFTER} header.
    */
    public ApiRetryableException(@NotNull ApiException exception, Date retryAfter) {
        super(exception);
        this.retryAfter = retryAfter != null ? retryAfter.getTime() : null;
    }

    /**
    * Sometimes corresponds to the {@link feign.Util#RETRY_AFTER} header present in {@code 503}
    * status. Other times parsed from an application-specific response. Null if unknown.
    */
    public @Nullable Date retryAfter() {
        return retryAfter != null ? new Date(retryAfter) : null;
    }

}
