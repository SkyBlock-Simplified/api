package dev.sbs.api.client;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.util.builder.ClassBuilder;
import dev.sbs.api.util.concurrent.Concurrent;
import feign.Feign;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import dev.sbs.api.util.helper.FormatUtil;

import java.util.Map;

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
                .requestInterceptor(template -> ApiBuilder.this.getHeaders().forEach(template::header))
                .errorDecoder(this.getErrorDecoder())
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

}