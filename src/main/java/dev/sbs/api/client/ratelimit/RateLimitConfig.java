package dev.sbs.api.client.ratelimit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimitConfig {

    /**
     * Maximum number of requests allowed in the window.
     */
    long limit() default Long.MAX_VALUE;

    /**
     * Window duration in seconds.
     * <p>
     * Mirrors RateLimit-Reset semantics.
     */
    long resetSeconds() default 600; // 10 minutes

    /**
     * Whether this route has unlimited rate limiting.
     * <p>
     * Overrides other values.
     */
    boolean unlimited() default false;

}
