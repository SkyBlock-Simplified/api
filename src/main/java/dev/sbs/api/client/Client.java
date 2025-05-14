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
import dev.sbs.api.util.builder.ClassBuilder;
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

@Getter
@Setter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Client<R extends IRequest> implements ClassBuilder<R> {

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

    public final @NotNull Optional<Request> getLastRequest() {
        return this.getRecentRequests()
            .stream()
            .reduce((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()) <= 0 ? o2 : o1);
    }

    public final @NotNull Optional<Response> getLastResponse() {
        return this.getRecentResponses()
            .stream()
            .reduce((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()) <= 0 ? o2 : o1);
    }

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

    public final @NotNull String getUrl() {
        return String.format("https://%s", this.baseUrl.replaceFirst("^https?://", ""));
    }

}
