package dev.sbs.api.data.sql.converter.map;

import dev.sbs.api.SimplifiedApi;

import javax.persistence.AttributeConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringIntegerListMapConverter implements AttributeConverter<Map<String, List<Integer>>, String> {

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, List<Integer>> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new HashMap<>();

        try {
            return SimplifiedApi.getGson().fromJson(attr, HashMap.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(Map<String, List<Integer>> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
