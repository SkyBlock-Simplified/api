package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface MinionItemModel extends Model {

    MinionModel getMinion();

    CollectionItemModel getCollectionItem();

    double getAverageYield();

}
