package dev.sbs.api.data.sql.converter;

import dev.sbs.api.util.helper.CharUtil;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.StringUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UnicodeConverter implements AttributeConverter<Character, String> {

    @Override
    public Character convertToEntityAttribute(String attr) {
        if (attr == null)
            return null;

        try {
            return CharUtil.toChar(StringUtil.unescapeJava(FormatUtil.format("\\u{0}", attr)));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertToDatabaseColumn(Character attr) {
        try {
            return StringUtil.escapeJava(attr.toString())
                .toLowerCase()
                .replaceAll("\\\\u", "");
        } catch (Exception e) {
            return "";
        }
    }

}
