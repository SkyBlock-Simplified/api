package gg.sbs.api.data.sql.converter;

import gg.sbs.api.util.CharUtil;
import gg.sbs.api.util.FormatUtil;
import gg.sbs.api.util.RegexUtil;
import gg.sbs.api.util.StringUtil;

import javax.persistence.AttributeConverter;
import java.util.regex.Pattern;

public class UnicodeConverter implements AttributeConverter<Character, String> {

    public Character convertToEntityAttribute(String attr) {
        if (attr == null)
            return null;

        try {
            return CharUtil.toChar(StringUtil.unescapeJava(FormatUtil.format("\\u{0}", attr)));
        } catch (Exception e) {
            return null;
        }
    }

    public String convertToDatabaseColumn(Character attr) {
        try {
            return RegexUtil.replaceAll(StringUtil.escapeJava(attr.toString()), Pattern.compile("^\\+u"));
        } catch (Exception e) {
            return null;
        }
    }

}