package dev.sbs.api.data.model.items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.rarities.RarityModel;

import java.util.List;
import java.util.Map;

public interface ItemModel extends Model {

    String getName();

    String getMaterial();

    int getDurability();

    String getSkin();

    String getFurniture();

    String getItemId();

    RarityModel getRarity();

    String getGenerator();

    int getGeneratorTier();

    boolean isGlowing();

    String getCategory();

    Map<String, Double> getStats();

    double getNpcSellPrice();

    boolean isUnstackable();

    boolean isDungeonItem();

    String getColor();

    Map<String, List<Integer>> getTieredStats();

    int getGearScore();

    Map<String, Object> getRequirements();

    Map<String, Object> getCatacombsRequirements();

    Map<String, Object> getEssence();

    String getDescription();

    double getAbilityDamageScaling();

    Map<String, Integer> getEnchantments();

    String getCrystal();

    String getPrivateIsland();

}