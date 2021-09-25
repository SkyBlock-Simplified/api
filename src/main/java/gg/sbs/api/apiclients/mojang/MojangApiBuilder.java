package gg.sbs.api.apiclients.mojang;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;

public class MojangApiBuilder {
    public static <T> T buildApi(Class<T> tClass) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(tClass, "https://api.skyblocksimplified.com");
    }
}
