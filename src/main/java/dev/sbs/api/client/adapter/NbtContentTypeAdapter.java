package dev.sbs.api.client.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockIsland;
import dev.sbs.api.reflection.Reflection;

import java.lang.reflect.Type;

public class NbtContentTypeAdapter extends TypeAdapter<SkyBlockIsland.NbtContent> {

    @Override
    public SkyBlockIsland.NbtContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Reflection<SkyBlockIsland.NbtContent> nbtContentReflection = Reflection.of(SkyBlockIsland.NbtContent.class);
        SkyBlockIsland.NbtContent nbtContent = nbtContentReflection.newInstance();
        String data = json.isJsonPrimitive() ? json.getAsString() : json.getAsJsonObject().get("data").getAsString(); // Auctions are bad
        nbtContentReflection.setValue(String.class, nbtContent, data);
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
