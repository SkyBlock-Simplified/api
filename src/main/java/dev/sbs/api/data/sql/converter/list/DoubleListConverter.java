package dev.sbs.api.data.sql.converter.list;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.collection.concurrent.Concurrent;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class DoubleListConverter implements AttributeConverter<List<Double>, String> {

    @Override
    public List<Double> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new ArrayList<>();

        try {
            Double[] doubleArray = SimplifiedApi.getGson().fromJson(attr, Double[].class);
            return Arrays.stream(doubleArray).collect(Concurrent.toList());
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
