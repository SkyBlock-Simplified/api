package gg.sbs.api.apiclients;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import feign.Feign;
import feign.Request;
import feign.gson.DoubleToIntMapTypeAdapter;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import gg.sbs.api.util.FormatUtil;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Map;

public abstract class ApiBuilder<I extends RequestInterface> {

    private final transient Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new DoubleToIntMapTypeAdapter()) // Feign
            .registerTypeAdapter(Instant.class, new InstantTypeConverter())
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
                .options(new Request.Options())
                .target(tClass, this.getUrl());
    }

    public final String getUrl() {
        return FormatUtil.format("https://{0}", this.url);
    }

    private static class InstantTypeConverter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

        @Override
        public JsonElement serialize(Instant src, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(src.getEpochSecond());
        }

        @Override
        public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            return Instant.ofEpochSecond(json.getAsLong());
        }

    }

    // TODO
    private static class SkyBlockTimeTypeConverter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

        @Override
        public JsonElement serialize(Instant src, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(src.getEpochSecond());
        }

        @Override
        public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            return Instant.ofEpochSecond(json.getAsLong());
        }

    }

}