package dev.sbs.api.client.sbs.response;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.pair.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockImagesResponse {

    @Getter private final ConcurrentMap<String, Image> items;

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Image {

        @Getter private final String normal;
        @Getter private final String enchanted;

    }

    public static class Deserializer implements JsonDeserializer<SkyBlockImagesResponse> {

        @Override
        public SkyBlockImagesResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return new SkyBlockImagesResponse(Concurrent.newUnmodifiableMap(
                jsonElement.getAsJsonObject()
                    .entrySet()
                    .stream()
                    .map(entry -> Pair.of(
                        entry.getKey(),
                        new Image(
                            entry.getValue().getAsJsonObject().get("normal").getAsString(),
                            entry.getValue().getAsJsonObject().get("enchanted").getAsString()
                        )
                    ))
                    .collect(Concurrent.toMap())
            ));
        }

    }

}
