package dev.sbs.api.data.model.pet_item_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.items.ItemModel;
import dev.sbs.api.data.model.stats.StatModel;

public interface PetItemStatModel extends Model {

    ItemModel getItem();

    StatModel getStat();

    int getStatValue();

    boolean isPercentage();

}
