package dev.sbs.api.data.model.minion_tiers;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.items.ItemModel;
import dev.sbs.api.data.model.minions.MinionModel;

public interface MinionTierModel extends Model {

    MinionModel getMinion();

    ItemModel getItem();

    int getSpeed();

}
