package dev.sbs.api.data.model.skyblock.shop_bit_item_craftables;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.shop_bit_items.ShopBitItemModel;

public interface ShopBitItemCraftableModel extends Model {

    ShopBitItemModel getBitItem();

    ItemModel getCraftableItem();

    String getDescription();

    String getExpression();

}
