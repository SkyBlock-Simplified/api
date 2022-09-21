package dev.sbs.api.data.model.skyblock.minion_data.minion_tiers;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.minion_data.minions.MinionModel;

public interface MinionTierModel extends Model {

    MinionModel getMinion();

    ItemModel getItem();

    Integer getSpeed();

}
