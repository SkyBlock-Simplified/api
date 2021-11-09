package dev.sbs.api.client.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

public final class InstantTypeAdapter extends TypeAdapter<Instant> {

    @Override
    public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Instant.ofEpochMilli(json.getAsLong());
    }

    @Override
    public JsonElement serialize(Instant src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(src.toEpochMilli());
    }

}
