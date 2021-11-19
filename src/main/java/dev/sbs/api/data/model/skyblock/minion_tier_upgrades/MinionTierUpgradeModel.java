package dev.sbs.api.data.model.skyblock.minion_tier_upgrades;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.minion_tiers.MinionTierModel;

public interface MinionTierUpgradeModel extends Model {

    MinionTierModel getMinionTier();

    Double getCoinCost();

    ItemModel getItemCost();

    Integer getItemQuantity();

}
