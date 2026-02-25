package dev.sbs.api.client.interceptor;

import dev.sbs.api.client.ratelimit.RateLimit;
import dev.sbs.api.client.ratelimit.RateLimitManager;
import dev.sbs.api.client.route.RouteDiscovery;
import feign.InvocationContext;
import feign.ResponseInterceptor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class RouteResponseInterceptor implements ResponseInterceptor {

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
