package dev.sbs.api.client.hypixel.response.skyblock.island;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.SimplifiedException;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockDate;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.accessories.AccessoryModel;
import dev.sbs.api.data.model.skyblock.accessory_enrichments.AccessoryEnrichmentModel;
import dev.sbs.api.data.model.skyblock.accessory_families.AccessoryFamilyModel;
import dev.sbs.api.data.model.skyblock.bonus_item_stats.BonusItemStatModel;
import dev.sbs.api.data.model.skyblock.bonus_pet_ability_stats.BonusPetAbilityStatModel;
import dev.sbs.api.data.model.skyblock.bonus_reforge_stats.BonusReforgeStatModel;
import dev.sbs.api.data.model.skyblock.dungeon_levels.DungeonLevelModel;
import dev.sbs.api.data.model.skyblock.dungeons.DungeonModel;
import dev.sbs.api.data.model.skyblock.enchantment_stats.EnchantmentStatModel;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentModel;
import dev.sbs.api.data.model.skyblock.essence_perks.EssencePerkModel;
import dev.sbs.api.data.model.skyblock.fairy_exchanges.FairyExchangeModel;
import dev.sbs.api.data.model.skyblock.gemstone_stats.GemstoneStatModel;
import dev.sbs.api.data.model.skyblock.gemstone_types.GemstoneTypeModel;
import dev.sbs.api.data.model.skyblock.gemstones.GemstoneModel;
import dev.sbs.api.data.model.skyblock.hot_potato_stats.HotPotatoStatModel;
import dev.sbs.api.data.model.skyblock.hotm_perk_stats.HotmPerkStatModel;
import dev.sbs.api.data.model.skyblock.hotm_perks.HotmPerkModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.melodys_songs.MelodySongModel;
import dev.sbs.api.data.model.skyblock.pet_abilities.PetAbilityModel;
import dev.sbs.api.data.model.skyblock.pet_ability_stats.PetAbilityStatModel;
import dev.sbs.api.data.model.skyblock.pet_scores.PetScoreModel;
import dev.sbs.api.data.model.skyblock.pet_stats.PetStatModel;
import dev.sbs.api.data.model.skyblock.potion_brew_buffs.PotionBrewBuffModel;
import dev.sbs.api.data.model.skyblock.potion_brews.PotionBrewModel;
import dev.sbs.api.data.model.skyblock.potion_tiers.PotionTierModel;
import dev.sbs.api.data.model.skyblock.potions.PotionModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.reforge_stats.ReforgeStatModel;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeModel;
import dev.sbs.api.data.model.skyblock.reforges.ReforgeModel;
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;
import dev.sbs.api.data.model.skyblock.slayer_levels.SlayerLevelModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.data.sql.function.FilterFunction;
import dev.sbs.api.minecraft.nbt.tags.array.ByteArrayTag;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.collection.ListTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.IntTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.StringTag;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.helper.RegexUtil;
import dev.sbs.api.util.math.Expression;
import dev.sbs.api.util.math.ExpressionBuilder;
import dev.sbs.api.util.mutable.MutableBoolean;
import dev.sbs.api.util.mutable.MutableDouble;
import dev.sbs.api.util.mutable.MutableObject;
import dev.sbs.api.util.tuple.Pair;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class PlayerStats {

    private static final Pattern nbtVariablePattern = Pattern.compile(".*?(nbt_([a-zA-Z0-9_\\-.]+)).*?");
    private static final Repository<StatModel> statRepository = SimplifiedApi.getRepositoryOf(StatModel.class);

    @Getter private long damageMultiplier = 0;
    @Getter private final ConcurrentMap<String, Double> expressionVariables = Concurrent.newMap();
    @Getter private final ConcurrentLinkedMap<Type, ConcurrentLinkedMap<StatModel, Data>> stats = Concurrent.newLinkedMap();
    @Getter private final ConcurrentMap<AccessoryModel, AccessoryData> accessories = Concurrent.newMap();
    @Getter private final ConcurrentMap<ItemModel, ItemData> armor = Concurrent.newMap();
    @Getter private final ConcurrentList<BonusPetAbilityStatModel> bonusPetAbilityStatModels = Concurrent.newList();
    @Getter private boolean bonusCalculated;

    PlayerStats(SkyBlockIsland.Member member) {
        // Initialize
        ConcurrentList<StatModel> statModels = statRepository.findAll();
        statModels.sort((s1, s2) -> Comparator.comparing(StatModel::getOrdinal).compare(s1, s2));
        Arrays.stream(Type.values()).forEach(type -> {
            this.stats.put(type, Concurrent.newLinkedMap());
            statModels.forEach(statModel -> this.stats.get(type).put(statModel, new Data()));
        });
        statModels.forEach(statModel -> this.stats.get(Type.BASE_STATS).get(statModel).addBase(statModel.getBaseValue()));

        // --- Populate Default Expression Variables ---
        member.getActivePet().ifPresent(petInfo -> this.expressionVariables.put("PET_LEVEL", (double) petInfo.getLevel()));
        this.expressionVariables.put("SKILL_AVERAGE", member.getSkillAverage());
        SimplifiedApi.getRepositoryOf(SkillModel.class).findAll().forEach(skillModel -> this.expressionVariables.put(FormatUtil.format("SKILL_LEVEL_{0}", skillModel.getKey()), (double) member.getSkill(skillModel).getLevel()));
        SimplifiedApi.getRepositoryOf(DungeonModel.class)
            .findAll()
            .forEach(dungeonModel -> member.getDungeons()
                .getDungeon(dungeonModel)
                .ifPresent(dungeon -> this.expressionVariables.put(FormatUtil.format("DUNGEON_LEVEL_{0}", dungeonModel.getKey()), (double) dungeon.getLevel())));

        // TODO
        //  Optimizer Request: No API Data
        //     Booster Cookie:     +15 Magic Find
        //     Beacon:             +5 Magic Find
        //  Optimizer Request: Missing API Data
        //     Defused Traps:      +6 Intelligence
        //     Bestiary:           +84 Health
        //     Account Upgrades:   +5 Magic Find
        //  Hypixel Bugs
        //     Catacombs:          2x Health

        this.loadDamageMultiplier(member);
        this.loadSkills(member);
        this.loadSlayers(member);
        this.loadDungeons(member);
        this.loadAccessories(member);
        this.loadArmor(member);
        this.loadActivePet(member);
        this.loadActivePotions(member);
        this.loadPetScore(member);
        this.loadMiningCore(member);
        this.loadCenturyCakes(member);
        this.loadEssencePerks(member);
        this.loadFairySouls(member);
        this.loadMelodysHarp(member);
        this.loadJacobsPerks(member);
    }

    public PlayerStats calculateBonusStats() {
        return new PlayerStats(this);
    }

    private PlayerStats(PlayerStats playerStats) {
        // Load Previous Data
        this.damageMultiplier = playerStats.getDamageMultiplier();
        this.expressionVariables.putAll(playerStats.getExpressionVariables());
        this.stats.putAll(playerStats.getStats());
        this.accessories.putAll(playerStats.getAccessories());
        this.armor.putAll(playerStats.getArmor());
        this.bonusPetAbilityStatModels.addAll(playerStats.getBonusPetAbilityStatModels());
        this.bonusCalculated = playerStats.isBonusCalculated();

        // Calculate Bonus Modifiers
        if (!this.isBonusCalculated()) {
            // --- Load Bonus Accessory Item Stats ---
            this.getAccessories().forEach(((accessoryModel, accessoryData) -> {
                // Handle Bonus Reforge Stats
                accessoryData.getBonusReforgeStatModel()
                    .ifPresent(bonusReforgeStatModel -> accessoryData.getStats(AccessoryData.Type.REFORGES)
                        .forEach((statModel, statData) -> statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), accessoryData.getCompoundTag(), this.getExpressionVariables(), bonusReforgeStatModel)));

                // Handle Bonus Item Stats
                accessoryData.getBonusItemStatModel()
                    .ifPresent(bonusItemStatModel -> {
                        // Handle Bonus Gemstone Stats
                        if (bonusItemStatModel.isForGems()) {
                            accessoryData.getStats(AccessoryData.Type.GEMSTONES)
                                .forEach((statModel, statData) -> statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), accessoryData.getCompoundTag(), this.getExpressionVariables(), bonusItemStatModel));
                        }

                        // Handle Bonus Reforge Stats
                        if (bonusItemStatModel.isForReforges()) {
                            accessoryData.getStats(AccessoryData.Type.REFORGES)
                                .forEach((statModel, statData) -> statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), accessoryData.getCompoundTag(), this.getExpressionVariables(), bonusItemStatModel));
                        }

                        // Handle Bonus Stats
                        if (bonusItemStatModel.isForStats()) {
                            accessoryData.getStats(AccessoryData.Type.STATS)
                                .forEach((statModel, statData) -> statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), accessoryData.getCompoundTag(), this.getExpressionVariables(), bonusItemStatModel));
                        }
                    });
            }));

            // --- Load Bonus Armor Stats ---
            this.getArmor().forEach((itemModel, itemData) -> {
                // Handle Reforges
                itemData.getBonusReforgeStatModel()
                    .ifPresent(bonusReforgeStatModel -> itemData.getStats(ItemData.Type.REFORGES)
                        .forEach((statModel, statData) -> {
                            statData.base = this.handleBonusEffects(statModel, statData.getBase(), itemData.getCompoundTag(), this.getExpressionVariables(), bonusReforgeStatModel);
                            statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), itemData.getCompoundTag(), this.getExpressionVariables(), bonusReforgeStatModel);
                        }));
            });

            // --- Load Armor Multiplier Enchantments ---
            this.getArmor().forEach((itemModel, itemData) -> itemData.getEnchantments().forEach((enchantmentModel, value) -> itemData.getEnchantmentStats().get(enchantmentModel)
                .stream()
                .filter(enchantmentStatModel -> enchantmentStatModel.getStat() != null)
                .filter(EnchantmentStatModel::isPercentage)
                .forEach(enchantmentStatModel -> {
                    double enchantMultiplier = 1 + (enchantmentStatModel.getBaseValue() / 100.0) + ((enchantmentStatModel.getLevelBonus() * value) / 100.0);

                    this.stats.forEach((type, statEntries) -> {
                        Data statModel = statEntries.get(enchantmentStatModel.getStat());

                        // Apply Multiplier
                        statModel.base = statModel.base * enchantMultiplier;
                        statModel.bonus = statModel.bonus * enchantMultiplier;
                    });
                })));

            // --- Load Bonus Armor Item Stats ---
            this.getArmor().forEach((itemModel, itemData) -> itemData.getBonusItemStatModel()
                .ifPresent(bonusItemStatModel -> {
                    // Handle Stats
                    this.stats.forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                        statData.base = this.handleBonusEffects(statModel, statData.getBase(), itemData.getCompoundTag(), this.getExpressionVariables(), bonusItemStatModel);
                        statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), itemData.getCompoundTag(), this.getExpressionVariables(), bonusItemStatModel);
                    }));

                    // Handle Armor
                    this.armor.forEach((itemModel2, itemData2) -> itemData2.getStats().forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                        statData.base = this.handleBonusEffects(statModel, statData.getBase(), itemData.getCompoundTag(), this.getExpressionVariables(), bonusItemStatModel);
                        statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), itemData.getCompoundTag(), this.getExpressionVariables(), bonusItemStatModel);
                    })));

                    // Handle Accessories
                    this.accessories.forEach((accessoryModel, accessoryData) -> accessoryData.getStats().forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                        statData.base = this.handleBonusEffects(statModel, statData.getBase(), itemData.getCompoundTag(), this.getExpressionVariables(), bonusItemStatModel);
                        statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), itemData.getCompoundTag(), this.getExpressionVariables(), bonusItemStatModel);
                    })));
                }));

            // --- Load Bonus Pet Item Stats ---
            this.getBonusPetAbilityStatModels().forEach(bonusPetAbilityStatModel -> {
                // Override Pet Variables
                this.expressionVariables.put("PET_ABILITY_VALUE", this.getExpressionVariables().getOrDefault(FormatUtil.format("PET_ABILITY_{0}_VALUE", bonusPetAbilityStatModel.getPetAbility().getKey()), 0.0));
                statRepository.findAll().forEach(statModel -> {
                    String newVariableName = FormatUtil.format("PET_ABILITY_{0}", statModel.getKey());
                    String currentVariableName = FormatUtil.format("PET_ABILITY_{0}_{1}", bonusPetAbilityStatModel.getPetAbility().getKey(), statModel.getKey());
                    this.expressionVariables.put(newVariableName, this.getExpressionVariables().getOrDefault(currentVariableName, 0.0));
                });

                // Handle Stats
                this.stats.forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                    statData.base = this.handleBonusEffects(statModel, statData.getBase(), null, this.getExpressionVariables(), bonusPetAbilityStatModel);
                    statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), null, this.getExpressionVariables(), bonusPetAbilityStatModel);
                }));

                // Handle Armor
                this.armor.forEach((itemModel, itemData) -> itemData.getStats().forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                    statData.base = this.handleBonusEffects(statModel, statData.getBase(), itemData.getCompoundTag(), this.getExpressionVariables(), bonusPetAbilityStatModel);
                    statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), itemData.getCompoundTag(), this.getExpressionVariables(), bonusPetAbilityStatModel);
                })));

                // Handle Accessories
                this.accessories.forEach((accessoryModel, accessoryData) -> accessoryData.getStats().forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                    statData.base = this.handleBonusEffects(statModel, statData.getBase(), accessoryData.getCompoundTag(), this.getExpressionVariables(), bonusPetAbilityStatModel);
                    statData.bonus = this.handleBonusEffects(statModel, statData.getBonus(), accessoryData.getCompoundTag(), this.getExpressionVariables(), bonusPetAbilityStatModel);
                })));
            });
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private double handleBonusEffects(StatModel statModel, double currentTotal, CompoundTag compoundTag, Map<String, Double> variables, BuffEffectsModel... bonusEffectsModels) {
        MutableDouble value = new MutableDouble(currentTotal);
        ConcurrentList<StatModel> statModels = statRepository.findAll();

        // Handle Bonus Stats
        for (BuffEffectsModel bonusBuffEffectModel : bonusEffectsModels) {
            value.add((double) bonusBuffEffectModel.getEffect(statModel.getKey(), 0.0));

            bonusBuffEffectModel.getBuffEffects().forEach((buffKey, buffValue) -> {
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
                                        .variable("CURRENT_VALUE")
                                        .variable("PET_LEVEL")
                                        .variable("SKILL_AVERAGE")
                                        .variable("PET_ABILITY_VALUE")
                                        .variables(statModels.stream().map(statModelX -> FormatUtil.format("{0}", statModelX.getKey())).collect(Concurrent.toSet()))
                                        .variables(statModels.stream().map(statModelX -> FormatUtil.format("SKILL_LEVEL_{0}", statModelX.getKey())).collect(Concurrent.toSet()))
                                        .variables(SimplifiedApi.getRepositoryOf(DungeonModel.class).findAll().stream().map(dungeonModelX -> FormatUtil.format("DUNGEON_LEVEL_{0}", dungeonModelX.getKey())).collect(Concurrent.toSet()))
                                        .variables(statModels.stream().map(statModelX -> FormatUtil.format("PET_ABILITY_{0}", statModelX.getKey())).collect(Concurrent.toSet()))
                                        .build();

                                    expression.setVariables(variables);
                                    expression.setVariable("CURRENT_VALUE", currentTotal);
                                    double newValue = expression.evaluate();
                                    value.set(newValue);
                                }
                            }
                        }
                    }
                }
            });
        }

        return value.get();
    }

    public ConcurrentLinkedMap<StatModel, Data> getAllStats() {
        // Initialize
        ConcurrentList<StatModel> statModels = statRepository.findAll();
        statModels.sort((s1, s2) -> Comparator.comparing(StatModel::getOrdinal).compare(s1, s2));
        ConcurrentLinkedMap<StatModel, Data> totalStats = Concurrent.newLinkedMap();
        statModels.forEach(statModel -> totalStats.put(statModel, new Data()));

        // Collect Stat Data
        Arrays.stream(PlayerStats.Type.values()).forEach(type -> this.stats.get(type).forEach(((statModel, statData) -> {
            Data totalData = totalStats.get(statModel);
            totalData.addBase(statData.getBase());
            totalData.addBonus(statData.getBonus());
        })));

        // Collect Accessory Data
        this.getAccessories().forEach((accessoryModel, accessoryData) -> accessoryData.getAllStats()
            .forEach((statModel, statData) -> {
                Data totalData = totalStats.get(statModel);
                totalData.addBase(statData.getBase());
                totalData.addBonus(statData.getBonus());
            }));

        // Collect Armor Data
        this.getArmor().forEach((itemModel, itemData) -> itemData.getAllStats()
            .forEach((statModel, statData) -> {
                Data totalData = totalStats.get(statModel);
                totalData.addBase(statData.getBase());
                totalData.addBonus(statData.getBonus());
            }));

        return totalStats;
    }

    public Data getAllData(String statName) {
        return this.getAllData(SimplifiedApi.getRepositoryOf(StatModel.class).findFirstOrNull(StatModel::getKey, statName.toUpperCase()));
    }

    public Data getAllData(StatModel statModel) {
        Data statData = new Data();

        for (Map.Entry<Type, ConcurrentLinkedMap<StatModel, Data>> newStat : this.stats) {
            Data statModelData = this.getData(statModel, newStat.getKey());
            statData.addBase(statModelData.getBase());
            statData.addBonus(statModelData.getBonus());
        }

        return statData;
    }

    public Data getData(String statName, Type... types) {
        return this.getData(SimplifiedApi.getRepositoryOf(StatModel.class).findFirstOrNull(StatModel::getKey, statName.toUpperCase()), types);
    }

    public Data getData(StatModel statModel, Type... types) {
        return this.getStatsOf(types).get(statModel);
    }

    public ConcurrentLinkedMap<StatModel, Data> getStatsOf(Type... types) {
        ConcurrentLinkedMap<StatModel, Data> totalStats = Concurrent.newLinkedMap();
        statRepository.findAll().forEach(statModel -> totalStats.put(statModel, new Data()));

        // Collect Stat Data
        Arrays.stream(types).forEach(type -> this.stats.get(type).forEach(((statModel, data) -> {
            Data statData = totalStats.get(statModel);
            statData.addBase(data.getBase());
            statData.addBonus(data.getBonus());
        })));

        return totalStats;
    }

    private void loadAccessories(SkyBlockIsland.Member member) {
        try {
            // --- Load Accessories ---
            ConcurrentMap<AccessoryModel, CompoundTag> accessoryTagStatModels = Concurrent.newMap();
            ConcurrentMap<AccessoryModel, CompoundTag> accessoryTagReforgeModels = Concurrent.newMap();
            ConcurrentMap<AccessoryFamilyModel, ConcurrentList<AccessoryModel>> familyAccessoryTagModels = Concurrent.newMap();

            // Load From Inventory
            if (member.hasStorage(SkyBlockIsland.Storage.INVENTORY)) {
                member.getStorage(SkyBlockIsland.Storage.INVENTORY)
                    .getNbtData()
                    .<CompoundTag>getList("i")
                    .stream()
                    .filter(CompoundTag::notEmpty)
                    .forEach(itemTag -> {
                        String itemId = itemTag.<StringTag>getPath("tag.ExtraAttributes.id").getValue();

                        SimplifiedApi.getRepositoryOf(AccessoryModel.class)
                            .findFirst(FilterFunction.combine(AccessoryModel::getItem, ItemModel::getItemId), itemId)
                            .ifPresent(accessoryModel -> {
                                accessoryTagStatModels.putIfAbsent(accessoryModel, itemTag);
                                accessoryTagReforgeModels.putIfAbsent(accessoryModel, itemTag);

                                if (accessoryModel.getFamily() != null) {
                                    // New Accessory Family
                                    if (!familyAccessoryTagModels.containsKey(accessoryModel.getFamily()))
                                        familyAccessoryTagModels.put(accessoryModel.getFamily(), Concurrent.newList());

                                    // Store Accessory
                                    familyAccessoryTagModels.get(accessoryModel.getFamily()).add(accessoryModel);
                                }
                            });
                    });
            }

            // Load From Accessory Bag
            if (member.hasStorage(SkyBlockIsland.Storage.ACCESSORIES)) {
                member.getStorage(SkyBlockIsland.Storage.ACCESSORIES)
                    .getNbtData()
                    .<CompoundTag>getList("i")
                    .stream()
                    .filter(CompoundTag::notEmpty)
                    .forEach(itemTag -> {
                        String itemId = itemTag.<StringTag>getPath("tag.ExtraAttributes.id").getValue();

                        SimplifiedApi.getRepositoryOf(AccessoryModel.class)
                            .findFirst(FilterFunction.combine(AccessoryModel::getItem, ItemModel::getItemId), itemId)
                            .ifPresent(accessoryModel -> {
                                accessoryTagStatModels.putIfAbsent(accessoryModel, itemTag);
                                accessoryTagReforgeModels.putIfAbsent(accessoryModel, itemTag);

                                if (accessoryModel.getFamily() != null) {
                                    // New Accessory Family
                                    if (!familyAccessoryTagModels.containsKey(accessoryModel.getFamily()))
                                        familyAccessoryTagModels.put(accessoryModel.getFamily(), Concurrent.newList());

                                    // Store Accessory
                                    familyAccessoryTagModels.get(accessoryModel.getFamily()).add(accessoryModel);
                                }
                            });
                    });
            }

            // Non-Stackable Families
            familyAccessoryTagModels.forEach((accessoryFamilyModel, accessories) -> {
                // Sort By Highest
                accessories.sort((a1, a2) -> Comparator.comparing(AccessoryModel::getFamilyRank).compare(a2, a1));

                if (!accessoryFamilyModel.isStatsStackable()) {
                    accessories.remove(0); // Keep First Accessory
                    accessories.forEach(accessoryTagStatModels::remove); // Remove Remaining Accessories
                } else if (!accessoryFamilyModel.isReforgesStackable()) {
                    accessories.remove(0); // Keep First Accessory
                    accessories.forEach(accessoryTagReforgeModels::remove); // Remove Remaining Accessories
                }
            });

            // Handle Accessories
            accessoryTagStatModels.putAll(accessoryTagReforgeModels);
            accessoryTagStatModels.forEach((accessoryModel, itemTag) -> {
                int rarityUpgrades = itemTag.getPathOrDefault("tag.ExtraAttributes.rarity_upgrades", IntTag.EMPTY).getValue();

                // Load Rarity
                SimplifiedApi.getRepositoryOf(RarityModel.class)
                    .findFirst(RarityModel::getOrdinal, accessoryModel.getRarity().getOrdinal() + rarityUpgrades)
                    .ifPresent(rarityModel -> {
                        this.accessories.put(accessoryModel, new AccessoryData(itemTag, rarityModel));

                        // Store Bonus Item Stat Model
                        SimplifiedApi.getRepositoryOf(BonusItemStatModel.class)
                            .findFirst(BonusItemStatModel::getItem, accessoryModel.getItem())
                            .ifPresent(bonusItemStatModel -> this.getAccessories().get(accessoryModel).setBonusItemStatModel(bonusItemStatModel));

                        // Handle Gemstone Stats
                        this.handleGemstoneBonus(itemTag, rarityModel)
                            .forEach((statModel, value) -> this.getAccessories().get(accessoryModel).getStats(AccessoryData.Type.GEMSTONES).get(statModel).addBonus(value));

                        // Handle Reforge Stats
                        this.handleReforgeBonus(this.getAccessories().get(accessoryModel).getReforgeStat())
                            .forEach((statModel, value) -> this.getAccessories().get(accessoryModel).getStats(AccessoryData.Type.REFORGES).get(statModel).addBonus(value));

                        // Load Stats
                        accessoryModel.getEffects()
                            .forEach((key, value) -> statRepository.findFirst(StatModel::getKey, key)
                                .ifPresent(statModel -> this.getAccessories().get(accessoryModel).getStats(AccessoryData.Type.STATS).get(statModel).addBonus(value)));

                        // Handle Enrichment Stats
                        if (itemTag.containsPath("tag.ExtraAttributes.talisman_enrichment")) {
                            SimplifiedApi.getRepositoryOf(AccessoryEnrichmentModel.class)
                                .findFirst(
                                    FilterFunction.combine(AccessoryEnrichmentModel::getStat, StatModel::getKey),
                                    itemTag.<StringTag>getPath("tag.ExtraAttributes.talisman_enrichment").getValue()
                                ).ifPresent(accessoryEnrichmentModel -> {
                                    // Save Enrichment Stat
                                    StatModel statModel = accessoryEnrichmentModel.getStat();
                                    this.getAccessories().get(accessoryModel).getStats(AccessoryData.Type.ENRICHMENTS).get(statModel).addBonus(accessoryEnrichmentModel.getValue());
                                });
                        }

                        // New Year Cake Bag
                        if ("NEW_YEAR_CAKE_BAG".equals(accessoryModel.getItem().getItemId())) {
                            try {
                                Byte[] nbtCakeBag = itemTag.<ByteArrayTag>getPath("tag.ExtraAttributes.new_year_cake_bag_data").getValue();
                                ListTag<CompoundTag> cakeBagItems = SimplifiedApi.getNbtFactory().fromByteArray(nbtCakeBag).getList("i");
                                statRepository.findFirst(StatModel::getKey, "HEALTH")
                                    .ifPresent(statModel -> this.getAccessories().get(accessoryModel).getStats(AccessoryData.Type.CAKE_BAG).get(statModel).addBonus(cakeBagItems.size()));
                            } catch (IOException ignore) { }
                        }
                    });
            });
        } catch (IOException ioException) {
            throw SimplifiedException.wrapNative(ioException)
                .withMessage("Unable to read accessory nbt data!")
                .build();
        }
    }

    private void loadArmor(SkyBlockIsland.Member member) {
        try {
            // --- Load Armor ---
            if (member.hasStorage(SkyBlockIsland.Storage.ARMOR)) {
                member.getStorage(SkyBlockIsland.Storage.ARMOR)
                    .getNbtData()
                    .<CompoundTag>getList("i")
                    .stream()
                    .filter(CompoundTag::notEmpty)
                    .forEach(itemTag -> SimplifiedApi.getRepositoryOf(ItemModel.class)
                        .findFirst(ItemModel::getItemId, itemTag.<StringTag>getPath("tag.ExtraAttributes.id").getValue())
                        .ifPresent(itemModel -> SimplifiedApi.getRepositoryOf(RarityModel.class)
                            .findFirst(RarityModel::getOrdinal, itemModel.getRarity().getOrdinal() + itemTag.getPathOrDefault("tag.ExtraAttributes.rarity_upgrades", IntTag.EMPTY).getValue())
                            .ifPresent(rarityModel -> {
                                this.armor.put(itemModel, new ItemData(itemTag, rarityModel));

                                // Store Bonus Item Stat Model
                                SimplifiedApi.getRepositoryOf(BonusItemStatModel.class)
                                    .findFirst(BonusItemStatModel::getItem, itemModel)
                                    .ifPresent(bonusItemStatModel -> this.armor.get(itemModel).setBonusItemStatModel(bonusItemStatModel));

                                // Save Stats
                                itemModel.getStats().forEach((key, value) -> statRepository.findFirst(StatModel::getKey, key)
                                    .ifPresent(statModel -> this.armor.get(itemModel).getStats(ItemData.Type.STATS).get(statModel).addBonus(value)));

                                // Handle Enchantment Stats
                                if (itemTag.containsPath("tag.ExtraAttributes.enchantments")) {
                                    CompoundTag enchantments = itemTag.getPath("tag.ExtraAttributes.enchantments");

                                    enchantments.entrySet()
                                        .stream()
                                        .map(entry -> Pair.of(entry.getKey().toUpperCase(), ((IntTag)entry.getValue()).getValue()))
                                        .forEach(pair -> SimplifiedApi.getRepositoryOf(EnchantmentModel.class)
                                            .findFirst(EnchantmentModel::getKey, pair.getKey())
                                            .ifPresent(enchantmentModel -> this.armor.get(itemModel).addEnchantment(enchantmentModel, pair.getValue())));
                                }

                                // Save Reforge Stats
                                this.handleReforgeBonus(this.armor.get(itemModel).getReforgeStat())
                                    .forEach((statModel, value) -> this.armor.get(itemModel).getStats(ItemData.Type.REFORGES).get(statModel).addBonus(value));

                                // Save Gemstone Stats
                                this.handleGemstoneBonus(itemTag, rarityModel)
                                    .forEach((statModel, value) -> this.armor.get(itemModel).getStats(ItemData.Type.GEMSTONES).get(statModel).addBonus(value));
                            })));
            }
        } catch (IOException ioException) {
            throw SimplifiedException.wrapNative(ioException)
                .withMessage("Unable to read armor nbt data!")
                .build();
        }
    }

    private void loadActivePet(SkyBlockIsland.Member member) {
        // --- Load Active Pet ---
        member.getActivePet().ifPresent(petInfo -> petInfo.getPet().ifPresent(pet -> {
            // Get Rarity Filtered Pet Stats
            ConcurrentList<PetStatModel> petStatModels = SimplifiedApi.getRepositoryOf(PetStatModel.class)
                .findAll(PetStatModel::getPet, pet)
                .stream()
                .filter(petStatModel -> petStatModel.getRarities().contains(petInfo.getRarity().getOrdinal()))
                .collect(Concurrent.toList());

            // Get Rarity Filtered Ability Stats
            ConcurrentMap<PetAbilityModel, ConcurrentList<PetAbilityStatModel>> petAbilityStatMap = Concurrent.newMap();
            SimplifiedApi.getRepositoryOf(PetAbilityModel.class)
                .findAll(PetAbilityModel::getPet, pet)
                .parallelStream()
                .forEach(petAbilityModel -> {
                    petAbilityStatMap.put(petAbilityModel, Concurrent.newList());
                    ConcurrentList<PetAbilityStatModel> petAbilityStatModels = petAbilityStatMap.get(petAbilityModel);

                    SimplifiedApi.getRepositoryOf(PetAbilityStatModel.class)
                        .findAll(PetAbilityStatModel::getAbility, petAbilityModel)
                        .parallelStream()
                        .filter(petAbilityStatModel -> petAbilityStatModel.getRarities().contains(petInfo.getRarity().getOrdinal()))
                        .forEach(petAbilityStatModels::add);
                });

            // Save Active Pet Stats
            petStatModels.forEach(petStatModel -> {
                Data statData = this.stats.get(Type.ACTIVE_PET).get(petStatModel.getStat());
                statData.addBonus(petStatModel.getBaseValue() + (petStatModel.getLevelBonus() * petInfo.getLevel()));
            });

            // Handle Ability Stats
            petAbilityStatMap.forEach((petAbilityModel, petAbilityStatModels) -> {
                // Load Bonus Pet Ability Stats
                SimplifiedApi.getRepositoryOf(BonusPetAbilityStatModel.class)
                    .findFirst(BonusPetAbilityStatModel::getPetAbility, petAbilityModel)
                    .ifPresent(this.bonusPetAbilityStatModels::add);

                petAbilityStatModels.forEach(petAbilityStatModel -> {
                    double abilityValue = petAbilityStatModel.getBaseValue() + (petAbilityStatModel.getLevelBonus() * petInfo.getLevel());

                    // Save Ability Stat
                    if (petAbilityStatModel.getStat() != null) {
                        Data statData = this.stats.get(Type.ACTIVE_PET).get(petAbilityStatModel.getStat());
                        statData.addBonus(abilityValue);
                    }

                    // Store Bonus Pet Ability
                    String statKey = (petAbilityStatModel.getStat() == null ? "VALUE" : petAbilityStatModel.getStat().getKey());
                    this.expressionVariables.put(FormatUtil.format("PET_ABILITY_{0}_{1}", petAbilityModel.getKey(), statKey), abilityValue);
                });
            });
        }));
    }

    @SuppressWarnings("unchecked")
    private void loadActivePotions(SkyBlockIsland.Member member) {
        // --- Load Active Potions ---
        member.getActivePotions()
            .stream()
            .filter(potion -> !member.getDisabledPotions().contains(potion.getEffect()))
            .forEach(potion -> {
                Optional<PotionModel> optionalPotionModel = SimplifiedApi.getRepositoryOf(PotionModel.class).findFirst(PotionModel::getKey, potion.getEffect().toUpperCase());
                ConcurrentMap<StatModel, Double> potionStatEffects = Concurrent.newMap();
                ConcurrentMap<String, Double> potionBuffEffects = Concurrent.newMap();

                // Load Potion
                optionalPotionModel.ifPresent(potionModel -> {
                    ConcurrentList<PotionTierModel> potionTierModels = SimplifiedApi.getRepositoryOf(PotionTierModel.class)
                        .findAll(PotionTierModel::getPotion, potionModel)
                        .parallelStream()
                        .filter(potionTierModel -> potionTierModel.getTier() == potion.getLevel())
                        .collect(Concurrent.toList());

                    // Load Stats
                    potionTierModels.forEach(potionTierModel -> {
                        potionTierModel.getEffects().forEach((key, value) -> statRepository.findFirst(StatModel::getKey, key)
                            .ifPresent(statModel -> potionStatEffects.put(statModel, potionTierModel.getEffect(key) + potionStatEffects.getOrDefault(statModel, 0.0))));

                        potionTierModel.getBuffEffects().forEach((key, value) -> potionBuffEffects.put(key, value + potionBuffEffects.getOrDefault(key, 0.0)));
                    });
                });

                // Handle Potion Brews
                potion.getModifiers().forEach(modifier -> SimplifiedApi.getRepositoryOf(PotionBrewModel.class).findFirst(
                    Pair.of(PotionBrewModel::getKey, modifier.getKey().toUpperCase()),
                    Pair.of(PotionBrewModel::getAmplified, modifier.getAmplifier())
                ).ifPresent(potionBrewModel -> SimplifiedApi.getRepositoryOf(PotionBrewBuffModel.class)
                    .findAll(PotionBrewBuffModel::getPotionBrew, potionBrewModel)
                    .forEach(potionBrewBuffModel -> {
                        final MutableBoolean isBuff = new MutableBoolean(true);

                        // Stats
                        statRepository.findFirst(StatModel::getKey, potionBrewBuffModel.getBuffKey())
                            .ifPresent(statModel -> {
                                if (statModel.getKey().equals(potionBrewBuffModel.getBuffKey())) {
                                    isBuff.setFalse();
                                    double existingValue = potionStatEffects.getOrDefault(statModel, 0.0);

                                    if (potionBrewBuffModel.isPercentage())
                                        potionStatEffects.put(statModel, existingValue * (1 + potionBrewBuffModel.getBuffValue() / 100.0));
                                    else
                                        potionStatEffects.put(statModel, existingValue + potionBrewBuffModel.getBuffValue());
                                }
                            });

                        // Buffs
                        if (isBuff.get()) {
                            double existingValue = potionBuffEffects.getOrDefault(potionBrewBuffModel.getBuffKey(), 0.0);

                            if (potionBrewBuffModel.isPercentage())
                                potionBuffEffects.put(potionBrewBuffModel.getBuffKey(), (potionBrewBuffModel.getBuffValue() * (1 + potionBrewBuffModel.getBuffValue() / 100.0)) + existingValue);
                            else
                                potionBuffEffects.put(potionBrewBuffModel.getBuffKey(), (potionBrewBuffModel.getBuffValue() + potionBrewBuffModel.getBuffValue() + existingValue));
                        }
                    })));

                // Save Active Potions
                potionStatEffects.forEach((statModel, value) -> {
                    Data statData = this.stats.get(Type.ACTIVE_POTIONS).get(statModel);
                    statData.addBonus(value);
                });
            });
    }

    private void loadDamageMultiplier(SkyBlockIsland.Member member) {
        // --- Load Damage Multiplier ---
        SimplifiedApi.getRepositoryOf(SkillModel.class)
            .findFirst(SkillModel::getKey, "COMBAT")
            .ifPresent(skillModel -> {
                int skillLevel = member.getSkill(skillModel).getLevel();

                if (skillLevel > 0) {
                    SimplifiedApi.getRepositoryOf(SkillLevelModel.class)
                        .findAll(SkillLevelModel::getSkill, skillModel)
                        .subList(0, skillLevel)
                        .stream()
                        .map(SkillLevelModel::getBuffEffects)
                        .flatMap(map -> map.entrySet().stream())
                        .forEach(entry -> this.damageMultiplier += (double) entry.getValue());
                }
            });
    }

    private void loadSkills(SkyBlockIsland.Member member) {
        // --- Load Skills ---
        ConcurrentMap<StatModel, Double> skillBonuses = Concurrent.newMap();
        SimplifiedApi.getRepositoryOf(SkillModel.class)
            .findAll()
            .forEach(skillModel -> {
                int skillLevel = member.getSkill(skillModel).getLevel();

                if (skillLevel > 0) {
                    SimplifiedApi.getRepositoryOf(SkillLevelModel.class)
                        .findAll(SkillLevelModel::getSkill, skillModel)
                        .subList(0, skillLevel)
                        .stream()
                        .map(SkillLevelModel::getEffects)
                        .flatMap(map -> map.entrySet().stream())
                        .forEach(entry -> {
                            Optional<StatModel> optionalStatModel = statRepository.findFirst(StatModel::getKey, entry.getKey());
                            optionalStatModel.ifPresent(statModel -> skillBonuses.put(statModel, entry.getValue() + skillBonuses.getOrDefault(statModel, 0.0)));
                        });
                }
            });

        // Save Skills
        skillBonuses.forEach(statBonus -> {
            Data statData = this.stats.get(Type.SKILLS).get(statBonus.getKey());
            statData.addBase(statBonus.getValue());
        });
    }

    private void loadSlayers(SkyBlockIsland.Member member) {
        // --- Load Slayers ---
        ConcurrentMap<StatModel, Double> slayerBonuses = Concurrent.newMap();
        SimplifiedApi.getRepositoryOf(SlayerModel.class)
            .findAll()
            .forEach(slayerModel -> {
                int slayerLevel = member.getSlayer(slayerModel).getLevel();

                if (slayerLevel > 0) {
                    SimplifiedApi.getRepositoryOf(SlayerLevelModel.class)
                        .findAll(SlayerLevelModel::getSlayer, slayerModel)
                        .subList(0, slayerLevel)
                        .stream()
                        .map(SlayerLevelModel::getEffects)
                        .flatMap(map -> map.entrySet().stream())
                        .forEach(entry -> {
                            Optional<StatModel> optionalStatModel = statRepository.findFirst(StatModel::getKey, entry.getKey());
                            optionalStatModel.ifPresent(statModel -> slayerBonuses.put(statModel, entry.getValue() + slayerBonuses.getOrDefault(statModel, 0.0)));
                        });
                }
            });

        // Save Slayers
        slayerBonuses.forEach(statBonus -> {
            Data statData = this.stats.get(Type.SLAYERS).get(statBonus.getKey());
            statData.addBase(statBonus.getValue());
        });
    }

    private void loadDungeons(SkyBlockIsland.Member member) {
        // --- Load Dungeons ---
        ConcurrentMap<StatModel, Double> dungeonBonuses = Concurrent.newMap();
        SimplifiedApi.getRepositoryOf(DungeonModel.class)
            .findAll()
            .forEach(dungeonModel -> member.getDungeons()
                .getDungeon(dungeonModel)
                .ifPresent(dungeon -> {
                    int dungeonLevel = dungeon.getLevel();

                    if (dungeonLevel > 0) {
                        SimplifiedApi.getRepositoryOf(DungeonLevelModel.class)
                            .findAll()
                            .subList(0, dungeonLevel)
                            .stream()
                            .map(DungeonLevelModel::getEffects)
                            .flatMap(map -> map.entrySet().stream())
                            .forEach(entry -> {
                                Optional<StatModel> optionalStatModel = statRepository.findFirst(StatModel::getKey, entry.getKey());
                                optionalStatModel.ifPresent(statModel -> dungeonBonuses.put(statModel, entry.getValue() + dungeonBonuses.getOrDefault(statModel, 0.0)));
                            });
                    }
                }));

        // Save Dungeons
        dungeonBonuses.forEach(statBonus -> {
            Data statData = this.stats.get(Type.DUNGEONS).get(statBonus.getKey());
            statData.addBase(statBonus.getValue());
        });
    }

    private void loadPetScore(SkyBlockIsland.Member member) {
        // --- Load Pet Score ---
        int petScore = member.getPetScore();
        ConcurrentList<PetScoreModel> petScoreModels = SimplifiedApi.getRepositoryOf(PetScoreModel.class)
            .findAll()
            .stream()
            .filter(petScoreModel -> petScore >= petScoreModel.getBreakpoint())
            .collect(Concurrent.toList());

        // Save Pet Score
        if (ListUtil.notEmpty(petScoreModels))
            statRepository.findFirst(StatModel::getKey, "MAGIC_FIND").ifPresent(magicFindStatModel -> this.stats.get(Type.PET_SCORE).get(magicFindStatModel).addBase(petScoreModels.size()));
    }

    private void loadMiningCore(SkyBlockIsland.Member member) {
        // --- Load Mining Core ---
        member.getMining().getNodes().forEach((key, level) -> {
            // Load Hotm Perk
            SimplifiedApi.getRepositoryOf(HotmPerkModel.class).findFirst(HotmPerkModel::getKey, key.toUpperCase())
                .ifPresent(hotmPerkModel -> {
                    // Save Perk Stats
                    SimplifiedApi.getRepositoryOf(HotmPerkStatModel.class)
                        .findAll(HotmPerkStatModel::getPerk, hotmPerkModel)
                        .forEach(hotmPerkStatModel -> this.stats.get(Type.MINING_CORE).get(hotmPerkStatModel.getStat()).addBonus(level * hotmPerkModel.getLevelBonus()));
                });
        });
    }

    private void loadCenturyCakes(SkyBlockIsland.Member member) {
        // --- Load Century Cakes ---
        member.getCenturyCakes()
            .stream()
            .filter(centuryCake -> centuryCake.getExpiresAt().getRealTime() > System.currentTimeMillis())
            .forEach(centuryCake -> {
                // Save Century Cake
                Data statData = this.stats.get(Type.CENTURY_CAKES).get(centuryCake.getStat());
                statData.addBonus(centuryCake.getAmount());
            });
    }

    private void loadJacobsPerks(SkyBlockIsland.Member member) {
        // --- Load Jacobs Perks ---
        if (member.getJacobsFarming() != null) {
            statRepository.findFirst(StatModel::getKey, "FARMING_FORTUNE")
                .ifPresent(farmingFortuneStatModel -> {
                    // Save Jacobs Perks
                    double farmingDrops = member.getJacobsFarming().getPerk(SkyBlockIsland.JacobsFarming.Perk.DOUBLE_DROPS);
                    this.stats.get(Type.JACOBS_FARMING).get(farmingFortuneStatModel).addBase(farmingDrops * 2.0);
                });
        }
    }

    private void loadMelodysHarp(SkyBlockIsland.Member member) {
        // --- Load Melody's Harp ---
        member.getMelodyHarp().getSongs().forEach((songName, songData) -> {
            // Load Song
            SimplifiedApi.getRepositoryOf(MelodySongModel.class)
                .findFirst(MelodySongModel::getKey, songName.toUpperCase())
                .ifPresent(melodySongModel -> {
                    // Load Intelligence Stat
                    statRepository.findFirst(StatModel::getKey, "INTELLIGENCE")
                        .ifPresent(statModel -> this.stats.get(Type.MELODYS_HARP).get(statModel).addBonus(melodySongModel.getReward()));
                });
        });
    }

    private void loadFairySouls(SkyBlockIsland.Member member) {
        // --- Load Fairy Souls ---
        SimplifiedApi.getRepositoryOf(FairyExchangeModel.class)
            .findAll()
            .stream()
            .filter(fairyExchangeModel -> fairyExchangeModel.getExchange() <= member.getFairyExchanges())
            .flatMap(fairyExchangeModel -> fairyExchangeModel.getEffects().entrySet().stream())
            .forEach(entry -> {
                // Save Fairy Souls
                statRepository.findFirst(StatModel::getKey, entry.getKey()).ifPresent(statModel -> this.stats.get(Type.FAIRY_SOULS).get(statModel).addBase(entry.getValue()));
            });
    }

    private void loadEssencePerks(SkyBlockIsland.Member member) {
        // --- Load Essence Perks ---
        member.getEssencePerks().forEach(entry -> SimplifiedApi.getRepositoryOf(EssencePerkModel.class)
            .findFirst(EssencePerkModel::getKey, entry.getKey().toUpperCase())
            .ifPresent(essencePerkModel -> {
                // Only Permanent Perks
                if (essencePerkModel.isPermanent()) {
                    // Save Essence Perk
                    Data statData = this.stats.get(Type.ESSENCE).get(essencePerkModel.getStat());
                    statData.addBonus(entry.getValue() * essencePerkModel.getLevelBonus());
                }
            }));
    }

    private ConcurrentMap<StatModel, Double> handleReforgeBonus(Optional<ReforgeStatModel> optionalReforgeStatModel) {
        ConcurrentMap<StatModel, Double> reforgeBonuses = Concurrent.newMap();

        // Load Reforge Stat Effects
        optionalReforgeStatModel.ifPresent(reforgeStatModel -> reforgeStatModel.getEffects()
            .forEach((key, value) -> statRepository.findFirst(StatModel::getKey, key)
                .ifPresent(statModel -> reforgeBonuses.put(statModel, value + reforgeBonuses.getOrDefault(statModel, 0.0)))));

        return reforgeBonuses;
    }

    @SuppressWarnings("unchecked")
    private ConcurrentMap<StatModel, Double> handleGemstoneBonus(CompoundTag compoundTag, RarityModel rarityModel) {
        ConcurrentMap<StatModel, Double> gemstoneAdjusted = Concurrent.newMap();
        CompoundTag gemTag = compoundTag.getPath("tag.ExtraAttributes.gems");

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
                optionalGemstoneModel.ifPresent(gemstoneModel -> SimplifiedApi.getRepositoryOf(GemstoneTypeModel.class)
                        .findFirst(GemstoneTypeModel::getKey, gemTypeKey.get())
                        .flatMap(gemstoneTypeModel -> SimplifiedApi.getRepositoryOf(GemstoneStatModel.class).findFirst(
                            Pair.of(GemstoneStatModel::getGemstone, gemstoneModel),
                            Pair.of(GemstoneStatModel::getType, gemstoneTypeModel),
                            Pair.of(GemstoneStatModel::getRarity, rarityModel)
                        ))
                        .ifPresent(gemstoneStatModel -> gemstoneAdjusted.put(gemstoneModel.getStat(), gemstoneStatModel.getValue() + gemstoneAdjusted.getOrDefault(gemstoneModel.getStat(), 0.0))));
            });
        }

        return gemstoneAdjusted;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Data {

        @Getter
        private double base;
        @Getter
        private double bonus;

        public Data() {
            this(0, 0);
        }

        public Data(Data data) {
            this(data.getBase(), data.getBonus());
        }

        private void addBase(double value) {
            this.base += value;
        }

        private void addBonus(double value) {
            this.bonus += value;
        }

        public final double getTotal() {
            return this.getBase() + this.getBonus();
        }

    }

    public enum Type {

        ACTIVE_PET,
        ACTIVE_POTIONS,
        BASE_STATS,
        CENTURY_CAKES,
        DUNGEONS,
        ESSENCE,
        FAIRY_SOULS,
        JACOBS_FARMING,
        MELODYS_HARP,
        MINING_CORE,
        PET_SCORE,
        SKILLS,
        SLAYERS

    }

    public static abstract class ObjectData<T> {

        @Getter
        protected final ConcurrentMap<T, ConcurrentLinkedMap<StatModel, Data>> stats = Concurrent.newMap();

        @Getter
        private final CompoundTag compoundTag;

        @Getter
        private final RarityModel rarityModel;

        @Getter
        private Optional<ReforgeModel> reforge = Optional.empty();

        @Getter
        private Optional<ReforgeStatModel> reforgeStat = Optional.empty();

        @Getter
        private Optional<BonusItemStatModel> bonusItemStatModel = Optional.empty();

        @Getter
        private Optional<BonusReforgeStatModel> bonusReforgeStatModel = Optional.empty();

        @SuppressWarnings("unchecked")
        protected ObjectData(CompoundTag compoundTag, RarityModel rarityModel) {
            this.compoundTag = compoundTag;
            this.rarityModel = rarityModel;

            // Initialize Stats
            Arrays.stream(this.getAllTypes()).forEach(type -> {
                this.stats.put(type, Concurrent.newLinkedMap());
                statRepository.findAll().forEach(statModel -> this.stats.get(type).put(statModel, new Data()));
            });

            // Load Reforge Model
            SimplifiedApi.getRepositoryOf(ReforgeModel.class)
                .findFirst(ReforgeModel::getKey, this.getCompoundTag().getPathOrDefault("tag.ExtraAttributes.modifier", StringTag.EMPTY).getValue().toUpperCase())
                .ifPresent(reforgeModel -> {
                    this.reforge = Optional.of(reforgeModel);

                    // Load Bonus Reforge Model
                    SimplifiedApi.getRepositoryOf(BonusReforgeStatModel.class)
                        .findFirst(BonusReforgeStatModel::getReforge, reforgeModel)
                        .ifPresent(bonusReforgeStatModel -> this.bonusReforgeStatModel = Optional.of(bonusReforgeStatModel));

                    // Load Reforge Stat Model
                    SimplifiedApi.getRepositoryOf(ReforgeStatModel.class)
                        .findFirst(
                            Pair.of(ReforgeStatModel::getReforge, reforgeModel),
                            Pair.of(ReforgeStatModel::getRarity, rarityModel)
                        ).ifPresent(reforgeStatModel -> this.reforgeStat = Optional.of(reforgeStatModel));
                });
        }

        public final Data getAllData(String statName) {
            return this.getAllData(SimplifiedApi.getRepositoryOf(StatModel.class).findFirstOrNull(StatModel::getKey, statName.toUpperCase()));
        }

        public final Data getAllData(StatModel statModel) {
            Data statData = new Data();

            for (Map.Entry<T, ConcurrentLinkedMap<StatModel, Data>> newStat : this.stats) {
                Data statModelData = this.getData(statModel, newStat.getKey());
                statData.addBase(statModelData.getBase());
                statData.addBonus(statModelData.getBonus());
            }

            return statData;
        }

        @SafeVarargs
        public final Data getData(String statName, T... types) {
            return this.getData(SimplifiedApi.getRepositoryOf(StatModel.class).findFirstOrNull(StatModel::getKey, statName.toUpperCase()), types);
        }

        @SafeVarargs
        public final Data getData(StatModel statModel, T... types) {
            return this.getStatsOf(types).get(statModel);
        }

        public final ConcurrentLinkedMap<StatModel, Data> getAllStats() {
            return this.getStatsOf(this.getAllTypes());
        }

        public final ConcurrentLinkedMap<StatModel, Data> getStats(T type) {
            return this.getStats().get(type);
        }

        @SafeVarargs
        public final ConcurrentLinkedMap<StatModel, Data> getStatsOf(T... types) {
            ConcurrentLinkedMap<StatModel, Data> totalStats = Concurrent.newLinkedMap();
            statRepository.findAll().forEach(statModel -> totalStats.put(statModel, new Data()));

            // Collect Stat Data
            Arrays.stream(types).forEach(type -> this.getStats(type).forEach(((statModel, data) -> {
                Data statData = totalStats.get(statModel);
                statData.addBase(data.getBase());
                statData.addBonus(data.getBonus());
            })));

            return totalStats;
        }

        protected abstract T[] getAllTypes();

        protected void setBonusItemStatModel(BonusItemStatModel bonusItemStatModel) {
            this.bonusItemStatModel = Optional.ofNullable(bonusItemStatModel);
        }

    }

    public static class ItemData extends ObjectData<ItemData.Type> {

        @Getter
        private final ConcurrentMap<EnchantmentModel, Integer> enchantments = Concurrent.newMap();

        @Getter
        private final ConcurrentMap<EnchantmentModel, ConcurrentList<EnchantmentStatModel>> enchantmentStats = Concurrent.newMap();

        public ItemData(CompoundTag compoundTag, RarityModel rarityModel) {
            super(compoundTag, rarityModel);

            // Handle Hot Potatos
            if (compoundTag.containsPath("tag.ExtraAttributes.hot_potato_count")) {
                Integer hotPotatoCount = compoundTag.getPathOrDefault("tag.ExtraAttributes.hot_potato_count", IntTag.EMPTY).getValue();

                SimplifiedApi.getRepositoryOf(HotPotatoStatModel.class)
                    .findAll(FilterFunction.combine(HotPotatoStatModel::getType, ReforgeTypeModel::getKey), "ARMOR")
                    .forEach(hotPotatoStatModel -> this.stats.get(Type.HOT_POTATOS).get(hotPotatoStatModel.getStat()).addBonus(hotPotatoCount * hotPotatoStatModel.getValue()));
            }
        }

        public void addEnchantment(EnchantmentModel enchantmentModel, Integer value) {
            this.enchantments.put(enchantmentModel, value);
            this.enchantmentStats.put(enchantmentModel, Concurrent.newList());

            // Handle Static Enchants
            SimplifiedApi.getRepositoryOf(EnchantmentStatModel.class)
                .findAll(EnchantmentStatModel::getEnchantment, enchantmentModel)
                .forEach(enchantmentStatModel -> {
                    this.enchantmentStats.get(enchantmentModel).add(enchantmentStatModel);

                    if (!enchantmentStatModel.isPercentage() && enchantmentStatModel.getStat() != null) {
                        double enchantBonus = enchantmentStatModel.getBaseValue() + (enchantmentStatModel.getLevelBonus() * value);
                        this.stats.get(Type.ENCHANTS).get(enchantmentStatModel.getStat()).addBonus(enchantBonus);
                    }
                });
        }

        @Override
        protected Type[] getAllTypes() {
            return ItemData.Type.values();
        }

        public enum Type {

            ENCHANTS,
            GEMSTONES,
            HOT_POTATOS,
            REFORGES,
            STATS

        }

    }

    public static class AccessoryData extends ObjectData<AccessoryData.Type> {

        private AccessoryData(CompoundTag compoundTag, RarityModel rarityModel) {
            super(compoundTag, rarityModel);
        }

        @Override
        protected Type[] getAllTypes() {
            return AccessoryData.Type.values();
        }

        public enum Type {

            CAKE_BAG,
            GEMSTONES,
            REFORGES,
            STATS,
            ENRICHMENTS

        }

    }

}
