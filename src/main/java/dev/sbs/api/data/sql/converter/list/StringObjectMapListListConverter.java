package dev.sbs.api.data.sql.converter.list;

import dev.sbs.api.SimplifiedApi;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Converter
public class StringObjectMapListListConverter implements AttributeConverter<List<List<Map<String, Object>>>, String> {

    @Override
    @SuppressWarnings("unchecked")
    public List<List<Map<String, Object>>> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new ArrayList<>();

        try {
            return SimplifiedApi.getGson().fromJson(attr, List.class);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(List<List<Map<String, Object>>> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
