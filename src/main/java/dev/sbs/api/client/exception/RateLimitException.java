package dev.sbs.api.client.exception;

import dev.sbs.api.client.ratelimit.RateLimit;
import dev.sbs.api.client.response.HttpStatus;
import dev.sbs.api.client.route.RouteDiscovery;
import feign.RequestTemplate;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown when a rate limit is exceeded.
 *
 * @apiNote Only for internal use.
 */
@Getter
public final class RateLimitException extends ApiException {

    private final boolean serverEnforced;
    private final @NotNull String bucketId;
    private final @NotNull RateLimit rateLimit;

    /**
     * Server-side reactive rate limit exception (from 429 response).
     */
    public RateLimitException(@NotNull String methodKey, @NotNull feign.Response response, @NotNull RouteDiscovery.Metadata routeMetadata) {
        super(methodKey, response, "RateLimit");
        this.serverEnforced = true;
        this.bucketId = routeMetadata.getRoute();
        this.rateLimit = routeMetadata.getRateLimit();
    }

    /**
     * Client-side proactive rate limit exception.
     */
    public RateLimitException(@NotNull RequestTemplate template, @NotNull RouteDiscovery.Metadata routeMetadata) {
        super(
            template.methodMetadata().method().getName(),
            feign.Response.builder()
                .status(HttpStatus.TOO_MANY_REQUESTS.getCode())
                .reason(HttpStatus.TOO_MANY_REQUESTS.getMessage())
                .headers(template.headers())
                .request(template.request())
                .protocolVersion(template.request().protocolVersion())
                .build(),
            "RateLimit"
        );
        this.serverEnforced = false;
        this.bucketId = routeMetadata.getRoute();
        this.rateLimit = routeMetadata.getRateLimit();
    }

}
