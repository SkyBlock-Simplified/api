package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface SlayerLevelModel extends Model {

    SlayerModel getSlayer();

    int getLevel();

    double getTotalExpRequired();

}
