package dev.sbs.api.data.model;

import java.util.Map;

public interface EffectsModel<V> extends Model {

    Map<String, V> getEffects();

    default V getEffect(String key) {
        return this.getEffect(key, null);
    }

    default V getEffect(String key, V defaultValue) {
        return this.getEffects().getOrDefault(key, defaultValue);
    }

}
