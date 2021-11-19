package dev.sbs.api.data.model.skyblock.potion_tiers;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.potions.PotionModel;
import dev.sbs.api.minecraft.text.MinecraftChatFormatting;

import java.util.Map;

public interface PotionTierModel extends EffectsModel {

    PotionModel getPotion();

    int getTier();

    default MinecraftChatFormatting getChatFormatting() {
        if (this.getTier() >= 7)
            return MinecraftChatFormatting.DARK_PURPLE;
        else if (this.getTier() >= 5)
            return MinecraftChatFormatting.BLUE;
        else if (this.getTier() >= 3)
            return MinecraftChatFormatting.GREEN;
        else
            return MinecraftChatFormatting.WHITE;
    }

    ItemModel getIngredientItem();

    String getBaseItem();

    int getExperienceYield();

    double getSellPrice();

    Map<String, Object> getBuffEffects();

}
