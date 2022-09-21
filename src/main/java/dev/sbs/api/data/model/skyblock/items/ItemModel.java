package dev.sbs.api.data.model.skyblock.items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.item_types.ItemTypeModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;

import java.util.List;
import java.util.Map;

public interface ItemModel extends Model {

    String getName();

    String getMaterial();

    Integer getDurability();

    String getSkin();

    String getFurniture();

    RarityModel getRarity();

    ItemTypeModel getType();

    String getItemId();

    String getColor();

    String getGenerator();

    Integer getGeneratorTier();

    boolean isObtainable();

    default boolean isNotObtainable() {
        return !this.isObtainable();
    }

    boolean isGlowing();

    default boolean isNotGlowing() {
        return !this.isGlowing();
    }

    boolean isUnstackable();

    default boolean isStackable() {
        return !this.isUnstackable();
    }

    boolean isInMuseum();

    default boolean isNotInMuseum() {
        return !this.isInMuseum();
    }

    boolean isDungeonItem();

    default boolean isNotDungeonItem() {
        return !this.isDungeonItem();
    }

    boolean isAttributable();

    default boolean isNotAttributable() {
        return !this.isAttributable();
    }

    Double getNpcSellPrice();

    Integer getGearScore();

    Map<String, Double> getStats();

    Map<String, List<Double>> getTieredStats();

    List<Map<String, Object>> getRequirements();

    List<Map<String, Object>> getCatacombsRequirements();

    List<List<Map<String, Object>>> getUpgradeCosts();

    List<Map<String, Object>> getGemstoneSlots();

    Map<String, Double> getEnchantments();

    Map<String, Object> getDungeonItemConversionCost();

    Map<String, Object> getPrestige();

    String getDescription();

    Double getAbilityDamageScaling();

    String getCrystal();

    String getPrivateIsland();

}
