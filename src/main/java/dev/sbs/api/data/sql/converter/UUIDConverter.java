package dev.sbs.api.data.sql.converter;

import dev.sbs.api.util.StringUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

@Converter
public class UUIDConverter implements AttributeConverter<UUID, String> {

    @Override
    public UUID convertToEntityAttribute(String attr) {
        if (StringUtil.isEmpty(attr))
            return null;

        return StringUtil.toUUID(attr);
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
