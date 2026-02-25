package dev.sbs.api.util;

import dev.sbs.api.client.response.ConnectionDetails;
import dev.sbs.api.client.route.Route;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.stream.pair.Pair;
import lombok.experimental.UtilityClass;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public final class ClientUtil {

    public static void addHeader(@NotNull HttpRequest request, @NotNull HttpContext context, @NotNull String id) {
        Object value = context.getAttribute(id);

        if (value != null)
            request.addHeader(id, String.valueOf(value));
    }

    public static @NotNull String extractDefaultRoute(@NotNull Class<?> targetInterface) {
        Route routeAnnotation = targetInterface.getAnnotation(Route.class);

        if (routeAnnotation != null)
            return routeAnnotation.value().replaceFirst("^https?://", "");

        throw new IllegalArgumentException("No default host configured for " + targetInterface.getName() + ". Add @DefaultHost annotation to the interface.");
    }

    public static Optional<String> extractHeader(@NotNull Map<String, Collection<String>> headers, @NotNull String key) {
        return Optional.ofNullable(headers.get(key)).flatMap(values -> values.stream().findFirst());
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(@NotNull HttpContext context, @NotNull String id, T defaultValue) {
        return context.getAttribute(id) != null ? (T) context.getAttribute(id) : defaultValue;
    }

    public static @NotNull ConcurrentMap<String, ConcurrentList<String>> getHeaders(@NotNull Map<String, Collection<String>> headers) {
        return headers.entrySet()
            .stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .filter(entry -> !ConnectionDetails.isInternalHeader(entry.getKey()))
            .map(entry -> Pair.of(
                entry.getKey(),
                (ConcurrentList<String>) Concurrent.newUnmodifiableList(entry.getValue())
            ))
            .collect(Concurrent.toUnmodifiableMap());
    }

    public static @NotNull String stripProtocol(@NotNull String route) {
        return route.replaceFirst("^https?://", "");
    }

    public static @NotNull String toFullUrl(@NotNull String route) {
        String cleanRoute = stripProtocol(route);
        return String.format("https://%s", cleanRoute);
    }

}
