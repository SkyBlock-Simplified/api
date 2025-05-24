package dev.sbs.api.client.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Represents the caching status of a resource as determined by Cloudflare.
 * <p>
 * The values in this enum correspond to possible statuses returned by Cloudflare for a resource,
 * indicating its caching behavior or eligibility. These statuses provide insight into
 * whether a resource was cached, served from the origin server, or encountered specific
 * caching behaviors.
 */
@Getter
@RequiredArgsConstructor
public enum CFCacheStatus {

    // https://developers.cloudflare.com/cache/concepts/default-cache-behavior/#cloudflare-cache-responses

    UNKNOWN("The Cloudflare caching status of this resource could not be found."),
    NONE("Cloudflare generated a response that denotes the asset is not eligible for caching."),

    BYPASS("The origin server instructed Cloudflare to bypass cache via a Cache-Control header set to no-cache,private, or max-age=0 even though Cloudflare originally preferred to cache the asset."),
    DYNAMIC("Cloudflare does not consider the asset eligible to cache and your Cloudflare settings do not explicitly instruct Cloudflare to cache the asset. Instead, the asset was requested from the origin web server."),
    EXPIRED("The resource was found in Cloudflare’s cache but was expired and served from the origin web server."),
    HIT("The resource was found in Cloudflare’s cache."),
    MISS("The resource was not found in Cloudflare’s cache and was served from the origin web server."),
    REVALIDATED("The resource is served from Cloudflare’s cache but is stale. The resource was revalidated by either an If-Modified-Since header or an If-None-Match header."),
    STALE("The resource was served from Cloudflare’s cache but was expired. Cloudflare could not contact the origin to retrieve an updated resource."),
    UPDATING("The resource was served from Cloudflare’s cache and was expired, but the origin web server is updating the resource.");

    public static final @NotNull String HEADER_KEY = "CF-Cache-Status";
    private final @NotNull String description;

    public static @NotNull CFCacheStatus of(@NotNull String name) {
        return Arrays.stream(values())
            .filter(value -> value.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(UNKNOWN);
    }

}
