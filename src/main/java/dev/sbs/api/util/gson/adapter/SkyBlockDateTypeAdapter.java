package dev.sbs.api.util.gson.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

@NoArgsConstructor(access = AccessLevel.NONE)
public class SkyBlockDateTypeAdapter {

    public static class RealTime implements TypeAdapter<SkyBlockDate.RealTime> {

        @Override
        public SkyBlockDate.RealTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new SkyBlockDate.RealTime(json.getAsLong());
        }

        @Override
        public JsonElement serialize(SkyBlockDate.RealTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getRealTime());
        }

    }

    public static class SkyBlockTime implements TypeAdapter<SkyBlockDate.SkyBlockTime> {

        @Override
        public SkyBlockDate.SkyBlockTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new SkyBlockDate.SkyBlockTime(json.getAsLong());
        }

        @Override
        public JsonElement serialize(SkyBlockDate.SkyBlockTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getSkyBlockTime());
        }

    }

}
