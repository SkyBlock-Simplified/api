package dev.sbs.api.client.ratelimit;

import dev.sbs.api.builder.ClassBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Rate limit configuration for a route, aligned with RFC RateLimit fields.
 * <p>
 * Conceptually maps to:
 * <ul>
 *   <li>RateLimit-Limit    → {@link #limit}</li>
 *   <li>RateLimit-Reset    → {@link #resetSeconds}</li>
 * </ul>
 *
 * This class is the *policy*; the actual per-window request count is tracked
 * in {@code RateLimitBucket}.
 */
@Getter
public final class RateLimit {

    /**
     * An unlimited rate limit (for testing or unrestricted endpoints).
     */
    public static final @NotNull RateLimit UNLIMITED = new RateLimit(Long.MAX_VALUE, Long.MAX_VALUE / 1000L, true);

    /**
     * Maximum number of requests allowed in a window.
     * <p>
     * Mirrors RateLimit-Limit.
     */
    private final long limit;

    /**
     * Window duration in seconds.
     * <p>
     * Mirrors RateLimit-Reset (delta seconds until quota resets).
     */
    private final long resetSeconds;

    /**
     * Normalized window duration.
     */
    private final @NotNull Duration windowDuration;

    /**
     * Normalized window duration in milliseconds for efficient comparisons.
     */
    private final long windowDurationMillis;

    /**
     * Whether this represents an "unlimited" configuration on the client side.
     * <p>
     * RFC does not define this, but it's useful for internal modeling.
     */
    private final boolean unlimited;

    /**
     * Primary constructor.
     *
     * @param limit         maximum allowed in window
     * @param resetSeconds  window length or server-advertised reset in seconds
     * @param unlimited     flag for internal use to indicate no effective limit
     */
    private RateLimit(long limit, long resetSeconds, boolean unlimited) {
        this.limit = limit;
        this.resetSeconds = resetSeconds;
        this.unlimited = unlimited;

        // For "unlimited", we still give a very large window for consistency
        long effectiveReset = unlimited ? Long.MAX_VALUE / 1000L : Math.max(resetSeconds, 1L);
        this.windowDuration = Duration.ofSeconds(effectiveReset);
        this.windowDurationMillis = this.windowDuration.toMillis();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Creates a client-configured rate limit.
     * <p>
     * For client configuration we don't have a meaningful "remaining" from the server,
     * so we initialize it equal to {@code limit}. The actual remaining is tracked externally.
     */
    public RateLimit(long limit, long window, @NotNull ChronoUnit unit) {
        this(
            limit,
            unit.getDuration().multipliedBy(window).getSeconds(),
            false
        );
    }

    public static @NotNull RateLimit fromAnnotation(@NotNull RateLimitConfig config) {
        if (config.unlimited())
            return RateLimit.UNLIMITED;

        return new RateLimit(
            config.limit(),
            config.resetSeconds(),
            false
        );
    }

    /**
     * Parses rate limit headers from HTTP responses.
     * <p>
     * Supports multiple header formats:
     * <ul>
     *     <li>RFC: RateLimit-Limit, RateLimit-Reset</li>
     *     <li>Common: X-RateLimit-Limit, X-RateLimit-Reset</li>
     *     <li>Retry-After: For 429 responses</li>
     * </ul>
     */
    public static @NotNull Optional<RateLimit> fromHeaders(@NotNull Map<String, Collection<String>> headers) {
        // Try standard headers first (RFC draft)
        Optional<Long> limit = getFirstLong(headers, "RateLimit-Limit", "ratelimit-limit");
        Optional<Long> reset = getFirstLong(headers, "RateLimit-Reset", "ratelimit-reset");

        // Fall back to X- prefixed headers (common)
        if (limit.isEmpty()) {
            limit = getFirstLong(headers, "X-RateLimit-Limit", "x-ratelimit-limit");
            reset = getFirstLong(headers, "X-RateLimit-Reset", "x-ratelimit-reset");
        }

        // If we don't have enough info to build a RateLimit, treat as "no info"
        if (limit.isEmpty() || reset.isEmpty())
            return Optional.empty();

        return Optional.of(fromHeaders(limit.get(), reset.get()));
    }

    /**
     * Creates a rate limit from server response headers.
     * <p>
     * This maps directly from RateLimit-Limit and RateLimit-Reset.
     * <p>
     * Remaining, if relevant, should be handled by the tracker via its own counters.
     */
    public static @NotNull RateLimit fromHeaders(long limit, long resetSeconds) {
        return new RateLimit(limit, resetSeconds, false);
    }

    /**
     * Creates an unlimited rate limit (for testing or unrestricted endpoints).
     */
    public static @NotNull RateLimit unlimited() {
        return new RateLimit(Long.MAX_VALUE, Long.MAX_VALUE / 1000L, true);
    }

    private static @NotNull Optional<String> getFirst(@NotNull Map<String, Collection<String>> headers, @NotNull String... keys) {
        for (String key : keys) {
            Collection<String> values = headers.get(key);

            if (values != null && !values.isEmpty())
                return Optional.of(values.iterator().next());
        }

        return Optional.empty();
    }

    private static @NotNull Optional<Long> getFirstLong(@NotNull Map<String, Collection<String>> headers, @NotNull String... keys) {
        return getFirst(headers, keys).flatMap(value -> {
            try {
                return Optional.of(Long.parseLong(value.trim()));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder implements ClassBuilder<RateLimit> {

        private long limit = 600;
        private long windowDuration = 10;
        private ChronoUnit windowUnit = ChronoUnit.MINUTES;

        public @NotNull Builder limit(long limit) {
            this.limit = limit;
            return this;
        }

        public @NotNull Builder window(long duration, @NotNull ChronoUnit unit) {
            this.windowDuration = duration;
            this.windowUnit = unit;
            return this;
        }

        @Override
        public @NotNull RateLimit build() {
            return new RateLimit(
                this.limit,
                this.windowDuration,
                this.windowUnit
            );
        }

    }

}
