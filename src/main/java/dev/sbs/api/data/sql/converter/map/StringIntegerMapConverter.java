package dev.sbs.api.data.sql.converter.map;

import dev.sbs.api.SimplifiedApi;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

@Converter
public class StringIntegerMapConverter implements AttributeConverter<Map<String, Integer>, String> {

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Integer> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new HashMap<>();

        try {
            return SimplifiedApi.getGson().fromJson(attr, HashMap.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Integer> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
