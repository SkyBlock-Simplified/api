package dev.sbs.api.data.model.collection_items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.collections.CollectionModel;
import dev.sbs.api.data.model.items.ItemModel;

public interface CollectionItemModel extends Model {

    CollectionModel getCollection();

    ItemModel getItem();

    Integer getMaxTiers();

}
