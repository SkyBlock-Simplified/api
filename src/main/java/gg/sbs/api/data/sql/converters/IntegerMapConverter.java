package gg.sbs.api.data.sql.converters;

import gg.sbs.api.SimplifiedApi;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter
public class IntegerMapConverter implements AttributeConverter<Map<String, Integer>, String> {

    @SuppressWarnings("unchecked")
    public Map<String, Integer> convertToEntityAttribute(String attr) {
        if (attr == null) {
            return new HashMap<>();
        }
        try {
            return SimplifiedApi.getGson().fromJson(attr, HashMap.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public String convertToDatabaseColumn(Map<String, Integer> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}