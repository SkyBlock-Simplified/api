package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface ReforgeModel extends Model {

    String getKey();

    String getName();

    ReforgeTypeModel getType();

    boolean isStone();

}
