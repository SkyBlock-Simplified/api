package gg.sbs.api.data.sql.model;

import java.util.Map;

public abstract class SqlEffectsModel implements SqlModel {

    public abstract Map<String, Object> getEffects();

    public <T> T getEffect(String key) {
        return this.getEffect(key, null);
    }

    @SuppressWarnings("unchecked cast")
    public <T> T getEffect(String key, Object defaultValue) {
        Object value = this.getEffects().get(key);

        if (value != null) {
            try {
                return (T) value;
            } catch (Exception ignore) { }
        }

        try {
            return (T) defaultValue;
        } catch (Exception defaultException) {
            throw new IllegalArgumentException("Default value does not match type T");
        }
    }

}