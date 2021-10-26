package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface PetItemStatModel extends Model {

    ItemModel getItem();

    StatModel getStat();

    int getStatValue();

    boolean isPercentage();

}
