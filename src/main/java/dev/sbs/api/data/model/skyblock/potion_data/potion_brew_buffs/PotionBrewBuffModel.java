package dev.sbs.api.data.model.skyblock.potion_data.potion_brew_buffs;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.potion_data.potion_brews.PotionBrewModel;

public interface PotionBrewBuffModel extends Model {

    PotionBrewModel getPotionBrew();

    String getBuffKey();

    Double getBuffValue();

    boolean isPercentage();

}