package dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.accessories.AccessoryModel;
import dev.sbs.api.data.model.skyblock.accessory_enrichments.AccessoryEnrichmentModel;
import dev.sbs.api.data.model.skyblock.bonus_item_stats.BonusItemStatModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.minecraft.nbt.exception.NbtException;
import dev.sbs.api.minecraft.nbt.tags.array.ByteArrayTag;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.collection.ListTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.StringTag;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.search.function.SearchFunction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

public class AccessoryData extends ObjectData<AccessoryData.Type> {

    @Getter private final AccessoryModel accessory;
    @Getter private boolean bonusCalculated;
    @Getter private final Optional<AccessoryEnrichmentModel> enrichment;

    public AccessoryData(AccessoryModel accessory, CompoundTag compoundTag) {
        super(accessory.getItem(), compoundTag);
        this.accessory = accessory;

        // Load Enrichment
        this.enrichment = SimplifiedApi.getRepositoryOf(AccessoryEnrichmentModel.class).findFirst(
            SearchFunction.combine(AccessoryEnrichmentModel::getStat, StatModel::getKey),
            compoundTag.getPathOrDefault("tag.ExtraAttributes.talisman_enrichment", StringTag.EMPTY).getValue()
        );

        // Handle Gemstone Stats
        PlayerDataHelper.handleGemstoneBonus(this)
            .forEach((statModel, value) -> this.addBonus(this.getStats(AccessoryData.Type.GEMSTONES).get(statModel), value));

        // Handle Reforge Stats
        PlayerDataHelper.handleReforgeBonus(this.getReforgeStat())
            .forEach((statModel, value) -> this.addBonus(this.getStats(AccessoryData.Type.REFORGES).get(statModel), value));

        // Handle Stats
        this.getAccessory().getEffects().forEach((key, value) -> SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, key)
            .ifPresent(statModel -> this.addBonus(this.getStats(AccessoryData.Type.STATS).get(statModel), value)));

        // Handle Enrichment Stats
        this.getEnrichment()
            .ifPresent(accessoryEnrichmentModel -> this.addBonus(this.getStats(AccessoryData.Type.ENRICHMENTS).get(accessoryEnrichmentModel.getStat()), accessoryEnrichmentModel.getValue()));

        // New Year Cake Bag
        if ("NEW_YEAR_CAKE_BAG".equals(this.getAccessory().getItem().getItemId())) {
            try {
                Byte[] nbtCakeBag = compoundTag.<ByteArrayTag>getPath("tag.ExtraAttributes.new_year_cake_bag_data").getValue();
                ListTag<CompoundTag> cakeBagItems = SimplifiedApi.getNbtFactory().fromByteArray(nbtCakeBag).getList("i");
                SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, "HEALTH")
                    .ifPresent(statModel -> this.addBonus(this.getStats(AccessoryData.Type.CAKE_BAG).get(statModel), cakeBagItems.size()));
            } catch (NbtException ignore) { }
        }
    }

    @Override
    protected int handleRarityUpgrades(int rarityOrdinal) {
        boolean upgradeRarity = false;

        if (this.getItem().getItemId().equals("POWER_ARTIFACT")) {
            if (this.getGemstones().size() == 7) {
                long perfects = this.getGemstones()
                    .stream()
                    .flatMap(entry -> entry.getValue().stream())
                    .filter(gemstoneTypeModel -> gemstoneTypeModel.getKey().equals("PERFECT"))
                    .count();

                upgradeRarity = (perfects == 7);
            }
        }

        return rarityOrdinal + (upgradeRarity ? 1 : 0);
    }

    @Override
    public AccessoryData calculateBonus(ConcurrentMap<String, Double> expressionVariables) {
        if (!this.isBonusCalculated()) {
            this.bonusCalculated = true;

            // Handle Reforges
            this.getBonusReforgeStatModel().ifPresent(bonusReforgeStatModel -> this.getStats(AccessoryData.Type.REFORGES)
                .forEach((statModel, statData) -> statData.bonus = PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), this.getCompoundTag(), expressionVariables, bonusReforgeStatModel))
            );

            // Handle Bonus Item Stats
            this.getBonusItemStatModels()
                .stream()
                .filter(BonusItemStatModel::noRequiredMobType)
                .forEach(bonusItemStatModel -> {
                    // Handle Bonus Gemstone Stats
                    if (bonusItemStatModel.isForGems()) {
                        this.getStats(AccessoryData.Type.GEMSTONES)
                            .forEach((statModel, statData) -> statData.bonus = PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), this.getCompoundTag(), expressionVariables, bonusItemStatModel));
                    }

                    // Handle Bonus Reforges
                    if (bonusItemStatModel.isForReforges()) {
                        this.getStats(AccessoryData.Type.REFORGES)
                            .forEach((statModel, statData) -> statData.bonus = PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), this.getCompoundTag(), expressionVariables, bonusItemStatModel));
                    }

                    // Handle Bonus Stats
                    if (bonusItemStatModel.isForStats()) {
                        this.getStats(AccessoryData.Type.STATS)
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

        AccessoryData that = (AccessoryData) o;

        return new EqualsBuilder()
            .append(this.isBonusCalculated(), that.isBonusCalculated())
            .append(this.getAccessory(), that.getAccessory())
            .build();
    }

    @Override
    protected Type[] getAllTypes() {
        return AccessoryData.Type.values();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .appendSuper(super.hashCode())
            .append(this.getAccessory())
            .append(this.isBonusCalculated())
            .build();
    }

    public final boolean isMissingEnrichment() {
        return this.getRarity().isEnrichable() && !this.getEnrichment().isPresent();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Type implements ObjectData.Type {

        CAKE_BAG(true),
        GEMSTONES(true),
        REFORGES(false),
        STATS(true),
        ENRICHMENTS(true);

        @Getter private final boolean optimizerConstant;

    }

}
