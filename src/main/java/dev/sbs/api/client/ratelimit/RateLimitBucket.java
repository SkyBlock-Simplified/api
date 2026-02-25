package dev.sbs.api.client.ratelimit;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Thread-safe rate limit bucket for tracking requests per domain
 */
@Getter
class RateLimitBucket {
    
    private final @NotNull AtomicLong windowStart;
    private final @NotNull AtomicLong requestCount;
    private final @NotNull AtomicReference<RateLimit> rateLimit;
    
    RateLimitBucket(@NotNull RateLimit initialRateLimit) {
        this.windowStart = new AtomicLong(System.currentTimeMillis());
        this.requestCount = new AtomicLong(0);
        this.rateLimit = new AtomicReference<>(initialRateLimit);
    }
    
    /**
     * Checks if the bucket is currently rate limited
     */
    public boolean isRateLimited() {
        RateLimit limit = rateLimit.get();
        if (limit.isUnlimited()) {
            return false;
        }

        long now = System.currentTimeMillis();
        long start = windowStart.get();
        long windowMillis = limit.getWindowDurationMillis();

        if (now - start >= windowMillis) {
            if (windowStart.compareAndSet(start, now)) {
                requestCount.set(0);
                return false;
            }
        }

        long current = requestCount.get();
        long hardLimit = limit.getLimit();


        // If you still want a buffer, apply it here instead of in RateLimit itself:
        // long softLimit = (long) (hardLimit * 0.9); // 10% buffer
        // return current >= softLimit;

        return current >= hardLimit;
    }
    
    /**
     * Tracks a request in this bucket
     */
    public void trackRequest() {
        RateLimit limit = this.rateLimit.get();

        if (limit.isUnlimited())
            return;

        long now = System.currentTimeMillis();
        long start = this.windowStart.get();
        long windowMillis = limit.getWindowDurationMillis();

        if (now - start >= windowMillis) {
            if (this.windowStart.compareAndSet(start, now)) {
                this.requestCount.set(1);
                return;
            }
        }

        this.requestCount.incrementAndGet();
    }
    
    /**
     * Updates the rate limit configuration (from server headers)
     */
    public void updateRateLimit(@NotNull RateLimit newLimit) {
        this.rateLimit.set(newLimit);
    }
    
    /**
     * Resets this bucket (clears all tracking)
     */
    public void reset() {
        this.windowStart.set(System.currentTimeMillis());
        this.requestCount.set(0);
    }
    
    /**
     * Gets the current request count
     */
    public long getCount() {
        return this.requestCount.get();
    }
    
    /**
     * Gets remaining requests in current window
     */
    public long getRemaining() {
        RateLimit limit = this.rateLimit.get();

        if (limit.isUnlimited())
            return Long.MAX_VALUE;

        long remaining = limit.getLimit() - this.requestCount.get();
        return Math.max(0, remaining);
    }

}
