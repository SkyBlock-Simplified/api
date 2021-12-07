package dev.sbs.api.data.model.skyblock.bit_items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.bit_types.BitTypeModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

public interface BitItemModel extends Model {

    ItemModel getItem();

    BitTypeModel getType();

    Integer getBitCost();

}
