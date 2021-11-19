package dev.sbs.api.data.model.skyblock.sack_items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.sacks.SackModel;

public interface SackItemModel extends Model {

    SackModel getSack();

    ItemModel getItem();

}
