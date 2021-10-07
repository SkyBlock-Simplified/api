package gg.sbs.api.data.sql.model;

import java.util.Map;

public abstract class SqlEffectsModel implements SqlModel {

    public abstract Map<String, Object> getEffects();

    @SuppressWarnings("unchecked cast") // Checked
    public <T> T getEffect(Class<T> tClass, String key) {
        Object value = getEffects().get(key);
        if (tClass.isInstance(value)) return (T) value;
        return null;
    }

    public double getEffectNum(String key) {
        Double value = getEffect(Double.class, key);
        if (value == null) return 0.;
        return value;
    }

    public String getEffectStr(String key) {
        String value = getEffect(String.class, key);
        if (value == null) return "";
        return value;
    }

}