package dev.sbs.api.client.route;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the route (host + optional base path) for API endpoints.
 * <ul>
 *     <li>Type level: Sets default route for all methods in the interface</li>
 *     <li>Method level: Overrides the default route for a specific method</li>
 * </ul>
 * Examples:
 * <pre><code>
 * {@code @Route("api.sbs.dev")}              // Simple host
 * {@code @Route("api.sbs.dev/v2")}           // Host with base path
 * {@code @Route("sessionserver.mojang.com")} // Different subdomain
 * </code></pre>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@DynamicRoute
public @interface Route {

    /**
     * The route (host + optional path).
     *
     * @return Route string like "api.sbs.dev" or "api.sbs.dev/v2"
     */
    @NotNull String value();

}