package dev.sbs.api.data.yaml.converter;

import dev.sbs.api.data.yaml.InternalConverter;
import dev.sbs.api.reflection.Reflection;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ListConverter extends YamlConverter {

    public ListConverter(InternalConverter converter) {
        super(converter);
    }

    @Override
    public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
        java.util.List<Object> values = (java.util.List<Object>) section;
        java.util.List<Object> newList = new ArrayList<>();

        try {
            newList = (java.util.List<Object>) Reflection.of(type).newInstance();
        } catch (Exception ignore) {
        }

        if (genericType != null && genericType.getActualTypeArguments()[0] instanceof Class) {
            YamlConverter converter = this.getConverter((Class<?>) genericType.getActualTypeArguments()[0]);

            if (converter != null) {
                for (Object value : values)
                    newList.add(converter.fromConfig((Class<?>) genericType.getActualTypeArguments()[0], value, null));
            } else if (!values.isEmpty())
                newList.addAll(values);
        } else if (!values.isEmpty())
            newList.addAll(values);

        return newList;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
        java.util.List<Object> values = (java.util.List<Object>) obj;
        java.util.List<Object> newList = new ArrayList<>();

        for (Object value : values) {
            YamlConverter converter = this.getConverter(value.getClass());
            newList.add(converter != null ? converter.toConfig(value.getClass(), value, null) : value);
        }

        return newList;
    }

    @Override
    public boolean supports(Class<?> type) {
        return java.util.List.class.isAssignableFrom(type);
    }

}
