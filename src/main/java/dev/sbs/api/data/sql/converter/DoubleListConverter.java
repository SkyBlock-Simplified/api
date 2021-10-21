package dev.sbs.api.data.sql.converter;

import dev.sbs.api.SimplifiedApi;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class DoubleListConverter implements AttributeConverter<List<Double>, String> {

    @Override
    @SuppressWarnings("unchecked")
    public List<Double> convertToEntityAttribute(String attr) {
        if (attr == null) {
            return new ArrayList<>();
        }
        try {
            return SimplifiedApi.getGson().fromJson(attr, ArrayList.class);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(List<Double> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}