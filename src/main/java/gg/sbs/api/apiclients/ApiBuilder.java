package gg.sbs.api.apiclients;

import feign.Feign;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.util.FormatUtil;
import gg.sbs.api.util.builder.ClassBuilder;
import gg.sbs.api.util.concurrent.Concurrent;

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
                .requestInterceptor(template -> ApiBuilder.this.buildHeaders().forEach(template::header))
                .errorDecoder(this.getErrorDecoder())
                .target(tClass, this.getUrl());
    }

    public final String getUrl() {
        return FormatUtil.format("https://{0}", this.url);
    }

    public Map<String, String> buildHeaders() {
        return Concurrent.newMap();
    }

    public ErrorDecoder getErrorDecoder() {
        return new ErrorDecoder.Default();
    }

}