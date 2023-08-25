package dev.sbs.api.util.gson.adapter;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * Allows traversing into {@link com.google.gson.JsonObject JsonObjects}.
 */
interface TypeAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {

}
