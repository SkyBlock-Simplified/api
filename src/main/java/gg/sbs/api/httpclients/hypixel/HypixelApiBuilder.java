package gg.sbs.api.httpclients.hypixel;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import gg.sbs.api.util.ResourceUtil;

import java.util.HashMap;
import java.util.Map;

public class HypixelApiBuilder {
    public static <T> T buildApi(Class<T> tClass) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(tClass, ResourceUtil.getEnvironmentVariables().get("HYPIXEL_API_URL"));
    }

    public static Map<String, String> buildHeaders() {
        return new HashMap<String, String>() {{
           put("API-Key", ResourceUtil.getEnvironmentVariables().get("HYPIXEL_API_KEY"));
        }};
    }
}
