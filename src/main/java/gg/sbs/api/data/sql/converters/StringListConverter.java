package gg.sbs.api.data.sql.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @SuppressWarnings("unchecked")
    public List<String> convertToEntityAttribute(String attr) {
        if (attr == null) {
            return new ArrayList<>();
        }
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(attr, ArrayList.class);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String convertToDatabaseColumn(List<String> attr) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.toJson(attr);
        } catch (Exception e) {
            return "";
        }
    }

}