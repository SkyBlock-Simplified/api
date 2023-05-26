package dev.sbs.api.data.sql.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.awt.*;

@Converter
public class ColorConverter implements AttributeConverter<Color, String> {

    @Override
    public Color convertToEntityAttribute(String attr) {
        if (attr == null)
            return null;

        try {
            return Color.getColor(attr);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertToDatabaseColumn(Color attr) {
        try {
            return String.valueOf(attr.getRGB());
        } catch (Exception e) {
            return "";
        }
    }

}
