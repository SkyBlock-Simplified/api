package gg.sbs.api.apiclients;

import feign.Feign;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.util.FormatUtil;

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

                .target(tClass, this.getUrl());
    }

    public final String getUrl() {
        return FormatUtil.format("https://{0}", this.url);
    }

}