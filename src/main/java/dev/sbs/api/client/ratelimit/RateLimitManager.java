package dev.sbs.api.client.ratelimit;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Tracks rate limit state across multiple buckets (domains/endpoints)
 */
@NoArgsConstructor
public class RateLimitManager {
    
    private final @NotNull ConcurrentMap<String, RateLimitBucket> buckets = Concurrent.newMap();

    /**
     * Gets or creates a bucket for the given ID
     */
    private @NotNull RateLimitBucket getOrCreateBucket(@NotNull String bucketId, @NotNull RateLimit rateLimit) {
        return this.buckets.computeIfAbsent(bucketId, __ -> new RateLimitBucket(rateLimit));
    }

    /**
     * Checks if a bucket is currently rate-limited.
     *
     * @apiNote Will not create a bucket.
     */
    public boolean isRateLimited(@NotNull String bucketId) {
        RateLimitBucket bucket = this.buckets.get(bucketId);
        return bucket != null && bucket.isRateLimited();
    }

    /**
     * Checks if a bucket is currently rate-limited.
     *
     * @apiNote Known sources will create a missing bucket.
     */
    public boolean isRateLimited(@NotNull String bucketId, @NotNull RateLimit rateLimit) {
        return this.getOrCreateBucket(bucketId, rateLimit).isRateLimited();
    }
    
    /**
     * Tracks a request for a bucket.
     *
     * @apiNote Known sources will create a missing bucket.
     */
    public void trackRequest(@NotNull String bucketId, @NotNull RateLimit rateLimit) {
        this.getOrCreateBucket(bucketId, rateLimit).trackRequest();
    }
    
    /**
     * Updates rate limit configuration for a bucket (from server headers).
     *
     * @apiNote Known sources will create a missing bucket.
     */
    public void updateRateLimit(@NotNull String bucketId, @NotNull RateLimit newLimit) {
        RateLimitBucket bucket = this.buckets.get(bucketId);

        if (bucket != null)
            bucket.updateRateLimit(newLimit);
        else {
            // If server tells us about a new/unknown route, create it
            this.buckets.put(bucketId, new RateLimitBucket(newLimit));
        }
    }
    
    /**
     * Gets the current request count for a bucket.
     *
     * @return Current window request count, or 0 if bucket not found.
     * @apiNote Will not create a bucket.
     */
    public long getRequestCount(@NotNull String bucketId) {
        RateLimitBucket bucket = this.buckets.get(bucketId);
        return bucket != null ? bucket.getCount() : 0;
    }
    
    /**
     * Gets remaining requests for a bucket.
     *
     * @return Remaining request count, or {@link RateLimit#UNLIMITED} if bucket not found.
     * @apiNote Will not create a bucket.
     */
    public long getRemaining(@NotNull String bucketId) {
        RateLimitBucket bucket = this.buckets.get(bucketId);
        return bucket != null ? bucket.getRemaining() : RateLimit.UNLIMITED.getLimit();
    }
    
    /**
     * Clears all buckets
     */
    public void clear() {
        this.buckets.clear();
    }

    /**
     * Checks if a bucket exists.
     *
     * @apiNote Will not create a bucket.
     */
    public boolean hasBucket(@NotNull String bucketId) {
        return this.buckets.containsKey(bucketId);
    }

    /**
     * Resets a bucket.
     *
     * @apiNote Will not create a bucket.
     */
    public void reset(@NotNull String bucketId) {
        RateLimitBucket bucket = this.buckets.get(bucketId);

        if (bucket != null)
            bucket.reset();
    }

}
