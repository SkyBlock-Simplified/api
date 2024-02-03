package dev.sbs.api.data.sql.converter.list;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.collection.concurrent.Concurrent;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public List<String> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new ArrayList<>();

        try {
            String[] stringArray = SimplifiedApi.getGson().fromJson(attr, String[].class);
            return Arrays.stream(stringArray).collect(Concurrent.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(List<String> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
