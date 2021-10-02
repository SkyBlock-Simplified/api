package gg.sbs.api.apiclients.converter;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public abstract class TypeConverter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

}