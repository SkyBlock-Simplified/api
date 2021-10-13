package gg.sbs.api.apiclients.converter;

import com.google.gson.*;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockDate;

import java.lang.reflect.Type;

public class SkyBlockTimeTypeConverter extends TypeConverter<SkyBlockDate.SkyBlockTime> {

    @Override
    public SkyBlockDate.SkyBlockTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        long milliseconds = json.getAsLong();
        return milliseconds != 0L ? new SkyBlockDate.SkyBlockTime(milliseconds) : null;
    }

    @Override
    public JsonElement serialize(SkyBlockDate.SkyBlockTime src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(src.getSkyBlockTime());
    }

}