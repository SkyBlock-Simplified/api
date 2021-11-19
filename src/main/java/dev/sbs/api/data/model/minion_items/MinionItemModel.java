package dev.sbs.api.data.model.minion_items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.collection_items.CollectionItemModel;
import dev.sbs.api.data.model.items.ItemModel;
import dev.sbs.api.data.model.minions.MinionModel;

public interface MinionItemModel extends Model {

    MinionModel getMinion();

    CollectionItemModel getCollectionItem();

    ItemModel getItem();

    Double getAverageYield();

}
