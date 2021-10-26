package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface MinionTierUpgradeModel extends Model {

    MinionTierModel getMinionTier();

    double getCoinCost();

    String getItemCost();

    int getItemQuantity();

}
