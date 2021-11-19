package dev.sbs.api.data.model.skyblock.slayers;

import dev.sbs.api.data.model.Model;

public interface SlayerModel extends Model {

    String getKey();

    String getName();

    Double getWeightDivider();

    Double getWeightModifier();

}
