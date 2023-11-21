package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.SkyBlockIsland;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.account.Banking;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats.data.AccessoryData;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats.data.Data;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats.data.ItemData;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats.data.ObjectData;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats.data.PlayerDataHelper;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats.data.StatData;
import dev.sbs.api.data.model.skyblock.accessory_data.accessories.AccessoryModel;
import dev.sbs.api.data.model.skyblock.accessory_data.accessory_families.AccessoryFamilyModel;
import dev.sbs.api.data.model.skyblock.accessory_data.accessory_powers.AccessoryPowerModel;
import dev.sbs.api.data.model.skyblock.bonus_data.bonus_armor_sets.BonusArmorSetModel;
import dev.sbs.api.data.model.skyblock.bonus_data.bonus_pet_ability_stats.BonusPetAbilityStatModel;
import dev.sbs.api.data.model.skyblock.collection_data.collections.CollectionModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_levels.DungeonLevelModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeons.DungeonModel;
import dev.sbs.api.data.model.skyblock.enchantment_data.enchantment_stats.EnchantmentStatModel;
import dev.sbs.api.data.model.skyblock.essence_perks.EssencePerkModel;
import dev.sbs.api.data.model.skyblock.hotm_perk_stats.HotmPerkStatModel;
import dev.sbs.api.data.model.skyblock.hotm_perks.HotmPerkModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.melodys_songs.MelodySongModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_abilities.PetAbilityModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_ability_stats.PetAbilityStatModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_items.PetItemModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_scores.PetScoreModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_stats.PetStatModel;
import dev.sbs.api.data.model.skyblock.potion_data.potion_brew_buffs.PotionBrewBuffModel;
import dev.sbs.api.data.model.skyblock.potion_data.potion_brews.PotionBrewModel;
import dev.sbs.api.data.model.skyblock.potion_data.potion_tiers.PotionTierModel;
import dev.sbs.api.data.model.skyblock.potion_data.potions.PotionModel;
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;
import dev.sbs.api.data.model.skyblock.slayer_levels.SlayerLevelModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.StringTag;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.collection.search.function.SearchFunction;
import dev.sbs.api.util.data.mutable.MutableBoolean;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.helper.StreamUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public class ProfileStats extends StatData<ProfileStats.Type> {

    @Getter private final double damageMultiplier;
    @Getter private AccessoryBag accessoryBag;
    @Getter private final ConcurrentList<Optional<ItemData>> armor = Concurrent.newList();
    @Getter private final ConcurrentList<BonusPetAbilityStatModel> bonusPetAbilityStatModels = Concurrent.newList();
    @Getter private Optional<BonusArmorSetModel> bonusArmorSetModel = Optional.empty();
    @Getter private boolean bonusCalculated;
    private final ConcurrentMap<String, Double> expressionVariables = Concurrent.newMap();

    public ProfileStats(SkyBlockIsland skyBlockIsland, SkyBlockIsland.Member member) {
        this(skyBlockIsland, member, true);
    }

    public ProfileStats(SkyBlockIsland skyBlockIsland, SkyBlockIsland.Member member, boolean calculateBonusStats) {
        // --- Initialize ---
        ConcurrentList<StatModel> statModels = SimplifiedApi.getRepositoryOf(StatModel.class)
            .findAll()
            .sorted(StatModel::getOrdinal);
        Arrays.stream(Type.values()).forEach(type -> {
            this.stats.put(type, Concurrent.newLinkedMap());
            statModels.forEach(statModel -> this.stats.get(type).put(statModel, new Data()));
        });
        statModels.forEach(statModel -> this.addBase(this.stats.get(Type.BASE_STATS).get(statModel), statModel.getBaseValue()));

        // --- Populate Default Expression Variables ---
        member.getPetData().getActivePet().ifPresent(petInfo -> this.expressionVariables.put("PET_LEVEL", (double) petInfo.getLevel()));
        this.expressionVariables.put("SKILL_AVERAGE", member.getSkillAverage());
        this.expressionVariables.put("SKYBLOCK_LEVEL", (double) member.getLeveling().getLevel());
        this.expressionVariables.put("BESTIARY_MILESTONE", (double) member.getBestiary().getMilestone());
        this.expressionVariables.put("BANK", skyBlockIsland.getBanking().map(Banking::getBalance).orElse(0.0));
        SimplifiedApi.getRepositoryOf(SkillModel.class).findAll().forEach(skillModel -> this.expressionVariables.put(String.format("SKILL_LEVEL_%s", skillModel.getKey()), (double) member.getSkill(skillModel).getLevel()));

        SimplifiedApi.getRepositoryOf(DungeonModel.class)
            .stream()
            .forEach(dungeonModel -> this.expressionVariables.put(String.format("DUNGEON_LEVEL_%s", dungeonModel.getKey()), (double) member.getDungeons().getDungeon(dungeonModel).getLevel()));

        SimplifiedApi.getRepositoryOf(CollectionModel.class)
            .stream()
            .map(member::getCollection)
            .flatMap(collection -> collection.getCollected().stream())
            .forEach(collectionItemEntry -> this.expressionVariables.put(String.format("COLLECTION_%s", collectionItemEntry.getKey().getItem().getItemId()), (double) collectionItemEntry.getValue()));

        // TODO
        //  Optimizer Request: No API Data
        //     Booster Cookie:     +15 Magic Find
        //     Beacon:             +5 Magic Find
        //  Optimizer Request: Missing API Data
        //     Defused Traps:      +6 Intelligence
        //     Account Upgrades:   +5 Magic Find

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
        this.loadLevels(member);
        this.loadBestiary(member);
        this.loadMelodyHarp(member);
        this.loadJacobsPerks(member);

        if (calculateBonusStats) {
            ConcurrentMap<String, Double> expressionVariables = this.getExpressionVariables();
            // --- Load Bonus Accessory Item Stats ---
            this.getAccessoryBag().getFilteredAccessories().forEach(accessoryData -> accessoryData.calculateBonus(expressionVariables));

            // --- Load Bonus Armor Stats ---
            this.getArmor()
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(itemData -> itemData.calculateBonus(expressionVariables));

            // --- Load Armor Multiplier Enchantments ---
            this.getArmor()
                .stream()
                .flatMap(StreamUtil::flattenOptional)
                .forEach(itemData -> itemData.getEnchantments().forEach((enchantmentModel, value) -> itemData.getEnchantmentStats().get(enchantmentModel)
                    .stream()
                    .filter(EnchantmentStatModel::hasStat)
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
                .filter(BonusPetAbilityStatModel::noRequiredItem)
                .filter(BonusPetAbilityStatModel::noRequiredMobType)
                .forEach(bonusPetAbilityStatModel -> {
                    // Handle Stats
                    this.getStats().forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                        this.setBase(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBase(), null, petExpressionVariables, bonusPetAbilityStatModel));
                        this.setBonus(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), null, petExpressionVariables, bonusPetAbilityStatModel));
                    }));

                    // Handle Armor
                    this.getArmor()
                        .stream()
                        .flatMap(StreamUtil::flattenOptional)
                        .forEach(itemData -> itemData.getStats().forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                            this.setBase(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBase(), itemData.getCompoundTag(), petExpressionVariables, bonusPetAbilityStatModel));
                            this.setBonus(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), itemData.getCompoundTag(), petExpressionVariables, bonusPetAbilityStatModel));
                        })));

                    // Handle Accessories
                    this.getAccessoryBag().getFilteredAccessories().forEach(accessoryData -> accessoryData.getStats().forEach((type, statEntries) -> statEntries.forEach((statModel, statData) -> {
                        this.setBase(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBase(), accessoryData.getCompoundTag(), petExpressionVariables, bonusPetAbilityStatModel));
                        this.setBonus(statData, PlayerDataHelper.handleBonusEffects(statModel, statData.getBonus(), accessoryData.getCompoundTag(), petExpressionVariables, bonusPetAbilityStatModel));
                    })));
                });

            // TODO: Load Post Bonus Stats
        }
    }

    public ConcurrentMap<String, Double> getExpressionVariables() {
        ConcurrentMap<String, Double> expressionVariables = Concurrent.newMap(this.expressionVariables);
        this.getAllStats().forEach((statModel, statData) -> expressionVariables.put(String.format("STAT_%s", statModel.getKey()), statData.getTotal()));
        return expressionVariables;
    }

    public ConcurrentLinkedMap<StatModel, Data> getCombinedStats() {
        return this.getCombinedStats(false);
    }

    public ConcurrentLinkedMap<StatModel, Data> getCombinedStats(boolean optimizerConstant) {
        // Initialize
        ConcurrentLinkedMap<StatModel, Data> totalStats = SimplifiedApi.getRepositoryOf(StatModel.class)
            .findAll()
            .sorted(StatModel::getOrdinal)
            .stream()
            .map(statModel -> Pair.of(statModel, new Data()))
            .collect(Concurrent.toLinkedMap());

        // Collect Stat Data
        this.getStats()
            .stream()
            .filter(entry -> !optimizerConstant || entry.getKey().isOptimizerConstant())
            .forEach(entry -> entry.getValue().forEach((statModel, statData) -> {
                this.addBase(totalStats.get(statModel), statData.getBase());
                this.addBonus(totalStats.get(statModel), statData.getBonus());
            }));

        // Collect Accessory Data
        this.getAccessoryBag()
            .getFilteredAccessories()
            .stream()
            .map(StatData::getStats)
            .forEach(statTypeEntries -> statTypeEntries.stream()
                .filter(entry -> !optimizerConstant || entry.getKey().isOptimizerConstant())
                .forEach(entry -> entry.getValue().forEach((statModel, statData) -> {
                    this.addBase(totalStats.get(statModel), statData.getBase());
                    this.addBonus(totalStats.get(statModel), statData.getBonus());
                }))
            );

        // Collect Armor Data
        this.getArmor()
            .stream()
            .flatMap(StreamUtil::flattenOptional)
            .map(StatData::getStats)
            .forEach(statTypeEntries -> statTypeEntries.stream()
                .filter(entry -> !optimizerConstant || entry.getKey().isOptimizerConstant())
                .forEach(entry -> entry.getValue().forEach((statModel, statData) -> {
                    this.addBase(totalStats.get(statModel), statData.getBase());
                    this.addBonus(totalStats.get(statModel), statData.getBonus());
                }))
            );

        return totalStats;
    }

    @Override
    protected Type[] getAllTypes() {
        return ProfileStats.Type.values();
    }

    private void loadAccessories(SkyBlockIsland.Member member) {
        // --- Load Accessories ---
        ConcurrentMap<CompoundTag, AccessoryModel> tagAccessoryModels = Concurrent.newMap();
        ConcurrentList<AccessoryData> accessories = Concurrent.newList();
        ConcurrentList<AccessoryData> filteredAccessories = Concurrent.newList();

        // Load From Accessory Bag
        if (member.hasStorage(SkyBlockIsland.Storage.ACCESSORIES)) {
            member.getStorage(SkyBlockIsland.Storage.ACCESSORIES)
                .getNbtData()
                .<CompoundTag>getList("i")
                .stream()
                .filter(CompoundTag::notEmpty)
                .forEach(compoundTag -> SimplifiedApi.getRepositoryOf(AccessoryModel.class)
                    .findFirst(
                        SearchFunction.combine(AccessoryModel::getItem, ItemModel::getItemId),
                        compoundTag.getPathOrDefault("tag.ExtraAttributes.id", StringTag.EMPTY).getValue()
                    )
                    .ifPresent(accessoryModel -> tagAccessoryModels.put(compoundTag, accessoryModel))
                );
        }

        // Create Accessory Data
        accessories.addAll(
            tagAccessoryModels.stream()
                .map(entry -> new AccessoryData(entry.getValue(), entry.getKey()))
                .collect(Concurrent.toList())
        );

        // Store Families
        ConcurrentMap<AccessoryFamilyModel, ConcurrentSet<AccessoryModel>> familyAccessoryDataMap = Concurrent.newMap();
        accessories.stream()
            .filter(accessoryData -> Objects.nonNull(accessoryData.getAccessory().getFamily()))
            .forEach(accessoryData -> {
                // New Accessory Family
                if (!familyAccessoryDataMap.containsKey(accessoryData.getAccessory().getFamily()))
                    familyAccessoryDataMap.put(accessoryData.getAccessory().getFamily(), Concurrent.newSet());

                // Store Accessory
                familyAccessoryDataMap.get(accessoryData.getAccessory().getFamily()).add(accessoryData.getAccessory());
            });

        // Store Non-Stackable Families
        ConcurrentSet<AccessoryModel> processedAccessories = Concurrent.newSet();
        filteredAccessories.addAll(
            accessories.stream()
                .filter(accessoryData -> {
                    AccessoryFamilyModel accessoryFamilyModel = accessoryData.getAccessory().getFamily();

                    // Handle Families
                    if (Objects.nonNull(accessoryFamilyModel)) {
                        if (accessoryFamilyModel.isStatsStackable())
                            return true;
                        else if (accessoryFamilyModel.isReforgesStackable())
                            return true;
                        else {
                            ConcurrentList<AccessoryModel> familyData = Concurrent.newList(familyAccessoryDataMap.get(accessoryFamilyModel));

                            if (accessoryData.getAccessory().getFamilyRank() != null) {
                                familyData = familyData.sorted(AccessoryModel::getFamilyRank)
                                    .inverse(); // Sort By Highest

                                // Ignore Lowest Accessories
                                AccessoryModel topAccessory = familyData.remove(0);
                                processedAccessories.addAll(familyData);

                                // Top Accessory Only
                                if (!accessoryData.getAccessory().equals(topAccessory))
                                    return false;
                            } else {
                                if (processedAccessories.contains(accessoryData.getAccessory()))
                                    return false;

                                // Ignore All Accessories
                                processedAccessories.addAll(familyData);
                                return true;
                            }
                        }
                    }

                    return processedAccessories.add(accessoryData.getAccessory());
                })
                .collect(Concurrent.toList())
        );

        this.accessoryBag = new AccessoryBag(
            member,
            accessories,
            filteredAccessories,
            member.getAccessoryBag().getSelectedPower(),
            member.getAccessoryBag().getTuning().getCurrent()
        );

        // Accessory Power Effects
        member.getAccessoryBag()
            .getSelectedPower()
            .ifPresent(accessoryPowerModel -> accessoryPowerModel.getEffects()
                .forEach((key, value) -> SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, key)
                    .ifPresent(statModel -> this.addBonus(this.stats.get(Type.ACCESSORY_POWER).get(statModel), this.accessoryBag.getMagicalPowerMultiplier() * value))
                )
            );

        // Accessory Power Unique Effects
        member.getAccessoryBag()
            .getSelectedPower()
            .ifPresent(accessoryPowerModel -> accessoryPowerModel.getUniqueEffects()
                .forEach((key, value) -> SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, key)
                    .ifPresent(statModel -> this.addBonus(this.stats.get(Type.ACCESSORY_POWER).get(statModel), value))
                )
            );
    }

    private void loadActivePet(SkyBlockIsland.Member member) {
        member.getPetData().getActivePet().ifPresent(petInfo -> {
            // Load Rarity Filtered Pet Stats
            SimplifiedApi.getRepositoryOf(PetStatModel.class)
                .findAll(PetStatModel::getPet, petInfo.getPet())
                .stream()
                .filter(petStatModel -> petStatModel.getRarities().contains(petInfo.getRarity().getOrdinal()))
                .forEach(petStatModel -> this.addBonus(this.stats.get(Type.ACTIVE_PET).get(petStatModel.getStat()), petStatModel.getBaseValue() + (petStatModel.getLevelBonus() * petInfo.getLevel())));

            // Save Pet Stats to Expression Variables
            this.stats.get(Type.ACTIVE_PET).forEach((statModel, statData) -> this.expressionVariables.put(String.format("STAT_PET_%s", statModel.getKey()), statData.getTotal()));

            // Load Rarity Filtered Ability Stats
            SimplifiedApi.getRepositoryOf(PetAbilityModel.class)
                .findAll(PetAbilityModel::getPet, petInfo.getPet())
                .stream()
                .map(petAbilityModel -> Pair.of(petAbilityModel, SimplifiedApi.getRepositoryOf(PetAbilityStatModel.class)
                    .findAll(PetAbilityStatModel::getAbility, petAbilityModel)
                    .stream()
                    .filter(petAbilityStatModel -> petAbilityStatModel.getRarities().contains(petInfo.getRarity().getOrdinal()))
                    .collect(Concurrent.toList())
                ))
                .filter(petAbilityStatPair -> ListUtil.notEmpty(petAbilityStatPair.getRight()))
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
                        String statKey = (petAbilityStatModel.getStat() == null ? "" : "_" + petAbilityStatModel.getStat().getKey());
                        this.expressionVariables.put(String.format("PET_ABILITY_%s%s", petAbilityStatPair.getKey().getKey(), statKey), abilityValue);
                    });
                });

            // Handle Static Pet Item Bonuses
            petInfo.getHeldItem()
                .flatMap(itemModel -> SimplifiedApi.getRepositoryOf(PetItemModel.class)
                    .findFirst(PetItemModel::getItem, itemModel)
                )
                .filter(PetItemModel::notPercentage)
                .ifPresent(petItemModel -> petItemModel.getEffects().forEach((key, value) -> SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, key)
                    .ifPresent(statModel -> this.addBonus(this.stats.get(Type.ACTIVE_PET).get(statModel), petItemModel.getEffect(key)))
                ));

            // Handle Static Pet Stat Bonuses
            ConcurrentMap<String, Double> petExpressionVariables = this.getExpressionVariables();
            this.getBonusPetAbilityStatModels()
                .stream()
                .filter(BonusPetAbilityStatModel::notPercentage)
                .filter(BonusPetAbilityStatModel::noRequiredItem)
                .filter(BonusPetAbilityStatModel::noRequiredMobType)
                .forEach(bonusPetAbilityStatModel -> this.stats.get(Type.ACTIVE_PET)
                    .forEach((statModel, statData) -> this.setBonus(
                        statData,
                        PlayerDataHelper.handleBonusEffects(
                            statModel,
                            statData.getBonus(),
                            null,
                            petExpressionVariables,
                            bonusPetAbilityStatModel
                        ))
                    )
                );

            // Handle Percentage Pet Item Bonuses
            petInfo.getHeldItem()
                .flatMap(itemModel -> SimplifiedApi.getRepositoryOf(PetItemModel.class)
                    .findFirst(PetItemModel::getItem, itemModel)
                )
                .filter(PetItemModel::isPercentage)
                .ifPresent(petItemModel -> petItemModel.getEffects().forEach((key, value) -> SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, key)
                    .ifPresent(statModel -> {
                        double statMultiplier = 1 + (petItemModel.getEffect(key, 0.0) / 100.0);
                        Data statData = this.stats.get(Type.ACTIVE_PET).get(statModel);

                        this.setBonus(
                            statData,
                            statData.getBonus() * statMultiplier
                        );
                    })
                ));
        });
    }

    private void loadActivePotions(SkyBlockIsland.Member member) {
        member.getPotionData()
            .getActive()
            .stream()
            .filter(potion -> !member.getPotionData().getDisabled().contains(potion.getEffect()))
            .forEach(potion -> {
                ConcurrentMap<StatModel, Double> potionStatEffects = Concurrent.newMap();
                ConcurrentMap<String, Double> potionBuffEffects = Concurrent.newMap();

                // Load Potion
                SimplifiedApi.getRepositoryOf(PotionModel.class)
                    .findFirst(PotionModel::getKey, potion.getEffect().toUpperCase())
                    .ifPresent(potionModel -> {
                        ConcurrentList<PotionTierModel> potionTierModels = SimplifiedApi.getRepositoryOf(PotionTierModel.class)
                            .findAll(PotionTierModel::getPotion, potionModel)
                            .stream()
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
        if (member.hasStorage(SkyBlockIsland.Storage.ARMOR)) {
            ConcurrentList<ItemModel> items = SimplifiedApi.getRepositoryOf(ItemModel.class).findAll();
            ConcurrentList<Pair<CompoundTag, Optional<ItemModel>>> armorItemModels = member.getStorage(SkyBlockIsland.Storage.ARMOR)
                .getNbtData()
                .<CompoundTag>getList("i")
                .stream()
                .map(itemTag -> Pair.of(
                    itemTag,
                    items.findFirst(ItemModel::getItemId, itemTag.getPathOrDefault("tag.ExtraAttributes.id", StringTag.EMPTY).getValue())
                ))
                .collect(Concurrent.toList())
                .inverse();

            this.bonusArmorSetModel = SimplifiedApi.getRepositoryOf(BonusArmorSetModel.class).findFirst(
                Pair.of(BonusArmorSetModel::getHelmetItem, armorItemModels.get(0).getRight().orElse(null)),
                Pair.of(BonusArmorSetModel::getChestplateItem, armorItemModels.get(1).getRight().orElse(null)),
                Pair.of(BonusArmorSetModel::getLeggingsItem, armorItemModels.get(2).getRight().orElse(null)),
                Pair.of(BonusArmorSetModel::getBootsItem, armorItemModels.get(3).getRight().orElse(null))
            );

            armorItemModels.forEach(armorItemModelPair -> {
                ItemData itemData = null;

                if (armorItemModelPair.getLeft().notEmpty() && armorItemModelPair.getRight().isPresent())
                    itemData = new ItemData(
                        armorItemModelPair.getRight().get(),
                        armorItemModelPair.getLeft()
                    );

                this.armor.add(Optional.ofNullable(itemData));
            });
        }
    }

    private void loadBestiary(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, "HEALTH")
            .ifPresent(healthStatModel -> this.addBase(this.stats.get(Type.BESTIARY).get(healthStatModel), member.getBestiary().getMilestone() * 2.0));
    }

    private void loadCenturyCakes(SkyBlockIsland.Member member) {
        member.getCenturyCakes()
            .stream()
            .filter(centuryCake -> centuryCake.getExpiresAt().getRealTime() > System.currentTimeMillis())
            .forEach(centuryCake -> this.addBonus(this.stats.get(Type.CENTURY_CAKES).get(centuryCake.getStat()), centuryCake.getAmount()));
    }

    private void loadDungeons(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(DungeonModel.class)
            .stream()
            .forEach(dungeonModel -> {
                int dungeonLevel = member.getDungeons().getDungeon(dungeonModel).getLevel();

                if (dungeonLevel > 0) {
                    SimplifiedApi.getRepositoryOf(DungeonLevelModel.class)
                        .stream()
                        .filter(dungeonLevelModel -> dungeonLevelModel.getLevel() <= dungeonLevel)
                        .map(DungeonLevelModel::getEffects)
                        .flatMap(map -> map.entrySet().stream())
                        .forEach(entry -> SimplifiedApi.getRepositoryOf(StatModel.class)
                            .findFirst(StatModel::getKey, entry.getKey())
                            .ifPresent(statModel -> this.addBase(this.stats.get(Type.DUNGEONS).get(statModel), entry.getValue())));
                }
            });
    }

    private void loadEssencePerks(SkyBlockIsland.Member member) {
        member.getEssence().getPerks().forEach(entry -> SimplifiedApi.getRepositoryOf(EssencePerkModel.class)
            .findFirst(EssencePerkModel::getKey, entry.getKey().toUpperCase())
            .filter(EssencePerkModel::isPermanent)
            .ifPresent(essencePerkModel -> this.addBonus(this.stats.get(Type.ESSENCE).get(essencePerkModel.getStat()), entry.getValue() * essencePerkModel.getLevelBonus())));
    }

    private void loadJacobsPerks(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, "FARMING_FORTUNE")
            .ifPresent(farmingFortuneStatModel -> this.addBase(this.stats.get(Type.JACOBS_FARMING).get(farmingFortuneStatModel), member.getJacobsFarming().getDoubleDrops() * 4.0));
    }

    private void loadLevels(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, "HEALTH")
            .ifPresent(healthStatModel -> this.addBase(this.stats.get(Type.SKYBLOCK_LEVELS).get(healthStatModel), member.getLeveling().getLevel() * 5.0));
        SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, "STRENGTH")
            .ifPresent(strengthStatModel -> this.addBase(this.stats.get(Type.SKYBLOCK_LEVELS).get(strengthStatModel), member.getLeveling().getLevel() / 5.0));
    }

    private void loadMelodyHarp(SkyBlockIsland.Member member) {
        member.getMelodyHarp()
            .getSongs()
            .forEach((songName, songData) -> SimplifiedApi.getRepositoryOf(MelodySongModel.class)
                .findFirst(MelodySongModel::getKey, songName.toUpperCase())
                .ifPresent(melodySongModel -> SimplifiedApi.getRepositoryOf(StatModel.class).findFirst(StatModel::getKey, "INTELLIGENCE")
                    .ifPresent(statModel -> this.addBonus(this.stats.get(Type.MELODYS_HARP).get(statModel), melodySongModel.getReward()))
                )
            );
    }

    private void loadMiningCore(SkyBlockIsland.Member member) {
        member.getMining()
            .getNodes()
            .forEach((key, level) -> SimplifiedApi.getRepositoryOf(HotmPerkModel.class)
                .findFirst(HotmPerkModel::getKey, key.toUpperCase())
                .ifPresent(hotmPerkModel -> SimplifiedApi.getRepositoryOf(HotmPerkStatModel.class)
                    .findAll(HotmPerkStatModel::getPerk, hotmPerkModel)
                    .forEach(hotmPerkStatModel -> this.addBonus(this.stats.get(Type.MINING_CORE).get(hotmPerkStatModel.getStat()), level * hotmPerkModel.getLevelBonus()))
                )
            );
    }

    private void loadPetScore(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(StatModel.class)
            .findFirst(StatModel::getKey, "MAGIC_FIND")
            .ifPresent(magicFindStatModel -> this.addBase(this.stats.get(Type.PET_SCORE).get(magicFindStatModel), SimplifiedApi.getRepositoryOf(PetScoreModel.class)
                .stream()
                .filter(petScoreModel -> member.getPetData().getPetScore() >= petScoreModel.getBreakpoint())
                .collect(Concurrent.toList())
                .size())
            );
    }

    private void loadSkills(SkyBlockIsland.Member member) {
        SimplifiedApi.getRepositoryOf(SkillModel.class)
            .stream()
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
            .stream()
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

    public static class AccessoryBag {

        @Getter private final @NotNull ConcurrentList<AccessoryData> accessories;
        @Getter private final @NotNull ConcurrentList<AccessoryData> filteredAccessories;
        @Getter private final @NotNull Optional<AccessoryPowerModel> currentPower;
        @Getter private final @NotNull ConcurrentMap<StatModel, Integer> currentTuning;
        @Getter private final int magicalPower;
        @Getter private final int tuningPoints;
        @Getter private final double magicalPowerMultiplier;

        private AccessoryBag(
            @NotNull SkyBlockIsland.Member member,
            @NotNull ConcurrentList<AccessoryData> accessories,
            @NotNull ConcurrentList<AccessoryData> filteredAccessories,
            @NotNull Optional<AccessoryPowerModel> currentPowerModel,
            @NotNull ConcurrentMap<StatModel, Integer> currentTuning
        ) {
            this.accessories = accessories;
            this.filteredAccessories = filteredAccessories;
            this.currentPower = currentPowerModel;
            this.currentTuning = currentTuning;

            int highestMagicalPower = member.getAccessoryBag().getHighestMagicalPower();
            int currentMagicalPower = this.filteredAccessories.stream()
                .mapToInt(accessoryData -> this.handleMagicalPower(accessoryData, member))
                .sum();

            // Rift Prism
            if ((highestMagicalPower - currentMagicalPower) >= 11)
                currentMagicalPower += 11;

            this.magicalPower = currentMagicalPower;
            this.magicalPowerMultiplier = 29.97 * Math.pow(Math.log(0.0019 * this.magicalPower + 1), 1.2);
            this.tuningPoints = this.magicalPower / 10;
        }

        private int handleMagicalPower(AccessoryData accessoryData, SkyBlockIsland.Member member) {
            int magicalPower = accessoryData.getRarity().getMagicPowerMultiplier();

            if (accessoryData.getItem().getItemId().equals("HEGEMONY_ARTIFACT"))
                magicalPower *= 2;

            if (accessoryData.getItem().getItemId().equals("ABICASE"))
                magicalPower += member.getCrimsonIsle().getAbiphone().getContacts().size() / 2;

            return magicalPower;
        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Type implements ObjectData.Type {

        ACCESSORY_POWER(false),
        ACTIVE_PET(true),
        ACTIVE_POTIONS(true),
        BASE_STATS(true),
        BESTIARY(true),
        CENTURY_CAKES(true),
        DUNGEONS(true),
        ESSENCE(true),
        SKYBLOCK_LEVELS(true),
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
