package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface CollectionItemModel extends Model {

    CollectionModel getCollection();

    ItemModel getItem();

    int getMaxTiers();

}
