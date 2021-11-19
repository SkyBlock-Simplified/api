package dev.sbs.api.data.model.potion_mixins;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.slayers.SlayerModel;

import java.util.Map;

public interface PotionMixinModel extends EffectsModel {

    String getKey();

    String getName();

    SlayerModel getSlayerRequirement();

    Integer getSlayerLevelRequirement();

    Map<String, Object> getBuffEffects();

}
