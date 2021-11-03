package dev.sbs.api.data.model.minion_tier_upgrades;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.collection_items.CollectionItemModel;
import dev.sbs.api.data.model.minion_tiers.MinionTierModel;

public interface MinionTierUpgradeModel extends Model {

    MinionTierModel getMinionTier();

    double getCoinCost();

    CollectionItemModel getItemCost();

    int getItemQuantity();

}
