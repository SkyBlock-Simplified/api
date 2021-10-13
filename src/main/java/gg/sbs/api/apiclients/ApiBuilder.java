package gg.sbs.api.apiclients;

import feign.*;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.util.FormatUtil;
import gg.sbs.api.util.concurrent.Concurrent;

import java.util.Map;

public abstract class ApiBuilder<I extends RequestInterface> {

    private final String url;

    protected ApiBuilder(String url) {
        this.url = url;
    }

    public final <T extends I> T build(Class<T> tClass) {
        ErrorDecoder ed = new ErrorDecoder.Default();
        // TODO: Implement custom error handlers for
        //       ApiBuilder instances

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder(SimplifiedApi.getGson()))
                .decoder(new GsonDecoder(SimplifiedApi.getGson()))
                .requestInterceptor(template -> ApiBuilder.this.buildHeaders().forEach(template::header))
                .target(tClass, this.getUrl());
    }

    public final String getUrl() {
        return FormatUtil.format("https://{0}", this.url);
    }

    public Map<String, String> buildHeaders() {
        return Concurrent.newMap();
    }

}