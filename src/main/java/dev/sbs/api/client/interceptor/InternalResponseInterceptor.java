package dev.sbs.api.client.interceptor;

import dev.sbs.api.client.ratelimit.RateLimit;
import dev.sbs.api.client.ratelimit.RateLimitManager;
import dev.sbs.api.client.route.RouteDiscovery;
import feign.InvocationContext;
import feign.ResponseInterceptor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link ResponseInterceptor} that processes responses
 * to manage rate limiting and route discovery metadata.
 *
 * <p>This class intercepts responses received during HTTP calls, extracts
 * rate limit information from the response headers, and updates the
 * appropriate rate limit configuration using {@link RateLimitManager}.
 * It also utilizes {@link RouteDiscovery} to resolve metadata
 * related to the request route.
 *
 * <p>This interceptor ensures proper tracking of rate limits associated with
 * routes, enabling efficient management of resources and adherence to
 * server-imposed restrictions.
 *
 * <h3>Core Functionality</h3>
 * <ul>
 *   <li>Extracts rate limit information from response headers.</li>
 *   <li>Identifies route metadata using {@link RouteDiscovery}.</li>
 *   <li>Updates rate limit data in {@link RateLimitManager} based on the
 *   extracted headers and resolved route metadata.</li>
 *   <li>Proceeds with the subsequent steps in the interceptor chain.</li>
 * </ul>
 *
 * <h3>Dependencies</h3>
 * <ul>
 *   <li>{@link RateLimitManager}: Handles rate limit state and updates.</li>
 *   <li>{@link RouteDiscovery}: Discovers and resolves metadata for the
 *   request's URL route.</li>
 * </ul>
 *
 * <h3>Methods</h3>
 * <dl>
 *   <dt>{@link #intercept(InvocationContext, Chain)}</dt>
 *   <dd>Processes the response, managing route metadata and rate limit updates,
 *   and then delegates to the next interceptor in the chain.</dd>
 * </dl>
 *
 * @apiNote Only for internal use.
 */
@RequiredArgsConstructor
public final class InternalResponseInterceptor implements ResponseInterceptor {

    private final @NotNull RateLimitManager rateLimitManager;
    private final @NotNull RouteDiscovery routeDiscovery;

    @Override
    public Object intercept(InvocationContext invocationContext, Chain chain) throws Exception {
        feign.Response response = invocationContext.response();
        RouteDiscovery.Metadata routeMetadata = this.routeDiscovery.findMatchingMetadata(response.request().url());
        RateLimit.fromHeaders(response.headers()).ifPresent(serverLimit -> this.rateLimitManager.updateRateLimit(routeMetadata.getRoute(), serverLimit));
        return chain.next(invocationContext);
    }

}
