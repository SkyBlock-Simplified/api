package dev.sbs.api.client.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;

import java.lang.reflect.Type;

public class SkyBlockTimeTypeAdapter extends TypeAdapter<SkyBlockDate.SkyBlockTime> {

    @Override
    public SkyBlockDate.SkyBlockTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new SkyBlockDate.SkyBlockTime(json.getAsLong());
    }

    @Override
    public JsonElement serialize(SkyBlockDate.SkyBlockTime src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(src.getSkyBlockTime());
    }

}
