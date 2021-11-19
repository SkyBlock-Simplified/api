package dev.sbs.api.data.model.skyblock.minions;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.collections.CollectionModel;

public interface MinionModel extends Model {

    String getKey();

    CollectionModel getCollection();

    String getName();

}
