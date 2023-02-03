package dev.sbs.api.client;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.util.builder.ClassBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.FormatUtil;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class ApiBuilder<R extends RequestInterface> implements ClassBuilder<R> {

    private final String url;

    protected ApiBuilder(String url) {
        this.url = url;
    }

    @Override
    public final <T extends R> T build(Class<T> tClass) {
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder(SimplifiedApi.getGson()))
            .decoder(new GsonDecoder(SimplifiedApi.getGson()))
            .requestInterceptor(template -> {
                ApiBuilder.this.getHeaders().forEach(template::header);
                ApiBuilder.this.getQueries().forEach((key, values) -> template.query(key, values));
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

    public final String getUrl() {
        return FormatUtil.format("https://{0}", this.url);
    }

    public Map<String, String> getHeaders() {
        return Concurrent.newMap();
    }

    public ErrorDecoder getErrorDecoder() {
        return new ErrorDecoder.Default();
    }

    public ConcurrentMap<String, ConcurrentList<String>> getQueries() {
        return Concurrent.newMap();
    }

}
