package dev.sbs.api.data.model.skyblock.shop_data.shop_bit_items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.shop_data.shop_bit_types.ShopBitTypeModel;

public interface ShopBitItemModel extends Model {

    ItemModel getItem();

    ShopBitTypeModel getType();

    Integer getBitCost();

}
