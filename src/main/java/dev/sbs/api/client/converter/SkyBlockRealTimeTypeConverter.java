package dev.sbs.api.client.converter;

import com.google.gson.*;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockDate;

import java.lang.reflect.Type;

public class SkyBlockRealTimeTypeConverter extends TypeConverter<SkyBlockDate.RealTime> {

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
