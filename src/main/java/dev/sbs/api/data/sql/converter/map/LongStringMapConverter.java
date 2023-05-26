package dev.sbs.api.data.sql.converter.map;

import dev.sbs.api.SimplifiedApi;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter
public class LongStringMapConverter implements AttributeConverter<Map<Long, String>, String> {

    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, String> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new HashMap<>();

        try {
            return SimplifiedApi.getGson().fromJson(attr, HashMap.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(Map<Long, String> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
