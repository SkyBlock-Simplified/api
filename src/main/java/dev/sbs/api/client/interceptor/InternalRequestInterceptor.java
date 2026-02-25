package dev.sbs.api.client.interceptor;

import dev.sbs.api.client.exception.RateLimitException;
import dev.sbs.api.client.ratelimit.RateLimitManager;
import dev.sbs.api.client.route.RouteDiscovery;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * Intercepts and processes HTTP request templates to apply routing and rate limiting logic.
 * <p>
 * This interceptor integrates with a rate-limiting system and a route discovery mechanism to monitor
 * and control outbound HTTP requests. It ensures that requests adhere to configured rate limits
 * and are routed to the appropriate target URLs.
 * </p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Enforces rate limits for identified routes using {@code RateLimitManager}.</li>
 *   <li>Discovers metadata about the target route, such as the full URL and associated rate limits,
 *       via {@code RouteDiscovery}.</li>
 *   <li>Throws a {@code RateLimitException} for requests that exceed the allowed rate limit.</li>
 *   <li>Tracks processed requests for future rate limit evaluations.</li>
 *   <li>Updates the target URL of the request based on discovered route metadata.</li>
 * </ul>
 *
 * <h2>Mechanism</h2>
 * The interceptor applies the following sequence to each HTTP request template:
 * <ol>
 *   <li>Identifies metadata related to the target route using {@code routeDiscovery}.</li>
 *   <li>Checks the rate limit status of the route via {@code rateLimitManager}.</li>
 *   <li>If the request is rate-limited, an exception is thrown to prevent execution.</li>
 *   <li>Tracks the request in the rate-limiting system for compliance checks.</li>
 *   <li>Sets the final target URL in the request template based on discovered route metadata.</li>
 * </ol>
 *
 * <h2>Core Components</h2>
 * <ul>
 *   <li><b>RateLimitManager:</b> The component responsible for tracking and enforcing rate limits
 *       for specific routes. It ensures that requests adhere to the allowed request quotas.</li>
 *   <li><b>RouteDiscovery:</b> A mechanism that provides metadata about a method's associated route,
 *       including the endpoint's URL and its rate-limiting configuration.</li>
 * </ul>
 *
 * <h2>Exception Handling</h2>
 * Throws a {@code RateLimitException} if a request is determined to exceed the allowed rate limit
 * for the associated route.
 *
 * @apiNote Only for internal use.
 */
@RequiredArgsConstructor
public final class InternalRequestInterceptor implements RequestInterceptor {

    private final @NotNull RateLimitManager rateLimitManager;
    private final @NotNull RouteDiscovery routeDiscovery;

    @Override
    public void apply(@NotNull RequestTemplate template) {
        Method method = template.methodMetadata().method();
        RouteDiscovery.Metadata routeMetadata = this.routeDiscovery.getMetadata(method);

        if (this.rateLimitManager.isRateLimited(routeMetadata.getRoute(), routeMetadata.getRateLimit()))
            throw new RateLimitException(template, routeMetadata);

        this.rateLimitManager.trackRequest(routeMetadata.getRoute(), routeMetadata.getRateLimit());

        template.target(routeMetadata.getFullUrl());
    }

}
