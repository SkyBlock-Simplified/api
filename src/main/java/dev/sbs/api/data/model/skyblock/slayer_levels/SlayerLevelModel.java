package dev.sbs.api.data.model.skyblock.slayer_levels;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;

public interface SlayerLevelModel extends EffectsModel {

    SlayerModel getSlayer();

    Integer getLevel();

    Double getTotalExpRequired();

}
