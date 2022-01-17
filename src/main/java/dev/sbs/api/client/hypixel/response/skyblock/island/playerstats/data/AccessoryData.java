package dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data;

import dev.sbs.api.data.model.skyblock.accessories.AccessoryModel;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AccessoryData extends ObjectData<AccessoryData.Type> {

    @Getter private final AccessoryModel accessory;

    public AccessoryData(AccessoryModel accessory, CompoundTag compoundTag) {
        super(accessory.getItem(), compoundTag);
        this.accessory = accessory;
    }

    @Override
    public AccessoryData calculateBonus(ConcurrentMap<String, Double> expressionVariables) {
        // Handle Reforges
        this.getBonusReforgeStatModel()
            .ifPresent(bonusReforgeStatModel -> this.getStats(AccessoryData.Type.REFORGES)
                .forEach((statModel, statData) -> {
                    statData.base = PlayerDataHelper.handleBonusEffects(statModel, statData.getBase(), this.getCompoundTag(), expressionVariables, bonusReforgeStatModel);
                    statData.bonus = PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), this.getCompoundTag(), expressionVariables, bonusReforgeStatModel);
                })
            );

        // Handle Bonus Item Stats
        this.getBonusItemStatModel().ifPresent(bonusItemStatModel -> {
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

        return this;
    }

    @Override
    protected Type[] getAllTypes() {
        return AccessoryData.Type.values();
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
