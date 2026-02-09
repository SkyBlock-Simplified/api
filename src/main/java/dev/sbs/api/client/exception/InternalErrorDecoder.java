package dev.sbs.api.client.exception;

import dev.sbs.api.client.response.Response;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import feign.RetryableException;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Internal error decoder that wraps custom decoders and handles retry logic transparently.
 * This keeps all retry complexity confined to the Client class.
 */
public final class InternalErrorDecoder implements ErrorDecoder {

    private final @NotNull ClientErrorDecoder customDecoder;
    private final @NotNull ConcurrentList<Response> recentResponses;
    private final @NotNull RetryAfterParser retryAfterParser;
    private final @NotNull ThreadLocal<RetryContext> retryContext;

    public InternalErrorDecoder(@NotNull ClientErrorDecoder customDecoder, @NotNull ConcurrentList<Response> recentResponses) {
        this.customDecoder = customDecoder;
        this.recentResponses = recentResponses;
        this.retryAfterParser = new RetryAfterParser();
        this.retryContext = ThreadLocal.withInitial(RetryContext::new);
    }

    @Override
    public Exception decode(String methodKey, feign.Response response) {
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

        // Decode using the custom decoder
        ClientException exception = this.customDecoder.decode(methodKey, response);
        exception.setRetryAttempts(context.retryAttempt);
        this.recentResponses.add(exception);

        // If retryable, wrap for Feign's retry mechanism
        Date retryAfter = this.retryAfterParser.parse(response.headers().get(Util.RETRY_AFTER));

        if (retryAfter != null)
            return new InternalRetryableWrapper(exception, retryAfter);

        // If this was the final attempt (no retry-after), clean up context
        if (!isRetry)
            this.retryContext.remove();

        return exception;
    }

    @SuppressWarnings("all")
    private <T> T firstOrNull(Map<String, Collection<T>> map, String key) {
        if (map.containsKey(key) && !map.get(key).isEmpty())
            return map.get(key).iterator().next();

        return null;
    }

    /**
     * Tracks retry state per thread.
     */
    private static final class RetryContext {
        String lastMethodKey = null;
        int retryAttempt = 0;
    }

    /**
     * Parses Retry-After header into a Date.
     */
    static final class RetryAfterParser {

        private static final DateFormat RFC822_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);

        public @Nullable Date parse(@Nullable Collection<String> retryAfterValues) {
            if (retryAfterValues == null || retryAfterValues.isEmpty())
                return null;

            String retryAfter = retryAfterValues.iterator().next();

            // Try parsing as seconds
            if (retryAfter.matches("^[0-9]+\\.?0*$")) {
                retryAfter = retryAfter.replaceAll("\\.0*$", "");
                long deltaMillis = TimeUnit.SECONDS.toMillis(Long.parseLong(retryAfter));
                return new Date(System.currentTimeMillis() + deltaMillis);
            }

            // Try parsing as RFC822 date
            synchronized (RFC822_FORMAT) {
                try {
                    return RFC822_FORMAT.parse(retryAfter);
                } catch (ParseException ignored) {
                    return null;
                }
            }
        }

    }

    /**
     * Internal wrapper that Feign can recognize for retries.
     */
    @SuppressWarnings("deprecation")
    public static final class InternalRetryableWrapper extends RetryableException {

        private final @NotNull ClientException wrappedException;

        InternalRetryableWrapper(@NotNull ClientException clientException, @NotNull Date retryAfter) {
            super(
                clientException.getStatus().getCode(),
                clientException.getMessage(),
                feign.Request.HttpMethod.valueOf(clientException.getRequest().getMethod().name()),
                clientException.getCause(),
                retryAfter,
                toFeignRequest(clientException.getRequest())
            );

            this.wrappedException = clientException;
        }

        public @NotNull ClientException getWrappedException() {
            return this.wrappedException;
        }

        private static feign.Request toFeignRequest(dev.sbs.api.client.request.Request request) {
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
}