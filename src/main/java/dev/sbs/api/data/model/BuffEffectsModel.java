package dev.sbs.api.data.model;

import java.util.Map;

public interface BuffEffectsModel<V> extends EffectsModel<V> {

    Map<String, V> getBuffEffects();

    default <T> T getBuffEffects(String key) {
        return this.getBuffEffects(key, null);
    }

    @SuppressWarnings("unchecked cast")
    default <T> T getBuffEffects(String key, Object defaultValue) {
        Object value = this.getBuffEffects().get(key);

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
