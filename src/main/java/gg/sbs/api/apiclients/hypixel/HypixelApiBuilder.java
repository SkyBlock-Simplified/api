package gg.sbs.api.apiclients.hypixel;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HypixelApiBuilder {
    private static String apiKey;
    public static final Pattern apiKeyRegex = Pattern.compile("[a-z0-9]{8}-(?:[a-z0-9]{4}-){3}[a-z0-9]{12}");

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String newApiKey) {
        if (newApiKey == null) {
            throw new IllegalArgumentException("New Hypixel API key must not be null");
        } else if (apiKeyRegex.matcher(newApiKey).matches()) {
            throw new IllegalArgumentException("New Hypixel API key must be valid");
        }
        apiKey = newApiKey;
    }

    public static <T> T buildApi(Class<T> tClass) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(tClass, "https://api.hypixel.net");
    }

    public static Map<String, String> buildHeaders() {
        return new HashMap<String, String>() {{
           put("API-Key", apiKey);
        }};
    }
}
