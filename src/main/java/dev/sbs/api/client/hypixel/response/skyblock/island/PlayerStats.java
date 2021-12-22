package dev.sbs.api.client.hypixel.response.skyblock.island;

import dev.sbs.api.SimplifiedApi;
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

public class PlayerStats {

    private static final Pattern nbtVariablePattern = Pattern.compile(".*?(nbt_([a-zA-Z0-9_\\-\\.]+)).*?");
    private static final Repository<StatModel> statRepository = SimplifiedApi.getRepositoryOf(StatModel.class);

    @Getter private long damageMultiplier = 0;
    @Getter private final ConcurrentLinkedMap<Type, ConcurrentLinkedMap<StatModel, Data>> stats = Concurrent.newLinkedMap();
    @Getter private final ConcurrentMap<String, Double> expressionVariables = Concurrent.newMap();
    @Getter private boolean bonusCalculated;

    // Bonus Modifiers
    @Getter private final ConcurrentMap<BonusPetAbilityStatModel, Pair<String, Double>> bonusPetAbilityStatModels = Concurrent.newMap();
    @Getter private final ConcurrentMap<BonusItemStatModel, CompoundTag> bonusAccessoryItemStatModels = Concurrent.newMap();
    @Getter private final ConcurrentMap<BonusItemStatModel, CompoundTag> bonusArmorItemStatModels = Concurrent.newMap();
    @Getter private final ConcurrentMap<BonusReforgeStatModel, CompoundTag> bonusReforgeStatModels = Concurrent.newMap();

    public enum Type {

        ACCESSORY_CAKE_BAG,
        ACCESSORY_GEMSTONES,
        ACCESSORY_REFORGES,
        ACCESSORY_STATS,
        ACCESSORY_ENRICHMENTS,


        ACTIVE_PET,
        ACTIVE_POTIONS,
        ARMOR,
        CENTURY_CAKES,
        DUNGEONS,
        ENCHANTS,
        ESSENCE,
        FAIRY_SOULS,
        JACOBS_FARMING,
        MELODYS_HARP,
        MINING_CORE,
        PET_SCORE,
        SKILLS,
        SLAYERS

    }

    PlayerStats(SkyBlockIsland.Member member) {
        // Initialize
        ConcurrentList<StatModel> statModels = statRepository.findAll();
        statModels.sort((s1, s2) -> Comparator.comparing(StatModel::getOrdinal).compare(s1, s2));
        Arrays.stream(Type.values())
            .forEach(type -> {
                this.stats.put(type, Concurrent.newLinkedMap());
                statModels.forEach(statModel -> this.stats.get(type).put(statModel, new Data(statModel.getBaseValue(), 0)));
            });

        // TODO: Optimizer Request: No API Data
        //  Booster Cookie:     +15 Magic Find
        //  Beacon:             +5 Magic Find

        // TODO: Optimizer Request: Missing API Data
        //  Defused Traps:      +6 Intelligence
        //  Bestiary:           +84 Health
        //  Account Upgrades:   +5 Magic Find

        // TODO: Hypixel Bugs
        //  Catacombs:          2x Health

        // --- Populate Default Expression Variables ---
        member.getActivePet().ifPresent(petInfo -> this.expressionVariables.put("PET_LEVEL", (double) petInfo.getLevel()));
        this.expressionVariables.put("SKILL_AVERAGE", member.getSkillAverage());
        SimplifiedApi.getRepositoryOf(SkillModel.class).findAll().forEach(skillModel -> this.expressionVariables.put(FormatUtil.format("SKILL_LEVEL_{0}", skillModel.getKey()), (double) member.getSkill(skillModel).getLevel()));
        SimplifiedApi.getRepositoryOf(DungeonModel.class)
            .findAll()
            .forEach(dungeonModel -> member.getDungeons()
                .getDungeon(dungeonModel)
                .ifPresent(dungeon -> this.expressionVariables.put(FormatUtil.format("DUNGEON_LEVEL_{0}", dungeonModel.getKey()), (double) dungeon.getLevel())));

        this.loadDamageMultiplier(member);
        this.loadSkills(member);
        this.loadSlayers(member);
        this.loadDungeons(member);

        this.loadAccessories(member); // TODO: Store bonus modifiers
        this.loadArmor(member); // TODO: Separate into sub types, store bonus modifiers

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
        playerStats.getStats().forEach((type, statEntries) -> {
            this.stats.put(type, Concurrent.newLinkedMap());
            statEntries.forEach((statModel, data) -> this.stats.get(type).put(statModel, new Data(data)));
        });

        this.damageMultiplier = playerStats.getDamageMultiplier();
        this.expressionVariables.putAll(playerStats.getExpressionVariables());

        if (!this.isBonusCalculated()) {
            this.bonusCalculated = true;

            // --- Load Bonus Accessory Reforge Stats ---
            this.getBonusReforgeStatModels().forEach((bonusReforgeStatModel, compoundTag) -> {
                ConcurrentLinkedMap<StatModel, Data> accessoryReforgeData = this.stats.get(Type.ACCESSORY_REFORGES);

                accessoryReforgeData.forEach((statModel, statData) -> {
                    double adjustedBase = this.handleBonusEffects(statModel, statData.getBase(), compoundTag, this.getExpressionVariables(), bonusReforgeStatModel);
                    double adjustedBonus = this.handleBonusEffects(statModel, statData.getBonus(), compoundTag, this.getExpressionVariables(), bonusReforgeStatModel);
                    statData.base = adjustedBase;
                    statData.bonus = adjustedBonus;
                });
            });

            // --- Load Bonus Accessory Item Stats ---
            this.getBonusAccessoryItemStatModels().forEach((bonusItemStatModel, compoundTag) -> {
                ConcurrentLinkedMap<StatModel, Data> accessoryGemstoneData = this.stats.get(Type.ACCESSORY_GEMSTONES);
                ConcurrentLinkedMap<StatModel, Data> accessoryReforgeData = this.stats.get(Type.ACCESSORY_REFORGES);
                ConcurrentLinkedMap<StatModel, Data> accessoryStatData = this.stats.get(Type.ACCESSORY_STATS);

                // Handle Bonus Gemstone Stats
                if (bonusItemStatModel.isForGems()) {
                    accessoryGemstoneData.forEach((statModel, statData) -> {
                        double adjustedBase = this.handleBonusEffects(statModel, statData.getBase(), compoundTag, this.getExpressionVariables(), bonusItemStatModel);
                        double adjustedBonus = this.handleBonusEffects(statModel, statData.getBonus(), compoundTag, this.getExpressionVariables(), bonusItemStatModel);
                        statData.base = adjustedBase;
                        statData.bonus = adjustedBonus;
                    });
                }

                // Handle Bonus Reforge Stats
                if (bonusItemStatModel.isForReforges()) {
                    accessoryReforgeData.forEach((statModel, statData) -> {
                        double adjustedBase = this.handleBonusEffects(statModel, statData.getBase(), compoundTag, this.getExpressionVariables(), bonusItemStatModel);
                        double adjustedBonus = this.handleBonusEffects(statModel, statData.getBonus(), compoundTag, this.getExpressionVariables(), bonusItemStatModel);
                        statData.base = adjustedBase;
                        statData.bonus = adjustedBonus;
                    });
                }

                // Handle Bonus Stats
                if (bonusItemStatModel.isForStats()) {
                    accessoryStatData.forEach((statModel, statData) -> {
                        double adjustedBase = this.handleBonusEffects(statModel, statData.getBase(), compoundTag, this.getExpressionVariables(), bonusItemStatModel);
                        double adjustedBonus = this.handleBonusEffects(statModel, statData.getBonus(), compoundTag, this.getExpressionVariables(), bonusItemStatModel);
                        statData.base = adjustedBase;
                        statData.bonus = adjustedBonus;
                    });
                }
            });

            // --- Load Bonus Armor Item Stats ---
            this.getBonusArmorItemStatModels().forEach((bonusItemStatModel, compoundTag) -> this.stats.forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                double adjustedBase = this.handleBonusEffects(statModel, statData.getBase(), compoundTag, this.getExpressionVariables(), bonusItemStatModel);
                double adjustedBonus = this.handleBonusEffects(statModel, statData.getBonus(), compoundTag, this.getExpressionVariables(), bonusItemStatModel);
                statData.base = adjustedBase;
                statData.bonus = adjustedBonus;
            })));

            // --- Load Bonus Pet Item Stats ---
            this.getBonusPetAbilityStatModels().forEach((bonusPetAbilityStatModel, pair) -> this.stats.forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                expressionVariables.put(pair.getKey(), pair.getValue());
                double adjustedBase = this.handleBonusEffects(statModel, statData.getBase(), null, this.getExpressionVariables(), bonusPetAbilityStatModel);
                double adjustedBonus = this.handleBonusEffects(statModel, statData.getBonus(), null, this.getExpressionVariables(), bonusPetAbilityStatModel);
                statData.base = adjustedBase;
                statData.bonus = adjustedBonus;
            })));
        }
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
                String reforgeKey = itemTag.getPathOrDefault("tag.ExtraAttributes.modifier", StringTag.EMPTY).getValue().toUpperCase();
                int rarityUpgrades = itemTag.getPathOrDefault("tag.ExtraAttributes.rarity_upgrades", IntTag.EMPTY).getValue();

                // Load Rarity
                SimplifiedApi.getRepositoryOf(RarityModel.class)
                    .findFirst(RarityModel::getOrdinal, accessoryModel.getRarity().getOrdinal() + rarityUpgrades)
                    .ifPresent(rarityModel -> {
                        // Store Bonus Item Stat Model
                        SimplifiedApi.getRepositoryOf(BonusItemStatModel.class)
                            .findFirst(BonusItemStatModel::getItem, accessoryModel.getItem())
                            .ifPresent(bonusItemStatModel -> this.bonusAccessoryItemStatModels.put(bonusItemStatModel, itemTag));

                        // Load Gemstone Stats
                        ConcurrentMap<StatModel, Double> gemstoneStat = this.handleGemstoneBonus(itemTag, rarityModel);

                        // Save Gemstone Stats
                        gemstoneStat.forEach((statModel, value) -> this.stats.get(Type.ACCESSORY_GEMSTONES).get(statModel).addBonus(value));

                        // Handle Reforge
                        ConcurrentMap<StatModel, Double> reforgeBonuses = this.handleReforgeBonus(reforgeKey, rarityModel, itemTag);

                        // Save Reforge
                        reforgeBonuses.forEach((statModel, value) -> this.stats.get(Type.ACCESSORY_REFORGES).get(statModel).addBonus(value));

                        // Load Stats
                        accessoryModel.getEffects()
                            .forEach((key, value) -> statRepository.findFirst(StatModel::getKey, key)
                            .ifPresent(statModel -> this.stats.get(Type.ACCESSORY_STATS).get(statModel).addBonus(value)));

                        // Handle Enrichment
                        if (itemTag.containsPath("tag.ExtraAttributes.talisman_enrichment")) {
                            SimplifiedApi.getRepositoryOf(AccessoryEnrichmentModel.class)
                                .findFirst(
                                    FilterFunction.combine(AccessoryEnrichmentModel::getStat, StatModel::getKey),
                                    itemTag.<StringTag>getPath("tag.ExtraAttributes.talisman_enrichment").getValue()
                                ).ifPresent(accessoryEnrichmentModel -> {
                                    // Save Enrichment Stat
                                    StatModel statModel = accessoryEnrichmentModel.getStat();
                                    this.stats.get(Type.ACCESSORY_ENRICHMENTS).get(statModel).addBonus(accessoryEnrichmentModel.getValue());
                                });
                        }

                        // New Year Cake Bag
                        if ("NEW_YEAR_CAKE_BAG".equals(accessoryModel.getItem().getItemId())) {
                            try {
                                Byte[] nbtCakeBag = itemTag.<ByteArrayTag>getPath("tag.ExtraAttributes.new_year_cake_bag_data").getValue();
                                ListTag<CompoundTag> cakeBagItems = SimplifiedApi.getNbtFactory().fromByteArray(nbtCakeBag).getList("i");
                                statRepository.findFirst(StatModel::getKey, "HEALTH")
                                    .ifPresent(statModel -> this.stats.get(Type.ACCESSORY_CAKE_BAG).get(statModel).addBonus(cakeBagItems.size()));
                            } catch (IOException ignore) {
                            }
                        }
                    });
            });
        } catch (IOException ioException) {
            ioException.printStackTrace(); // TODO
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
                    .forEach(itemTag -> {
                        String itemId = itemTag.<StringTag>getPath("tag.ExtraAttributes.id").getValue();

                        // Load Item
                        SimplifiedApi.getRepositoryOf(ItemModel.class)
                            .findFirst(ItemModel::getItemId, itemId)
                            .ifPresent(itemModel -> {
                                // Load Rarity
                                String reforgeKey = itemTag.getPathOrDefault("tag.ExtraAttributes.modifier", StringTag.EMPTY).getValue().toUpperCase();
                                int rarityUpgrades = itemTag.getPathOrDefault("tag.ExtraAttributes.rarity_upgrades", IntTag.EMPTY).getValue();

                                SimplifiedApi.getRepositoryOf(RarityModel.class)
                                    .findFirst(RarityModel::getOrdinal, itemModel.getRarity().getOrdinal() + rarityUpgrades)
                                    .ifPresent(rarityModel -> {
                                        // Save Stats
                                        itemModel.getStats().forEach((key, value) -> {
                                            Optional<StatModel> optionalStatModel = statRepository.findFirst(StatModel::getKey, key);
                                            optionalStatModel.ifPresent(statModel -> this.stats.get(Type.ARMOR).get(statModel).addBonus(value));
                                        });

                                        // Save Enchantments
                                        if (itemTag.containsPath("tag.ExtraAttributes.enchantments")) {
                                            CompoundTag enchantments = itemTag.getPath("tag.ExtraAttributes.enchantments");

                                            enchantments.entrySet()
                                                .stream()
                                                .map(entry -> Pair.of(entry.getKey(), ((IntTag)entry.getValue()).getValue()))
                                                .forEach(pair -> {
                                                    // Load Enchantment & Stat
                                                    SimplifiedApi.getRepositoryOf(EnchantmentModel.class)
                                                        .findFirst(EnchantmentModel::getKey, pair.getKey().toUpperCase())
                                                        .flatMap(enchantmentModel -> SimplifiedApi.getRepositoryOf(EnchantmentStatModel.class)
                                                            .findFirst(EnchantmentStatModel::getEnchantment, enchantmentModel)
                                                        )
                                                        .ifPresent(enchantmentStatModel -> {
                                                            // Save Enchant Stat
                                                            double enchantBonus = enchantmentStatModel.getBaseValue();

                                                            if (!enchantmentStatModel.isPercentage())
                                                                enchantBonus += enchantmentStatModel.getLevelBonus() * pair.getValue();
                                                            else {
                                                                // TODO: STORE PERCENTAGE FOR LATER
                                                            }

                                                            //(enchantmentStatModel.getLevelBonus() * pair.getValue());
                                                            this.stats.get(Type.ENCHANTS).get(enchantmentStatModel.getStat()).addBonus(enchantBonus);
                                                        });
                                                });
                                        }

                                        // Save Reforge Stats
                                        ConcurrentMap<StatModel, Double> reforgeBonuses = this.handleReforgeBonus(reforgeKey, rarityModel, itemTag);
                                        reforgeBonuses.forEach((statModel, value) -> this.stats.get(Type.ARMOR).get(statModel).addBonus(value));

                                        // Save Gemstone Stat
                                        ConcurrentMap<StatModel, Double> gemstoneStat = this.handleGemstoneBonus(itemTag, rarityModel);
                                        gemstoneStat.forEach((statModel, value) -> this.stats.get(Type.ARMOR).get(statModel).addBonus(value));
                                    });

                                // Store Bonus Item Stats
                                Optional<BonusItemStatModel> optionalBonusItemStatModel = SimplifiedApi.getRepositoryOf(BonusItemStatModel.class).findFirst(BonusItemStatModel::getItem, itemModel);
                                optionalBonusItemStatModel.ifPresent(bonusItemStatModel -> bonusArmorItemStatModels.put(bonusItemStatModel, itemTag));
                            });
                    });
            }
        } catch (IOException ioException) {
            ioException.printStackTrace(); // TODO
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

            // Save Active Pet Ability Stats
            petAbilityStatMap.forEach((petAbilityModel, petAbilityStatModels) -> {
                Optional<BonusPetAbilityStatModel> optionalBonusPetAbilityStatModel = SimplifiedApi.getRepositoryOf(BonusPetAbilityStatModel.class).findFirst(BonusPetAbilityStatModel::getPetAbility, petAbilityModel);

                petAbilityStatModels.forEach(petAbilityStatModel -> {
                    double abilityValue = petAbilityStatModel.getBaseValue() + (petAbilityStatModel.getLevelBonus() * petInfo.getLevel());

                    // Save Ability Stat
                    if (petAbilityStatModel.getStat() != null) {
                        Data statData = this.stats.get(Type.ACTIVE_PET).get(petAbilityStatModel.getStat());
                        statData.addBonus(abilityValue);
                    }

                    // Store Bonus Pet Ability
                    String statKey = (petAbilityStatModel.getStat() == null ? "ABILITY_VALUE" : petAbilityStatModel.getStat().getKey());
                    optionalBonusPetAbilityStatModel.ifPresent(bonusPetAbilityStatModel -> bonusPetAbilityStatModels.put(bonusPetAbilityStatModel, Pair.of(statKey, abilityValue)));
                });
            });
        }));
    }

    private void loadActivePotions(SkyBlockIsland.Member member) {
        // --- Load Active Potions ---
        member.getActivePotions().forEach(potion -> {
            String effect = potion.getEffect();

            if (!member.getDisabledPotions().contains(effect)) {
                Optional<PotionModel> optionalPotionModel = SimplifiedApi.getRepositoryOf(PotionModel.class).findFirst(PotionModel::getKey, effect.toUpperCase());
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
                        potionTierModel.getEffects().forEach((key, value) -> {
                            Optional<StatModel> optionalStatModel = statRepository.findFirst(StatModel::getKey, key);
                            optionalStatModel.ifPresent(statModel -> potionStatEffects.put(statModel, potionTierModel.getEffect(key) + potionStatEffects.getOrDefault(statModel, 0.0)));
                        });

                        potionTierModel.getBuffEffects().forEach((key, value) -> potionBuffEffects.put(key, value + potionBuffEffects.getOrDefault(key, 0.0)));
                    });
                });

                // Load Potion Brews
                potion.getModifiers().forEach(modifier -> {
                    Optional<PotionBrewModel> optionalPotionBrewModel = SimplifiedApi.getRepositoryOf(PotionBrewModel.class).findFirst(
                        Pair.of(PotionBrewModel::getKey, modifier.getKey().toUpperCase()),
                        Pair.of(PotionBrewModel::getAmplified, modifier.getAmplifier())
                    );

                    optionalPotionBrewModel.ifPresent(potionBrewModel -> {
                        // Handle Brews
                        SimplifiedApi.getRepositoryOf(PotionBrewBuffModel.class)
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
                            });
                    });
                });

                // Save Active Potions
                potionStatEffects.forEach((statModel, value) -> {
                    Data statData = this.stats.get(Type.ACTIVE_POTIONS).get(statModel);
                    statData.addBonus(value);
                });
            }
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

    private void loadJacobsPerks(SkyBlockIsland.Member member) {
        // --- Load Jacobs Perks ---
        statRepository.findFirst(StatModel::getKey, "FARMING_FORTUNE")
            .ifPresent(farmingFortuneStatModel -> {
                double farmingDrops = member.getJacobsFarming().getPerk(SkyBlockIsland.JacobsFarming.Perk.DOUBLE_DROPS);

                // Save Jacobs Perks
                this.stats.get(Type.JACOBS_FARMING).get(farmingFortuneStatModel).addBase(farmingDrops * 2.0);
            });
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
                        .ifPresent(statModel -> {
                            // Save Song Stat
                            this.stats.get(Type.MELODYS_HARP).get(statModel).addBonus(melodySongModel.getReward());
                        });
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

    @SuppressWarnings("all")
    private ConcurrentMap<StatModel, Double> handleGemstoneBonus(CompoundTag compoundTag, RarityModel rarityModel) {
        ConcurrentMap<StatModel, Double> gemstoneAdjusted = Concurrent.newMap();
        CompoundTag gemTag = compoundTag.getPath("tag.ExtraAttributes.gems");
        String id = compoundTag.<StringTag>getPath("tag.ExtraAttributes.id").getValue();

        if (gemTag != null && gemTag.notEmpty()) {
            gemTag.forEach((key, tag) -> {
                key = RegexUtil.replaceAll(key, "_[\\d]", "");

                // Load Gemstone
                SimplifiedApi.getRepositoryOf(GemstoneModel.class)
                    .findFirst(GemstoneModel::getKey, key)
                    .ifPresent(gemstoneModel -> {
                        // Load Gemstone Type
                        // Load Gemstone Stat
                        SimplifiedApi.getRepositoryOf(GemstoneTypeModel.class)
                            .findFirst(GemstoneTypeModel::getKey, ((StringTag) tag).getValue().toUpperCase())
                            .flatMap(gemstoneTypeModel -> SimplifiedApi.getRepositoryOf(GemstoneStatModel.class).findFirst(
                                Pair.of(GemstoneStatModel::getGemstone, gemstoneModel),
                                Pair.of(GemstoneStatModel::getType, gemstoneTypeModel),
                                Pair.of(GemstoneStatModel::getRarity, rarityModel)
                            ))
                            .ifPresent(gemstoneStatModel -> gemstoneAdjusted.put(gemstoneModel.getStat(), gemstoneStatModel.getValue()));
                    });
            });
        }

        return gemstoneAdjusted;
    }

    @SuppressWarnings("all")
    private ConcurrentMap<StatModel, Double> handleReforgeBonus(String reforgeKey, RarityModel rarityModel, CompoundTag compoundTag) {
        ConcurrentMap<StatModel, Double> reforgeBonuses = Concurrent.newMap();

        // Load Reforge
        SimplifiedApi.getRepositoryOf(ReforgeModel.class)
            .findFirst(ReforgeModel::getKey, reforgeKey)
            .ifPresent(reforgeModel -> {
                // Load Reforge Stat
                SimplifiedApi.getRepositoryOf(ReforgeStatModel.class)
                    .findFirst(
                        Pair.of(ReforgeStatModel::getReforge, reforgeModel),
                        Pair.of(ReforgeStatModel::getRarity, rarityModel)
                    ).ifPresent(reforgeStatModel -> {
                        // Load Bonus Stats
                        Optional<BonusReforgeStatModel> optionalBonusReforgeStatModel = SimplifiedApi.getRepositoryOf(BonusReforgeStatModel.class).findFirst(BonusReforgeStatModel::getReforge, reforgeModel);

                        // TODO: NEW
                        optionalBonusReforgeStatModel.ifPresent(bonusReforgeStatModel -> this.bonusReforgeStatModels.put(bonusReforgeStatModel, compoundTag));

                        // Load Reforge Stat Effects
                        reforgeStatModel.getEffects()
                            .forEach((key, value1) -> statRepository.findFirst(StatModel::getKey, key)
                                .ifPresent(statModel -> {
                                    MutableDouble value = new MutableDouble(value1);

                                    // Handle Bonus Stats TODO: DISABLED
                                    //optionalBonusReforgeStatModel.ifPresent(bonusReforgeStatModel -> value.set(this.handleBonusEffects(statModel, value1, compoundTag, bonusReforgeStatModel)));

                                    // Save Stats
                                    reforgeBonuses.put(statModel, value.get() + reforgeBonuses.getOrDefault(statModel, 0.0));
                                }));
                    });
            });

        return reforgeBonuses;
    }

    @SuppressWarnings("all")
    private double handleBonusEffects(StatModel statModel, double currentTotal, CompoundTag compoundTag, BuffEffectsModel... bonusEffectsModels) {
        return this.handleBonusEffects(statModel, currentTotal, compoundTag, Concurrent.newMap(), bonusEffectsModels);
    }

    @SuppressWarnings("all")
    private double handleBonusEffects(StatModel statModel, double currentTotal, CompoundTag compoundTag, Map<String, Double> variables, BuffEffectsModel... bonusEffectsModels) {
        MutableDouble value = new MutableDouble(currentTotal);
        ConcurrentList<StatModel> statModels = statRepository.findAll();

        // Handle Bonus Reforge Stats
        for (BuffEffectsModel bonusBuffEffectModel : bonusEffectsModels) {
            // Handle Stat
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
                                        .variable("ABILITY_VALUE")
                                        .variables(statModels.stream().map(statModelX -> FormatUtil.format("{0}", statModelX.getKey())).collect(Concurrent.toSet()))
                                        .variables(statModels.stream().map(statModelX -> FormatUtil.format("SKILL_LEVEL_{0}", statModelX.getKey())).collect(Concurrent.toSet()))
                                        .variables(SimplifiedApi.getRepositoryOf(DungeonModel.class).findAll().stream().map(dungeonModelX -> FormatUtil.format("DUNGEON_LEVEL_{0}", dungeonModelX.getKey())).collect(Concurrent.toSet()))
                                        .variables(statModels.stream().map(statModelX -> FormatUtil.format("ABILITY_VALUE_{0}", statModelX.getKey())).collect(Concurrent.toSet()))
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

    public ConcurrentLinkedMap<StatModel, Data> getStats(Type type) {
        return this.stats.get(type);
    }

    public Data getAllData(String statName) {
        return this.getAllData(SimplifiedApi.getRepositoryOf(StatModel.class).findFirstOrNull(StatModel::getKey, statName.toUpperCase()));
    }

    public Data getAllData(StatModel statModel) {
        Data statData = new Data();

        for (Map.Entry<Type, ConcurrentLinkedMap<StatModel, Data>> newStat : this.getStats()) {
            Data statModelData = this.getData(newStat.getKey(), statModel);
            statData.addBase(statModelData.getBase());
            statData.addBonus(statModelData.getBonus());
        }

        return statData;
    }

    public Data getData(Type type, String statName) {
        return this.getData(type, SimplifiedApi.getRepositoryOf(StatModel.class).findFirstOrNull(StatModel::getKey, statName.toUpperCase()));
    }

    public Data getData(Type type, StatModel statModel) {
        return this.getStats().get(type).get(statModel);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Data {

        @Getter
        private double base;
        @Getter
        private double bonus;

        private Data() {
            this(0, 0);
        }

        private Data(Data data) {
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

}
