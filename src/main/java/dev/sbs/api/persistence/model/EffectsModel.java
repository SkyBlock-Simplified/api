package dev.sbs.api.persistence.model;

import dev.sbs.api.persistence.Model;

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
