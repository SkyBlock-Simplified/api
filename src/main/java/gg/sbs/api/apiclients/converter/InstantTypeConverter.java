package gg.sbs.api.apiclients.converter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

public final class InstantTypeConverter extends TypeConverter<Instant> {

    @Override
    public JsonElement serialize(Instant src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(src.toEpochMilli());
    }

    @Override
    public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Instant.ofEpochMilli(json.getAsLong());
    }

}