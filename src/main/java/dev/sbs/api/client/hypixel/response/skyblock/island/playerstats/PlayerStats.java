package dev.sbs.api.client.hypixel.response.skyblock.island.playerstats;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.SimplifiedException;
import dev.sbs.api.client.hypixel.response.skyblock.island.SkyBlockIsland;
import dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data.AccessoryData;
import dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data.Data;
import dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data.ItemData;
import dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data.ObjectData;
import dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data.PlayerDataHelper;
import dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.data.StatData;
import dev.sbs.api.data.model.skyblock.accessories.AccessoryModel;
import dev.sbs.api.data.model.skyblock.accessory_enrichments.AccessoryEnrichmentModel;
import dev.sbs.api.data.model.skyblock.accessory_families.AccessoryFamilyModel;
import dev.sbs.api.data.model.skyblock.bonus_armor_sets.BonusArmorSetModel;
import dev.sbs.api.data.model.skyblock.bonus_pet_ability_stats.BonusPetAbilityStatModel;
import dev.sbs.api.data.model.skyblock.collections.CollectionModel;
import dev.sbs.api.data.model.skyblock.dungeon_levels.DungeonLevelModel;
import dev.sbs.api.data.model.skyblock.dungeons.DungeonModel;
import dev.sbs.api.data.model.skyblock.enchantment_stats.EnchantmentStatModel;
import dev.sbs.api.data.model.skyblock.essence_perks.EssencePerkModel;
import dev.sbs.api.data.model.skyblock.fairy_exchanges.FairyExchangeModel;
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
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;
import dev.sbs.api.data.model.skyblock.slayer_levels.SlayerLevelModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.data.sql.function.FilterFunction;
import dev.sbs.api.minecraft.nbt.tags.array.ByteArrayTag;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.collection.ListTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.StringTag;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.mutable.MutableBoolean;
import dev.sbs.api.util.tuple.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
public class PlayerStats extends StatData<PlayerStats.Type> {

    @Getter private final double damageMultiplier;
    @Getter private final ConcurrentList<AccessoryData> accessories = Concurrent.newList();
    @Getter private final ConcurrentList<ItemData> armor = Concurrent.newList();
    @Getter private final ConcurrentList<BonusPetAbilityStatModel> bonusPetAbilityStatModels = Concurrent.newList();
    @Getter private Optional<BonusArmorSetModel> bonusArmorSetModel = Optional.empty();
    @Getter private boolean bonusCalculated;
    private final ConcurrentMap<String, Double> expressionVariables = Concurrent.newMap();

    public PlayerStats(SkyBlockIsland.Member member) {
        this(member, true);
    }

    public PlayerStats(SkyBlockIsland.Member member, boolean calculateBonusStats) {
        // --- Initialize ---
        ConcurrentList<StatModel> statModels = SimplifiedApi.getRepositoryOf(StatModel.class).findAll();
        statModels.sort((s1, s2) -> Comparator.comparing(StatModel::getOrdinal).compare(s1, s2));
        Arrays.stream(Type.values()).forEach(type -> {
            this.stats.put(type, Concurrent.newLinkedMap());
            statModels.forEach(statModel -> this.stats.get(type).put(statModel, new Data()));
        });
        statModels.forEach(statModel -> this.addBase(this.stats.get(Type.BASE_STATS).get(statModel), statModel.getBaseValue()));

        // --- Populate Default Expression Variables ---
        member.getActivePet().ifPresent(petInfo -> this.expressionVariables.put("PET_LEVEL", (double) petInfo.getLevel()));
        this.expressionVariables.put("SKILL_AVERAGE", member.getSkillAverage());
        SimplifiedApi.getRepositoryOf(SkillModel.class).findAll().forEach(skillModel -> this.expressionVariables.put(FormatUtil.format("SKILL_LEVEL_{0}", skillModel.getKey()), (double) member.getSkill(skillModel).getLevel()));

        SimplifiedApi.getRepositoryOf(DungeonModel.class)
            .findAll()
            .forEach(dungeonModel -> member.getDungeons()
                .getDungeon(dungeonModel)
                .ifPresent(dungeon -> this.expressionVariables.put(FormatUtil.format("DUNGEON_LEVEL_{0}", dungeonModel.getKey()), (double) dungeon.getLevel())));

        SimplifiedApi.getRepositoryOf(CollectionModel.class)
            .findAll()
            .stream()
            .map(collectionModel -> member.getCollection(collectionModel.getSkill()))
            .flatMap(collection -> collection.getCollected().stream())
            .forEach(collectionItemEntry -> this.expressionVariables.put(FormatUtil.format("COLLECTION_{0}", collectionItemEntry.getKey().getItem().getItemId()), (double) collectionItemEntry.getValue()));

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

        // --- Load Damage Multiplier ---
        this.damageMultiplier = SimplifiedApi.getRepositoryOf(SkillModel.class)
            .findFirst(SkillModel::getKey, "COMBAT")
            .map(skillModel -> {
                int skillLevel = member.getSkill(skillModel).getLevel();

                if (skillLevel > 0) {
                    return SimplifiedApi.getRepositoryOf(SkillLevelModel.class)
                        .findAll(SkillLevelModel::getSkill, skillModel)
                        .stream()
                        .filter(skillLevelModel -> skillLevelModel.getLevel() <= skillLevel)
                        .map(SkillLevelModel::getBuffEffects)
                        .flatMap(map -> map.entrySet().stream())
                        .mapToDouble(Map.Entry::getValue)
                        .sum();
                }

                return 0.0;
            })
            .orElse(0.0) / 100.0;

        // --- Load Player Stats ---
        this.loadSkills(member);
        this.loadSlayers(member);
        this.loadDungeons(member);
        this.loadArmor(member);
        this.loadAccessories(member);
        this.loadActivePet(member);
        this.loadActivePotions(member);
        this.loadPetScore(member);
        this.loadMiningCore(member);
        this.loadCenturyCakes(member);
        this.loadEssencePerks(member);
        this.loadFairySouls(member);
        this.loadMelodysHarp(member);
        this.loadJacobsPerks(member);

        if (calculateBonusStats) {
            ConcurrentMap<String, Double> expressionVariables = this.getExpressionVariables();
            // --- Load Bonus Accessory Item Stats ---
            this.getAccessories().forEach(accessoryData -> accessoryData.calculateBonus(expressionVariables));

            // --- Load Bonus Armor Stats ---
            this.getArmor().forEach(itemData -> itemData.calculateBonus(expressionVariables));

            // --- Load Armor Multiplier Enchantments ---
            this.getArmor().forEach(itemData -> itemData.getEnchantments().forEach((enchantmentModel, value) -> itemData.getEnchantmentStats().get(enchantmentModel)
                .stream()
                .filter(enchantmentStatModel -> enchantmentStatModel.getStat() != null)
                .filter(EnchantmentStatModel::isPercentage)
                .forEach(enchantmentStatModel -> {
                    double enchantMultiplier = 1 + (enchantmentStatModel.getBaseValue() / 100.0) + ((enchantmentStatModel.getLevelBonus() * value) / 100.0);

                    this.stats.forEach((type, statEntries) -> {
                        Data statData = statEntries.get(enchantmentStatModel.getStat());

                        // Apply Multiplier
                        this.setBase(statData, statData.getBase() * enchantMultiplier);
                        this.setBonus(statData, statData.getBonus() * enchantMultiplier);
                    });
                }))
            );

            // --- Load Bonus Pet Item Stats ---
            ConcurrentMap<String, Double> petExpressionVariables = this.getExpressionVariables();
            this.getBonusPetAbilityStatModels()
                .stream()
                .filter(BonusPetAbilityStatModel::isPercentage)
                .forEach(bonusPetAbilityStatModel -> {
                    // Override Pet Variables
                    petExpressionVariables.put("PET_ABILITY_VALUE", petExpressionVariables.getOrDefault(FormatUtil.format("PET_ABILITY_{0}_VALUE", bonusPetAbilityStatModel.getPetAbility().getKey()), 0.0));
                    SimplifiedApi.getRepositoryOf(StatModel.class).findAll().forEach(statModel -> {
                        String newVariableName = FormatUtil.format("PET_ABILITY_{0}", statModel.getKey());
                        String currentVariableName = FormatUtil.format("PET_ABILITY_{0}_{1}", bonusPetAbilityStatModel.getPetAbility().getKey(), statModel.getKey());
                        petExpressionVariables.put(newVariableName, petExpressionVariables.getOrDefault(currentVariableName, 0.0));
                    });

                    // Handle Stats
                    this.stats.forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                        this.setBase(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBase(), null, petExpressionVariables, bonusPetAbilityStatModel));
                        this.setBonus(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), null, petExpressionVariables, bonusPetAbilityStatModel));
                    }));

                    // Handle Armor
                    this.armor.forEach(itemData -> itemData.getStats().forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                        this.setBase(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBase(), itemData.getCompoundTag(), petExpressionVariables, bonusPetAbilityStatModel));
                        this.setBonus(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), itemData.getCompoundTag(), petExpressionVariables, bonusPetAbilityStatModel));
                    })));

                    // Handle Accessories
                    this.accessories.forEach(accessoryData -> accessoryData.getStats().forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                        this.setBase(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBase(), accessoryData.getCompoundTag(), petExpressionVariables, bonusPetAbilityStatModel));
                        this.setBonus(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), accessoryData.getCompoundTag(), petExpressionVariables, bonusPetAbilityStatModel));
                    })));
                });
        }
    }

    public ConcurrentMap<String, Double> getExpressionVariables() {
        ConcurrentMap<String, Double> expressionVariables = Concurrent.newMap(this.expressionVariables);
        this.getAllStats().forEach((statModel, statData) -> expressionVariables.put(FormatUtil.format("STAT_{0}", statModel.getKey()), statData.getTotal()));
        return expressionVariables;
    }

    public ConcurrentLinkedMap<StatModel, Data> getCombinedStats() {
        // Initialize
        ConcurrentList<StatModel> statModels = SimplifiedApi.getRepositoryOf(StatModel.class).findAll(StatModel::getOrdinal);
        ConcurrentLinkedMap<StatModel, Data> totalStats = Concurrent.newLinkedMap();
        statModels.forEach(statModel -> totalStats.put(statModel, new Data()));

        // Collect Stat Data
        this.getStats().forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
            this.addBase(totalStats.get(statModel), statData.getBase());
            this.addBonus(totalStats.get(statModel), statData.getBonus());
        }));

        // Collect Accessory Data
        this.getAccessories()
            .stream()
            .map(StatData::getStats)
            .forEach(statTypeEntries -> statTypeEntries.forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                this.addBase(totalStats.get(statModel), statData.getBase());
                this.addBonus(totalStats.get(statModel), statData.getBonus());
            })));

        // Collect Armor Data
        this.getArmor()
            .stream()
            .map(StatData::getStats)
            .forEach(statTypeEntries -> statTypeEntries.forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                this.addBase(totalStats.get(statModel), statData.getBase());
                this.addBonus(totalStats.get(statModel), statData.getBonus());
            })));

        return totalStats;
    }

    @Override
    protected Type[] getAllTypes() {
        return PlayerStats.Type.values();
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
                        String itemId = itemTag.getPathOrDefault("tag.ExtraAttributes.id", StringTag.EMPTY).getValue();

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
                        String itemId = itemTag.getPathOrDefault("tag.ExtraAttributes.id", StringTag.EMPTY).getValue();

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
            accessoryTagStatModels.forEach((accessoryModel, compoundTag) -> {
                // Create New Accessory Data
                AccessoryData accessoryData = new AccessoryData(accessoryModel, compoundTag);
                this.accessories.add(accessoryData);

                // Handle Gemstone Stats
                PlayerDataHelper.handleGemstoneBonus(compoundTag, accessoryModel.getRarity())
                    .forEach((statModel, value) -> this.addBonus(accessoryData.getStats(AccessoryData.Type.GEMSTONES).get(statModel), value));

                // Handle Reforge Stats
                PlayerDataHelper.handleReforgeBonus(accessoryData.getReforgeStat())
                    .forEach((statModel, value) -> this.addBonus(accessoryData.getStats(AccessoryData.Type.REFORGES).get(statModel), value));

                // Handle Stats
                accessoryModel.getEffects()
                    .forEach((key, value) -> SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, key)
                        .ifPresent(statModel -> this.addBonus(accessoryData.getStats(AccessoryData.Type.STATS).get(statModel), value)));

                // Handle Enrichment Stats
                if (compoundTag.containsPath("tag.ExtraAttributes.talisman_enrichment")) {
                    SimplifiedApi.getRepositoryOf(AccessoryEnrichmentModel.class)
                        .findFirst(
                            FilterFunction.combine(AccessoryEnrichmentModel::getStat, StatModel::getKey),
                            compoundTag.<StringTag>getPath("tag.ExtraAttributes.talisman_enrichment").getValue()
                        ).ifPresent(accessoryEnrichmentModel -> this.addBonus(accessoryData.getStats(AccessoryData.Type.ENRICHMENTS).get(accessoryEnrichmentModel.getStat()), accessoryEnrichmentModel.getValue()));
                }

                // New Year Cake Bag
                if ("NEW_YEAR_CAKE_BAG".equals(accessoryModel.getItem().getItemId())) {
                    try {
                        Byte[] nbtCakeBag = compoundTag.<ByteArrayTag>getPath("tag.ExtraAttributes.new_year_cake_bag_data").getValue();
                        ListTag<CompoundTag> cakeBagItems = SimplifiedApi.getNbtFactory().fromByteArray(nbtCakeBag).getList("i");
                        SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, "HEALTH")
                            .ifPresent(statModel -> this.addBonus(accessoryData.getStats(AccessoryData.Type.CAKE_BAG).get(statModel), cakeBagItems.size()));
                    } catch (IOException ignore) { }
                }
            });
        } catch (IOException ioException) {
            throw SimplifiedException.wrapNative(ioException)
                .withMessage("Unable to read accessory nbt data!")
                .build();
        }
    }

    private void loadActivePet(SkyBlockIsland.Member member) {
        member.getActivePet().ifPresent(petInfo -> petInfo.getPet().ifPresent(pet -> {
            // Load Rarity Filtered Pet Stats
            SimplifiedApi.getRepositoryOf(PetStatModel.class)
                .findAll(PetStatModel::getPet, pet)
                .parallelStream()
                .filter(petStatModel -> petStatModel.getRarities().contains(petInfo.getRarity().getOrdinal()))
                .forEach(petStatModel -> this.addBonus(this.stats.get(Type.ACTIVE_PET).get(petStatModel.getStat()), petStatModel.getBaseValue() + (petStatModel.getLevelBonus() * petInfo.getLevel())));

            // Load Rarity Filtered Ability Stats
            SimplifiedApi.getRepositoryOf(PetAbilityModel.class)
                .findAll(PetAbilityModel::getPet, pet)
                .parallelStream()
                .map(petAbilityModel -> Pair.of(petAbilityModel, SimplifiedApi.getRepositoryOf(PetAbilityStatModel.class)
                    .findAll(PetAbilityStatModel::getAbility, petAbilityModel)
                    .parallelStream()
                    .filter(petAbilityStatModel -> petAbilityStatModel.getRarities().contains(petInfo.getRarity().getOrdinal()))
                    .collect(Concurrent.toList()))
                )
                .forEach(petAbilityStatPair -> {
                    // Load Bonus Pet Ability Stats
                    SimplifiedApi.getRepositoryOf(BonusPetAbilityStatModel.class)
                        .findFirst(BonusPetAbilityStatModel::getPetAbility, petAbilityStatPair.getKey())
                        .ifPresent(this.bonusPetAbilityStatModels::add);

                    petAbilityStatPair.getValue().forEach(petAbilityStatModel -> {
                        double abilityValue = petAbilityStatModel.getBaseValue() + (petAbilityStatModel.getLevelBonus() * petInfo.getLevel());

                        // Save Ability Stat
                        if (petAbilityStatModel.getStat() != null)
                            this.addBonus(this.stats.get(Type.ACTIVE_PET).get(petAbilityStatModel.getStat()), abilityValue);

                        // Store Bonus Pet Ability
                        String statKey = (petAbilityStatModel.getStat() == null ? "VALUE" : petAbilityStatModel.getStat().getKey());
                        this.expressionVariables.put(FormatUtil.format("PET_ABILITY_{0}_{1}", petAbilityStatPair.getKey().getKey(), statKey), abilityValue);
                    });
                });

            // Handle Static Pet Stat Bonuses
            ConcurrentMap<String, Double> petExpressionVariables = this.getExpressionVariables();
            this.getBonusPetAbilityStatModels()
                .stream()
                .filter(BonusPetAbilityStatModel::notPercentage)
                .forEach(bonusPetAbilityStatModel -> {
                    petExpressionVariables.put("PET_ABILITY_VALUE", petExpressionVariables.getOrDefault(FormatUtil.format("PET_ABILITY_{0}_VALUE", bonusPetAbilityStatModel.getPetAbility().getKey()), 0.0));
                    SimplifiedApi.getRepositoryOf(StatModel.class).findAll().forEach(statModel -> {
                        String newVariableName = FormatUtil.format("PET_ABILITY_{0}", statModel.getKey());
                        String currentVariableName = FormatUtil.format("PET_ABILITY_{0}_{1}", bonusPetAbilityStatModel.getPetAbility().getKey(), statModel.getKey());
                        petExpressionVariables.put(newVariableName, petExpressionVariables.getOrDefault(currentVariableName, 0.0));
                    });

                    this.stats.get(Type.ACTIVE_PET).forEach((statModel, statData) -> this.setBonus(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), null, petExpressionVariables, bonusPetAbilityStatModel)));
                });
        }));
    }

    private void loadActivePotions(SkyBlockIsland.Member member) {
        member.getActivePotions()
            .stream()
            .filter(potion -> !member.getDisabledPotions().contains(potion.getEffect()))
            .forEach(potion -> {
                ConcurrentMap<StatModel, Double> potionStatEffects = Concurrent.newMap();
                ConcurrentMap<String, Double> potionBuffEffects = Concurrent.newMap();

                // Load Potion
                SimplifiedApi.getRepositoryOf(PotionModel.class)
                    .findFirst(PotionModel::getKey, potion.getEffect().toUpperCase())
                    .ifPresent(potionModel -> {
                        ConcurrentList<PotionTierModel> potionTierModels = SimplifiedApi.getRepositoryOf(PotionTierModel.class)
                            .findAll(PotionTierModel::getPotion, potionModel)
                            .parallelStream()
                            .filter(potionTierModel -> potionTierModel.getTier() == potion.getLevel())
                            .collect(Concurrent.toList());

                        // Load Stats
                        potionTierModels.forEach(potionTierModel -> {
                            potionTierModel.getEffects().forEach((key, value) -> SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, key)
                                .ifPresent(statModel -> potionStatEffects.put(statModel, potionTierModel.getEffect(key) + potionStatEffects.getOrDefault(statModel, 0.0))));

                            potionTierModel.getBuffEffects().forEach((key, value) -> potionBuffEffects.put(key, value + potionBuffEffects.getOrDefault(key, 0.0)));
                        });
                    });

                // Handle Potion Brews
                potion.getModifiers().forEach(modifier -> SimplifiedApi.getRepositoryOf(PotionBrewModel.class)
                    .findFirst(
                        Pair.of(PotionBrewModel::getKey, modifier.getKey().toUpperCase()),
                        Pair.of(PotionBrewModel::getAmplified, modifier.getAmplifier())
                    )
                    .ifPresent(potionBrewModel -> SimplifiedApi.getRepositoryOf(PotionBrewBuffModel.class)
                        .findAll(PotionBrewBuffModel::getPotionBrew, potionBrewModel)
                        .forEach(potionBrewBuffModel -> {
                            final MutableBoolean isBuff = new MutableBoolean(true);

                            // Stats
                            SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, potionBrewBuffModel.getBuffKey())
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
                        }))
                );

                // Save Active Potions
                potionStatEffects.forEach((statModel, value) -> this.addBonus(this.stats.get(Type.ACTIVE_POTIONS).get(statModel), value));
            });
    }

    private void loadArmor(SkyBlockIsland.Member member) {
        try {
            Optional<BonusArmorSetModel> bonusArmorSetModel = Optional.empty();

            if (member.hasStorage(SkyBlockIsland.Storage.ARMOR)) {
                ConcurrentList<Optional<ItemModel>> armorItemModels = member.getStorage(SkyBlockIsland.Storage.ARMOR)
                    .getNbtData()
                    .<CompoundTag>getList("i")
                    .stream()
                    .map(itemTag -> SimplifiedApi.getRepositoryOf(ItemModel.class)
                        .findFirst(ItemModel::getItemId, itemTag.getPathOrDefault("tag.ExtraAttributes.id", StringTag.EMPTY).getValue())
                    )
                    .collect(Concurrent.toList())
                    .inverse();

                this.bonusArmorSetModel = SimplifiedApi.getRepositoryOf(BonusArmorSetModel.class).findFirst(
                    Pair.of(BonusArmorSetModel::getHelmetItem, armorItemModels.get(0).orElse(null)),
                    Pair.of(BonusArmorSetModel::getChestplateItem, armorItemModels.get(1).orElse(null)),
                    Pair.of(BonusArmorSetModel::getLeggingsItem, armorItemModels.get(2).orElse(null)),
                    Pair.of(BonusArmorSetModel::getBootsItem, armorItemModels.get(3).orElse(null))
                );

                member.getStorage(SkyBlockIsland.Storage.ARMOR)
                    .getNbtData()
                    .<CompoundTag>getList("i")
                    .stream()
                    .filter(CompoundTag::notEmpty)
                    .forEach(itemTag -> SimplifiedApi.getRepositoryOf(ItemModel.class)
                        .findFirst(ItemModel::getItemId, itemTag.getPathOrDefault("tag.ExtraAttributes.id", StringTag.EMPTY).getValue())
                        .ifPresent(itemModel -> this.armor.add(PlayerDataHelper.parseItemData(itemModel, itemTag, "ARMOR")))
                    );
            }
        } catch (IOException ioException) {
            throw SimplifiedException.wrapNative(ioException)
                .withMessage("Unable to read armor nbt data!")
                .build();
        }
    }

    private void loadCenturyCakes(SkyBlockIsland.Member member) {
        member.getCenturyCakes()
            .stream()
            .filter(centuryCake -> centuryCake.getExpiresAt().getRealTime() > System.currentTimeMillis())
            .forEach(centuryCake -> this.addBonus(this.stats.get(Type.CENTURY_CAKES).get(centuryCake.getStat()), centuryCake.getAmount()));
    }

    private void loadDungeons(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(DungeonModel.class)
            .findAll()
            .forEach(dungeonModel -> member.getDungeons()
                .getDungeon(dungeonModel)
                .ifPresent(dungeon -> {
                    int dungeonLevel = dungeon.getLevel();

                    if (dungeonLevel > 0) {
                        SimplifiedApi.getRepositoryOf(DungeonLevelModel.class)
                            .findAll()
                            .stream()
                            .filter(dungeonLevelModel -> dungeonLevelModel.getLevel() <= dungeonLevel)
                            .map(DungeonLevelModel::getEffects)
                            .flatMap(map -> map.entrySet().stream())
                            .forEach(entry -> SimplifiedApi.getRepositoryOf(StatModel.class)
                                .findFirst(StatModel::getKey, entry.getKey())
                                .ifPresent(statModel -> this.addBase(this.stats.get(Type.DUNGEONS).get(statModel), entry.getValue())));
                    }
                })
            );
    }

    private void loadEssencePerks(SkyBlockIsland.Member member) {
        member.getEssencePerks().forEach(entry -> SimplifiedApi.getRepositoryOf(EssencePerkModel.class)
            .findFirst(EssencePerkModel::getKey, entry.getKey().toUpperCase())
            .filter(EssencePerkModel::isPermanent)
            .ifPresent(essencePerkModel -> this.addBonus(this.stats.get(Type.ESSENCE).get(essencePerkModel.getStat()), entry.getValue() * essencePerkModel.getLevelBonus())));
    }

    private void loadFairySouls(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(FairyExchangeModel.class)
            .findAll()
            .stream()
            .filter(fairyExchangeModel -> fairyExchangeModel.getExchange() <= member.getFairyExchanges())
            .flatMap(fairyExchangeModel -> fairyExchangeModel.getEffects().entrySet().stream())
            .forEach(entry -> SimplifiedApi.getRepositoryOf(StatModel.class)
                .findFirst(StatModel::getKey, entry.getKey())
                .ifPresent(statModel -> this.addBase(this.stats.get(Type.FAIRY_SOULS).get(statModel), entry.getValue())));
    }

    private void loadJacobsPerks(SkyBlockIsland.Member member) {
        if (member.getJacobsFarming().isPresent()) {
            SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, "FARMING_FORTUNE")
                .ifPresent(farmingFortuneStatModel -> this.addBase(this.stats.get(Type.JACOBS_FARMING).get(farmingFortuneStatModel), member.getJacobsFarming().get().getPerk(SkyBlockIsland.JacobsFarming.Perk.DOUBLE_DROPS) * 2.0));
        }
    }

    private void loadMelodysHarp(SkyBlockIsland.Member member) {
        if (member.getMelodyHarp() != null) {
            member.getMelodyHarp()
                .getSongs()
                .forEach((songName, songData) -> SimplifiedApi.getRepositoryOf(MelodySongModel.class)
                    .findFirst(MelodySongModel::getKey, songName.toUpperCase())
                    .ifPresent(melodySongModel -> SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, "INTELLIGENCE")
                        .ifPresent(statModel -> this.addBonus(this.stats.get(Type.MELODYS_HARP).get(statModel), melodySongModel.getReward()))
                    )
                );
        }
    }

    private void loadMiningCore(SkyBlockIsland.Member member) {
        member.getMining()
            .ifPresent(mining -> mining.getNodes()
                .forEach((key, level) -> SimplifiedApi.getRepositoryOf(HotmPerkModel.class)
                    .findFirst(HotmPerkModel::getKey, key.toUpperCase())
                    .ifPresent(hotmPerkModel -> SimplifiedApi.getRepositoryOf(HotmPerkStatModel.class)
                        .findAll(HotmPerkStatModel::getPerk, hotmPerkModel)
                        .forEach(hotmPerkStatModel -> this.addBonus(this.stats.get(Type.MINING_CORE).get(hotmPerkStatModel.getStat()), level * hotmPerkModel.getLevelBonus()))
                    )
                )
            );
    }

    private void loadPetScore(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(StatModel.class)
            .findFirst(StatModel::getKey, "MAGIC_FIND")
            .ifPresent(magicFindStatModel -> this.addBase(this.stats.get(Type.PET_SCORE).get(magicFindStatModel), SimplifiedApi.getRepositoryOf(PetScoreModel.class)
                .findAll()
                .stream()
                .filter(petScoreModel -> member.getPetScore() >= petScoreModel.getBreakpoint())
                .collect(Concurrent.toList())
                .size())
            );
    }

    private void loadSkills(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(SkillModel.class)
            .findAll()
            .forEach(skillModel -> {
                int skillLevel = member.getSkill(skillModel).getLevel();

                if (skillLevel > 0) {
                    SimplifiedApi.getRepositoryOf(SkillLevelModel.class)
                        .findAll(SkillLevelModel::getSkill, skillModel)
                        .stream()
                        .filter(skillLevelModel -> skillLevelModel.getLevel() <= skillLevel)
                        .map(SkillLevelModel::getEffects)
                        .flatMap(map -> map.entrySet().stream())
                        .forEach(entry -> SimplifiedApi.getRepositoryOf(StatModel.class)
                            .findFirst(StatModel::getKey, entry.getKey())
                            .ifPresent(statModel -> this.addBase(this.stats.get(Type.SKILLS).get(statModel), entry.getValue()))
                        );
                }
            });
    }

    private void loadSlayers(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(SlayerModel.class)
            .findAll()
            .forEach(slayerModel -> {
                int slayerLevel = member.getSlayer(slayerModel).getLevel();

                if (slayerLevel > 0) {
                    SimplifiedApi.getRepositoryOf(SlayerLevelModel.class)
                        .findAll(SlayerLevelModel::getSlayer, slayerModel)
                        .stream()
                        .filter(slayerLevelModel -> slayerLevelModel.getLevel() <= slayerLevel)
                        .map(SlayerLevelModel::getEffects)
                        .flatMap(map -> map.entrySet().stream())
                        .forEach(entry -> SimplifiedApi.getRepositoryOf(StatModel.class)
                            .findFirst(StatModel::getKey, entry.getKey())
                            .ifPresent(statModel -> this.addBase(this.stats.get(Type.SKILLS).get(statModel), entry.getValue()))
                        );
                }
            });
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Type implements ObjectData.Type {

        ACTIVE_PET(true),
        ACTIVE_POTIONS(true),
        BASE_STATS(true),
        CENTURY_CAKES(true),
        DUNGEONS(true),
        ESSENCE(true),
        FAIRY_SOULS(true),
        JACOBS_FARMING(true),
        MELODYS_HARP(true),
        MINING_CORE(true),
        PET_SCORE(true),
        SKILLS(true),
        SLAYERS(true);

        @Getter
        private final boolean optimizerConstant;

    }

}