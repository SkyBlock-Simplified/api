package dev.sbs.api.data.model.skyblock.bit_item_craftables;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.bit_items.BitItemModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

public interface BitItemCraftableModel extends Model {

    BitItemModel getBitItem();

    ItemModel getCraftableItem();

    String getDescription();

    String getExpression();

}
