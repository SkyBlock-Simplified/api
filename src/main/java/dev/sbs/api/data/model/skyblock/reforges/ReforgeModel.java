package dev.sbs.api.data.model.skyblock.reforges;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.reforge_conditions.ReforgeConditionModel;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeModel;

import java.util.List;

public interface ReforgeModel extends Model {

    String getKey();

    String getName();

    ReforgeTypeModel getType();

    boolean isBlacksmith();

    boolean isStone();

    default List<ReforgeConditionModel> getConditions() {
        return SimplifiedApi.getRepositoryOf(ReforgeConditionModel.class).findAll(ReforgeConditionModel::getReforge, this);
    }

}
