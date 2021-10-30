package dev.sbs.api.data.model.potions;

import dev.sbs.api.data.model.EffectsModel;

import java.util.Map;

public interface PotionModel extends EffectsModel {

    String getName();

    int getItemLevel();

    Map<String, Object> getEffects();

}
