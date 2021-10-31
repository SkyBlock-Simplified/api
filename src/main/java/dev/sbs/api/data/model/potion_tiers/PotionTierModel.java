package dev.sbs.api.data.model.potion_tiers;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.items.ItemModel;
import dev.sbs.api.data.model.potions.PotionModel;

import java.util.Map;

public interface PotionTierModel extends EffectsModel {

    PotionModel getPotion();

    int getTier();

    ItemModel getItem();

    int getExperienceYield();

    double getSellCost();

    Map<String, Object> getBuffEffects();

}
