package dev.sbs.api.data.sql.converter.list;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.util.collection.concurrent.Concurrent;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Converter
public class UUIDListConverter implements AttributeConverter<List<UUID>, String> {

    @Override
    public List<UUID> convertToEntityAttribute(String attr) {
        if (attr == null)
            return new ArrayList<>();

        try {
            UUID[] uuidArray = SimplifiedApi.getGson().fromJson(attr, UUID[].class);
            return Arrays.stream(uuidArray).collect(Concurrent.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String convertToDatabaseColumn(List<UUID> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
