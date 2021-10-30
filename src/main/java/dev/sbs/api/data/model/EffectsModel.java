package dev.sbs.api.data.model;

import java.util.Map;

public interface EffectsModel extends Model {

    Map<String, Object> getEffects();

    default <T> T getEffect(String key) {
        return this.getEffect(key, null);
    }

    @SuppressWarnings("unchecked cast")
    default <T> T getEffect(String key, Object defaultValue) {
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
