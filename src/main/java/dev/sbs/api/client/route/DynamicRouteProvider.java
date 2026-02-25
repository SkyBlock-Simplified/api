package dev.sbs.api.client.route;

import dev.sbs.api.client.ratelimit.RateLimit;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for providing dynamic routes to API endpoints with optional rate limiting.
 * <p>
 * The route should be returned WITHOUT the protocol prefix (e.g., "api.sbs.dev", not "https&colon;//api.sbs.dev").
 *
 * @apiNote The protocol will be added automatically when constructing the full URL.
 */
public interface DynamicRouteProvider {

    /**
     * Returns the route (host + optional base path).
     *
     * @return Route string like "api.sbs.dev" or "api.sbs.dev/v2"
     */
    @NotNull String getRoute();

    /**
     * Returns the rate limit configuration for this route.
     * <p>
     * Unlimited rate limit by default.
     *
     * @return RateLimit configuration
     * @apiNote Rate limits are applied per route, not per endpoint.
     */
    default @NotNull RateLimit getRateLimit() {
        return RateLimit.UNLIMITED;
    }

    /**
     * Returns a unique identifier for rate limit bucket tracking.
     * <p>
     * Default uses the route itself as the ID.
     *
     * @return Unique identifier (typically the route)
     * @apiNote Different routes with the same ID will share rate limit tracking.
     */
    default @NotNull String getBucketId() {
        return this.getRoute();
    }

}
