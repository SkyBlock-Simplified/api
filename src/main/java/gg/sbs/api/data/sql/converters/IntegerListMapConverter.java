package gg.sbs.api.data.sql.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.AttributeConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegerListMapConverter implements AttributeConverter<Map<String, List<Integer>>, String> {
    @SuppressWarnings("unchecked")
    public Map<String, List<Integer>> convertToEntityAttribute(String attr) {
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

    public String convertToDatabaseColumn(Map<String, List<Integer>> attr) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }
}
