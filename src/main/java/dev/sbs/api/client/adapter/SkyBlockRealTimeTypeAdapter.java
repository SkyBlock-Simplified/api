package dev.sbs.api.client.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;

import java.lang.reflect.Type;

public class SkyBlockRealTimeTypeAdapter extends TypeAdapter<SkyBlockDate.RealTime> {

    @Override
    public SkyBlockDate.RealTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        long milliseconds = json.getAsLong();
        return milliseconds != 0L ? new SkyBlockDate.RealTime(milliseconds) : null;
    }

    @Override
    public JsonElement serialize(SkyBlockDate.RealTime src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(src.getRealTime());
    }

}
