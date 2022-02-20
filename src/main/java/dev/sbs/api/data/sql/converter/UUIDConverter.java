package dev.sbs.api.data.sql.converter;

import dev.sbs.api.util.helper.StringUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

@Converter
public class UUIDConverter implements AttributeConverter<UUID, String> {

    @Override
    public UUID convertToEntityAttribute(String attr) {
        if (attr == null)
            return null;

        try {
            return StringUtil.toUUID(attr);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertToDatabaseColumn(UUID attr) {
        try {
            return attr.toString();
        } catch (Exception e) {
            return "";
        }
    }

}
