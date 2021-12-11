package dev.sbs.api.data.model;

import java.util.Map;

public interface BuffEffectsModel<BV, EV> extends EffectsModel<EV> {

    Map<String, BV> getBuffEffects();

    default BV getBuffEffect(String key) {
        return this.getBuffEffect(key, null);
    }

    default BV getBuffEffect(String key, BV defaultValue) {
        return this.getBuffEffects().getOrDefault(key, defaultValue);
    }

}
