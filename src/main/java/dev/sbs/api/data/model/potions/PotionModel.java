package dev.sbs.api.data.model.potions;

import dev.sbs.api.data.model.Model;

public interface PotionModel extends Model {

    String getKey();

    String getName();

    String getDescription();

    boolean isBuff();

}
