package dev.sbs.api.client;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.util.builder.ClassBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.ListUtil;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class ApiBuilder<R extends RequestInterface> implements ClassBuilder<R> {

    private final @NotNull String url;
    @Getter private @NotNull ConcurrentMap<String, ConcurrentList<String>> headerCache = Concurrent.newUnmodifiableMap();
    @Getter private long lastRequestTime;
    @Getter private long lastResponseTime;

    protected ApiBuilder(@NotNull String url) {
        this.url = url;
    }

    @Override
    public final <T extends R> T build(Class<T> tClass) {
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder(SimplifiedApi.getGson()))
            .decoder(new GsonDecoder(SimplifiedApi.getGson()))
            .requestInterceptor(template -> {
                ApiBuilder.this.getRequestHeaders().forEach(template::header);
                ApiBuilder.this.getRequestQueries().forEach((key, values) -> template.query(key, values));
                this.lastRequestTime = System.currentTimeMillis();
            })
            .responseInterceptor(context -> {
                if (ListUtil.notEmpty(this.getResponseCacheHeaders())) {
                    this.headerCache = Concurrent.newUnmodifiableMap(
                        context.response()
                            .headers()
                            .entrySet()
                            .stream()
                            .filter(entry -> this.getResponseCacheHeaders()
                                .stream()
                                .anyMatch(key -> entry.getKey().matches(key))
                            )
                            .map(entry -> Pair.of(entry.getKey(), Concurrent.newUnmodifiableList(entry.getValue())))
                            .collect(Concurrent.toMap())
                    );
                }

                this.lastResponseTime = System.currentTimeMillis();
                return context.decoder().decode(context.response(), context.returnType());
            })
            .errorDecoder(this.getErrorDecoder())
            .retryer(Retryer.NEVER_RETRY)
            .options(new Request.Options(
                5,
                TimeUnit.SECONDS,
                10,
                TimeUnit.SECONDS,
                true
            ))
            .target(tClass, this.getUrl());
    }

    public ErrorDecoder getErrorDecoder() {
        return new ErrorDecoder.Default();
    }

    public final long getLatency() {
        return this.getLastResponseTime() - this.getLastRequestTime();
    }

    public Map<String, String> getRequestHeaders() {
        return Concurrent.newUnmodifiableMap();
    }

    public ConcurrentMap<String, ConcurrentList<String>> getRequestQueries() {
        return Concurrent.newUnmodifiableMap();
    }

    public ConcurrentSet<String> getResponseCacheHeaders() {
        return Concurrent.newUnmodifiableSet();
    }

    public final String getUrl() {
        return FormatUtil.format("https://{0}", this.url);
    }

}
