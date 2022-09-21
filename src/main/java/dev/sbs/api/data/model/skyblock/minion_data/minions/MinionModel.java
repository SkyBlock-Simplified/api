package dev.sbs.api.data.model.skyblock.minion_data.minions;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.collection_data.collections.CollectionModel;

public interface MinionModel extends Model {

    String getKey();

    CollectionModel getCollection();

    String getName();

}
