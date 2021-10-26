package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.util.List;

public interface CollectionItemTierModel extends Model {

    CollectionItemModel getCollectionItem();

    int getTier();

    double getAmountRequired();

    List<String> getUnlocks();

}

