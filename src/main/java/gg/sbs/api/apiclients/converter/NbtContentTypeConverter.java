package gg.sbs.api.apiclients.converter;

import com.google.gson.*;
import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockIsland;
import gg.sbs.api.reflection.Reflection;

import java.lang.reflect.Type;

public class NbtContentTypeConverter extends TypeConverter<SkyBlockIsland.NbtContent> {

    @Override
    public SkyBlockIsland.NbtContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) { // Auctions are bad
            Reflection reflection = new Reflection(SkyBlockIsland.NbtContent.class);
            SkyBlockIsland.NbtContent nbtContent = (SkyBlockIsland.NbtContent) new Reflection(SkyBlockIsland.NbtContent.class).newInstance();
            reflection.setValue(String.class, nbtContent, json.getAsString());
            return nbtContent;
        } else
            return SimplifiedApi.getGson().fromJson(json, SkyBlockIsland.NbtContent.class);
    }

    @Override
    public JsonElement serialize(SkyBlockIsland.NbtContent src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(SimplifiedApi.getGson().toJson(src));
    }

}