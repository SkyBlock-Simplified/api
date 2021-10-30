package dev.sbs.api.data.model.slayer_levels;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.slayers.SlayerModel;

public interface SlayerLevelModel extends Model {

    SlayerModel getSlayer();

    int getLevel();

    double getTotalExpRequired();

}
