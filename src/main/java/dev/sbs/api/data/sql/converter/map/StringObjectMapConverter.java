package dev.sbs.api.data.sql.converter.map;

import dev.sbs.api.SimplifiedApi;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter
public class StringObjectMapConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new HashMap<>();

        try {
            return SimplifiedApi.getGson().fromJson(attr, HashMap.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
