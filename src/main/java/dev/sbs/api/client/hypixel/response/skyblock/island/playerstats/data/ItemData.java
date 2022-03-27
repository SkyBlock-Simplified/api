package dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.enchantment_stats.EnchantmentStatModel;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.IntTag;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.ListUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ItemData extends ObjectData<ItemData.Type> {

    @Getter private final ConcurrentMap<EnchantmentModel, Integer> enchantments = Concurrent.newMap();
    @Getter private final ConcurrentMap<EnchantmentModel, ConcurrentList<EnchantmentStatModel>> enchantmentStats = Concurrent.newMap();
    @Getter private final int hotPotatoBooks;
    private final Boolean hasArtOfWar;
    @Getter private boolean bonusCalculated;

    public ItemData(ItemModel itemModel, CompoundTag compoundTag, String reforgeTypeKey) {
        super(itemModel, compoundTag);
        this.hotPotatoBooks = compoundTag.getPathOrDefault("tag.ExtraAttributes.hot_potato_count", IntTag.EMPTY).getValue();
        this.hasArtOfWar = compoundTag.containsPath("tag.ExtraAttributes.art_of_war_count");
    }

    @Override
    public ItemData calculateBonus(ConcurrentMap<String, Double> expressionVariables) {
        if (!this.isBonusCalculated()) {
            this.bonusCalculated = true;

            // Handle Reforges
            this.getBonusReforgeStatModel().ifPresent(bonusReforgeStatModel -> this.getStats(ItemData.Type.REFORGES)
                .forEach((statModel, statData) -> statData.bonus = PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), this.getCompoundTag(), expressionVariables, bonusReforgeStatModel))
            );

            // Handle Bonus Item Stats
            this.getBonusItemStatModel().ifPresent(bonusItemStatModel -> {
                // Handle Bonus Gemstone Stats
                if (bonusItemStatModel.isForGems()) {
                    this.getStats(Type.GEMSTONES)
                        .forEach((statModel, statData) -> statData.bonus = PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), this.getCompoundTag(), expressionVariables, bonusItemStatModel));
                }

                // Handle Bonus Reforges
                if (bonusItemStatModel.isForReforges()) {
                    this.getStats(Type.REFORGES)
                        .forEach((statModel, statData) -> statData.bonus = PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), this.getCompoundTag(), expressionVariables, bonusItemStatModel));
                }

                // Handle Bonus Stats
                if (bonusItemStatModel.isForStats()) {
                    this.getStats(Type.STATS)
                        .forEach((statModel, statData) -> statData.bonus = PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), this.getCompoundTag(), expressionVariables, bonusItemStatModel));
                }
            });
        }

        return this;
    }

    public void addEnchantment(EnchantmentModel enchantmentModel, Integer value) {
        this.enchantments.put(enchantmentModel, value);
        this.enchantmentStats.put(enchantmentModel, Concurrent.newList());

        // Handle Static Enchants
        SimplifiedApi.getRepositoryOf(EnchantmentStatModel.class)
            .findAll(EnchantmentStatModel::getEnchantment, enchantmentModel)
            .forEach(enchantmentStatModel -> {
                this.enchantmentStats.get(enchantmentModel).add(enchantmentStatModel);

                if (!enchantmentStatModel.isPercentage() && enchantmentStatModel.getStat() != null && ListUtil.isEmpty(enchantmentModel.getMobTypes())) {
                    double enchantBonus = enchantmentStatModel.getBaseValue() + (enchantmentStatModel.getLevelBonus() * value);
                    this.getStats(Type.ENCHANTS).get(enchantmentStatModel.getStat()).addBonus(enchantBonus);
                }
            });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ItemData itemData = (ItemData) o;

        return new EqualsBuilder()
            .append(this.isBonusCalculated(), itemData.isBonusCalculated())
            .append(this.getEnchantments(), itemData.getEnchantments())
            .append(this.getEnchantmentStats(), itemData.getEnchantmentStats())
            .build();
    }

    @Override
    protected Type[] getAllTypes() {
        return ItemData.Type.values();
    }

    public final boolean hasArtOfWar() {
        return this.hasArtOfWar;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .appendSuper(super.hashCode())
            .append(this.getEnchantments())
            .append(this.getEnchantmentStats())
            .append(this.isBonusCalculated())
            .build();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Type implements ObjectData.Type {

        ART_OF_WAR(true),
        ENCHANTS(true),
        GEMSTONES(true),
        HOT_POTATOES(true),
        REFORGES(false),
        STATS(true);

        @Getter
        private final boolean optimizerConstant;

    }

}