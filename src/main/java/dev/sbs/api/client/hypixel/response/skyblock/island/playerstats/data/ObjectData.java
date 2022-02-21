package dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.bonus_item_stats.BonusItemStatModel;
import dev.sbs.api.data.model.skyblock.bonus_reforge_stats.BonusReforgeStatModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.reforge_stats.ReforgeStatModel;
import dev.sbs.api.data.model.skyblock.reforges.ReforgeModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.IntTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.StringTag;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.tuple.Pair;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public abstract class ObjectData<T extends ObjectData.Type> extends StatData<T> {

    @Getter private final ItemModel item;
    @Getter private final CompoundTag compoundTag;
    @Getter private final RarityModel rarity;
    @Getter private final Optional<BonusItemStatModel> bonusItemStatModel;
    @Getter private final Optional<ReforgeModel> reforge;
    @Getter private final Optional<BonusReforgeStatModel> bonusReforgeStatModel;
    @Getter private final Optional<ReforgeStatModel> reforgeStat;
    @Getter private final boolean recombobulated;

    protected ObjectData(ItemModel itemModel, CompoundTag compoundTag) {
        this.item = itemModel;
        this.compoundTag = compoundTag;

        // Load Recombobulator
        this.recombobulated = compoundTag.getPathOrDefault("tag.ExtraAttributes.rarity_upgrades", IntTag.EMPTY).getValue() == 1;

        // Load Rarity
        this.rarity = SimplifiedApi.getRepositoryOf(RarityModel.class)
            .findFirst(RarityModel::getOrdinal, itemModel.getRarity().getOrdinal() + (recombobulated ? 1 : 0))
            .orElse(itemModel.getRarity());

        // Initialize Stats
        ConcurrentList<StatModel> statModels = SimplifiedApi.getRepositoryOf(StatModel.class).findAll(StatModel::getOrdinal);
        Arrays.stream(this.getAllTypes()).forEach(type -> {
            this.stats.put(type, Concurrent.newLinkedMap());
            statModels.forEach(statModel -> this.stats.get(type).put(statModel, new Data()));
        });

        // Load Reforge Model
        this.reforge = SimplifiedApi.getRepositoryOf(ReforgeModel.class)
            .findFirst(ReforgeModel::getKey, this.getCompoundTag()
                .getPathOrDefault("tag.ExtraAttributes.modifier", StringTag.EMPTY)
                .getValue()
                .toUpperCase()
            );

        // Load Bonus Reforge Model
        this.bonusReforgeStatModel = this.reforge.flatMap(reforgeModel -> SimplifiedApi.getRepositoryOf(BonusReforgeStatModel.class)
            .findFirst(BonusReforgeStatModel::getReforge, reforgeModel)
        );

        // Load Reforge Stat Model
        this.reforgeStat = this.reforge.flatMap(reforgeModel -> SimplifiedApi.getRepositoryOf(ReforgeStatModel.class)
            .findFirst(
                Pair.of(ReforgeStatModel::getReforge, reforgeModel),
                Pair.of(ReforgeStatModel::getRarity, rarity)
            )
        );

        // Load Bonus Item Stat Model
        this.bonusItemStatModel = SimplifiedApi.getRepositoryOf(BonusItemStatModel.class)
            .findFirst(BonusItemStatModel::getItem, itemModel);
    }

    public abstract ObjectData<T> calculateBonus(ConcurrentMap<String, Double> expressionVariables);

    public interface Type {

        boolean isOptimizerConstant();

    }

}
