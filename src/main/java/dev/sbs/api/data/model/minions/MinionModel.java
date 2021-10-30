package dev.sbs.api.data.model.minions;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.collections.CollectionModel;

public interface MinionModel extends Model {

    String getKey();

    CollectionModel getCollection();

    String getName();

}
