package dev.sbs.api.data.sql.converter.map;

import dev.sbs.api.SimplifiedApi;

import javax.persistence.AttributeConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringDoubleListMapConverter implements AttributeConverter<Map<String, List<Double>>, String> {

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, List<Double>> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new HashMap<>();

        try {
            return SimplifiedApi.getGson().fromJson(attr, HashMap.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(Map<String, List<Double>> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
