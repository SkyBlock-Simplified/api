package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_type_levels;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_types.BestiaryTypeModel;

public interface BestiaryTypeLevelModel extends EffectsModel<Double> {

    BestiaryTypeModel getType();

    Integer getLevel();

    Integer getTotalKillsRequired();

}
