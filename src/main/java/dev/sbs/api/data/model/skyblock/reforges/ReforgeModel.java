package dev.sbs.api.data.model.skyblock.reforges;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeModel;

public interface ReforgeModel extends Model {

    String getKey();

    String getName();

    ReforgeTypeModel getType();

    boolean isBlacksmith();

    boolean isStone();

}
