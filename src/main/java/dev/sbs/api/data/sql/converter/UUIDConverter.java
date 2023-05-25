package dev.sbs.api.data.sql.converter;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.util.helper.StringUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter
public class UUIDConverter implements AttributeConverter<UUID, String> {

    @Override
    public UUID convertToEntityAttribute(String attr) {
        if (attr == null)
            return null;

        return StringUtil.toUUID(attr);
    }

    @Override
    public String convertToDatabaseColumn(UUID attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}
