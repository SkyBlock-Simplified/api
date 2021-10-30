package dev.sbs.api.data.model.reforges;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.reforge_types.ReforgeTypeModel;

public interface ReforgeModel extends Model {

    String getKey();

    String getName();

    ReforgeTypeModel getType();

    boolean isStone();

}
