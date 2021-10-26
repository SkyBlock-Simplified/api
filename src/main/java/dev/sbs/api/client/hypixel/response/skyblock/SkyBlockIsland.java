package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.model.sql.items.ItemSqlModel;
import dev.sbs.api.model.sql.items.ItemRepository;
import dev.sbs.api.model.sql.minions.MinionSqlModel;
import dev.sbs.api.model.sql.minions.MinionRepository;
import dev.sbs.api.model.sql.pets.PetSqlModel;
import dev.sbs.api.model.sql.pets.PetRepository;
import dev.sbs.api.model.sql.skills.SkillSqlModel;
import dev.sbs.api.hypixel_old.skyblock.Skyblock;
import dev.sbs.api.minecraft.nbt.NbtFactory;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.concurrent.ConcurrentSet;
import dev.sbs.api.util.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.helper.*;
import dev.sbs.api.model.sql.collections.CollectionSqlModel;
import dev.sbs.api.model.sql.rarities.RaritySqlModel;
import dev.sbs.api.model.sql.rarities.RarityRepository;
import lombok.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class SkyBlockIsland {

    @SerializedName("profile_id")
    private String islandId;
    private ConcurrentLinkedMap<String, Member> members;
    @SerializedName("community_upgrades")
    private CommunityUpgrades communityUpgrades;
    private Banking banking;
    @SerializedName("cute_name")
    private ProfileName profileName;
    @SerializedName("game_mode")
    private String gameMode;
    private boolean membersCached;
    private Member currentMember; // TODO: Mod uses this, should be able to phase it out later

    private void cacheMembers() {
        // Store UUID inside Member
        if (!this.membersCached) {
            this.membersCached = true;

            this.members.forEach((key, value) -> {
                value.uniqueId = StringUtil.toUUID(key);
            });
        }
    }

    public Optional<Banking> getBanking() {
        return Optional.ofNullable(this.banking);
    }

    public Collection getCollection(SkillSqlModel type) {
        Collection collection = new Collection(type);

        for (Member profile : this.getMembers()) {
            Collection profileCollection = profile.getCollection(type);
            profileCollection.collected.forEach(entry -> collection.collected.put(entry.getKey(), Math.max(collection.collected.getOrDefault(entry.getKey(), 0), entry.getValue())));
            collection.unlocked.putAll(profileCollection.unlocked);
        }

        return collection;
    }

    public Optional<CommunityUpgrades> getCommunityUpgrades() {
        return Optional.ofNullable(communityUpgrades);
    }

    public Optional<Member> getCurrentMember() {
        return Optional.ofNullable(this.currentMember);
    }

    public Optional<String> getGameMode() {
        return Optional.ofNullable(this.gameMode);
    }

    public Optional<Member> getMember(int index) {
        this.cacheMembers();

        if (!this.members.isEmpty())
            return Optional.of(Concurrent.newList(this.members.values()).get(index));

        return Optional.empty();
    }

    public Optional<Member> getMember(UUID uniqueId) {
        this.cacheMembers();

        for (Member profile : this.members.values()) {
            if (profile.getUniqueId().equals(uniqueId))
                return Optional.of(profile);
        }

        return Optional.empty();
    }

    public ConcurrentList<Member> getMembers() {
        this.cacheMembers();
        return Concurrent.newList(this.members.values());
    }

    public Optional<ProfileName> getProfileName() {
        return Optional.ofNullable(this.profileName);
    }
/*  TODO: MinionModel
    public Minion getMinion(Skyblock.Minion type) {
        Minion minion = new Minion(type);

        for (Member member : this.getMembers()) {
            Minion profileMinion = member.getMinion(type);
            minion.unlocked.addAll(profileMinion.unlocked);
        }

        return minion;
    }
*/
    public UUID getIslandId() {
        return StringUtil.toUUID(this.islandId);
    }

    public boolean hasMember(UUID uniqueId) {
        return this.getMember(uniqueId).isPresent();
    }

    public boolean isIronMan() {
        return this.getGameMode().isPresent() && this.getGameMode().get().equals("ironman");
    }

    public void setCurrentMember(Member currentMember) {
        this.currentMember = currentMember;
    }

    public static class Member {

        // Player Stats
        @Getter private UUID uniqueId;
        @SerializedName("first_join")
        @Getter private SkyBlockDate.RealTime firstJoin; // Real Time
        @SerializedName("first_join_hub")
        @Getter private SkyBlockDate.SkyBlockTime firstJoinHub; // SkyBlock Time
        @SerializedName("last_death")
        @Getter private SkyBlockDate.SkyBlockTime lastDeath; // SkyBlock Time
        @SerializedName("death_count")
        @Getter private int deathCount;
        @SerializedName("last_save")
        @Getter private SkyBlockDate.RealTime lastSave; // Real Time
        @SerializedName("coin_purse")
        @Getter private double purse;
        @SerializedName("fishing_treasure_caught")
        @Getter private int caughtFishingTreasure;
        @SerializedName("fairy_souls_collected")
        @Getter private int collectedFairySouls;
        @SerializedName("fairy_souls")
        @Getter private int unclaimedFairySouls;
        @SerializedName("fairy_exchanges")
        @Getter private int fairyExchanges;
        @SerializedName("wardrobe_equipped_slot")
        @Getter private int equippedWardrobeSlot;

        // Zones/Islands
        @Getter private ConcurrentList<String> tutorial;
        @SerializedName("visited_zones")
        @Getter private ConcurrentList<String> visited_zones;
        @SerializedName("achievement_spawned_island_types")
        @Getter private ConcurrentList<String> spawnedIslandTypes;

        // Miscellaneous
        @Getter private ConcurrentList<PetInfo> pets;
        @Getter private ConcurrentMap<String, Double> stats;
        private ConcurrentLinkedMap<String, Objective> objectives;
        private ConcurrentLinkedMap<String, Quest> quests;
        @SerializedName("crafted_generators")
        private ConcurrentList<String> craftedMinions;
        @SerializedName("sack_counts")
        private ConcurrentLinkedMap<String, Integer> sackCounts;
        private ConcurrentLinkedMap<String, SlayerBoss> slayer_bosses;
        private ConcurrentList<String> unlocked_coll_tiers;
        private ConcurrentLinkedMap<String, Integer> collection;
        @SerializedName("griffin.burrows")
        private ConcurrentList<GriffinBurrow> griffinBurrows;
        @SerializedName("harp_quest")
        private ConcurrentLinkedMap<String, Object> harpQuest;
        @SerializedName("active_effects")
        @Getter private ConcurrentList<Potion> activePotions;
        @SerializedName("paused_effects")
        @Getter private ConcurrentList<Potion> pausedPotions;
        @SerializedName("disabled_potion_effects")
        @Getter private ConcurrentSet<String> disabledPotionEffects;
        @SerializedName("temp_stat_buffs")
        @Getter private ConcurrentList<CenturyCake> centuryCakes;
        @SerializedName("mining_core")
        @Getter private Mining mining;
        @SerializedName("jacob2")
        @Getter private JacobsFarming jacobsFarming;
        @SerializedName("forge.forge_processes.forge_1")
        @Getter private ConcurrentList<ForgeItem> forgeItems;

        // Experience, DO NOT RENAME
        private double experience_skill_farming = -1;
        private double experience_skill_mining = -1;
        private double experience_skill_combat = -1;
        private double experience_skill_foraging = -1;
        private double experience_skill_fishing = -1;
        private double experience_skill_enchanting = -1;
        private double experience_skill_alchemy = -1;
        private double experience_skill_taming = -1;
        private double experience_skill_carpentry = -1;
        private double experience_skill_runecrafting = -1;

        // Essence
        @SerializedName("essence_undead")
        private int essenceUndead;
        @SerializedName("essence_diamond")
        private int essenceDiamond;
        @SerializedName("essence_dragon")
        private int essenceDragon;
        @SerializedName("essence_gold")
        private int essenceGold;
        @SerializedName("essence_ice")
        private int essenceIce;
        @SerializedName("essence_wither")
        private int essenceWither;
        @SerializedName("essence_spider")
        private int essenceSpider;
        @SerializedName("perks")
        @Getter private ConcurrentMap<String, Integer> essencePerks;

        // Inventory Contents
        @SerializedName("inv_armor")
        private NbtContent armorContents;
        @SerializedName("inv_contents")
        private NbtContent inventoryContents;
        @SerializedName("ender_chest_contents")
        private NbtContent enderChestContents;
        @SerializedName("fishing_bag")
        private NbtContent fishingBagContents;
        @SerializedName("quiver")
        private NbtContent quiverBagContents;
        @SerializedName("potion_bag")
        private NbtContent potionBagContents;
        @SerializedName("talisman_bag")
        private NbtContent accessoryBagContents;
        @SerializedName("candy_inventory_contents")
        private NbtContent candyBagContents;
        @SerializedName("wardrobe_contents")
        private NbtContent wardrobeContents;
        @SerializedName("personal_vault_contents")
        private NbtContent personalVaultContents;
        @SerializedName("backpack_contents")
        private ConcurrentMap<Integer, NbtContent> backpackContents;
        @SerializedName("backpack_icons")
        private ConcurrentMap<Integer, NbtContent> backpackIcons;

        public Backpacks getBackpacks() {
            return new Backpacks(this.backpackContents, this.backpackIcons);
        }

        @SneakyThrows
        public Collection getCollection(SkillSqlModel type) {
            Collection collection = new Collection(type);
            //SimplifiedApi.getSqlRepository(CollectionRepository.class).findAllCached().stream().filter(model -> model.getName().equals(type.getName())); // TODO: Foreign Keys
/*
            if (this.collection != null) {
                for (CollectionModel item : items) {
                    collection.collected.put(item, this.collection.getOrDefault(item.getName(), 0));

                    List<String> unlocked = this.unlocked_coll_tiers.stream().filter(tier -> tier.matches(FormatUtil.format("^{0}_[\\d]+$", item.getName()))).collect(Collectors.toList());
                    unlocked.forEach(tier -> {
                        int current = collection.unlocked.getOrDefault(item, 0);
                        collection.unlocked.put(item, Math.max(current, Integer.parseInt(tier.replace(FormatUtil.format("{0}_", item.getName()), ""))));
                    });
                }
            }
*/
            return collection;
        }

        public Essence getEssence() {
            return new Essence(this.essenceUndead, this.essenceDiamond, this.essenceDragon, this.essenceGold, this.essenceIce, this.essenceWither, this.essenceSpider);
        }

        public MelodyHarp getMelodyHarp() {
            return new MelodyHarp(this.harpQuest);
        }

        public Minion getMinion(MinionSqlModel type) {
            Minion minion = new Minion(type);

            if (this.craftedMinions != null) {
                minion.unlocked.addAll(
                        this.craftedMinions.stream()
                                .filter(item -> item.matches(FormatUtil.format("^{0}_[\\d]+$", type.getCollection().getSkill().getKey())))
                                .map(item -> Integer.parseInt(item.replace(FormatUtil.format("{0}_", type.getCollection().getSkill().getKey()), "")))
                                .collect(Collectors.toList())
                );
            }

            return minion;
        }

        public ConcurrentList<Minion> getMinions() {
            return SimplifiedApi.getSqlRepository(MinionRepository.class).findAll().stream().map(this::getMinion).collect(Concurrent.toList());
        }

        public ConcurrentLinkedMap<String, Objective> getObjectives() {
            return this.getObjectives(null);
        }

        public ConcurrentLinkedMap<String, Objective> getObjectives(BasicObjective.Status status) {
            return this.objectives.stream().filter(entry -> status == null || entry.getValue().getStatus() == status).sorted(Comparator.comparingLong(o -> o.getValue().getCompleted().getRealTime())).collect(Concurrent.toLinkedMap());
        }

        public int getPetScore() {
            int petScore = 0;

            if (ListUtil.notEmpty(this.getPets())) {
                for (PetInfo petInfo : this.getPets())
                    petScore += petInfo.getRarity().getOrdinal() + 1;
            }

            return petScore;
        }

        public PlayerStats getPlayerStats() {
            return new PlayerStats(this);
        }

        public ConcurrentLinkedMap<String, Quest> getQuests() {
            return this.getQuests(null);
        }

        public ConcurrentLinkedMap<String, Quest> getQuests(BasicObjective.Status status) {
            return this.quests.stream().filter(entry -> status == null || entry.getValue().getStatus() == status).sorted(Comparator.comparingLong(o -> o.getValue().getCompleted().getRealTime())).collect(Concurrent.toLinkedMap());
        }

        public Skill getSkill(SkillSqlModel skill) {
            double experience = (double)new Reflection(Member.class).getValue(FormatUtil.format("experience_skill_{0}", skill.getName().toLowerCase()), this);
            return new Skill(skill, experience);
        }

        public Slayer getSlayer(Skyblock.Slayer type) {
            return new Slayer(type, this.slayer_bosses.get(type.getName()));
        }

        public Sack getSack(Skyblock.Sack type) {
            Sack collection = new Sack(type);
            ConcurrentList<Skyblock.Item> items = type.getItems();

            if (this.sackCounts != null) {
                for (Skyblock.Item item : items)
                    collection.stored.put(item, this.sackCounts.getOrDefault(item.getItemName(), 0));
            }

            return collection;
        }
/*
        public double getSkillAverage() {
            ConcurrentList<SkillModel> skills = Arrays.stream(Skyblock.Skill.values()).filter(skill -> !skill.isCosmetic()).collect(Concurrent.toList());
            double total = skills.stream().mapToInt(skill -> this.getSkill(skill).getLevel()).sum();
            return total / skills.size();
        }
*/
        public NbtContent getStorage(Storage type) {
            switch (type) {
                case INVENTORY:
                    return inventoryContents;
                case ENDER_CHEST:
                    return enderChestContents;
                case FISHING:
                    return fishingBagContents;
                case QUIVER:
                    return quiverBagContents;
                case POTIONS:
                    return potionBagContents;
                case ACCESSORIES:
                    return accessoryBagContents;
                case CANDY:
                    return candyBagContents;
                case PERSONAL_VAULT:
                    return personalVaultContents;
                case ARMOR:
                default:
                    return armorContents;
            }
        }

        /*
        public double getWeight(Skyblock.Stat stat) {
            return this.getWeight().get(stat);
        }

        public ConcurrentMap<Skyblock.Stat, Double> getWeight() {
            return null; // TODO
        }
        */

        /*
SKILL_WEIGHT_VALUES = {
    'experience_skill_combat': {'exponent': 1.15797687265, 'divider': 275862},
    'experience_skill_mining': {'exponent': 1.18207448, 'divider': 259634},
    'experience_skill_farming': {'exponent': 1.217848139, 'divider': 220689},
    'experience_skill_fishing': {'exponent': 1.406418, 'divider': 88274},
    'experience_skill_foraging': {'exponent': 1.232826, 'divider': 259634},
    'experience_skill_taming': {'exponent': 1.14744, 'divider': 441379},
    'experience_skill_alchemy': {'exponent': 1.0, 'divider': 1103448},
    'experience_skill_enchanting': {'exponent': 0.96976583, 'divider': 882758}
}

SLAYER_WEIGHT_VALUES = {
    'zombie': {'divider': 2208, 'modifier': 0.15},
    'spider': {'divider': 2118, 'modifier': 0.08},
    'wolf': {'divider': 1962, 'modifier': 0.015},
    'enderman': {'divider': 1430, 'modifier': 0.017}
}

DUNGEON_WEIGHT_VALUES = {
    'catacomb': 0.0002149604615,
    'healer': 0.0000045254834,
    'mage': 0.0000045254834,
    'berserk': 0.0000045254834,
    'tank': 0.0000045254834,
    'archer': 0.0000045254834
}

// TODO: Skill Weight Python Code
async def get_skill_weight(skills_info):
    skill_weight = {}
    for key, value in skills_info.items():
        if key not in BLACKLISTED_SKILLS:
            skill_level = min(await get_skill_level(value['xp']), SKILLS_TEMPLATE[key]['max_level'])
            if skill_level < SKILLS_TEMPLATE[key]['max_level']:
                skill_level = skill_level + (value['xp'] - SKILL_XP_TABLE[skill_level]) / (SKILL_INDIVIDUAL_XP_TABLE[skill_level + 1])

            base = math.pow(skill_level * 10, 0.5 + SKILL_WEIGHT_VALUES[key]['exponent'] + skill_level / 100) / 1250

            if value['xp'] <= SKILL_XP_TABLE[SKILLS_TEMPLATE[key]['max_level']]:
                skill_weight[value['name']] = {'weight': round(base), 'overflow': 0}
            else:
                overflow = math.pow((value['xp'] - SKILL_XP_TABLE[SKILLS_TEMPLATE[key]['max_level']]) / SKILL_WEIGHT_VALUES[key]['divider'], 0.968)
                skill_weight[value['name']] = {'weight': round(base), 'overflow': round(overflow)}
    return skill_weight


// TODO: Slayer Weight Python Code
async def get_slayer_weight(slayer_info):
    slayer_weight = {}
    for key, value in slayer_info.items():
        divider = SLAYER_WEIGHT_VALUES[key]['divider']
        modifier = SLAYER_WEIGHT_VALUES[key]['modifier']

        if value['xp'] <= 1000000:
            slayer_weight[value['name']] = {'weight': (round(value['xp'] / divider) if value['xp'] > 0 else 0), 'overflow': 0}

        if value['xp'] > 1000000:
            base = 1000000 / divider
            remaining = value['xp'] - 1000000

            overflow = 0
            while remaining > 0:
                left = min(remaining, 1000000)
                overflow += math.pow(left / (divider * (1.5 + modifier)), 0.942)
                remaining -= left
                modifier += modifier

            slayer_weight[value['name']] = {'weight': round(base), 'overflow': round(overflow)}
    return slayer_weight

// TODO: Dungeon Weight Python Code
async def get_dungeon_weight(cata_xp, cata_level, class_xp):
    dungeon_weight = {}
    if cata_level != 50:
        cata_level = cata_level + ((cata_xp - DUNGEON_XP_TABLE[cata_level]) / DUNGEON_INDIVIDUAL_XP_TABLE[cata_level + 1])

    base = math.pow(cata_level, 4.5) * DUNGEON_WEIGHT_VALUES['catacomb']
    if cata_xp <= DUNGEON_XP_TABLE[50]:
        dungeon_weight['catacomb'] = {'weight': round(base), 'overflow': 0}
    else:
        dungeon_weight['catacomb'] = {'weight': round(base), 'overflow': round(math.pow((cata_xp - DUNGEON_XP_TABLE[50]) / ((4 * DUNGEON_XP_TABLE[50]) / base), 0.968))}

    for key, value in class_xp.items():
        class_level = await get_class_level(value)
        if class_level != 50:
            class_level += await get_progress(await get_class_level(value), value)
        base = math.pow(class_level, 4.5) * DUNGEON_WEIGHT_VALUES[key]
        if value <= DUNGEON_XP_TABLE[50]:
            dungeon_weight[key] = {'weight': round(base), 'overflow': 0}
        else:
            dungeon_weight[key] = {'weight': round(base), 'overflow': round(
                math.pow((value - DUNGEON_XP_TABLE[50]) / (4 * DUNGEON_XP_TABLE[50] / base), 0.968))}
    return dungeon_weight
         */


/*
            // TODO: Stats Implementation
            private ConcurrentMap<String, Double> getFilteredStats(boolean onlyFound) {
                return this.stats.entrySet().stream().filter(entry -> {
                    try {
                        Stat.valueOf(entry.getKey().toUpperCase());
                        return onlyFound;
                    } catch (IllegalArgumentException iaex) {
                        return !onlyFound && !"highest_crit_damage".equals(entry.getKey()); // Ignore highest_crit_damage
                    }
                }).collect(Concurrent.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }

            public ConcurrentMap<String, Double> getMissingStats() {
                return this.getFilteredStats(false);
            }

            public ConcurrentMap<Stat, Double> getStats() {
                return this.getFilteredStats(true).entrySet().stream().collect(Concurrent.toMap(entry -> Stat.valueOf(entry.getKey().toUpperCase()), Map.Entry::getValue));
            }
*/
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Backpacks {

        @Getter private final ConcurrentMap<Integer, NbtContent> contents;
        @Getter private final ConcurrentMap<Integer, NbtContent> icons;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Banking {

        @Getter private double balance;
        @Getter private ConcurrentList<Banking.Transaction> transactions;

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Transaction {

            @Getter private double amount;
            @Getter private SkyBlockDate.RealTime timestamp;
            @Getter private Banking.Transaction.Action action;
            @SerializedName("initiator_name")
            @Getter private String initiatorName;

            public enum Action {

                WITHDRAW,
                DEPOSIT

            }

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Experimentation {

        @SerializedName("claims_resets")
        @Getter private int resetClaims;
        @SerializedName("claims_resets_timestamp")
        @Getter private SkyBlockDate.RealTime resetClaimsTimestamp;
        @SerializedName("pairings")
        private Table superpairs;
        @SerializedName("simon")
        private Table chronomatron;
        @SerializedName("numbers")
        private Table ultrasequencer;

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Table {

            @SerializedName("last_attempt")
            @Getter private SkyBlockDate.RealTime lastAttempt;
            @SerializedName("last_claimed")
            @Getter private SkyBlockDate.RealTime lastClaimed;

            /*
            "attempts_0": 8,
            "bonus_clicks": 3,
            "claims_0": 8,
            "best_score_0": 17,
            "attempts_1": 8,
            "claims_1": 6,
            "best_score_1": 18,
            "attempts_2": 6,
            "claims_2": 6,
            "best_score_2": 15,
            "attempts_3": 16,
            "claims_3": 11,
            "best_score_3": 15,
            "attempts_5": 75,
            "claims_5": 56,
            "best_score_5": 15
             */

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GriffinBurrow {

        @SerializedName("ts")
        @Getter private SkyBlockDate.RealTime timestamp;
        @Getter private int x;
        @Getter private int y;
        @Getter private int z;
        @Getter private int type; // TODO: Enum?
        @Getter private int tier; // TODO: Enum
        @Getter private int chain;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BasicObjective {

        @Getter private BasicObjective.Status status;
        @SerializedName("completed_at")
        @Getter private SkyBlockDate.RealTime completed;

        public enum Status {

            ACTIVE,
            COMPLETE;

            public String getName() {
                return WordUtil.capitalizeFully(this.name());
            }

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CenturyCake {

        @Getter private int stat; // TODO: Is in order in stat menu, 12 = Pet Luck, 11 = Magic Find
        @Getter private String key;
        @Getter private int amount;
        @SerializedName("expire_at")
        @Getter private SkyBlockDate.RealTime expiresAt;

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Collection {

        @Getter private final SkillSqlModel type;
        @SerializedName("items")
        @Getter private ConcurrentLinkedMap<CollectionSqlModel, Integer> collected = Concurrent.newLinkedMap();
        @Getter private ConcurrentLinkedMap<CollectionSqlModel, Integer> unlocked = Concurrent.newLinkedMap();

        public int getCollected(CollectionSqlModel collection) {
            return this.collected.get(collection);
        }

        public int getUnlocked(CollectionSqlModel collection) {
            return this.unlocked.getOrDefault(collection, 0);
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CommunityUpgrades {

        @SerializedName("currently_upgrading")
        private CommunityUpgrades.UpgradeState.Upgrade currentlyUpgrading;
        @SerializedName("upgrade_states")
        @Getter private ConcurrentList<CommunityUpgrades.UpgradeState> upgradeStates;

        public Optional<CommunityUpgrades.UpgradeState.Upgrade> getCurrentlyUpgrading() {
            return Optional.ofNullable(this.currentlyUpgrading);
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class UpgradeState {

            @Getter private CommunityUpgrades.UpgradeState.Upgrade upgrade;
            @Getter private int tier;
            @SerializedName("started_ms")
            @Getter private SkyBlockDate.RealTime started;
            @SerializedName("started_by")
            @Getter private String startedBy;
            @SerializedName("claimed_ms")
            @Getter private SkyBlockDate.RealTime claimed;
            @SerializedName("claimed_by")
            @Getter private String claimedBy;
            @SerializedName("fasttracked")
            @Getter private boolean fastTracked;

            public enum Upgrade {

                @SerializedName("minion_slots")
                MINION_SLOTS,
                @SerializedName("coins_allowance")
                COINS_ALLOWANCE,
                @SerializedName("guests_count")
                GUESTS_COUNT,
                @SerializedName("island_size")
                ISLAND_SIZE,
                @SerializedName("coop_slots")
                COOP_SLOTS

            }

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Dungeons {

        @SerializedName("dungeons_blah_blah")
        private ConcurrentSet<String> dungeonsBlahBlah;
        @SerializedName("selected_dungeon_class")
        @Getter private Dungeon.Class selectedClass;
        @SerializedName("dungeon_journal.journal_entries")
        @Getter private ConcurrentMap<String, ConcurrentList<Integer>> journalEntries;
        @SerializedName("player_classes")
        @Getter private ConcurrentMap<Dungeon.Class.Type, Dungeon.Class> playerClasses;
        @Getter private ConcurrentMap<String, Dungeon> dungeonTypes;

        public Optional<Dungeon> getDungeon(String dungeonName) {
            return Optional.ofNullable(this.getDungeonTypes().get(dungeonName));
        }

        public Dungeon.Class getPlayerClass(Dungeon.Class.Type type) {
            return this.getPlayerClasses().get(type);
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Dungeon extends ExperienceCalculator {

        @Getter private double experience;
        @SerializedName("highest_tier_completed")
        @Getter private int highestCompletedTier;
        @SerializedName("best_runs")
        @Getter private ConcurrentMap<Floor, ConcurrentList<Run>> bestRuns;

        @SerializedName("times_played")
        @Getter private ConcurrentMap<Floor, Integer> timesPlayed;
        @SerializedName("tier_completions")
        @Getter private ConcurrentMap<Floor, Integer> completions;
        @SerializedName("milestone_completions")
        @Getter private ConcurrentMap<Floor, Integer> milestoneCompletions;

        @SerializedName("best_score")
        @Getter private ConcurrentMap<Floor, Integer> bestScore;
        @SerializedName("watcher_kills")
        @Getter private ConcurrentMap<Floor, Integer> watcherKills;
        @SerializedName("mobs_killed")
        @Getter private ConcurrentMap<Floor, Integer> mobsKilled;
        @SerializedName("most_mobs_killed")
        @Getter private ConcurrentMap<Floor, Integer> mostMobsKilled;
        @SerializedName("most_healing")
        @Getter private ConcurrentMap<Floor, Double> mostHealing;

        // Class Damage
        @SerializedName("most_damage_healer")
        @Getter private ConcurrentMap<Floor, Double> mostDamageHealer;
        @SerializedName("most_damage_mage")
        @Getter private ConcurrentMap<Floor, Double> mostDamageMage;
        @SerializedName("most_damage_berserk")
        @Getter private ConcurrentMap<Floor, Double> mostDamageBerserk;
        @SerializedName("most_damage_archer")
        @Getter private ConcurrentMap<Floor, Double> mostDamageArcher;
        @SerializedName("most_damage_tank")
        @Getter private ConcurrentMap<Floor, Double> mostDamageTank;

        // Fastest Times
        @SerializedName("fastest_time")
        @Getter private ConcurrentMap<Floor, Integer> fastestTime;
        @SerializedName("fastest_time_s")
        @Getter private ConcurrentMap<Floor, Integer> fastestSTierTime;
        @SerializedName("fastest_time_s_plus")
        @Getter private ConcurrentMap<Floor, Integer> fastestSPlusTierTime;

        public ConcurrentList<Run> getBestRuns(Floor floor) {
            return this.getBestRuns().get(floor);
        }

        public int getBestScore(Floor floor) {
            return this.getBestScore().get(floor);
        }

        public int getCompletions(Floor floor) {
            return this.getCompletions().get(floor);
        }

        public int getFastestTime(Floor floor) {
            return this.getFastestTime().get(floor);
        }

        public int getFastestSTierTime(Floor floor) {
            return this.getFastestSTierTime().get(floor);
        }

        public int getFastestSPlusTierTime(Floor floor) {
            return this.getFastestSPlusTierTime().get(floor);
        }

        public int getMilestoneCompletions(Floor floor) {
            return this.getMilestoneCompletions().get(floor);
        }

        public ConcurrentMap<Floor, Double> getMostDamage(Class.Type type) {
            switch (type) {
                case HEALER:
                    return this.getMostDamageHealer();
                case MAGE:
                    return this.getMostDamageMage();
                case BERSERK:
                    return this.getMostDamageBerserk();
                case ARCHER:
                    return this.getMostDamageArcher();
                case TANK:
                    return this.getMostDamageTank();
                default:
                    return null;
            }
        }

        public double getMostDamage(Class.Type type, Floor floor) {
            return this.getMostDamage(type).get(floor);
        }

        public int getTimesPlayed(Floor floor) {
            return this.getTimesPlayed().get(floor);
        }

        public int getWatcherKills(Floor floor) {
            return this.getWatcherKills().get(floor);
        }

        public int getMobsKilled(Floor floor) {
            return this.getMobsKilled().get(floor);
        }

        public int getMostMobsKilled(Floor floor) {
            return this.getMostMobsKilled().get(floor);
        }

        public double getMostHealing(Floor floor) {
            return this.getMostHealing().get(floor);
        }

        @Override
        public ConcurrentList<Integer> getExperienceTiers() {
            return null; // TODO
        }

        @Override
        public int getMaxLevel() {
            return 50;
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Class extends ExperienceCalculator {

            @Getter private final Type type;
            @Getter private double experience;

            @Override
            public ConcurrentList<Integer> getExperienceTiers() {
                return null; // TODO
            }

            @Override
            public int getMaxLevel() {
                return 50;
            }

            public enum Type {

                @SerializedName("healer")
                HEALER,
                @SerializedName("mage")
                MAGE,
                @SerializedName("berserk")
                BERSERK,
                @SerializedName("archer")
                ARCHER,
                @SerializedName("tank")
                TANK

            }

        }

        public enum Floor {

            @SerializedName("0")
            ENTRANCE,
            @SerializedName("1")
            ONE,
            @SerializedName("2")
            TWO,
            @SerializedName("3")
            THREE,
            @SerializedName("4")
            FOUR,
            @SerializedName("5")
            FIVE,
            @SerializedName("6")
            SIX,
            @SerializedName("7")
            SEVEN

        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Run {

            // Time
            @Getter private SkyBlockDate.RealTime timestamp;
            @SerializedName("elapsed_time")
            @Getter private int elapsedTime;

            // Score
            @SerializedName("score_exploration")
            @Getter private int explorationScore;
            @SerializedName("score_speed")
            @Getter private int speedScore;
            @SerializedName("score_skill")
            @Getter private int skillScore;
            @SerializedName("score_bonus")
            @Getter private int bonusScore;

            // Damage
            @SerializedName("damage_dealt")
            @Getter private double damageDealt;
            @SerializedName("damage_mitigated")
            @Getter private double damageMitigated;
            @SerializedName("ally_healing")
            @Getter private double allyHealing;

            @SerializedName("dungeon_class")
            @Getter private Dungeon.Class dungeonClass;
            @Getter private ConcurrentList<UUID> teammates;
            @SerializedName("deaths")
            @Getter private int deaths;
            @SerializedName("mobs_killed")
            @Getter private int mobsKilled;
            @SerializedName("secrets_found")
            @Getter private int secretsFound;

        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Essence {

        @Getter private final int undead;
        @Getter private final int diamond;
        @Getter private final int dragon;
        @Getter private final int gold;
        @Getter private final int ice;
        @Getter private final int wither;
        @Getter private final int spider;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ForgeItem {

        @Getter private String type;
        @Getter private String id;
        @Getter private SkyBlockDate.RealTime startTime;
        @Getter private int slot;
        @Getter private boolean notified;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class JacobsFarming {

        @SerializedName("medals_inv")
        @Getter private ConcurrentMap<Medal, Integer> medalInventory;
        @Getter private ConcurrentMap<Perk, Integer> perks;
        @SerializedName("unique_golds2")
        @Getter private ConcurrentSet<String> uniqueGolds; // TODO: SQL Collections
        private ConcurrentMap<String, Contest> contests = Concurrent.newMap();
        private ConcurrentList<Contest> contestData = Concurrent.newList();
        private boolean talked;

        public ConcurrentList<Contest> getContests() {
            if (ListUtil.isEmpty(this.contestData)) {
                this.contests.forEach(entry -> {
                    Contest contest = entry.getValue();

                    String[] dataString = entry.getKey().split(":");
                    String[] calendarString = dataString[1].split("_");
                    int year = NumberUtil.toInt(dataString[0]);
                    int month = NumberUtil.toInt(calendarString[0]);
                    int day = NumberUtil.toInt(calendarString[1]);
                    String collectionName = StringUtil.join(dataString, ":", 2, dataString.length);

                    contest.skyBlockDate = new SkyBlockDate(year, month, day);
                    contest.collectionName = collectionName;
                    this.contestData.add(contest);
                });
            }

            return this.contestData;
        }

        public int getMedals(Medal medal) {
            return this.getMedalInventory().get(medal);
        }

        public int getPerk(Perk perk) {
            return this.getPerks().get(perk);
        }

        public boolean hasTalked() {
            return this.talked;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Contest {

            @Getter private int collected;
            @SerializedName("claimed_rewards")
            private boolean claimedRewards;
            @SerializedName("claimed_position")
            @Getter private int position;
            @SerializedName("claimed_participants")
            @Getter private int participants;

            @Getter private SkyBlockDate skyBlockDate;
            @Getter private String collectionName;

            public boolean hasClaimedRewards() {
                return this.claimedRewards;
            }

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Data {

                @Getter private SkyBlockDate skyBlockDate;
                @Getter private String collectionName;

            }

        }

        public enum Medal {

            @SerializedName("bronze")
            BRONZE,
            @SerializedName("silver")
            SILVER,
            @SerializedName("gold")
            GOLD

        }

        public enum Perk {

            @SerializedName("double_drops")
            DOUBLE_DROPS,
            @SerializedName("farming_level_cap")
            FARMING_LEVEL_CAP

        }

    }

    public static class MelodyHarp {

        @Getter private final boolean talismanClaimed;
        @Getter private final String selectedSong;
        @Getter private final SkyBlockDate.RealTime selectedSongTimestamp;
        @Getter private final ConcurrentMap<String, Song> songs = Concurrent.newMap();

        @SuppressWarnings("all")
        private MelodyHarp(ConcurrentLinkedMap<String, Object> harpQuest) {
            ConcurrentLinkedMap<String, Object> newHarpQuest = Concurrent.newLinkedMap(harpQuest);
            this.talismanClaimed = (boolean)newHarpQuest.remove("claimed_talisman");
            this.selectedSong = (String)newHarpQuest.remove("selected_song");
            this.selectedSongTimestamp = new SkyBlockDate.RealTime((long)newHarpQuest.remove("selected_song_epoch") * 1000);
            ConcurrentLinkedMap<String, ConcurrentMap<String, Integer>> songMap = Concurrent.newLinkedMap();

            newHarpQuest.forEach(entry -> {
                String key = entry.getKey().replace("song_", "");
                String songName = key.replaceAll("_((best|perfect)_)?completions?", "");
                String category = key.replace(songName, "");

                if (!songMap.containsKey(songName))
                    songMap.put(songName, Concurrent.newMap());

                songMap.get(songName).put(category, (Integer)entry.getValue());
            });

            songMap.forEach(entry -> {
                Song song = new Song(entry.getValue().get("best_completion"), entry.getValue().get("best_completion"), entry.getValue().get("best_completion"));
                songs.put(entry.getKey(), song);
            });
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Song {

            @Getter private final int bestCompletion;
            @Getter private final int completions;
            @Getter private final int perfectCompletions;

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Mining {

        private ConcurrentMap<String, Object> nodes;
        private ConcurrentMap<String, Boolean> toggles;
        @SerializedName("last_reset")
        @Getter private SkyBlockDate.RealTime lastReset;
        @Getter private int experience;
        @SerializedName("received_free_tier")
        @Getter private boolean receivedFreeTier;
        @SerializedName("retroactive_tier2_token")
        @Getter private boolean retroactiveTier2Token;
        @SerializedName("tokens")
        @Getter private int remainingTokens;
        @SerializedName("tokens_spent")
        @Getter private int usedTokens;
        @SerializedName("selected_pickaxe_ability")
        @Getter private String selectedPickaxeAbility;
        @SerializedName("greater_mines_last_access")
        @Getter private SkyBlockDate.RealTime lastAccessToGreaterMines;
        @Getter private ConcurrentMap<Crystal.Type, Crystal> crystals;

        // Powder
        @SerializedName("powder_mithril")
        @Getter private int mithrilPowder;
        @SerializedName("powder_mithril_total")
        @Getter private int totalMithrilPowder;
        @SerializedName("powder_spent_mithril")
        @Getter private int usedMithrilPowder;
        @SerializedName("powder_gemstone")
        @Getter private int gemstonePowder;
        @SerializedName("powder_gemstone_total")
        @Getter private int totalGemstonePowder;
        @SerializedName("powder_spent_gemstone")
        @Getter private int usedGemstonePowder;

        // Daily Ores
        @SerializedName("daily_ores_mined")
        @Getter private int dailyOresMined;
        @SerializedName("daily_ores_mined_mithril_ore")
        @Getter private int dailyOresMinedMithrilOre;
        @SerializedName("daily_ores_mined_gemstone")
        @Getter private int dailyOresMinedGemstone;
        @SerializedName("daily_ores_mined_day")
        @Getter private int dailyOresMinedDay;
        @SerializedName("daily_ores_mined_day_mithril_ore")
        @Getter private int dailyOresMinedDayMithrilOre;
        @SerializedName("daily_ores_mined_day_gemstone")
        @Getter private int dailyOresMinedDayGemstone;

        // Biomes
        @SerializedName("biomes.dwarven")
        @Getter private Biome.Dwarven dwarvenMinesBiome;
        @SerializedName("biomes.precursor")
        @Getter private Biome.Precursor precursorCityBiome;
        @SerializedName("biomes.goblin")
        @Getter private Biome.Goblin goblinHideoutBiome;

        public Crystal getCrystal(Crystal.Type type) {
            return this.crystals.get(type);
        }

        public ConcurrentMap<String, Integer> getNodes() {
            return this.nodes.stream().filter(entry -> !(entry.getValue() instanceof Boolean)).collect(Concurrent.toMap());
        }

        public ConcurrentMap<String, Boolean> getToggles() {
            if (this.toggles == null) {
                this.toggles = Concurrent.newMap();

                this.nodes.stream().filter(entry -> (entry.getValue() instanceof Boolean)).forEach(entry -> {
                    toggles.put(entry.getKey().replace("toggle_", ""), (boolean)entry.getValue());
                });
            }

            return this.toggles;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Biome {

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Dwarven {

                @SerializedName("statues_placed")
                @Getter private ConcurrentList<Object> placedStatues;

            }

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Precursor {

                @SerializedName("parts_delivered")
                @Getter private ConcurrentList<Object> deliveredParts;

            }

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Goblin {

                @SerializedName("king_quest_active")
                @Getter private boolean kingQuestActive;
                @SerializedName("king_quests_completed")
                @Getter private int completedKingQuests;

            }

        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Crystal {

            private State state;
            @SerializedName("total_placed")
            @Getter private int totalPlaced;

            public State getState() {
                return this.state != null ? this.state : State.NOT_FOUND;
            }

            public enum State {

                FOUND,
                NOT_FOUND

            }

            public enum Type {

                @SerializedName("jade_crystal")
                JADE,
                @SerializedName("amber_crystal")
                AMBER,
                @SerializedName("topaz_crystal")
                TOPAZ,
                @SerializedName("sapphire_crystal")
                SAPHIRE,
                @SerializedName("amethyst_crystal")
                AMETHYST,
                @SerializedName("jasper_crystal")
                JASPER,
                @SerializedName("ruby_crystal")
                RUBY

            }

        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Minion {

        @Getter private ConcurrentSet<Integer> unlocked = Concurrent.newSet();
        @Getter private final MinionSqlModel type;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NbtContent {

        private int type; // Always 0
        @SerializedName("data")
        @Getter private String rawData;

        public byte[] getData() {
            return DataUtil.decode(this.getRawData().toCharArray());
        }

        public CompoundTag getNbtData() throws IOException {
            return new NbtFactory().fromByteArray(this.getData()); // TODO: NBT
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Objective extends BasicObjective {

        @Getter private int progress;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PetInfo extends ExperienceCalculator {

        @SerializedName("uuid")
        @Getter private UUID uniqueId;
        @SerializedName("type")
        @Getter private String name;
        @SerializedName("exp")
        @Getter private double experience;
        @Getter private boolean active;
        @SerializedName("tier")
        private String rarity;
        @Getter private int candyUsed;
        private String heldItem;
        private String skin;

        @Override
        public ConcurrentList<Integer> getExperienceTiers() {
            return Concurrent.newList(); // TODO: SQL
            //return Skyblock.PET_EXP_TIER_SCALE.get(this.getRarity());
        }

        @SneakyThrows
        public Optional<ItemSqlModel> getHeldItem() {
            return Optional.ofNullable(SimplifiedApi.getSqlRepository(ItemRepository.class).findFirstOrNullCached(ItemSqlModel::getItemId, this.heldItem));
        }

        @Override
        public int getMaxLevel() {
            return this.getPet().get().getMaxLevel();
        }

        @SneakyThrows
        public Optional<PetSqlModel> getPet() {
            return Optional.of(SimplifiedApi.getSqlRepository(PetRepository.class).findFirstOrNullCached(PetSqlModel::getKey, this.name));
        }

        public String getPrettyName() {
            return WordUtil.capitalizeFully(this.getName().replace("_", " "));
        }

        @SneakyThrows
        public RaritySqlModel getRarity() {
            return SimplifiedApi.getSqlRepository(RarityRepository.class).findFirstOrNullCached(RaritySqlModel::getKey, this.rarity);
        }

        public String getDefaultSkin() {
            return this.getPet().get().getSkin();
        }

        public Optional<String> getSkin() {
            return Optional.ofNullable(this.skin);
        }

        @Override
        public int getStartingLevel() {
            return 1;
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Potion {

        @Getter private String effect;
        @Getter private int level;
        @SerializedName("ticks_remaining")
        @Getter private int remainingTicks;
        @Getter private boolean infinite;
        @Getter private ConcurrentList<Modifier> modifiers;

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Modifier {

            @Getter private String key;
            @SerializedName("amp")
            @Getter private int amplifier;

        }

    }

    public enum ProfileName {

        @SerializedName("Apple")
        APPLE,
        @SerializedName("Banana")
        BANANA,
        @SerializedName("Blueberry")
        BLUEBERRY,
        @SerializedName("Coconut")
        COCONUT,
        @SerializedName("Cucumber")
        CUCUMBER,
        @SerializedName("Grapes")
        GRAPES,
        @SerializedName("Kiwi")
        KIWI,
        @SerializedName("Lemon")
        LEMON,
        @SerializedName("Lime")
        LIME,
        @SerializedName("Mango")
        MANGO,
        @SerializedName("Orange")
        ORANGE,
        @SerializedName("Papaya")
        PAPAYA,
        @SerializedName("Peach")
        PEACH,
        @SerializedName("Pear")
        PEAR,
        @SerializedName("Pineapple")
        PINEAPPLE,
        @SerializedName("Pomegranate")
        POMEGRANATE,
        @SerializedName("Raspberry")
        RASPBERRY,
        @SerializedName("Strawberry")
        STRAWBERRY,
        @SerializedName("Tomato")
        TOMATO,
        @SerializedName("Watermelon")
        WATERMELON,
        @SerializedName("Zucchini")
        ZUCCHINI

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Quest extends BasicObjective {

        @SerializedName("activated_at")
        @Getter private long activated;
        @SerializedName("ativated_at_sb")
        @Getter private SkyBlockDate.SkyBlockTime activatedAt;
        @SerializedName("completed_at_sb")
        @Getter private SkyBlockDate.SkyBlockTime completedAt;

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Sack {

        @Getter private final Skyblock.Sack type;
        @Getter private ConcurrentLinkedMap<Skyblock.Item, Integer> stored = Concurrent.newLinkedMap();

        public int getStored(Skyblock.Item item) {
            return this.stored.getOrDefault(item, (this.type.getItems().contains(item) ? 0 : -1));
        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Skill extends ExperienceCalculator {

        @Getter private final SkillSqlModel type;
        private final double experience;

        @Override
        public double getExperience() {
            return Math.max(this.experience, 0);
        }

        @Override
        public ConcurrentList<Integer> getExperienceTiers() {
            return this.getType().getName().equals("RUNECRAFTING") ? Skyblock.RUNECRAFTING_EXP_SCALE : Skyblock.SKILL_EXP_SCALE; // TODO: Experience Table Loading
        }

        @Override
        public int getMaxLevel() {
            return this.getType().getMaxLevel();
        }

    }

    public static class Slayer extends ExperienceCalculator {

        private final static Reflection slayerBossRef = new Reflection(SlayerBoss.class);
        private final ConcurrentMap<Integer, Boolean> claimed = Concurrent.newMap();
        private final ConcurrentMap<Integer, Boolean> claimedSpecial = Concurrent.newMap();
        private final ConcurrentMap<Integer, Integer> kills = Concurrent.newMap();
        @Getter private final Skyblock.Slayer type;
        @Getter private final double experience;

        private Slayer(Skyblock.Slayer type, SlayerBoss slayerBoss) {
            this.type = type;
            this.experience = slayerBoss.xp;

            for (Map.Entry<String, Boolean> entry : slayerBoss.claimed_levels.entrySet()) {
                String entryKey = entry.getKey().replace("level_", "");

                if (entryKey.endsWith("_special"))
                    this.claimedSpecial.put(Integer.parseInt(entryKey.replace("_special", "")), entry.getValue());
                else
                    this.claimed.put(Integer.parseInt(entryKey), entry.getValue());
            }

            for (int i = 0; i < 5; i++)
                this.kills.put(i + 1, (int)slayerBossRef.getValue(FormatUtil.format("boss_kills_tier_{0}", i), slayerBoss));
        }

        @Override
        public ConcurrentList<Integer> getExperienceTiers() {
            return Skyblock.SLAYER_EXP_SCALE.get(this.getType());
        }

        public int getKills(int tier) {
            return this.kills.getOrDefault(tier, 0);
        }

        @Override
        public int getMaxLevel() {
            return 9;
        }

        public boolean isClaimed(int level) {
            return this.claimed.get(level);
        }

    }

    public static class PlayerStats {
/*
        @Getter private final ConcurrentMap<Skyblock.Stat, Data> stat = Concurrent.newMap();
*/
        private PlayerStats(Member member) {
            //Arrays.stream(Skyblock.Stat.values()).forEach(skyblockStat -> stat.put(skyblockStat, new Data(0, 0)));
            // TODO: Load stats from API data in member
            // Optimal solution would be to go through everywhere stats can be,
            // and parse stats dynamically instead of adding them one at a time

            // TODO: Before Stats Calculation
            // Always There: Accessory Bag, Cake Bag#items, Essence Bonus, Century Cakes
            // Optional: Armor, Potions, Pet
            // Requires Query: Weapon

            try {
                CompoundTag accessoryBag = member.getStorage(Storage.ACCESSORIES).getNbtData(); // Pull Cake Bag
                CompoundTag armorInventory = member.getStorage(Storage.ARMOR).getNbtData();
                ConcurrentList<CenturyCake> centuryCakes = member.getCenturyCakes();
                ConcurrentMap<String, Integer> essencePerks = member.getEssencePerks();
                ConcurrentList<Potion> activePotions = member.getActivePotions();
/*
                for (Skyblock.Skill sbSkill : Skyblock.Skill.values()) {
                    Skill skill = member.getSkill(sbSkill);
                    int level = skill.getLevel();
                    // Pull skill stat bonus from resource tables
                    // Store stats
                }
*/
                // Should be a list
                accessoryBag.values().stream().map(item -> (CompoundTag)item).forEach(accessory -> {
                    // Pull base stats from AccessoryModel
                    // Pull rarity and reforge from accessory
                    // Pull stats from RarityModel and ReforgeModel
                    // Perform modifiers based on Accessory modifiers
                    // Store stats

                    // Check if Cake Bag
                    // Store number of cakes for Health
                });

                armorInventory.values().stream().map(item -> (CompoundTag)item).forEach(armorItem -> {
                    // Pull rarity and reforge from armor item
                    // Pull stats from RarityModel and ReforgeModel
                    // Pull non-reforge stats from armor item lore
                    // Store stats
                });

                centuryCakes.forEach(centuryCake -> {
                    if (centuryCake.getExpiresAt().getRealTime() > System.currentTimeMillis()) {
                        int statIndex = centuryCake.getStat(); // 11: magic find
                        String key = centuryCake.getKey(); // cake_magic_find
                        int amount = centuryCake.getAmount();
                        // Store amount into stat
                    }
                });

                essencePerks.forEach(entry -> {
                    if (entry.getKey().startsWith("permanent_")) {
                        String skillKey = entry.getKey().replace("permanent_", "");
                        int value = entry.getValue();
                        // Convert value into total bonus
                        // Store total bonus into stat
                    }
                });

                activePotions.forEach(potion -> {
                    String effect = potion.getEffect();
                    // Get stat bonus from PotionModel

                    potion.getModifiers().forEach(modifier -> {
                        modifier.getKey(); // cola
                        modifier.getAmplifier();

                        // Get stat bonus from potion brew
                        // Store into stat
                    });

                    // Store into stat
                });

                member.getPets().forEach(petInfo -> {
                    if (petInfo.isActive()) {
                        petInfo.getPet().ifPresent(pet -> {
                            /*// Pet has stats in database
                            Skyblock.Pet pet = petInfo.getPet().get();

                            ConcurrentList<Skyblock.Stat> petStats = pet.getStats();
                            ConcurrentList<Object> statValues = pet.getData().apply(petInfo.getLevel(), petInfo.getRarity());

                            for (int i = 0; i < petStats.size(); i++) {
                                Skyblock.Stat petStat = petStats.get(i);
                                double value = Double.parseDouble(statValues.get(i).toString());
                                // Store stat
                            }*/
                        });
                    }
                });

                // Parse item nbt lore for weapon query
            } catch (IOException ioException) { }
        }
/*
        public Data getData(Skyblock.Stat stat) {
            return this.getStat().get(stat);
        }
*/
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Data {

            @Setter(AccessLevel.PRIVATE)
            @Getter private int base;
            @Setter(AccessLevel.PRIVATE)
            @Getter private int bonus;

            private Data() {
                this(0, 0);
            }

        }

    }

    public enum Storage {

        ARMOR,
        INVENTORY,
        ENDER_CHEST,
        FISHING,
        QUIVER,
        POTIONS,
        ACCESSORIES,
        CANDY,
        PERSONAL_VAULT

    }

    abstract static class ExperienceCalculator {

        public int getStartingLevel() {
            return 0;
        }

        public abstract double getExperience();

        public abstract ConcurrentList<Integer> getExperienceTiers();

        public int getLevel() {
            int lastTotal = 0;

            for (int total : this.getExperienceTiers()) {
                lastTotal += total;

                if (lastTotal > this.getExperience())
                    return this.getStartingLevel() + this.getExperienceTiers().indexOf(total);
            }

            return this.getMaxLevel();
        }

        public abstract int getMaxLevel();

        public double getNextExperience() {
            return this.getNextExperience(this.getExperience());
        }

        public double getNextExperience(double experience) {
            int lastTotal = 0;

            for (int total : this.getExperienceTiers()) {
                lastTotal += total;

                if (lastTotal > experience)
                    return total;
            }

            return -1; // Max Level
        }

        public double getProgressExperience() {
            return this.getProgressExperience(this.getExperience());
        }

        public double getProgressExperience(double experience) {
            int lastTotal = 0;

            for (int total : this.getExperienceTiers()) {
                lastTotal += total;

                if (lastTotal > experience)
                    return experience - (lastTotal - total);
            }

            return -1; // Max Level
        }

        public double getMissingExperience() {
            return this.getMissingExperience(this.getExperience());
        }

        public double getMissingExperience(double experience) {
            int lastTotal = 0;

            for (int total : this.getExperienceTiers()) {
                lastTotal += total;

                if (lastTotal > experience)
                    return lastTotal - experience;
            }

            return -1; // Max Level
        }

        public double getProgressPercentage() {
            return this.getProgressPercentage(this.getExperience());
        }

        public double getProgressPercentage(double experience) {
            return (this.getProgressExperience(experience) / this.getNextExperience(experience)) * 100;
        }

    }

    static class SlayerBoss {

        private Map<String, Boolean> claimed_levels; // level_#: true
        private int boss_kills_tier_0;
        private int boss_kills_tier_1;
        private int boss_kills_tier_2;
        private int boss_kills_tier_3;
        private int boss_kills_tier_4;
        private int xp;

    }

}
