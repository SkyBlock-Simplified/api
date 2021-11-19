package dev.sbs.api.data.model.skyblock.slayer_levels;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;

public interface SlayerLevelModel extends Model {

    SlayerModel getSlayer();

    Integer getLevel();

    Double getTotalExpRequired();

}
