package dev.sbs.api.data.model.skyblock.collection_item_tiers;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.collection_items.CollectionItemModel;

import java.util.List;

public interface CollectionItemTierModel extends Model {

    CollectionItemModel getCollectionItem();

    int getTier();

    double getAmountRequired();

    List<String> getUnlocks();

}

