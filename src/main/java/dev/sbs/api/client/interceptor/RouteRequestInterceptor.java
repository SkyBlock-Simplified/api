package dev.sbs.api.client.interceptor;

import dev.sbs.api.client.exception.RateLimitException;
import dev.sbs.api.client.ratelimit.RateLimitManager;
import dev.sbs.api.client.route.DynamicRoute;
import dev.sbs.api.client.route.Route;
import dev.sbs.api.client.route.RouteDiscovery;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * RequestInterceptor for dynamic route routing based on method annotations.
 * <p>
 * Scans for methods with {@link Route} or {@link DynamicRoute} meta-annotations and caches them at construction time.
 *
 * @apiNote Methods without annotations will use the default route from the interface-level annotation.
 */
@RequiredArgsConstructor
public class RouteRequestInterceptor implements RequestInterceptor {

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
