package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface AccessoryFamilyModel extends Model {

    String getKey();

    String getName();

    boolean isReforgesStackable();

    boolean isItemsStackable();

}
