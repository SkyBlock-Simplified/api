package dev.sbs.api.data.model.pet_items;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.items.ItemModel;
import dev.sbs.api.data.model.stats.StatModel;

import java.util.Map;

public interface PetItemModel extends EffectsModel {

    ItemModel getItem();

    String getDescription();

    boolean isPercentage();

    Map<String, Object> getBuffEffects();

}
