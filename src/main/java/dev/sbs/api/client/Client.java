package dev.sbs.api.client;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiErrorDecoder;
import dev.sbs.api.client.exception.ApiException;
import dev.sbs.api.client.request.HttpMethod;
import dev.sbs.api.client.request.IRequest;
import dev.sbs.api.client.request.Request;
import dev.sbs.api.client.response.HttpStatus;
import dev.sbs.api.client.response.Response;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.collection.concurrent.ConcurrentSet;
import dev.sbs.api.mutable.pair.Pair;
import dev.sbs.api.util.builder.ClassCompiler;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Inet6Address;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Represents an abstract base class for API clients built using Feign. The {@code Client} class provides
 * configurable components for handling request and response processing, including caching response headers,
 * managing dynamic/static headers and queries, tracking recent requests and responses, and managing error decoding.
 * <p>
 * Subclasses can extend this class to create specific API client implementations.
 *
 * @param <R> The request type that extends {@link IRequest}.
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Client<R extends IRequest> implements ClassCompiler<R> {

    private final static long ONE_HOUR = Duration.ofHours(1).toMillis();
    private final @NotNull String baseUrl;
    private final @NotNull Optional<Inet6Address> inet6Address;
    private final @NotNull ConcurrentList<Request> recentRequests = Concurrent.newList();
    private final @NotNull ConcurrentList<Response> recentResponses = Concurrent.newList();
    private @NotNull ApiErrorDecoder errorDecoder = new ApiErrorDecoder.Default();
    private @NotNull ConcurrentMap<String, String> staticRequestQueries = Concurrent.newUnmodifiableMap();
    private @NotNull ConcurrentMap<String, String> staticRequestHeaders = Concurrent.newUnmodifiableMap();
    private @NotNull ConcurrentMap<String, Supplier<Optional<String>>> dynamicRequestHeaders = Concurrent.newUnmodifiableMap();
    private @NotNull ConcurrentSet<String> cachedResponseHeaders = Concurrent.newUnmodifiableSet();

    protected Client(@NotNull String url) {
        this(url, Optional.empty());
    }

    protected Client(@NotNull String url, @Nullable Inet6Address inet6Address) {
        this(url, Optional.ofNullable(inet6Address));
    }

    /**
     * Builds and configures an instance of the specified target class using Feign, based on the provided configurations.
     *
     * @param <T>    The type of the object to be built, which must extend from the parent type {@code R}.
     * @param tClass The {@code Class} object representing the type of the object to be created. Must be non-null.
     * @return An instance of the specified class type {@code T}, configured with the Feign builder.
     * @throws NullPointerException if {@code tClass} is null.
     */
    @Override
    public final <T extends R> @NotNull T build(@NotNull Class<T> tClass) {
        return Feign.builder()
            .client(
                this.getInet6Address()
                    .map(inet6Address -> new ApacheHttpClient(
                        HttpClientBuilder.create().setDefaultRequestConfig(
                            RequestConfig.copy(RequestConfig.DEFAULT)
                                .setLocalAddress(inet6Address)
                                .build()
                        ).build()
                    ))
                    .map(feign.Client.class::cast)
                    .orElse(new ApacheHttpClient())
            )
            .encoder(new GsonEncoder(SimplifiedApi.getGson()))
            .decoder(new GsonDecoder(SimplifiedApi.getGson()))
            .requestInterceptor(context -> {
                this.getStaticRequestQueries().forEach((key, value) -> context.query(key, value));
                this.getStaticRequestHeaders().forEach((key, value) -> context.header(key, value));
                this.getDynamicRequestHeaders().forEach((key, supplier) -> supplier.get()
                    .ifPresent(value -> context.header(key, value))
                );

                this.recentRequests.add(new Request.Impl(
                    System.currentTimeMillis(),
                    HttpMethod.of(context.method()),
                    context.url(),
                    context.headers()
                        .entrySet()
                        .stream()
                        .filter(entry -> !entry.getValue().isEmpty())
                        .map(entry -> Pair.of(
                            entry.getKey(),
                            (ConcurrentList<String>) Concurrent.newUnmodifiableList(entry.getValue())
                        ))
                        .collect(Concurrent.toUnmodifiableMap())
                ));
                this.recentRequests.removeIf(request -> request.getTimestamp().toEpochMilli() < System.currentTimeMillis() - ONE_HOUR);
            })
            .responseInterceptor((context, chain) -> {
                this.recentResponses.add(new Response.Impl(
                    System.currentTimeMillis(),
                    HttpStatus.of(context.response().status()),
                    context.response()
                        .headers()
                        .entrySet()
                        .stream()
                        .filter(entry -> !entry.getValue().isEmpty())
                        .filter(entry -> this.getCachedResponseHeaders()
                            .stream()
                            .anyMatch(key -> entry.getKey().equalsIgnoreCase(key))
                        )
                        .map(entry -> Pair.of(
                            entry.getKey(),
                            (ConcurrentList<String>) Concurrent.newUnmodifiableList(entry.getValue())
                        ))
                        .collect(Concurrent.toUnmodifiableMap())
                ));

                this.recentResponses.removeIf(response -> response.getTimestamp().toEpochMilli() < System.currentTimeMillis() - ONE_HOUR);
                return chain.next(context);
            })
            .errorDecoder((methodKey, response) -> {
                ApiException exception = this.getErrorDecoder().decode(methodKey, response);
                this.recentResponses.add(exception);
                return exception;
            })
            .options(new feign.Request.Options(
                5,
                TimeUnit.SECONDS,
                10,
                TimeUnit.SECONDS,
                true
            ))
            .target(tClass, this.getUrl());
    }

    /**
     * Retrieves the most recent request from the list of recent requests.
     * <p>
     * The determination of the most recent request is based on the timestamp
     * associated with each request.
     *
     * @return An {@code Optional} containing the most recent {@code Request}
     *         if available; otherwise, an empty {@code Optional}.
     */
    public final @NotNull Optional<Request> getLastRequest() {
        return this.getRecentRequests()
            .stream()
            .reduce((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()) <= 0 ? o2 : o1);
    }

    /**
     * Retrieves the most recent response from the list of recent responses.
     * <p>
     * The determination of the most recent response is based on the timestamp
     * associated with each response.
     *
     * @return An {@code Optional} containing the most recent {@code Response}
     *         if available; otherwise, an empty {@code Optional}.
     */
    public final @NotNull Optional<Response> getLastResponse() {
        return this.getRecentResponses()
            .stream()
            .reduce((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()) <= 0 ? o2 : o1);
    }

    /**
     * Calculates the latency between the most recent request and response.
     * <p>
     * The method retrieves the most recent request and response timestamps, if available,
     * calculates the time difference, and returns this value as the latency.
     * <p>
     * If either the request or response is not available, the method returns -1.
     *
     * @return The latency in milliseconds as a {@code long} value,
     *         or -1 if the request or response is not available.
     */
    public final long getLatency() {
        return this.getLastRequest()
            .map(Request::getTimestamp)
            .map(Instant::toEpochMilli)
            .flatMap(request -> this.getLastResponse()
                .map(Response::getTimestamp)
                .map(Instant::toEpochMilli)
                .map(response -> request - response)
            )
            .orElse(-1L);
    }

    /**
     * Retrieves the URL for the client instance, ensuring the returned string is consistently formatted.
     * <p>
     * Any protocol prefix (http:// or https://) from the base URL is stripped and replaced with "https://".
     *
     * @return The formatted URL as a non-null string.
     */
    public final @NotNull String getUrl() {
        return String.format("https://%s", this.baseUrl.replaceFirst("^https?://", ""));
    }

}
