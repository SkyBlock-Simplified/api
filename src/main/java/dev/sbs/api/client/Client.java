package dev.sbs.api.client;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.decoder.ClientErrorDecoder;
import dev.sbs.api.client.decoder.ResponseWrapperDecoder;
import dev.sbs.api.client.exception.ApiException;
import dev.sbs.api.client.exception.InternalErrorDecoder;
import dev.sbs.api.client.metric.ConnectionDetails;
import dev.sbs.api.client.metric.TimedPlainConnectionSocketFactory;
import dev.sbs.api.client.metric.TimedSecureConnectionSocketFactory;
import dev.sbs.api.client.metric.Timings;
import dev.sbs.api.client.request.Endpoints;
import dev.sbs.api.client.response.Response;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.reflection.Reflection;
import feign.Feign;
import feign.FeignException;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.protocol.HttpContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.net.Inet6Address;
import java.util.Objects;
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

    private final @NotNull String baseUrl;
    private final @NotNull Optional<Inet6Address> inet6Address;
    @Getter(AccessLevel.NONE) private final @NotNull ApacheHttpClient internalClient;
    private final @NotNull E endpoints;
    private final @NotNull Timings timings;

    // Requests
    private final @NotNull ConcurrentMap<String, String> queries;
    private final @NotNull ConcurrentMap<String, String> headers;
    private final @NotNull ConcurrentMap<String, Supplier<Optional<String>>> dynamicHeaders;

    // Responses
    private final @NotNull ConcurrentList<Response<?>> recentResponses = Concurrent.newList();
    private final @NotNull ClientErrorDecoder errorDecoder;

    protected Client(@NotNull String url) {
        this(url, Optional.empty());
    }

    protected Client(@NotNull String url, @Nullable Inet6Address inet6Address) {
        this(url, Optional.ofNullable(inet6Address));
    }

    protected Client(@NotNull String url, @NotNull Optional<Inet6Address> inet6Address) {
        this.baseUrl = url;
        this.inet6Address = inet6Address;
        this.timings = this.configureTimings();
        this.errorDecoder = this.configureErrorDecoder();
        this.queries = this.configureQueries();
        this.headers = this.configureHeaders();
        this.dynamicHeaders = this.configureDynamicHeaders();
        this.internalClient = this.configureInternalClient();
        this.endpoints = this.configureUnwrappingProxy(this.configureEndpoints());
    }

    private static void addHeader(@NotNull HttpRequest request, @NotNull HttpContext context, @NotNull String id) {
        Object value = context.getAttribute(id);

        if (Objects.nonNull(value))
            request.addHeader(id, String.valueOf(value));
    }

    protected final @NotNull ApacheHttpClient configureInternalClient() {
        @SuppressWarnings("deprecation")
        HttpClientBuilder httpClient = HttpClientBuilder.create()
            .setConnectionManager(new PoolingHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new TimedPlainConnectionSocketFactory(
                        PlainConnectionSocketFactory.getSocketFactory(),
                        SystemDefaultDnsResolver.INSTANCE
                    ))
                    .register("https", new TimedSecureConnectionSocketFactory(
                        SSLConnectionSocketFactory.getSocketFactory(),
                        SystemDefaultDnsResolver.INSTANCE
                    ))
                    .build()
            ))
            .addInterceptorFirst((HttpRequestInterceptor) (request, context) -> {
                long requestStart = System.nanoTime();
                context.setAttribute(ConnectionDetails.REQUEST_START, requestStart);

                // Store all available connection details in headers
                addHeader(request, context, ConnectionDetails.REQUEST_START);
                addHeader(request, context, ConnectionDetails.DNS_TIME);
                addHeader(request, context, ConnectionDetails.TCP_CONNECT_TIME);
                addHeader(request, context, ConnectionDetails.TLS_HANDSHAKE_TIME);
                addHeader(request, context, ConnectionDetails.TLS_PROTOCOL);
                addHeader(request, context, ConnectionDetails.TLS_CIPHER);

                // Append Custom Queries and Headers
                this.getQueries().forEach((key, value) -> request.getParams().setParameter(key, value));
                this.getHeaders().forEach((key, value) -> request.addHeader(key, value));
                this.getDynamicHeaders().forEach((key, supplier) -> supplier.get()
                    .ifPresent(value -> request.addHeader(key, value))
                );
            })
            .addInterceptorLast((HttpResponseInterceptor) (response, context) -> {
                long responseReceived = System.nanoTime();
                context.setAttribute(ConnectionDetails.RESPONSE_RECEIVED, responseReceived);
                response.addHeader(ConnectionDetails.RESPONSE_RECEIVED, String.valueOf(responseReceived));
                this.recentResponses.removeIf(r -> r.getTimestamp().toEpochMilli() < System.currentTimeMillis() - this.getTimings().getCacheDuration());
            })
            .setMaxConnTotal(this.getTimings().getMaxConnections())
            .setMaxConnPerRoute(this.getTimings().getMaxConnectionsPerRoute())
            .evictIdleConnections(this.getTimings().getConnectionEvictIdleTimeout(), TimeUnit.SECONDS)
            .setConnectionTimeToLive(this.getTimings().getConnectionTimeToLive(), TimeUnit.SECONDS)
            .setKeepAliveStrategy((response, context) -> {
                long keepAlive = DefaultConnectionKeepAliveStrategy.INSTANCE.getKeepAliveDuration(response, context);
                return (keepAlive == -1) ? this.getTimings().getConnectionKeepAliveTimeout() * 1_000 : Math.min(keepAlive, 60_000);
            });

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
            .decoder(new ResponseWrapperDecoder(
                new GsonDecoder(SimplifiedApi.getGson()),
                this.getRecentResponses()
            ))
            .errorDecoder(new InternalErrorDecoder(
                this.getErrorDecoder(),
                this.getRecentResponses()
            ))
            .options(new feign.Request.Options(
                this.getTimings().getConnectionTimeout(),
                TimeUnit.SECONDS,
                this.getTimings().getSocketTimeout(),
                TimeUnit.SECONDS,
                true
            ))
            .target(Reflection.getSuperClass(this), this.getUrl());
    }

    @SuppressWarnings("unchecked")
    private <T extends E> @NotNull T configureUnwrappingProxy(@NotNull T target) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                try {
                    return method.invoke(target, args);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();

                    // Unwrap our internal wrapper to expose the original typed exception
                    if (cause instanceof InternalErrorDecoder.InternalRetryableWrapper)
                        throw ((InternalErrorDecoder.InternalRetryableWrapper) cause).getWrappedException();

                    throw cause;
                }
            }
        );
    }

    protected @NotNull ConcurrentMap<String, String> configureQueries() {
        return Concurrent.newUnmodifiableMap();
    }

    protected @NotNull ConcurrentMap<String, String> configureHeaders() {
        return Concurrent.newUnmodifiableMap();
    }

    protected @NotNull ConcurrentMap<String, Supplier<Optional<String>>> configureDynamicHeaders() {
        return Concurrent.newUnmodifiableMap();
    }

    protected @NotNull ClientErrorDecoder configureErrorDecoder() {
        return (methodKey, response) -> new ApiException(
            FeignException.errorStatus(
                methodKey,
                response,
                null,
                null
            ),
            response,
            "Client"
        );
    }

    protected @NotNull Timings configureTimings() {
        return Timings.createDefault();
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
    public final @NotNull Optional<Response<?>> getLastResponse() {
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
            .map(Response::getDetails)
            .map(ConnectionDetails::getTotalTime)
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
