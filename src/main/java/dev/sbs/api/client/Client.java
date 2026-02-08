package dev.sbs.api.client;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiErrorDecoder;
import dev.sbs.api.client.exception.ApiException;
import dev.sbs.api.client.request.Endpoints;
import dev.sbs.api.client.request.HttpMethod;
import dev.sbs.api.client.request.Request;
import dev.sbs.api.client.response.HttpStatus;
import dev.sbs.api.client.response.Response;
import dev.sbs.api.client.timing.Latency;
import dev.sbs.api.client.timing.TimedPlainConnectionSocketFactory;
import dev.sbs.api.client.timing.TimedSecureConnectionSocketFactory;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.collection.concurrent.ConcurrentSet;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.stream.pair.Pair;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Inet6Address;
import java.time.Duration;
import java.util.Arrays;
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
 * @param <E> An interface with annotated feign methods.
 */
@Getter
public abstract class Client<E extends Endpoints> {

    private final static long ONE_HOUR = Duration.ofHours(1).toMillis();
    private final @NotNull String baseUrl;
    private final @NotNull Optional<Inet6Address> inet6Address;
    @Getter(AccessLevel.NONE) private final @NotNull ApacheHttpClient internalClient;
    private final @NotNull E endpoints;

    // Requests
    private final @NotNull ConcurrentList<Request> recentRequests = Concurrent.newList();
    private final @NotNull ConcurrentMap<String, String> requestQueries;
    private final @NotNull ConcurrentMap<String, String> requestHeaders;
    private final @NotNull ConcurrentMap<String, Supplier<Optional<String>>> dynamicHeaders;

    // Responses
    private final @NotNull ConcurrentList<Response> recentResponses = Concurrent.newList();
    private final @NotNull ApiErrorDecoder errorDecoder;
    private final @NotNull ConcurrentSet<String> responseHeaders;

    protected Client(@NotNull String url) {
        this(url, Optional.empty());
    }

    protected Client(@NotNull String url, @Nullable Inet6Address inet6Address) {
        this(url, Optional.ofNullable(inet6Address));
    }

    protected Client(@NotNull String url, @NotNull Optional<Inet6Address> inet6Address) {
        this.baseUrl = url;
        this.inet6Address = inet6Address;
        this.internalClient = this.configureInternalClient();
        this.requestQueries = this.configureRequestQueries();
        this.requestHeaders = this.configureRequestHeaders();
        this.dynamicHeaders = this.configureDynamicHeaders();
        this.errorDecoder = this.configureErrorDecoder();
        this.responseHeaders = this.configureResponseHeaders();
        this.endpoints = this.configureEndpoints();
    }

    protected final @NotNull ApacheHttpClient configureInternalClient() {
        // Timed Socket Factories
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", new TimedPlainConnectionSocketFactory(
                PlainConnectionSocketFactory.getSocketFactory(),
                SystemDefaultDnsResolver.INSTANCE
            ))
            .register("https", new TimedSecureConnectionSocketFactory(
                SSLConnectionSocketFactory.getSocketFactory(),
                SystemDefaultDnsResolver.INSTANCE
            ))
            .build();

        // HTTP Client
        @SuppressWarnings("deprecation")
        HttpClientBuilder httpClient = HttpClientBuilder.create()
            .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
            .setMaxConnTotal(100)
            .setMaxConnPerRoute(20)
            .addInterceptorFirst((HttpRequestInterceptor) (request, context) -> {
                context.setAttribute(Latency.REQUEST_START, System.nanoTime());
                this.getRequestQueries().forEach((key, value) -> request.getParams().setParameter(key, value));
                this.getRequestHeaders().forEach((key, value) -> request.addHeader(key, value));
                this.getDynamicHeaders().forEach((key, supplier) -> supplier.get()
                    .ifPresent(value -> request.addHeader(key, value))
                );

                this.recentRequests.add(new Request.Impl(
                    System.currentTimeMillis(),
                    HttpMethod.of(request.getRequestLine().getMethod()),
                    request.getRequestLine().getUri(),
                    Arrays.stream(request.getAllHeaders())
                        .flatMap(header -> Arrays.stream(header.getElements()))
                        .filter(entry -> !entry.getValue().isEmpty())
                        .map(entry -> Pair.of(
                            entry.getName(),
                            (ConcurrentList<String>) Concurrent.newUnmodifiableList(entry.getValue())
                        ))
                        .collect(Concurrent.toUnmodifiableMap())
                ));
                this.recentRequests.removeIf(recentRequest -> recentRequest.getTimestamp().toEpochMilli() < System.currentTimeMillis() - ONE_HOUR);
            })
            .addInterceptorLast((HttpResponseInterceptor) (response, context) -> {
                context.setAttribute(Latency.RESPONSE_RECEIVED, System.nanoTime());

                this.recentResponses.add(new Response.Impl(
                    new Latency(context),
                    HttpStatus.of(response.getStatusLine().getStatusCode()),
                    Arrays.stream(response.getAllHeaders())
                        .flatMap(header -> Arrays.stream(header.getElements()))
                        .filter(entry -> !entry.getValue().isEmpty())
                        .filter(entry -> this.getResponseHeaders()
                            .stream()
                            .anyMatch(key -> entry.getName().equalsIgnoreCase(key))
                        )
                        .map(entry -> Pair.of(
                            entry.getName(),
                            (ConcurrentList<String>) Concurrent.newUnmodifiableList(entry.getValue())
                        ))
                        .collect(Concurrent.toUnmodifiableMap())
                ));

                this.recentResponses.removeIf(recentResponse -> recentResponse.getTimestamp().toEpochMilli() < System.currentTimeMillis() - ONE_HOUR);
            })
            .evictIdleConnections(30, TimeUnit.SECONDS);

        // Custom Local Address
        this.getInet6Address().ifPresent(inet6Address -> httpClient.setDefaultRequestConfig(
            RequestConfig.copy(RequestConfig.DEFAULT)
                .setLocalAddress(inet6Address)
                .build()
        ));

        return new ApacheHttpClient(httpClient.build());
    }

    /**
     * Builds and configures an instance of the specified target class using Feign, based on the provided configurations.
     *
     * @return An instance of the specified class type {@code T}, configured with the Feign builder.
     * @throws NullPointerException if {@code tClass} is null.
     */
    protected <T extends E> @NotNull T configureEndpoints() {
        return Feign.builder()
            .client(this.internalClient)
            .encoder(new GsonEncoder(SimplifiedApi.getGson()))
            .decoder(new GsonDecoder(SimplifiedApi.getGson()))
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
            .target(Reflection.getSuperClass(this), this.getUrl());
    }

    protected @NotNull ConcurrentMap<String, String> configureRequestQueries() {
        return Concurrent.newUnmodifiableMap();
    }

    protected @NotNull ConcurrentMap<String, String> configureRequestHeaders() {
        return Concurrent.newUnmodifiableMap();
    }

    protected @NotNull ConcurrentSet<String> configureResponseHeaders() {
        return Concurrent.newUnmodifiableSet();
    }

    protected @NotNull ConcurrentMap<String, Supplier<Optional<String>>> configureDynamicHeaders() {
        return Concurrent.newUnmodifiableMap();
    }

    protected @NotNull ApiErrorDecoder configureErrorDecoder() {
        return new ApiErrorDecoder.Default();
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
        return this.getLastResponse()
            .map(Response::getLatency)
            .map(Latency::getTotal)
            .orElse(-1L);
    }

    /**
     * Retrieves the URL for the client instance, ensuring the returned string is consistently formatted.
     * <p>
     * Any protocol prefix (http:// or https&colon;//) from the base URL is stripped and replaced with "https://".
     *
     * @return The formatted URL as a non-null string.
     */
    public final @NotNull String getUrl() {
        return String.format("https://%s", this.baseUrl.replaceFirst("^https?://", ""));
    }

}
