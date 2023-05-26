package dev.sbs.api.data.sql.converter.list;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.util.collection.concurrent.Concurrent;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {

    @Override
    public List<Integer> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new ArrayList<>();

        try {
            Integer[] intArray = SimplifiedApi.getGson().fromJson(attr, Integer[].class);
            return Arrays.stream(intArray).collect(Concurrent.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(List<Integer> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
