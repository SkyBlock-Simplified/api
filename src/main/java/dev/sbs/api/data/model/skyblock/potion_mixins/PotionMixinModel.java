package dev.sbs.api.data.model.skyblock.potion_mixins;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;

import java.util.Map;

public interface PotionMixinModel extends EffectsModel {

    String getKey();

    String getName();

    SlayerModel getSlayerRequirement();

    Integer getSlayerLevelRequirement();

    Map<String, Object> getBuffEffects();

}
