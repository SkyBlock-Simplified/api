package dev.sbs.api.data.model.skyblock.shop_profile_upgrades;

import dev.sbs.api.data.model.Model;

public interface ShopProfileUpgradeModel extends Model {

    String getKey();

    String getName();

    Integer getMaxLevel();

}
