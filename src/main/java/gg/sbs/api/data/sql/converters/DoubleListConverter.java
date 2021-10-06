package gg.sbs.api.data.sql.converters;

import gg.sbs.api.SimplifiedApi;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class DoubleListConverter implements AttributeConverter<List<Double>, String> {

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

    public String convertToDatabaseColumn(List<Double> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}