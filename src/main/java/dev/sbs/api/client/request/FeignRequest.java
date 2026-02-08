package dev.sbs.api.client.request;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.Client;

/**
 * Marker interface for Feign-based HTTP client request interfaces.
 * Used to constrain {@link Client} implementations and provide
 * type-safe lookup in {@link SimplifiedApi#getApiRequest}.
 */
public interface FeignRequest {

}
