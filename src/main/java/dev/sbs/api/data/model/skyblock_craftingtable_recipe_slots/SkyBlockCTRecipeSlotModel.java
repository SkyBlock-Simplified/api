package dev.sbs.api.data.model.skyblock_craftingtable_recipe_slots;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock_craftingtable_recipes.SkyBlockCTRecipeModel;
import dev.sbs.api.data.model.skyblock_craftingtable_slots.SkyBlockCTSlotModel;

public interface SkyBlockCTRecipeSlotModel extends Model {

    SkyBlockCTRecipeModel getRecipe();

    SkyBlockCTSlotModel getSlot();

    Integer getOrdinal();

}
