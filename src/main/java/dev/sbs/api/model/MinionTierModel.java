package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface MinionTierModel extends Model {

    MinionModel getMinion();

    ItemModel getItem();

    int getSpeed();

}
