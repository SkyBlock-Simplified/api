package dev.sbs.api.data.model.items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.rarities.RarityModel;

import java.util.List;
import java.util.Map;

public interface ItemModel extends Model {

    String getName();

    String getMaterial();

    Integer getDurability();

    String getSkin();

    String getFurniture();

    String getItemId();

    RarityModel getRarity();

    String getGenerator();

    Integer getGeneratorTier();

    boolean isGlowing();

    String getCategory();

    Map<String, Double > getStats();

    Double getNpcSellPrice();

    boolean isUnstackable();

    boolean isDungeonItem();

    String getColor();

    Map<String, List<Integer>> getTieredStats();

    Integer getGearScore();

    Map<String, Object> getRequirements();

    Map<String, Object> getCatacombsRequirements();

    Map<String, Object> getEssence();

    String getDescription();

    Double getAbilityDamageScaling();

    Map<String, Integer> getEnchantments();

    String getCrystal();

    String getPrivateIsland();

}
