package gg.sbs.api.apiclients.converter;

import com.google.gson.*;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockIsland;
import gg.sbs.api.reflection.Reflection;

import java.lang.reflect.Type;

public class NbtContentTypeConverter extends TypeConverter<SkyBlockIsland.NbtContent> {

    @Override
    public SkyBlockIsland.NbtContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Reflection reflection = new Reflection(SkyBlockIsland.NbtContent.class);
        SkyBlockIsland.NbtContent nbtContent = (SkyBlockIsland.NbtContent) new Reflection(SkyBlockIsland.NbtContent.class).newInstance();
        String data = json.isJsonPrimitive() ? json.getAsString() : json.getAsJsonObject().get("data").getAsString(); // Auctions are bad
        reflection.setValue(String.class, nbtContent, data);
        return nbtContent;
    }

    @Override
    public JsonElement serialize(SkyBlockIsland.NbtContent src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", 0);
        jsonObject.addProperty("data", src.getRawData());
        return jsonObject;
    }

}