package gg.sbs.api.apiclients;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import feign.Feign;
import feign.Request;
import feign.gson.DoubleToIntMapTypeAdapter;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import gg.sbs.api.apiclients.converter.InstantTypeConverter;
import gg.sbs.api.util.FormatUtil;

import java.time.Instant;
import java.util.Map;

public abstract class ApiBuilder<I extends RequestInterface> {

    private final transient Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new DoubleToIntMapTypeAdapter()) // Feign
            .registerTypeAdapter(Instant.class, new InstantTypeConverter())
            //.registerTypeAdapter(Skyblock.Date.class, new SkyBlockTimeTypeConverter()) // TODO: SkyBlockTime
            .setPrettyPrinting().create();
    private final String url;

    protected ApiBuilder(String url) {
        this.url = url;
    }

    public final <T extends I> T build(Class<T> tClass) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder(this.gson))
                .decoder(new GsonDecoder(this.gson))
                .target(tClass, this.getUrl());
    }

    public final String getUrl() {
        return FormatUtil.format("https://{0}", this.url);
    }

}