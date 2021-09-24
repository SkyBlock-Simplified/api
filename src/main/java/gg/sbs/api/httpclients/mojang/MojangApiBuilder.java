package gg.sbs.api.httpclients.mojang;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import gg.sbs.api.util.ResourceUtil;

public class MojangApiBuilder {
    public static <T> T buildApi(Class<T> tClass) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(tClass, ResourceUtil.getEnvironmentVariables().get("MOJANG_API_URL"));
    }
}
