package dev.sbs.api.data.sql.converter;

import dev.sbs.api.util.CharUtil;
import dev.sbs.api.util.StringUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UnicodeConverter implements AttributeConverter<Character, String> {

    @Override
    public Character convertToEntityAttribute(String attr) {
        if (attr == null)
            return null;

        try {
            return CharUtil.toChar(StringUtil.unescapeUnicode(String.format("\\u%s", attr)));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertToDatabaseColumn(Character attr) {
        try {
            return StringUtil.escapeUnicode(attr.toString())
                .toLowerCase()
                .replaceAll("\\\\u", "");
        } catch (Exception e) {
            return "";
        }
    }

}
