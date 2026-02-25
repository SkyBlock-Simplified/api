package dev.sbs.api.client.ratelimit;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.temporal.ChronoUnit;

@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimitConfig {

    /**
     * Maximum number of requests allowed in the window.
     */
    long limit() default Long.MAX_VALUE;

    /**
     * Window duration.
     */
    long window() default 600;

    /**
     * Window duration unit.
     */
    @NotNull ChronoUnit unit() default ChronoUnit.SECONDS;

    /**
     * Whether this route has unlimited rate limiting.
     * <p>
     * Overrides other values.
     */
    boolean unlimited() default false;

}
