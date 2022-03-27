package dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.enchantment_stats.EnchantmentStatModel;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentModel;
import dev.sbs.api.data.model.skyblock.hot_potato_stats.HotPotatoStatModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.IntTag;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.search.function.SearchFunction;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.helper.ListUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

public class ItemData extends ObjectData<ItemData.Type> {

    @Getter private final ConcurrentMap<EnchantmentModel, Integer> enchantments;
    @Getter private final ConcurrentMap<EnchantmentModel, ConcurrentList<EnchantmentStatModel>> enchantmentStats;
    @Getter private final int hotPotatoBooks;
    private final Boolean hasArtOfWar;
    @Getter private boolean bonusCalculated;

    public ItemData(ItemModel itemModel, CompoundTag compoundTag, String reforgeTypeKey) {
        super(itemModel, compoundTag);
        this.hotPotatoBooks = compoundTag.getPathOrDefault("tag.ExtraAttributes.hot_potato_count", IntTag.EMPTY).getValue();
        this.hasArtOfWar = compoundTag.containsPath("tag.ExtraAttributes.art_of_war_count");

        // Save Stats
        itemModel.getStats().forEach((key, value) -> SimplifiedApi.getRepositoryOf(StatModel.class)
            .findFirst(StatModel::getKey, key)
            .ifPresent(statModel -> this.getStats(ItemData.Type.STATS).get(statModel).addBonus(value)));

        // Save Reforge Stats
        PlayerDataHelper.handleReforgeBonus(this.getReforgeStat())
            .forEach((statModel, value) -> this.getStats(ItemData.Type.REFORGES).get(statModel).addBonus(value));

        // Save Gemstone Stats
        PlayerDataHelper.handleGemstoneBonus(this)
            .forEach((statModel, value) -> this.getStats(ItemData.Type.GEMSTONES).get(statModel).addBonus(value));

        // Save Hot Potato Book Stats
        SimplifiedApi.getRepositoryOf(HotPotatoStatModel.class)
            .findAll(SearchFunction.combine(HotPotatoStatModel::getType, ReforgeTypeModel::getKey), reforgeTypeKey)
            .forEach(hotPotatoStatModel -> this.getStats(ItemData.Type.HOT_POTATOES).get(hotPotatoStatModel.getStat()).addBonus(this.getHotPotatoBooks() * hotPotatoStatModel.getValue()));

        // Save Art Of War Stats
        if (this.hasArtOfWar()) {
            SimplifiedApi.getRepositoryOf(StatModel.class)
                .findFirst(StatModel::getKey, "STRENGTH")
                .ifPresent(statModel -> this.getStats(ItemData.Type.ART_OF_WAR).get(statModel).addBonus(5.0));
        }

        // Save Enchantment Stats
        ConcurrentMap<EnchantmentModel, Integer> enchantments = Concurrent.newMap();
        ConcurrentMap<EnchantmentModel, ConcurrentList<EnchantmentStatModel>> enchantmentStats = Concurrent.newMap();

        if (compoundTag.containsPath("tag.ExtraAttributes.enchantments")) {
            compoundTag.<CompoundTag>getPath("tag.ExtraAttributes.enchantments")
                .entrySet()
                .stream()
                .map(entry -> Pair.of(entry.getKey().toUpperCase(), ((IntTag)entry.getValue()).getValue()))
                .map(pair -> Pair.of(
                    SimplifiedApi.getRepositoryOf(EnchantmentModel.class)
                    .findFirstOrNull(EnchantmentModel::getKey, pair.getKey()),
                    pair.getRight()
                ))
                .filter(enchantmentData -> Objects.nonNull(enchantmentData.getLeft()))
                .filter(enchantmentData -> ListUtil.isEmpty(enchantmentData.getLeft().getMobTypes()))
                .forEach(enchantmentData -> {
                    enchantments.put(enchantmentData.getKey(), enchantmentData.getValue());
                    enchantmentStats.put(enchantmentData.getKey(), Concurrent.newList());

                    SimplifiedApi.getRepositoryOf(EnchantmentStatModel.class)
                        .findAll(EnchantmentStatModel::getEnchantment, enchantmentData.getLeft())
                        .stream()
                        .filter(EnchantmentStatModel::notPercentage) // Percentage Only
                        .filter(EnchantmentStatModel::hasStat) // Has Stat
                        .filter(enchantmentStatModel -> enchantmentStatModel.getLevels().stream().anyMatch(level -> enchantmentData.getValue() >= level)) // Contains Level
                        .forEach(enchantmentStatModel -> {
                            enchantmentStats.get(enchantmentData.getKey()).add(enchantmentStatModel);
                            double enchantBonus = enchantmentStatModel.getLevels().stream().filter(level -> enchantmentData.getValue() >= level).mapToDouble(__ -> enchantmentStatModel.getLevelBonus()).sum();
                            this.getStats(Type.ENCHANTS).get(enchantmentStatModel.getStat()).addBonus(enchantBonus);
                        });
                });
        }

        this.enchantments = enchantments;
        this.enchantmentStats = enchantmentStats;
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