package dev.sbs.api.data.model.skyblock.potion_data.potion_mixins;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;

public interface PotionMixinModel extends BuffEffectsModel<Double, Double> {

    String getKey();

    String getName();

    SlayerModel getSlayerRequirement();

    Integer getSlayerLevelRequirement();

}
