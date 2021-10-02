package gg.sbs.api.apiclients.converter;

import com.google.gson.*;
import gg.sbs.api.hypixel_old.skyblock.Skyblock;

import java.lang.reflect.Type;

public class SkyBlockTimeTypeConverter extends TypeConverter<Skyblock.Date> {

    @Override
    public JsonElement serialize(Skyblock.Date src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTime());
    }

    @Override
    public Skyblock.Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new Skyblock.Date(Skyblock.Date.Season.EARLY_FALL, 0, 0, 0);
    }

}