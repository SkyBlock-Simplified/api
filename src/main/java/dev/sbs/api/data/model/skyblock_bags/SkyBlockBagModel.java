package dev.sbs.api.data.model.skyblock_bags;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.collection_items.CollectionItemModel;

public interface SkyBlockBagModel extends Model {

    String getKey();

    String getName();

    CollectionItemModel getCollectionItem();

}
