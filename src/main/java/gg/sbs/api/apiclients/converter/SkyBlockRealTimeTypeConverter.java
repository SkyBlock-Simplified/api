package gg.sbs.api.apiclients.converter;

import com.google.gson.*;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockDate;

import java.lang.reflect.Type;

public class SkyBlockRealTimeTypeConverter extends TypeConverter<SkyBlockDate.RealTime> {

    @Override
    public JsonElement serialize(SkyBlockDate.RealTime src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(src.getRealTime());
    }

    @Override
    public SkyBlockDate.RealTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new SkyBlockDate.RealTime(json.getAsLong());
    }

}