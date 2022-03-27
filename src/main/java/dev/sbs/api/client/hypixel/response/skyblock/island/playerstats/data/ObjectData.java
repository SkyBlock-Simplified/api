package dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.bonus_item_stats.BonusItemStatModel;
import dev.sbs.api.data.model.skyblock.bonus_reforge_stats.BonusReforgeStatModel;
import dev.sbs.api.data.model.skyblock.gemstone_types.GemstoneTypeModel;
import dev.sbs.api.data.model.skyblock.gemstones.GemstoneModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.reforge_stats.ReforgeStatModel;
import dev.sbs.api.data.model.skyblock.reforges.ReforgeModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.IntTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.StringTag;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.mutable.MutableObject;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.helper.RegexUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public abstract class ObjectData<T extends ObjectData.Type> extends StatData<T> {

    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("M/d/yy h:m a", Locale.US);
    @Getter private final ItemModel item;
    @Getter private final CompoundTag compoundTag;
    @Getter private final RarityModel rarity;
    @Getter private final Optional<BonusItemStatModel> bonusItemStatModel;
    @Getter private final Optional<ReforgeModel> reforge;
    @Getter private final Optional<BonusReforgeStatModel> bonusReforgeStatModel;
    @Getter private final Optional<ReforgeStatModel> reforgeStat;
    @Getter private final ConcurrentMap<GemstoneModel, ConcurrentList<GemstoneTypeModel>> gemstones;
    @Getter private final boolean recombobulated;
    @Getter private final boolean tierBoosted;
    @Getter private final Optional<Long> timestamp;

    protected ObjectData(ItemModel itemModel, CompoundTag compoundTag) {
        this.item = itemModel;
        this.compoundTag = compoundTag;

        // Load Timestamp
        this.timestamp = Optional.ofNullable(
                StringUtil.defaultIfEmpty(
                    compoundTag.getPathOrDefault("tag.ExtraAttributes.timestamp", StringTag.EMPTY).getValue(),
                    null
                )
            )
            .map(timestamp -> LocalDateTime.parse(timestamp, TIMESTAMP_FORMAT))
            .map(localDateTime -> localDateTime.atZone(ZoneId.of("EST", ZoneId.SHORT_IDS)))
            .map(ZonedDateTime::toInstant)
            .map(Instant::toEpochMilli);

        // Load Recombobulator
        this.recombobulated = compoundTag.getPathOrDefault("tag.ExtraAttributes.rarity_upgrades", IntTag.EMPTY).getValue() == 1;

        // Load Tier Boost
        this.tierBoosted = compoundTag.getPathOrDefault("tag.ExtraAttributes.baseStatBoostPercentage", IntTag.EMPTY).getValue() > 0;

        // Load Gemstones
        CompoundTag gemTag = compoundTag.getPath("tag.ExtraAttributes.gems");
        ConcurrentMap<GemstoneModel, ConcurrentList<GemstoneTypeModel>> gemstones = Concurrent.newMap();
        if (gemTag != null && gemTag.notEmpty()) {
            gemTag.forEach((key, tag) -> {
                String upperKey = key.toUpperCase();
                String gemKey = RegexUtil.replaceAll(upperKey, "_[\\d]", "");
                MutableObject<String> gemTypeKey = new MutableObject<>(((StringTag) tag).getValue().toUpperCase());
                Optional<GemstoneModel> optionalGemstoneModel = SimplifiedApi.getRepositoryOf(GemstoneModel.class).findFirst(GemstoneModel::getKey, gemKey);

                // Handle Typed Slots
                if (!optionalGemstoneModel.isPresent()) {
                    if (gemKey.endsWith("_GEM")) {
                        optionalGemstoneModel = SimplifiedApi.getRepositoryOf(GemstoneModel.class).findFirst(GemstoneModel::getKey, gemTypeKey.get());
                        gemTypeKey.set(gemTag.getValue(upperKey.replace("_GEM", "")));
                    }
                }

                // Load Gemstone
                optionalGemstoneModel.ifPresent(gemstoneModel -> {
                    // Handle New Gemstone
                    gemstones.putIfAbsent(gemstoneModel, Concurrent.newList());

                    // Add Gemstone Type
                    SimplifiedApi.getRepositoryOf(GemstoneTypeModel.class)
                        .findFirst(GemstoneTypeModel::getKey, gemTypeKey.get())
                        .ifPresent(gemstoneTypeModel -> gemstones.get(gemstoneModel).add(gemstoneTypeModel));
                });
            });
        }
        this.gemstones = Concurrent.newUnmodifiableMap(gemstones);

        // Initialize Stats
        ConcurrentList<StatModel> statModels = SimplifiedApi.getRepositoryOf(StatModel.class).findAll().sort(StatModel::getOrdinal);
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

        // Load Bonus Item Stat Model
        this.bonusItemStatModel = SimplifiedApi.getRepositoryOf(BonusItemStatModel.class)
            .findFirst(BonusItemStatModel::getItem, itemModel);

        // Load Rarity
        this.rarity = SimplifiedApi.getRepositoryOf(RarityModel.class)
            .findFirst(
                RarityModel::getOrdinal,
                this.handleRarityUpgrades(
                    itemModel.getRarity().getOrdinal() +
                        (this.isRecombobulated() ? 1 : 0) +
                        (this.isTierBoosted() ? 1 : 0)
                )
            )
            .orElse(itemModel.getRarity());

        // Load Reforge Stat Model
        this.reforgeStat = this.getReforge().flatMap(reforgeModel -> SimplifiedApi.getRepositoryOf(ReforgeStatModel.class)
            .findFirst(
                Pair.of(ReforgeStatModel::getReforge, reforgeModel),
                Pair.of(ReforgeStatModel::getRarity, this.getRarity())
            )
        );
    }

    public abstract ObjectData<T> calculateBonus(ConcurrentMap<String, Double> expressionVariables);

    protected int handleRarityUpgrades(int rarityOrdinal) {
        return rarityOrdinal;
    }

    public abstract boolean isBonusCalculated();

    public final boolean notRecombobulated() {
        return !this.isRecombobulated();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectData<?> that = (ObjectData<?>) o;

        return new EqualsBuilder()
            .append(this.isRecombobulated(), that.isRecombobulated())
            .append(this.getItem(), that.getItem())
            .append(this.getCompoundTag(), that.getCompoundTag())
            .append(this.getRarity(), that.getRarity())
            .append(this.getBonusItemStatModel(), that.getBonusItemStatModel())
            .append(this.getReforge(), that.getReforge())
            .append(this.getBonusReforgeStatModel(), that.getBonusReforgeStatModel())
            .append(this.getReforgeStat(), that.getReforgeStat())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getItem())
            .append(this.getCompoundTag())
            .append(this.getRarity())
            .append(this.getBonusItemStatModel())
            .append(this.getReforge())
            .append(this.getBonusReforgeStatModel())
            .append(this.getReforgeStat())
            .append(this.isRecombobulated())
            .build();
    }

    public interface Type {

        boolean isOptimizerConstant();

    }

}
