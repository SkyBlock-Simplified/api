package dev.sbs.api.data.model.skyblock.essence_perks;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.stats.StatModel;

public interface EssencePerkModel extends Model {

    String getKey();

    StatModel getStat();

    Integer getLevelBonus();

    boolean isPermanent();

}
