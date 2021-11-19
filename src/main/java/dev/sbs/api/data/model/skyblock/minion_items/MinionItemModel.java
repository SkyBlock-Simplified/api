package dev.sbs.api.data.model.skyblock.minion_items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.collection_items.CollectionItemModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.minions.MinionModel;

public interface MinionItemModel extends Model {

    MinionModel getMinion();

    CollectionItemModel getCollectionItem();

    ItemModel getItem();

    double getAverageYield();

}
