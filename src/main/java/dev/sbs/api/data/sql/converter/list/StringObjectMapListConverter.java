package dev.sbs.api.data.sql.converter.list;

import dev.sbs.api.SimplifiedApi;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Converter
public class StringObjectMapListConverter implements AttributeConverter<List<Map<String, Object>>, String> {

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new ArrayList<>();

        try {
            return SimplifiedApi.getGson().fromJson(attr, List.class);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(List<Map<String, Object>> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
