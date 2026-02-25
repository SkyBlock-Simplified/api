package dev.sbs.api.client.decoder;

import dev.sbs.api.client.exception.ApiException;
import dev.sbs.api.client.exception.RateLimitException;
import dev.sbs.api.client.exception.RetryableApiException;
import dev.sbs.api.client.response.HttpStatus;
import dev.sbs.api.client.response.Response;
import dev.sbs.api.client.response.RetryAfterParser;
import dev.sbs.api.client.route.RouteDiscovery;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.reflection.Reflection;
import feign.codec.ErrorDecoder;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Optional;

/**
 * This class is responsible for decoding internal errors encountered during HTTP communication
 * and transforming them into appropriate exceptions, including handling of retry mechanisms.
 * It integrates with a custom decoder for client-side errors, manages retry state, and provides
 * support for rate-limiting scenarios.
 *
 * <p>The decoder maintains a thread-local retry context to track ongoing retries for specific
 * method keys and ensures errors are correctly wrapped for Feign's retry mechanisms when applicable.
 *
 * <p>Additionally, the decoder tracks responses and processes retry-related headers, such as
 * "Retry-After", when present, to inform the application about retry behavior.
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Decoding HTTP responses into {@code ApiException} or other error types.</li>
 *   <li>Identifying rate-limiting HTTP responses and wrapping them into {@code RateLimitException}.</li>
 *   <li>Tracking retry attempts and associating them with exceptions via reflection.</li>
 *   <li>Optionally wrapping retryable exceptions for Feign's retry mechanisms.</li>
 *   <li>Clearing retry context when retries are no longer needed.</li>
 * </ul>
 *
 * @apiNote Only for internal use.
 */
public final class InternalErrorDecoder implements ErrorDecoder {

    private static final @NotNull Reflection<ApiException> API_EXCEPTION_REF = Reflection.of(ApiException.class);
    private final @NotNull ClientErrorDecoder customDecoder;
    private final @NotNull RouteDiscovery routeDiscovery;
    private final @NotNull ConcurrentList<Response<?>> recentResponses;
    private final @NotNull ThreadLocal<RetryContext> retryContext;

    public InternalErrorDecoder(@NotNull ClientErrorDecoder clientDecoder, @NotNull RouteDiscovery routeDiscovery, @NotNull ConcurrentList<Response<?>> recentResponses) {
        this.customDecoder = clientDecoder;
        this.routeDiscovery = routeDiscovery;
        this.recentResponses = recentResponses;
        this.retryContext = ThreadLocal.withInitial(RetryContext::new);
    }

    @Override
    public @NotNull Exception decode(@NotNull String methodKey, feign.Response response) {
        RetryContext context = this.retryContext.get();

        // Check if this is a retry of the same request
        boolean isRetry = methodKey.equals(context.lastMethodKey);

        if (isRetry) {
            context.retryAttempt++;
        } else {
            // New request - reset counter
            context.retryAttempt = 0;
            context.lastMethodKey = methodKey;
        }

        ApiException exception = response.status() == HttpStatus.TOO_MANY_REQUESTS.getCode() ?
            new RateLimitException(
                methodKey,
                response,
                this.routeDiscovery.findMatchingMetadata(response.request().url())
            ) : this.customDecoder.decode(methodKey, response);

        API_EXCEPTION_REF.setValue("retryAttempts", exception, context.retryAttempt);
        this.recentResponses.add(exception);

        // If retryable, wrap for Feign's retry mechanism
        Optional<Date> retryAfter = RetryAfterParser.parseFromHeaders(response.headers());

        if (retryAfter.isPresent())
            return new RetryableApiException(exception, retryAfter.get());

        // If this was the final attempt (no retry-after), clean up context
        if (!isRetry)
            this.retryContext.remove();

        return exception;
    }

    /**
     * Tracks retry state per thread.
     */
    private static final class RetryContext {

        private String lastMethodKey = null;
        private int retryAttempt = 0;

    }

}
