package dev.sbs.api.client.route;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.ClientUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * RequestInterceptor for dynamic route routing based on method annotations.
 * <p>
 * Scans for methods with {@link Route} or {@link DynamicRoute} meta-annotations and caches them at construction time.
 *
 * @apiNote Methods without annotations will use the default route from the interface-level {@link Route} annotation.
 */
public class RouteRequestInterceptor implements RequestInterceptor {

    private final @NotNull String defaultRoute;
    private final @Nullable RouteChangeListener routeChangeListener;
    private final @NotNull ConcurrentMap<Method, RouteInfo> routeCache;

    public RouteRequestInterceptor(@NotNull Class<?> targetInterface, @NotNull String defaultRoute) {
        this(targetInterface, defaultRoute, null);
    }

    public RouteRequestInterceptor(@NotNull Class<?> targetInterface, @NotNull String defaultRoute, @Nullable RouteChangeListener routeChangeListener) {
        this.routeCache = extractMethodRouteInfo(targetInterface);
        this.defaultRoute = ClientUtil.stripProtocol(defaultRoute);
        this.routeChangeListener = routeChangeListener;
    }

    @Override
    public void apply(@NotNull RequestTemplate template) {
        Method method = template.methodMetadata().method();
        RouteInfo routeInfo = this.routeCache.get(method);

        if (routeInfo != null) {
            String targetRoute = routeInfo.getRoute();
            RouteProvider routeProvider = routeInfo.getProvider();

            if (this.routeChangeListener != null)
                this.routeChangeListener.onBeforeRequest(method, targetRoute, routeProvider);

            template.target(ClientUtil.toFullUrl(targetRoute));
        } else {
            if (this.routeChangeListener != null)
                this.routeChangeListener.onBeforeRequest(method, this.defaultRoute, null);
        }
    }

    private static @NotNull Optional<RouteInfo> extractRouteFromMethod(@NotNull Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            DynamicRoute dynamicRoute = annotation.annotationType().getAnnotation(DynamicRoute.class);

            if (dynamicRoute == null)
                continue;

            try {
                // Get the method that returns the URL provider
                String methodName = dynamicRoute.methodName();
                Method valueMethod = annotation.annotationType().getMethod(methodName);
                Object value = valueMethod.invoke(annotation);

                if (value instanceof RouteProvider provider) {
                    String route = ClientUtil.stripProtocol(provider.getRoute());
                    return Optional.of(new RouteInfo(route, provider));
                } else if (value instanceof String stringRoute) {
                    String route = ClientUtil.stripProtocol(stringRoute);
                    return Optional.of(new RouteInfo(route, () -> route));
                }
            } catch (Exception ignore) { }

            break;
        }

        return Optional.empty();
    }

    private static @NotNull ConcurrentMap<Method, RouteInfo> extractMethodRouteInfo(@NotNull Class<?> targetInterface) {
        ConcurrentMap<Method, RouteInfo> hostCache = Concurrent.newMap();

        for (Method method : targetInterface.getDeclaredMethods())
            extractRouteFromMethod(method).ifPresent(info -> hostCache.put(method, info));

        return hostCache.toUnmodifiableMap();
    }

    @Getter
    @RequiredArgsConstructor
    private static class RouteInfo {

        private final @NotNull String route;
        private final @NotNull RouteProvider provider;

    }

}
