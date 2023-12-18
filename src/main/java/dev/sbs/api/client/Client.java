package dev.sbs.api.client;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.util.builder.ClassBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedList;
import dev.sbs.api.util.data.tuple.pair.Pair;
import dev.sbs.api.util.helper.ListUtil;
import feign.Feign;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;
import lombok.Getter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Inet6Address;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Getter
public abstract class Client<R extends IRequest> implements ClassBuilder<R> {

    private final static long ONE_HOUR = Duration.ofHours(1).toMillis();
    private final @NotNull String url;
    private final @NotNull Optional<Inet6Address> inet6Address;
    private final @NotNull ConcurrentLinkedList<Long> recentRequests = Concurrent.newLinkedList();
    private final @NotNull ConcurrentLinkedList<Long> recentResponses = Concurrent.newLinkedList();
    private @NotNull ConcurrentMap<String, ConcurrentList<String>> headerCache = Concurrent.newUnmodifiableMap();

    public Client(@NotNull String url) {
        this(url, null);
    }

    public Client(@NotNull String url, @Nullable Inet6Address inet6Address) {
        this.url = String.format("https://%s", url.replaceFirst("^https?://", ""));
        this.inet6Address = Optional.ofNullable(inet6Address);
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
            .requestInterceptor(template -> {
                Client.this.getRequestHeaders().forEach((key, value) -> template.header(key, value));
                Client.this.getRequestQueries().forEach((key, values) -> template.query(key, values));
                this.recentRequests.add(System.currentTimeMillis());
                this.recentRequests.removeIf(millis -> millis < System.currentTimeMillis() - ONE_HOUR);
            })
            .responseInterceptor(context -> {
                if (ListUtil.notEmpty(this.getResponseCacheHeaders())) {
                    this.headerCache = context.response()
                        .headers()
                        .entrySet()
                        .stream()
                        .filter(entry -> this.getResponseCacheHeaders()
                            .stream()
                            .anyMatch(key -> entry.getKey().matches(key))
                        )
                        .map(entry -> Pair.of(entry.getKey(), Concurrent.newList(entry.getValue())))
                        .collect(Concurrent.toUnmodifiableMap());
                }

                this.recentResponses.add(System.currentTimeMillis());
                this.recentResponses.removeIf(millis -> millis < System.currentTimeMillis() - ONE_HOUR);
                return context.decoder().decode(context.response(), context.returnType());
            })
            .errorDecoder(this.getErrorDecoder())
            .options(new feign.Request.Options(
                5,
                TimeUnit.SECONDS,
                10,
                TimeUnit.SECONDS,
                true
            ))
            .target(tClass, this.getUrl());
    }

    protected @NotNull ErrorDecoder getErrorDecoder() {
        return new ErrorDecoder.Default();
    }

    public final long getLastRequest() {
        return this.getRecentRequests().getLast().orElse(0L);
    }

    public final long getLastResponse() {
        return this.getRecentResponses().getLast().orElse(0L);
    }

    public final long getLatency() {
        return this.getLastResponse() - this.getLastRequest();
    }

    protected @NotNull ConcurrentMap<String, String> getRequestHeaders() {
        return Concurrent.newUnmodifiableMap();
    }

    protected @NotNull ConcurrentMap<String, ConcurrentList<String>> getRequestQueries() {
        return Concurrent.newUnmodifiableMap();
    }

    protected @NotNull ConcurrentSet<String> getResponseCacheHeaders() {
        return Concurrent.newUnmodifiableSet();
    }

}
