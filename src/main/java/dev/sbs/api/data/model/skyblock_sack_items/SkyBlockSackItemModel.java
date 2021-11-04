package dev.sbs.api.data.model.skyblock_sack_items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.items.ItemModel;
import dev.sbs.api.data.model.skyblock_sacks.SkyBlockSackModel;

public interface SkyBlockSackItemModel extends Model {

    SkyBlockSackModel getSack();

    ItemModel getItem();

}
