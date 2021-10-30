package dev.sbs.api.data.model.minion_tier_upgrades;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.minion_tiers.MinionTierModel;

public interface MinionTierUpgradeModel extends Model {

    MinionTierModel getMinionTier();

    double getCoinCost();

    String getItemCost();

    int getItemQuantity();

}
