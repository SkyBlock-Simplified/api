package dev.sbs.api.data.model.skyblock.hotm_perks;

import dev.sbs.api.data.model.Model;

public interface HotmPerkModel extends Model {

    String getKey();

    String getName();

    Integer getLevelBonus();

}
