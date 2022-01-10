package dev.sbs.api.data.model.skyblock.reforge_conditions;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.reforges.ReforgeModel;

public interface ReforgeConditionModel extends Model {

    ReforgeModel getReforge();

    ItemModel getItem();

}
