package dev.sbs.api.util.gson.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.awt.*;
import java.lang.reflect.Type;

public class ColorTypeAdapter implements TypeAdapter<Color> {

    @Override
    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Color(json.getAsInt(), true);
    }

    @Override
    public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
        int rgba = (src.getRed() << 24) |
            (src.getGreen() << 16) |
            (src.getBlue() << 8) |
            src.getAlpha();

        return new JsonPrimitive(rgba);
    }

}
