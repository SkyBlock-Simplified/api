package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface MinionModel extends Model {

    String getKey();

    CollectionItemModel getCollection();

    String getName();

}
