package dev.sbs.api.data.model.skyblock.collection_data.collection_items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.collection_data.collections.CollectionModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

public interface CollectionItemModel extends Model {

    CollectionModel getCollection();

    ItemModel getItem();

    Integer getMaxTiers();

    boolean isFarmingEvent();

}
