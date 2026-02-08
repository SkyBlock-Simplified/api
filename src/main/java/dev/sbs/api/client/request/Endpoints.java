package dev.sbs.api.client.request;

import dev.sbs.api.client.Client;

/**
 * Marker interface for Feign-based HTTP client request interface endpoints.
 * Used to constrain {@link Client} implementations and provide
 * type-safe lookup in {@link Client#getEndpoints()}.
 */
public interface Endpoints {

}
