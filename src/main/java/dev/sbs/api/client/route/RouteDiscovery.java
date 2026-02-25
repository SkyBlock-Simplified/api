package dev.sbs.api.client.route;

import dev.sbs.api.client.ratelimit.RateLimit;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.ClientUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

@Getter
public final class RouteDiscovery {

    private final @NotNull Metadata defaultRoute;
    private final @NotNull ConcurrentMap<Method, Metadata> methodRoutes;

    public RouteDiscovery(@NotNull Class<?> target) {
        Optional<Metadata> defaultRoute = extractRouteFromTarget(target);

        if (defaultRoute.isEmpty())
            throw new IllegalArgumentException("No @Route or @DynamicRoute found on type of " + target.getName());

        ConcurrentMap<Method, Metadata> methodRoutes = Concurrent.newMap();

        for (Method method : target.getDeclaredMethods())
            extractRouteFromTarget(method).ifPresent(info -> methodRoutes.put(method, info));

        this.defaultRoute = defaultRoute.get();
        this.methodRoutes = methodRoutes.toUnmodifiableMap();
    }

    private static @NotNull Optional<Metadata> extractRouteFromTarget(@NotNull Object target) {
        Class<?> targetClass;
        if (target instanceof Class<?> clazz)
            targetClass = clazz;
        else if (target instanceof Method method)
            targetClass = method.getDeclaringClass();
        else
            return Optional.empty();

        Route routeAnno = targetClass.getAnnotation(Route.class);
        if (routeAnno != null) {
            String route = ClientUtil.stripProtocol(routeAnno.value());
            RateLimit rateLimit = RateLimit.fromAnnotation(routeAnno.rateLimit());
            return Optional.of(new Metadata(route, rateLimit));
        }

        Annotation[] annotations = (target instanceof Method method)
            ? method.getAnnotations()
            : targetClass.getAnnotations();

        for (Annotation annotation : annotations) {
            DynamicRoute dynamicRoute = annotation.annotationType().getAnnotation(DynamicRoute.class);

            if (dynamicRoute == null)
                continue;

            try {
                // Get the method that returns the URL provider
                String methodName = dynamicRoute.methodName();
                Method valueMethod = annotation.annotationType().getMethod(methodName);
                Object value = valueMethod.invoke(annotation);

                if (value instanceof DynamicRouteProvider provider) {
                    return Optional.of(new Metadata(
                        ClientUtil.stripProtocol(provider.getRoute()),
                        provider.getRateLimit()
                    ));
                }
            } catch (Exception ignore) { }
        }

        return Optional.empty();
    }

    /**
     * Finds the longest known route that is a prefix of the given request URL.
     * <p>
     * Handles both plain host ("api.sbs.dev") and host+path ("api.sbs.dev/v2") routes.
     *
     * @apiNote Falls back to the default route if no match is found.
     */
    public @NotNull Metadata findMatchingMetadata(@NotNull String requestUrl) {
        String stripped = ClientUtil.stripProtocol(requestUrl);
        Metadata defaultRoute = this.defaultRoute;

        // Seed with the default route (always the fallback)
        Metadata bestMatch = defaultRoute;
        int bestMatchLength = stripped.startsWith(defaultRoute.getRoute()) ? defaultRoute.getRoute().length() : 0;

        // Find the longest prefix match among method-level route overrides
        for (Metadata metadata : this.methodRoutes.values()) {
            String route = metadata.getRoute();

            if (stripped.startsWith(route) && route.length() > bestMatchLength) {
                bestMatch = metadata;
                bestMatchLength = route.length();
            }
        }

        return bestMatch;
    }

    public @NotNull Metadata getMetadata(@NotNull Method method) {
        return this.getMethodRoutes().getOrDefault(method, this.getDefaultRoute());
    }

    @Getter
    @RequiredArgsConstructor
    public static class Metadata {

        private final @NotNull String route;
        private final @NotNull RateLimit rateLimit;

        public @NotNull String getFullUrl() {
            return String.format("https://%s", this.getRoute());
        }

    }

}
