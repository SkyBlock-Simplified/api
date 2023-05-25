package dev.sbs.api.data.sql.converter.list;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public List<Long> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new ArrayList<>();

        try {
            Long[] longArray = SimplifiedApi.getGson().fromJson(attr, Long[].class);
            return Arrays.stream(longArray).collect(Concurrent.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(List<Long> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
