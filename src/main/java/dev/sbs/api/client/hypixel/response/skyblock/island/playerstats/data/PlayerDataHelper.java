package dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockDate;
import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentModel;
import dev.sbs.api.data.model.skyblock.gemstone_stats.GemstoneStatModel;
import dev.sbs.api.data.model.skyblock.hot_potato_stats.HotPotatoStatModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.reforge_stats.ReforgeStatModel;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.IntTag;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.search.function.SearchFunction;
import dev.sbs.api.util.data.mutable.MutableBoolean;
import dev.sbs.api.util.data.mutable.MutableDouble;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.data.tuple.Triple;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.math.Expression;
import dev.sbs.api.util.math.ExpressionBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerDataHelper {

    private static final Pattern nbtVariablePattern = Pattern.compile(".*?(nbt_([a-zA-Z0-9_\\-.]+)).*?");

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static double handleBonusEffects(StatModel statModel, double currentTotal, CompoundTag compoundTag, Map<String, Double> variables, BuffEffectsModel... bonusEffectsModels) {
        MutableDouble value = new MutableDouble(currentTotal);

        // Handle Bonus Stats
        for (BuffEffectsModel bonusEffectModel : bonusEffectsModels) {
            value.add((double) bonusEffectModel.getEffect(statModel.getKey(), 0.0));

            bonusEffectModel.getBuffEffects().forEach((buffKey, buffValue) -> {
                String filterKey = (String) buffKey;

                if (filterKey.equals("TIME")) {
                    SkyBlockDate currentDate = new SkyBlockDate(System.currentTimeMillis());
                    int hour = currentDate.getHour();
                    List<String> timeConstraints = (List<String>) buffValue;
                    MutableBoolean insideConstraint = new MutableBoolean(false);

                    timeConstraints.forEach(timeConstraint -> {
                        String[] constraintParts = timeConstraint.split("-");
                        int start = NumberUtil.toInt(constraintParts[0]);
                        int end = NumberUtil.toInt(constraintParts[1]);

                        if (hour >= start && hour <= end)
                            insideConstraint.setTrue(); // At Least 1 Constraint is True
                    });

                    if (insideConstraint.isFalse())
                        value.set(0.0);
                } else {
                    boolean multiply = false;

                    if (filterKey.startsWith("MULTIPLY_")) {
                        filterKey = filterKey.replace("MULTIPLY_", "");
                        multiply = true;
                    } else if (filterKey.startsWith("ADD_"))
                        filterKey = filterKey.replace("ADD_", "");

                    if (filterKey.startsWith("STAT_")) {
                        filterKey = filterKey.replace("STAT_", "");

                        // Handle Buff Stat
                        if (statModel.getKey().equals(filterKey) || "ALL".equals(filterKey)) {
                            String valueString = String.valueOf(buffValue);

                            if (NumberUtil.isCreatable(valueString))
                                value.set(value.get() * (double) buffValue);
                            else {
                                if (!multiply || statModel.isMultipliable()) {
                                    if (compoundTag != null) {
                                        Matcher nbtMatcher = nbtVariablePattern.matcher(valueString);

                                        if (nbtMatcher.matches()) {
                                            String nbtTag = nbtMatcher.group(2);
                                            String nbtValue = String.valueOf(compoundTag.getPath(nbtTag).getValue());
                                            valueString = valueString.replace(nbtMatcher.group(1), nbtValue);
                                        }
                                    }

                                    Expression expression = new ExpressionBuilder(FormatUtil.format("{0,number,#} {1} ({2})", currentTotal, (multiply ? "*" : "+"), valueString))
                                        .variables(variables.keySet())
                                        .build()
                                        .setVariables(variables)
                                        .setVariable("CURRENT_VALUE", currentTotal);

                                    value.set(expression.evaluate());
                                }
                            }
                        }
                    }
                }
            });
        }

        return value.get();
    }

    public static ConcurrentMap<StatModel, Double> handleGemstoneBonus(ObjectData<?> objectData) {
        ConcurrentMap<StatModel, Double> gemstoneAdjusted = Concurrent.newMap();

        objectData.getGemstones().forEach(entry -> entry.getValue()
            .stream()
            .map(gemstoneTypeModel -> Triple.of(
                entry.getKey(),
                gemstoneTypeModel,
                SimplifiedApi.getRepositoryOf(GemstoneStatModel.class).findFirst(
                    Pair.of(GemstoneStatModel::getGemstone, entry.getKey()),
                    Pair.of(GemstoneStatModel::getType, gemstoneTypeModel),
                    Pair.of(GemstoneStatModel::getRarity, objectData.getRarity())
                )
            ))
            .filter(gemstoneData -> gemstoneData.getRight().isPresent())
            .forEach(gemstoneData -> gemstoneAdjusted.put(
                gemstoneData.getLeft().getStat(),
                gemstoneData.getRight().get().getValue() + gemstoneAdjusted.getOrDefault(gemstoneData.getLeft().getStat(), 0.0))
            )
        );

        return gemstoneAdjusted;
    }

    public static ConcurrentMap<StatModel, Double> handleReforgeBonus(Optional<ReforgeStatModel> optionalReforgeStatModel) {
        ConcurrentMap<StatModel, Double> reforgeBonuses = Concurrent.newMap();

        // Load Reforge Stat Effects
        optionalReforgeStatModel.ifPresent(reforgeStatModel -> reforgeStatModel.getEffects()
            .forEach((key, value) -> SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, key)
                .ifPresent(statModel -> reforgeBonuses.put(statModel, value + reforgeBonuses.getOrDefault(statModel, 0.0)))));

        return reforgeBonuses;
    }

    public static ItemData parseItemData(ItemModel itemModel, CompoundTag itemTag, String reforgeTypeKey) {
        ItemData itemData = new ItemData(itemModel, itemTag, reforgeTypeKey);

        // Save Stats
        itemModel.getStats().forEach((key, value) -> SimplifiedApi.getRepositoryOf(StatModel.class)
            .findFirst(StatModel::getKey, key)
            .ifPresent(statModel -> itemData.getStats(ItemData.Type.STATS).get(statModel).addBonus(value)));

        // Save Enchantment Stats
        if (itemTag.containsPath("tag.ExtraAttributes.enchantments")) {
            itemTag.<CompoundTag>getPath("tag.ExtraAttributes.enchantments")
                .entrySet()
                .stream()
                .map(entry -> Pair.of(entry.getKey().toUpperCase(), ((IntTag)entry.getValue()).getValue()))
                .forEach(pair -> SimplifiedApi.getRepositoryOf(EnchantmentModel.class)
                    .findFirst(EnchantmentModel::getKey, pair.getKey())
                    .ifPresent(enchantmentModel -> itemData.addEnchantment(enchantmentModel, pair.getValue())));
        }

        // Save Reforge Stats
        handleReforgeBonus(itemData.getReforgeStat())
            .forEach((statModel, value) -> itemData.getStats(ItemData.Type.REFORGES).get(statModel).addBonus(value));

        // Save Gemstone Stats
        handleGemstoneBonus(itemData)
            .forEach((statModel, value) -> itemData.getStats(ItemData.Type.GEMSTONES).get(statModel).addBonus(value));

        // Save Hot Potato Book Stats
        SimplifiedApi.getRepositoryOf(HotPotatoStatModel.class)
            .findAll(SearchFunction.combine(HotPotatoStatModel::getType, ReforgeTypeModel::getKey), reforgeTypeKey)
            .forEach(hotPotatoStatModel -> itemData.getStats(ItemData.Type.HOT_POTATOES).get(hotPotatoStatModel.getStat()).addBonus(itemData.getHotPotatoBooks() * hotPotatoStatModel.getValue()));

        // Save Art Of War Stats
        if (itemData.hasArtOfWar()) {
            SimplifiedApi.getRepositoryOf(StatModel.class)
                .findFirst(StatModel::getKey, "STRENGTH")
                .ifPresent(statModel -> itemData.getStats(ItemData.Type.ART_OF_WAR).get(statModel).addBonus(5.0));
        }

        return itemData;
    }

}
