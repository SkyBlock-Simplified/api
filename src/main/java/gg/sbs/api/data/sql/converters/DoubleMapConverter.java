package gg.sbs.api.data.sql.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter
public class DoubleMapConverter implements AttributeConverter<Map<String, Double>, String> {

    @SuppressWarnings("unchecked")
    public Map<String, Double> convertToEntityAttribute(String attr) {
        if (attr == null) {
            return new HashMap<>();
        }
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(attr, HashMap.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public String convertToDatabaseColumn(Map<String, Double> attr) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}