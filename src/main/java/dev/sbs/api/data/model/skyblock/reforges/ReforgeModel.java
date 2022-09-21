package dev.sbs.api.data.model.skyblock.reforges;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.reforge_conditions.ReforgeConditionModel;

import java.util.List;

public interface ReforgeModel extends Model {

    String getKey();

    String getName();

    ItemModel getItem();

    List<String> getItemTypes();

    default boolean isBlacksmith() {
        return this.getItem() == null;
    }

    default boolean isStone() {
        return this.getItem() != null;
    }

    default List<ReforgeConditionModel> getConditions() {
        return SimplifiedApi.getRepositoryOf(ReforgeConditionModel.class).findAll(ReforgeConditionModel::getReforge, this);
    }

}
