package dev.sbs.api.client.hypixel.response.skyblock.island;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockDate;
import dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.PlayerStats;
import dev.sbs.api.data.model.skyblock.collection_items.CollectionItemModel;
import dev.sbs.api.data.model.skyblock.collections.CollectionModel;
import dev.sbs.api.data.model.skyblock.dungeon_classes.DungeonClassModel;
import dev.sbs.api.data.model.skyblock.dungeon_levels.DungeonLevelModel;
import dev.sbs.api.data.model.skyblock.dungeons.DungeonModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.minions.MinionModel;
import dev.sbs.api.data.model.skyblock.pet_exp_scales.PetExpScaleModel;
import dev.sbs.api.data.model.skyblock.pets.PetModel;
import dev.sbs.api.data.model.skyblock.profiles.ProfileModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.sack_items.SackItemModel;
import dev.sbs.api.data.model.skyblock.sacks.SackModel;
import dev.sbs.api.data.model.skyblock.shop_profile_upgrades.ShopProfileUpgradeModel;
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;
import dev.sbs.api.data.model.skyblock.slayer_levels.SlayerLevelModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.minecraft.nbt.exception.NbtException;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.Range;
import dev.sbs.api.util.Vector;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.concurrent.ConcurrentSet;
import dev.sbs.api.util.concurrent.linked.ConcurrentLinkedList;
import dev.sbs.api.util.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.helper.DataUtil;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.helper.StringUtil;
import dev.sbs.api.util.helper.WordUtil;
import dev.sbs.api.util.search.function.SearchFunction;
import dev.sbs.api.util.tuple.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("all")
public class SkyBlockIsland {

    private static final DecimalFormat smallDecimalFormat = new DecimalFormat("#0.#");

    @SerializedName("profile_id")
    @Getter private UUID islandId;
    @SerializedName("community_upgrades")
    private CommunityUpgrades communityUpgrades;
    private Banking banking;
    @SerializedName("cute_name")
    private String profileName;
    @SerializedName("game_mode")
    private String gameMode;
    @Getter ConcurrentLinkedList<Member> members = Concurrent.newLinkedList();

    public Optional<Banking> getBanking() {
        return Optional.ofNullable(this.banking);
    }

    public Collection getCollection(SkillModel type) {
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

    public Optional<String> getGameMode() {
        return Optional.ofNullable(this.gameMode);
    }

    public Optional<Member> getMember(int index) {
        if (!this.members.isEmpty() && Range.between(0, this.members.size()).contains(index))
            return Optional.of(Concurrent.newList(this.members).get(index));

        return Optional.empty();
    }

    public Optional<Member> getMember(UUID uniqueId) {
        for (Member profile : this.members) {
            if (profile.getUniqueId().equals(uniqueId))
                return Optional.of(profile);
        }

        return Optional.empty();
    }

    public Optional<ProfileModel> getProfileName() {
        return SimplifiedApi.getRepositoryOf(ProfileModel.class).findFirst(ProfileModel::getKey, this.profileName.toUpperCase());
    }

    public Minion getMinion(String minionName) {
        return this.getMinion(SimplifiedApi.getRepositoryOf(MinionModel.class).findFirstOrNull(SearchFunction.Match.ANY, Pair.of(MinionModel::getKey, minionName), Pair.of(MinionModel::getName, minionName)));
    }

    public Minion getMinion(MinionModel minionModel) {
        Minion minion = new Minion(minionModel);

        for (Member member : this.getMembers()) {
            Minion profileMinion = member.getMinion(minionModel);
            minion.unlocked.addAll(profileMinion.unlocked);
        }

        return minion;
    }

    public PlayerStats getPlayerStats(Member member) {
        return this.getPlayerStats(member, true);
    }

    public PlayerStats getPlayerStats(Member member, boolean calculateBonus) {
        return new PlayerStats(this, member, calculateBonus);
    }

    public boolean hasMember(UUID uniqueId) {
        return this.getMember(uniqueId).isPresent();
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
        @Getter private ConcurrentList<String> tutorial = Concurrent.newList();
        @SerializedName("visited_zones")
        @Getter private ConcurrentList<String> visited_zones = Concurrent.newList();
        @SerializedName("achievement_spawned_island_types")
        @Getter private ConcurrentList<String> spawnedIslandTypes = Concurrent.newList();

        // Miscellaneous
        @Getter private ConcurrentList<PetInfo> pets = Concurrent.newList();
        @Getter private ConcurrentMap<String, Double> stats = Concurrent.newMap();
        private ConcurrentLinkedMap<String, Objective> objectives = Concurrent.newLinkedMap();
        private ConcurrentLinkedMap<String, Quest> quests = Concurrent.newLinkedMap();
        @SerializedName("crafted_generators")
        private ConcurrentList<String> craftedMinions = Concurrent.newList();
        @SerializedName("sacks_counts")
        private ConcurrentLinkedMap<String, Integer> sacksCounts = Concurrent.newLinkedMap();
        private ConcurrentLinkedMap<String, Slayer.SlayerBoss> slayer_bosses = Concurrent.newLinkedMap();
        private ConcurrentList<String> unlocked_coll_tiers = Concurrent.newList();
        private ConcurrentLinkedMap<String, Integer> collection = Concurrent.newLinkedMap();
        private MelodyHarp melodyHarp;
        @SerializedName("active_effects")
        @Getter private ConcurrentList<Potion> activePotions = Concurrent.newList();
        @SerializedName("paused_effects")
        @Getter private ConcurrentList<Potion> pausedPotions = Concurrent.newList();
        @SerializedName("disabled_potion_effects")
        @Getter private ConcurrentList<String> disabledPotions = Concurrent.newList();
        @SerializedName("temp_stat_buffs")
        @Getter private ConcurrentList<CenturyCake> centuryCakes = Concurrent.newList();
        @SerializedName("griffin.burrows")
        private ConcurrentList<GriffinBurrow> griffinBurrows = Concurrent.newList();
        @SerializedName("mining_core")
        private Mining mining;
        @SerializedName("jacob2")
        private JacobsFarming jacobsFarming;
        @SerializedName("forge.forge_processes.forge_1")
        @Getter private ConcurrentList<ForgeItem> forgeItems = Concurrent.newList();
        @Getter private Dungeons dungeons;
        @Getter private Experimentation experimentation;

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
        private double experience_skill_social = 0; // NOT IN USE

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
        @Getter private ConcurrentMap<String, Integer> essencePerks = Concurrent.newMap();

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
        private ConcurrentMap<Integer, NbtContent> backpackContents = Concurrent.newMap();
        @SerializedName("backpack_icons")
        private ConcurrentMap<Integer, NbtContent> backpackIcons = Concurrent.newMap();

        public Optional<PetInfo> getActivePet() {
            return this.getPets().stream().filter(PetInfo::isActive).findFirst();
        }

        public Backpacks getBackpacks() {
            return new Backpacks(this.backpackContents, this.backpackIcons);
        }

        public Collection getCollection(SkillModel type) {
            Collection collection = new Collection(type);

            // Fill Collection
            SimplifiedApi.getRepositoryOf(CollectionItemModel.class)
                .findAll(
                    SearchFunction.combine(
                        SearchFunction.combine(CollectionItemModel::getCollection, CollectionModel::getSkill),
                        SkillModel::getKey
                    ),
                    type.getKey()
                )
                .forEach(collectionItemModel -> {
                    collection.collected.put(collectionItemModel, this.collection.getOrDefault(collectionItemModel.getItem().getItemId(), 0));

                    this.unlocked_coll_tiers.stream()
                        .filter(tier -> tier.matches(FormatUtil.format("^{0}_[\\d]+$", collectionItemModel.getItem().getItemId())))
                        .forEach(tier -> {
                            int current = collection.unlocked.getOrDefault(collectionItemModel, 0);
                            int unlocked = Math.max(current, Integer.parseInt(tier.replace(FormatUtil.format("{0}_", collectionItemModel.getItem().getItemId()), "")));
                            collection.unlocked.put(collectionItemModel, unlocked);
                        });
                });

            return collection;
        }

        public Essence getEssence() {
            return new Essence(this.essenceUndead, this.essenceDiamond, this.essenceDragon, this.essenceGold, this.essenceIce, this.essenceWither, this.essenceSpider);
        }

        public Optional<JacobsFarming> getJacobsFarming() {
            return Optional.ofNullable(this.jacobsFarming);
        }

        public Optional<MelodyHarp> getMelodyHarp() {
            return Optional.ofNullable(this.melodyHarp);
        }

        public Optional<Mining> getMining() {
            return Optional.ofNullable(this.mining);
        }

        public Minion getMinion(MinionModel minionModel) {
            Minion minion = new Minion(minionModel);

            if (this.craftedMinions != null) {
                minion.unlocked.addAll(
                    this.craftedMinions.stream()
                        .filter(item -> item.matches(FormatUtil.format("^{0}_[\\d]+$", minionModel.getCollection().getSkill().getKey())))
                        .map(item -> Integer.parseInt(item.replace(FormatUtil.format("{0}_", minionModel.getCollection().getSkill().getKey()), "")))
                        .collect(Collectors.toList())
                );
            }

            return minion;
        }

        public ConcurrentList<Minion> getMinions() {
            return SimplifiedApi.getRepositoryOf(MinionModel.class).findAll().stream().map(this::getMinion).collect(Concurrent.toList());
        }

        public ConcurrentLinkedMap<String, Objective> getObjectives() {
            return this.getObjectives(null);
        }

        public ConcurrentLinkedMap<String, Objective> getObjectives(BasicObjective.Status status) {
            return this.objectives.stream()
                .filter(entry -> status == null || entry.getValue().getStatus() == status)
                .sorted(Comparator.comparingLong(o -> o.getValue().getCompleted().getRealTime()))
                .collect(Concurrent.toLinkedMap());
        }

        public int getPetScore() {
            int petScore = 0;
            ConcurrentList<String> noDuplicatePets = Concurrent.newList();

            for (PetInfo petInfo : this.getPets()) {
                if (noDuplicatePets.contains(petInfo.getName()))
                    continue;

                petScore += petInfo.getRarity().getOrdinal() + 1;
                noDuplicatePets.add(petInfo.getName());
            }

            return petScore;
        }

        public ConcurrentLinkedMap<String, Quest> getQuests() {
            return this.getQuests(null);
        }

        public ConcurrentLinkedMap<String, Quest> getQuests(BasicObjective.Status status) {
            return this.quests.stream().filter(entry -> status == null || entry.getValue().getStatus() == status).sorted(Comparator.comparingLong(o -> o.getValue().getCompleted().getRealTime())).collect(Concurrent.toLinkedMap());
        }

        public Skill getSkill(SkillModel skillModel) {
            double experience = (double) Reflection.of(Member.class).getValue(FormatUtil.format("experience_skill_{0}", skillModel.getKey().toLowerCase()), this);
            return new Skill(skillModel, experience, (skillModel.getKey().equals("FARMING") ? 10 - this.getJacobsFarming().map(JacobsFarming::getPerks).map(perk -> perk.getOrDefault(JacobsFarming.Perk.FARMING_LEVEL_CAP, 0)).orElse(0) : 0));
        }

        public ConcurrentList<Skill> getSkills() {
            return this.getSkills(true);
        }

        public ConcurrentList<Skill> getSkills(boolean cosmetic) {
            return SimplifiedApi.getRepositoryOf(SkillModel.class)
                .findAll()
                .stream()
                .filter(skillModel -> cosmetic || !skillModel.isCosmetic())
                .map(skillModel -> this.getSkill(skillModel))
                .collect(Concurrent.toList());
        }

        public Slayer getSlayer(SlayerModel slayerModel) {
            Slayer.SlayerBoss slayerBoss = this.slayer_bosses.get(slayerModel.getKey().toLowerCase());
            return new Slayer(slayerModel, slayerBoss != null ? slayerBoss : new Slayer.SlayerBoss());
        }

        public ConcurrentList<Slayer> getSlayers() {
            return SimplifiedApi.getRepositoryOf(SlayerModel.class)
                .findAll()
                .stream()
                .map(slayerModel -> this.getSlayer(slayerModel))
                .collect(Concurrent.toList());
        }

        public Sack getSack(SackModel sackModel) {
            Sack sack = new Sack(sackModel);

            SimplifiedApi.getRepositoryOf(SackItemModel.class)
                .findAll(SackItemModel::getSack, sackModel)
                .stream()
                .map(sackItem -> Pair.of(sackItem, this.sacksCounts.getOrDefault(sackItem.getItem().getItemId(), 0)))
                .forEach(entry -> sack.stored.put(entry.getKey(), entry.getValue()));

            return sack;
        }

        public double getSkillAverage() {
            ConcurrentList<Skill> skills = this.getSkills(false);
            return skills.stream().mapToDouble(Skill::getLevel).sum() / skills.size();
        }

        public double getSkillExperience() {
            ConcurrentList<Skill> skills = this.getSkills(false);
            return skills.stream().mapToDouble(Skill::getExperience).sum();
        }

        public double getSkillProgressPercentage() {
            ConcurrentList<Skill> skills = this.getSkills(false);
            return skills.stream().mapToDouble(Skill::getTotalProgressPercentage).sum() / skills.size();
        }

        public double getSlayerAverage() {
            ConcurrentList<Slayer> slayers = this.getSlayers();
            return slayers.stream().mapToDouble(Slayer::getLevel).sum() / slayers.size();
        }

        public double getSlayerExperience() {
            ConcurrentList<Slayer> slayers = this.getSlayers();
            return slayers.stream().mapToDouble(Slayer::getExperience).sum();
        }

        public double getSlayerProgressPercentage() {
            ConcurrentList<Slayer> slayers = this.getSlayers();
            return slayers.stream().mapToDouble(Slayer::getTotalProgressPercentage).sum() / slayers.size();
        }

        public double getDungeonClassAverage() {
            ConcurrentList<Dungeon.Class> dungeonClasses = this.getDungeons().getClasses();
            return dungeonClasses.stream().mapToDouble(Dungeon.Class::getLevel).sum() / dungeonClasses.size();
        }

        public double getDungeonClassExperience() {
            ConcurrentList<Dungeon.Class> dungeonClasses = this.getDungeons().getClasses();
            return dungeonClasses.stream().mapToDouble(Dungeon.Class::getExperience).sum();
        }

        public double getDungeonClassProgressPercentage() {
            ConcurrentList<Dungeon.Class> dungeonClasses = this.getDungeons().getClasses();
            return dungeonClasses.stream().mapToDouble(Dungeon.Class::getTotalProgressPercentage).sum() / dungeonClasses.size();
        }

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

        public ConcurrentMap<Skill, Experience.Weight> getSkillWeight() {
            return SimplifiedApi.getRepositoryOf(SkillModel.class)
                .findAll()
                .parallelStream()
                .filter(skillModel -> !skillModel.isCosmetic())
                .map(this::getSkill)
                .map(skill -> Pair.of(skill, skill.getWeight()))
                .collect(Concurrent.toMap());
        }

        public ConcurrentMap<Slayer, Experience.Weight> getSlayerWeight() {
            return SimplifiedApi.getRepositoryOf(SlayerModel.class)
                .findAll()
                .parallelStream()
                .map(this::getSlayer)
                .map(slayer -> Pair.of(slayer, slayer.getWeight()))
                .collect(Concurrent.toMap());
        }

        public ConcurrentMap<Dungeon, Experience.Weight> getDungeonWeight() {
            return SimplifiedApi.getRepositoryOf(DungeonModel.class)
                .findAll()
                .parallelStream()
                .map(this.getDungeons()::getDungeon)
                .map(dungeon -> Pair.of(dungeon, dungeon.getWeight()))
                .collect(Concurrent.toMap());
        }

        public ConcurrentMap<Dungeon.Class, Experience.Weight> getDungeonClassWeight() {
            return SimplifiedApi.getRepositoryOf(DungeonClassModel.class)
                .findAll()
                .parallelStream()
                .map(this.getDungeons()::getClass)
                .map(dungeonClass -> Pair.of(dungeonClass, dungeonClass.getWeight()))
                .collect(Concurrent.toMap());
        }

        public boolean hasStorage(Storage type) {
            return this.getStorage(type) != null;
        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Backpacks {

        @Getter private final ConcurrentMap<Integer, NbtContent> contents;
        @Getter private final ConcurrentMap<Integer, NbtContent> icons;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Banking {

        @Getter private double balance;
        @Getter private ConcurrentList<Banking.Transaction> transactions = Concurrent.newList();

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Transaction {

            @Getter private double amount;
            @Getter private SkyBlockDate.RealTime timestamp;
            @Getter private Banking.Transaction.Action action;
            @SerializedName("initiator_name") private String initiatorName;

            public String getInitiatorName() {
                return this.initiatorName.replace("Â", ""); // API Artifact
            }

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
            private ConcurrentMap<Integer, Integer> attempts = Concurrent.newMap();
            private ConcurrentMap<Integer, Integer> claims = Concurrent.newMap();
            @SerializedName("best_score")
            private ConcurrentMap<Integer, Integer> bestScore = Concurrent.newMap();

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GriffinBurrow {

        @SerializedName("ts")
        @Getter private SkyBlockDate.RealTime timestamp;
        private int x;
        private int y;
        private int z;
        private int tier; // Rarity (No Griffin is -1)
        @Getter private Type type; // Start/Empty, Mob, Treasure
        @Getter private int chain; // (Position - 1) / 4

        // Type == 0 + Chain == 0, Start
        // Type == 0 + Chain != 0, Empty

        public Vector getCoordinates() {
            return new Vector(this.x, this.y, this.z);
        }

        public RarityModel getGriffinRarity() {
            return SimplifiedApi.getRepositoryOf(RarityModel.class).findFirstOrNull(RarityModel::getOrdinal, this.tier);
        }

        public enum Type {

            @SerializedName("0")
            START,
            @SerializedName("1")
            MOB,
            @SerializedName(value = "2", alternate = { "3" })
            TREASURE

        }

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

        private int stat; // This is in ordinal order in stat menu
        @Getter private String key;
        @Getter private int amount;
        @SerializedName("expire_at")
        @Getter private SkyBlockDate.RealTime expiresAt;

        public StatModel getStat() {
            return SimplifiedApi.getRepositoryOf(StatModel.class).findFirstOrNull(StatModel::getOrdinal, this.stat);
        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Collection {

        @Getter private final SkillModel type;
        @SerializedName("items")
        @Getter private ConcurrentLinkedMap<CollectionItemModel, Integer> collected = Concurrent.newLinkedMap();
        @Getter private ConcurrentLinkedMap<CollectionItemModel, Integer> unlocked = Concurrent.newLinkedMap();

        public int getCollected(CollectionItemModel collection) {
            return this.collected.get(collection);
        }

        public int getUnlocked(CollectionItemModel collection) {
            return this.unlocked.getOrDefault(collection, 0);
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CommunityUpgrades {

        @SerializedName("currently_upgrading")
        private Upgrading upgrading;
        @SerializedName("upgrade_states")
        @Getter private ConcurrentList<Upgraded> upgraded;

        public Optional<Upgrading> getCurrentlyUpgrading() {
            return Optional.ofNullable(this.upgrading);
        }

        public int getHighestTier(ShopProfileUpgradeModel shopProfileUpgradeModel) {
            return this.getUpgraded()
                .stream()
                .filter(upgraded -> upgraded.getUpgradeName().equalsIgnoreCase(shopProfileUpgradeModel.getKey()))
                .sorted((o1, o2) -> Comparator.comparing(Upgraded::getTier).compare(o1, o2))
                .map(Upgraded::getTier)
                .findFirst()
                .orElse(0);
        }

        public ConcurrentList<Upgraded> getUpgrades(ShopProfileUpgradeModel shopProfileUpgradeModel) {
            return this.getUpgraded()
                .stream()
                .filter(upgraded -> upgraded.getUpgradeName().equalsIgnoreCase(shopProfileUpgradeModel.getKey()))
                .sorted((o1, o2) -> Comparator.comparing(Upgraded::getTier).compare(o1, o2))
                .collect(Concurrent.toList());
        }

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

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Upgraded {

            @SerializedName("upgrade")
            @Getter private String upgradeName;
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

            public Optional<ShopProfileUpgradeModel> getUpgrade() {
                return SimplifiedApi.getRepositoryOf(ShopProfileUpgradeModel.class).findFirst(ShopProfileUpgradeModel::getKey, this.getUpgradeName().toUpperCase());
            }

        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Upgrading {

            @SerializedName("upgrade")
            @Getter private String upgradeName;
            @SerializedName("new_tier")
            @Getter private int newTier;
            @SerializedName("start_ms")
            @Getter private SkyBlockDate.RealTime started;
            @SerializedName("who_started")
            @Getter private String startedBy;

            public Optional<ShopProfileUpgradeModel> getUpgrade() {
                return SimplifiedApi.getRepositoryOf(ShopProfileUpgradeModel.class).findFirst(ShopProfileUpgradeModel::getKey, this.getUpgradeName().toUpperCase());
            }

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Dungeons {

        @SerializedName("dungeons_blah_blah")
        private ConcurrentSet<String> dungeonsBlahBlah;
        @SerializedName("selected_dungeon_class")
        private String selectedClass;
        @SerializedName("dungeon_journal.journal_entries")
        @Getter private ConcurrentMap<String, ConcurrentList<Integer>> journalEntries;
        @SerializedName("player_classes")
        private ConcurrentMap<String, Dungeon.Class> playerClasses;
        @SerializedName("dungeon_types")
        private ConcurrentMap<String, Dungeon> types;

        public Dungeon.Class getClass(DungeonClassModel dungeonClassModel) {
            Dungeon.Class dungeonClass = this.playerClasses.get(dungeonClassModel.getKey().toLowerCase());
            dungeonClass.type = dungeonClassModel;
            return dungeonClass;
        }

        public ConcurrentList<Dungeon.Class> getClasses() {
            return this.playerClasses.stream()
                .map(entry -> this.getClass(SimplifiedApi.getRepositoryOf(DungeonClassModel.class)
                    .findFirstOrNull(DungeonClassModel::getKey, entry.getKey()))
                )
                .collect(Concurrent.toList());
        }

        public Dungeon getDungeon(DungeonModel dungeonModel) {
            Dungeon dungeon = this.types.get(dungeonModel.getKey().toLowerCase());
            dungeon.type = dungeonModel;
            return dungeon;
        }

        public Optional<Dungeon.Class> getSelectedClass() {
            return this.getSelectedClassModel().map(this::getClass);
        }

        public Optional<DungeonClassModel> getSelectedClassModel() {
            return SimplifiedApi.getRepositoryOf(DungeonClassModel.class).findFirst(DungeonClassModel::getKey, this.selectedClass.toUpperCase());
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Dungeon extends Experience implements Weighted {

        @Getter private DungeonModel type;
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

        public ConcurrentMap<Floor, Double> getMostDamage(DungeonClassModel dungeonClassModel) {
            switch (dungeonClassModel.getKey()) {
                case "HEALER":
                    return this.getMostDamageHealer();
                case "MAGE":
                    return this.getMostDamageMage();
                case "BERSERK":
                    return this.getMostDamageBerserk();
                case "ARCHER":
                    return this.getMostDamageArcher();
                case "TANK":
                    return this.getMostDamageTank();
                default:
                    return null;
            }
        }

        public double getMostDamage(DungeonClassModel dungeonClassModel, Floor floor) {
            return this.getMostDamage(dungeonClassModel).get(floor);
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
        public ConcurrentList<Double> getExperienceTiers() {
            return SimplifiedApi.getRepositoryOf(DungeonLevelModel.class)
                .findAll()
                .stream()
                .map(DungeonLevelModel::getTotalExpRequired)
                .collect(Concurrent.toList());
        }

        @Override
        public int getMaxLevel() {
            return this.getExperienceTiers().size();
        }

        @Override
        public Weight getWeight() {
            double rawLevel = this.getRawLevel();
            ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
            double maxDungeonClassExperienceRequired = experienceTiers.get(experienceTiers.size() - 1);

            if (rawLevel < this.getMaxLevel())
                rawLevel += (this.getProgressPercentage() / 100); // Add Percentage Progress to Next Level

            double base = Math.pow(rawLevel, 4.5) * this.getType().getWeightMultiplier();
            double weightValue = NumberUtil.round(base, 2);
            double weightOverflow = 0;

            if (this.getExperience() > maxDungeonClassExperienceRequired) {
                double overflow = Math.pow((this.getExperience() - maxDungeonClassExperienceRequired) / (4 * maxDungeonClassExperienceRequired / base), 0.968);
                weightOverflow = NumberUtil.round(overflow, 2);
            }

            return new Weight(weightValue, weightOverflow);
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

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Class extends Experience implements Weighted {

            @Getter private DungeonClassModel type;
            @Getter private double experience;

            @Override
            public ConcurrentList<Double> getExperienceTiers() {
                return SimplifiedApi.getRepositoryOf(DungeonLevelModel.class)
                    .findAll()
                    .stream()
                    .map(DungeonLevelModel::getTotalExpRequired)
                    .collect(Concurrent.toList());
            }

            @Override
            public int getMaxLevel() {
                return this.getExperienceTiers().size();
            }

            @Override
            public Weight getWeight() {
                double rawLevel = this.getRawLevel();
                ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
                double maxDungeonClassExperienceRequired = experienceTiers.get(experienceTiers.size() - 1);

                if (rawLevel < this.getMaxLevel())
                    rawLevel += (this.getProgressPercentage() / 100); // Add Percentage Progress to Next Level

                double base = Math.pow(rawLevel, 4.5) * this.getType().getWeightMultiplier();
                double weightValue = NumberUtil.round(base, 2);
                double weightOverflow = 0;

                if (this.getExperience() > maxDungeonClassExperienceRequired) {
                    double overflow = Math.pow((this.getExperience() - maxDungeonClassExperienceRequired) / (4 * maxDungeonClassExperienceRequired / base), 0.968);
                    weightOverflow = NumberUtil.round(overflow, 2);
                }

                return new Weight(weightValue, weightOverflow);
            }

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
            private String dungeonClass;
            @Getter private ConcurrentList<UUID> teammates;
            @SerializedName("deaths")
            @Getter private int deaths;
            @SerializedName("mobs_killed")
            @Getter private int mobsKilled;
            @SerializedName("secrets_found")
            @Getter private int secretsFound;

            public DungeonClassModel getDungeonClass() {
                return SimplifiedApi.getRepositoryOf(DungeonClassModel.class).findFirstOrNull(DungeonClassModel::getKey, this.dungeonClass.toUpperCase());
            }

        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Essence {

        @Getter
        private final int undead;
        @Getter
        private final int diamond;
        @Getter
        private final int dragon;
        @Getter
        private final int gold;
        @Getter
        private final int ice;
        @Getter
        private final int wither;
        @Getter
        private final int spider;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ForgeItem {

        @Getter
        private String type;
        private String id;
        @Getter
        private SkyBlockDate.RealTime startTime;
        @Getter
        private int slot;
        @Getter
        private boolean notified;

        public ItemModel getItem() {
            return SimplifiedApi.getRepositoryOf(ItemModel.class).findFirstOrNull(ItemModel::getItemId, this.id);
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class JacobsFarming {

        @SerializedName("medals_inv")
        @Getter private ConcurrentMap<Medal, Integer> medalInventory = Concurrent.newMap();
        @Getter private ConcurrentMap<Perk, Integer> perks = Concurrent.newMap();
        @SerializedName("unique_golds2")
        private ConcurrentSet<String> uniqueGolds = Concurrent.newSet();
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
            return this.getMedalInventory().getOrDefault(medal, 0);
        }

        public int getPerk(Perk perk) {
            return this.getPerks().getOrDefault(perk, 0);
        }

        public ConcurrentSet<CollectionItemModel> getUniqueGolds() {
            return SimplifiedApi.getRepositoryOf(CollectionItemModel.class)
                .findAll()
                .stream()
                .filter(collectionItem -> uniqueGolds.contains(collectionItem.getItem().getItemId()))
                .collect(Concurrent.toSet());
        }

        public boolean hasTalked() {
            return this.talked;
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

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MelodyHarp {

        @Getter private boolean talismanClaimed;
        @Getter private String selectedSong;
        @Getter private SkyBlockDate.RealTime selectedSongTimestamp;
        @Getter private ConcurrentMap<String, Song> songs = Concurrent.newMap();

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Song {

            @Getter private final int bestCompletion;
            @Getter private final int completions;
            @Getter private final int perfectCompletions;

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Mining {

        private ConcurrentMap<String, Object> nodes = Concurrent.newMap();
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
        @Getter private ConcurrentMap<Crystal.Type, Crystal> crystals = Concurrent.newMap();

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

        public ConcurrentMap<String, Double> getNodes() {
            return this.nodes.stream().filter(entry -> !(entry.getValue() instanceof Boolean)).collect(Concurrent.toMap());
        }

        public ConcurrentMap<String, Boolean> getToggles() {
            if (this.toggles == null) {
                this.toggles = this.nodes.stream()
                    .filter(entry -> (entry.getValue() instanceof Boolean))
                    .map(entry -> Pair.of(entry.getKey().replace("toggle_", ""), (boolean) entry.getValue()))
                    .collect(Concurrent.toMap());
            }

            return this.toggles;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Biome {

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Dwarven {

                @SerializedName("statues_placed")
                @Getter private ConcurrentList<Object> placedStatues = Concurrent.newList();

            }

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Precursor {

                @SerializedName("parts_delivered")
                @Getter private ConcurrentList<Object> deliveredParts = Concurrent.newList();

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

        @Getter private final MinionModel type;
        @Getter private ConcurrentSet<Integer> unlocked = Concurrent.newSet();

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NbtContent {

        private int type; // Always 0
        @SerializedName("data")
        @Getter private String rawData;

        public byte[] getData() {
            return DataUtil.decode(this.getRawData().toCharArray());
        }

        public CompoundTag getNbtData() throws NbtException {
            return SimplifiedApi.getNbtFactory().fromBase64(this.getRawData());
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Objective extends BasicObjective {

        @Getter private int progress;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PetInfo extends Experience {

        @SerializedName("uuid")
        @Getter private UUID uniqueId;
        @SerializedName("type")
        @Getter private String name;
        @SerializedName("exp")
        @Getter private double experience;
        @Getter private boolean active;
        @SerializedName("tier")
        private String rarityKey;
        @Getter private int candyUsed;
        private String heldItem;
        private String skin;

        @Override
        public ConcurrentList<Double> getExperienceTiers() {
            int petExpOffset = this.getRarity().getPetExpOffset();
            ConcurrentList<PetExpScaleModel> petExpScaleModels = SimplifiedApi.getRepositoryOf(PetExpScaleModel.class).findAll();

            // Load Experience Block
            ConcurrentList<Double> petExpScaleValues = petExpScaleModels
                .subList(petExpOffset, petExpOffset + this.getMaxLevel())
                .stream()
                .map(PetExpScaleModel::getValue)
                .collect(Concurrent.toList());

            return petExpScaleValues;
        }

        public Optional<String> getDefaultSkin() {
            return this.getPet().map(PetModel::getSkin);
        }

        public Optional<ItemModel> getHeldItem() {
            return SimplifiedApi.getRepositoryOf(ItemModel.class).findFirst(ItemModel::getItemId, this.heldItem);
        }

        @Override
        public int getMaxLevel() {
            return this.getPet().map(PetModel::getMaxLevel).orElse(100);
        }

        public Optional<PetModel> getPet() {
            return SimplifiedApi.getRepositoryOf(PetModel.class).findFirst(PetModel::getKey, this.getName());
        }

        public String getPrettyName() {
            return WordUtil.capitalizeFully(this.getName().replace("_", " "));
        }

        public RarityModel getRarity() {
            return SimplifiedApi.getRepositoryOf(RarityModel.class).findFirstOrNull(RarityModel::getKey, this.rarityKey);
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
        @Getter private ConcurrentList<Modifier> modifiers = Concurrent.newList();

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Modifier {

            @Getter private String key;
            @SerializedName("amp")
            @Getter private int amplifier;

        }

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

        @Getter private final SackModel type;
        @Getter private final ConcurrentMap<SackItemModel, Integer> stored = Concurrent.newMap();

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Skill extends Experience implements Weighted {

        @Getter private final SkillModel type;
        private final double experience;
        private final int levelSubtractor;

        @Override
        public double getExperience() {
            return Math.max(this.experience, 0);
        }

        @Override
        public ConcurrentList<Double> getExperienceTiers() {
            return SimplifiedApi.getRepositoryOf(SkillLevelModel.class)
                .findAll()
                .stream()
                .filter(slayerLevel -> slayerLevel.getSkill().getKey().equals(this.getType().getKey()))
                .map(SkillLevelModel::getTotalExpRequired)
                .collect(Concurrent.toList());
        }

        @Override
        public int getLevelSubtractor() {
            return this.getType().getKey().equals("FARMING") ? this.levelSubtractor : super.getLevelSubtractor();
        }

        @Override
        public int getMaxLevel() {
            return this.getType().getMaxLevel();
        }

        @Override
        public Weight getWeight() {
            double rawLevel = this.getRawLevel();
            ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
            double maxSkillExperienceRequired = experienceTiers.get(experienceTiers.size() - 1);

            if (rawLevel < this.getMaxLevel())
                rawLevel += (this.getProgressPercentage() / 100); // Add Percentage Progress to Next Level

            double base = Math.pow(rawLevel * 10, 0.5 + this.getType().getWeightExponent() + (rawLevel / 100.0)) / 1250;
            double weightValue = NumberUtil.round(base, 2);
            double weightOverflow = 0;

            if (this.getExperience() > maxSkillExperienceRequired) {
                double overflow = Math.pow((this.getExperience() - maxSkillExperienceRequired) / this.getType().getWeightDivider(), 0.968);
                weightOverflow = NumberUtil.round(overflow, 2);
            }

            return new Weight(weightValue, weightOverflow);
        }

    }

    public static class Slayer extends Experience implements Weighted {

        private final static Reflection<SlayerBoss> slayerBossRef = Reflection.of(SlayerBoss.class);
        private final ConcurrentMap<Integer, Boolean> claimed = Concurrent.newMap();
        private final ConcurrentMap<Integer, Boolean> claimedSpecial = Concurrent.newMap();
        private final ConcurrentMap<Integer, Integer> kills = Concurrent.newMap();
        @Getter private final SlayerModel type;
        @Getter private final double experience;

        private Slayer(SlayerModel type, SlayerBoss slayerBoss) {
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
                this.kills.put(i + 1, (int) slayerBossRef.getValue(FormatUtil.format("boss_kills_tier_{0}", i), slayerBoss));
        }

        @Override
        public ConcurrentList<Double> getExperienceTiers() {
            return SimplifiedApi.getRepositoryOf(SlayerLevelModel.class)
                .findAll()
                .stream()
                .filter(slayerLevel -> slayerLevel.getSlayer().getKey().equals(this.getType().getKey()))
                .map(SlayerLevelModel::getTotalExpRequired)
                .collect(Concurrent.toList());
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

        @Override
        public Weight getWeight() {
            ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
            double maxSlayerExperienceRequired = experienceTiers.get(experienceTiers.size() - 1);
            double base = Math.min(this.getExperience(), maxSlayerExperienceRequired) / this.getType().getWeightDivider();
            double weightValue = NumberUtil.round(base, 2);
            double weightOverflow = 0;

            if (this.getExperience() > maxSlayerExperienceRequired) {
                double remaining = this.getExperience() - maxSlayerExperienceRequired;
                double overflow = 0;
                double modifier = this.getType().getWeightModifier();

                while (remaining > 0) {
                    double left = Math.min(remaining, maxSlayerExperienceRequired);
                    overflow += Math.pow(left / (this.getType().getWeightDivider() * (1.5 + modifier)), 0.942);
                    remaining -= left;
                    modifier += modifier;
                }

                weightOverflow = NumberUtil.round(overflow, 2);
            }

            return new Weight(weightValue, weightOverflow);
        }

        static class SlayerBoss {

            private ConcurrentMap<String, Boolean> claimed_levels = Concurrent.newMap(); // level_#: true
            private int boss_kills_tier_0;
            private int boss_kills_tier_1;
            private int boss_kills_tier_2;
            private int boss_kills_tier_3;
            private int boss_kills_tier_4;
            private int xp;

        }

    }

    public abstract static class Experience {

        public int getStartingLevel() {
            return 0;
        }

        public abstract double getExperience();

        public abstract ConcurrentList<Double> getExperienceTiers();

        public int getLevelSubtractor() {
            return 0;
        }

        public final int getLevel() {
            return this.getLevel(this.getExperience());
        }

        public final int getLevel(double experience) {
            return Math.min(this.getRawLevel(experience), this.getMaxLevel() - this.getLevelSubtractor());
        }

        public final int getRawLevel() {
            return this.getRawLevel(this.getExperience());
        }

        public final int getRawLevel(double experience) {
            ConcurrentList<Double> experienceTiers = this.getExperienceTiers();

            return IntStream.range(0, experienceTiers.size())
                .filter(index -> experienceTiers.get(index) > experience)
                .findFirst()
                .orElseGet(this::getMaxLevel);
        }

        public abstract int getMaxLevel();

        public final double getNextExperience() {
            return this.getNextExperience(this.getExperience());
        }

        public final double getNextExperience(double experience) {
            ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
            int rawLevel = this.getRawLevel(experience);

            if (rawLevel == 0)
                return experienceTiers.get(0);
            else if (rawLevel >= this.getMaxLevel())
                return 0;
            else
                return experienceTiers.get(rawLevel) - experienceTiers.get(rawLevel - 1);
        }

        public final double getProgressExperience() {
            return this.getProgressExperience(this.getExperience());
        }

        public final double getProgressExperience(double experience) {
            ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
            int rawLevel = this.getRawLevel(experience);

            if (rawLevel == 0)
                return experience;
            else if (rawLevel >= this.getMaxLevel())
                return experience - experienceTiers.get(experienceTiers.size() - 1);
            else
                return experience - experienceTiers.get(rawLevel - 1);
        }

        public final double getMissingExperience() {
            return this.getMissingExperience(this.getExperience());
        }

        public final double getMissingExperience(double experience) {
            ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
            int rawLevel = this.getRawLevel(experience);
            return rawLevel >= this.getMaxLevel() ? 0 : (experienceTiers.get(rawLevel) - experience);
        }

        public final double getProgressPercentage() {
            return this.getProgressPercentage(this.getExperience());
        }

        public final double getProgressPercentage(double experience) {
            return (this.getProgressExperience(experience) / this.getNextExperience(experience)) * 100.0;
        }

        public final double getTotalExperience() {
            return this.getExperienceTiers()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        }

        public final double getTotalProgressPercentage() {
            return (this.getExperience() / this.getTotalExperience()) * 100.0;
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Weight {

            @Getter private final double value;
            @Getter private final double overflow;

        }

    }

    interface Weighted {

        Experience.Weight getWeight();

    }

    public static class Deserializer implements JsonDeserializer<SkyBlockIsland> {

        private <T extends JsonElement, R> Optional<R> wrapObject(JsonObject jsonObject, String memberName, Function<T, R> function) {
            return jsonObject.has(memberName) ? Optional.of(function.apply((T) jsonObject.get(memberName))) : Optional.empty();
        }

        @Override
        public SkyBlockIsland deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            Gson gson = SimplifiedApi.getGson();
            SkyBlockIsland skyBlockIsland = new SkyBlockIsland();
            JsonObject rootObject = jsonElement.getAsJsonObject();
            skyBlockIsland.islandId = StringUtil.toUUID(wrapObject(rootObject, "profile_id", JsonElement::getAsString).get());
            skyBlockIsland.gameMode = wrapObject(rootObject, "game_mode", JsonElement::getAsString).orElse(null);
            skyBlockIsland.banking = gson.fromJson(wrapObject(rootObject, "banking", JsonElement::getAsJsonObject).orElse(null), Banking.class);
            skyBlockIsland.profileName = wrapObject(rootObject, "cute_name", JsonElement::getAsString).orElse(null);
            skyBlockIsland.communityUpgrades = gson.fromJson(wrapObject(rootObject, "community_upgrades", JsonElement::getAsJsonObject).orElse(null), CommunityUpgrades.class);
            Map<String, LinkedTreeMap<String, Object>> memberMap = gson.fromJson(rootObject.getAsJsonObject("members"), Map.class);

            memberMap.forEach((key, value) -> {
                // Member UUID
                String memberJson = gson.toJson(value);
                Member member = gson.fromJson(memberJson, Member.class);
                member.uniqueId = StringUtil.toUUID(key);
                JsonObject memberObject = JsonParser.parseString(memberJson).getAsJsonObject();

                // Melody's Harp
                if (memberObject.has("harp_quest")) {
                    Map<String, Object> harpQuest = gson.fromJson(memberObject.getAsJsonObject("harp_quest"), Map.class);
                    MelodyHarp melodyHarp = new MelodyHarp();

                    melodyHarp.talismanClaimed = harpQuest.containsKey("claimed_talisman") ? (boolean) harpQuest.remove("claimed_talisman") : false;
                    melodyHarp.selectedSong = harpQuest.containsKey("selected_song") ? (String) harpQuest.remove("selected_song") : "";
                    long epoch = NumberUtil.createNumber(String.valueOf(harpQuest.containsKey("selected_song_epoch") ? harpQuest.remove("selected_song_epoch") : 0)).longValue();

                    if (epoch > 0)
                        melodyHarp.selectedSongTimestamp = new SkyBlockDate.RealTime(epoch * 1000);

                    ConcurrentLinkedMap<String, ConcurrentMap<String, Integer>> songMap = Concurrent.newLinkedMap();
                    harpQuest.forEach((harpKey, harpValue) -> {
                        if (harpValue instanceof Number) {
                            String songKey = harpKey.replace("song_", "");
                            String songName = songKey.replaceAll("_((best|perfect)_)?completions?", "");
                            String category = songKey.replace(FormatUtil.format("{0}_", songName), "");

                            if (!songMap.containsKey(songName))
                                songMap.put(songName, Concurrent.newMap());

                            songMap.get(songName).put(category, NumberUtil.createNumber(harpValue.toString()).intValue());
                        }
                    });

                    songMap.stream().forEach(entry -> melodyHarp.songs.put(
                        entry.getKey(),
                        new MelodyHarp.Song(
                            entry.getValue().getOrDefault("best_completion", 0),
                            entry.getValue().getOrDefault("completions", 0),
                            entry.getValue().getOrDefault("perfect_completions", 0)
                        )
                    ));
                    member.melodyHarp = melodyHarp;
                }

                // Mining
                if (memberObject.has("mining_core")) {
                    JsonObject miningCore = memberObject.getAsJsonObject("mining_core");

                    if (miningCore.has("biomes")) {
                        JsonObject miningBiomes = miningCore.getAsJsonObject("biomes");
                        member.mining.dwarvenMinesBiome = gson.fromJson(miningBiomes.getAsJsonObject("dwarven"), Mining.Biome.Dwarven.class);
                        member.mining.precursorCityBiome = gson.fromJson(miningBiomes.getAsJsonObject("precursor"), Mining.Biome.Precursor.class);
                        member.mining.goblinHideoutBiome = gson.fromJson(miningBiomes.getAsJsonObject("goblin"), Mining.Biome.Goblin.class);
                    }
                }

                if (memberObject.has("forge")) {
                    JsonObject forge = memberObject.getAsJsonObject("forge").getAsJsonObject("forge_processes");

                    if (forge.has("forge_1")) {
                        member.forgeItems.addAll(((ConcurrentMap<Integer, ForgeItem>) gson.fromJson(forge.getAsJsonObject("forge_1"), new ConcurrentMap<>().getClass()))
                                                     .stream()
                                                     .map(Map.Entry::getValue)
                                                     .collect(Concurrent.toList()));
                    }
                }

                // Griffin Burrows
                if (memberObject.has("griffin"))
                    member.griffinBurrows = gson.fromJson(memberObject.getAsJsonObject("griffin").getAsJsonArray("burrows"), new ConcurrentList<>().getClass());

                // Experimentation
                if (member.experimentation != null) {
                    ConcurrentMap<String, Experimentation.Table> tableLinkMap = Concurrent.newMap();
                    tableLinkMap.put("pairings", member.experimentation.superpairs);
                    tableLinkMap.put("simon", member.experimentation.chronomatron);
                    tableLinkMap.put("numbers", member.experimentation.ultrasequencer);

                    ConcurrentList<Pair<String, String>> replaceList = Concurrent.newList(
                        Pair.of("attempts", "attempts"),
                        Pair.of("bestScore", "best_score"),
                        Pair.of("claims", "claims")
                    );

                    tableLinkMap.forEach(tableLink -> {
                        ConcurrentMap<String, Double> experimentMap = gson.fromJson(memberObject.getAsJsonObject("experimentation").getAsJsonObject(tableLink.getKey()), ConcurrentMap.class);

                        replaceList.forEach(replacePair -> {
                            ConcurrentMap<Integer, Integer> tableMap = (ConcurrentMap<Integer, Integer>) Reflection.of(Experimentation.Table.class).getValue(replacePair.getLeft(), tableLink.getValue());

                            experimentMap.stream()
                                .filter(entry -> entry.getKey().startsWith(replacePair.getRight()))
                                .map(entry -> Pair.of(Integer.parseInt(entry.getKey().replace(FormatUtil.format("{0}_", replacePair.getRight()), "")), entry.getValue().intValue()))
                                .forEach(entry -> tableMap.put(entry.getKey(), entry.getValue()));
                        });
                    });
                }

                // Add Member
                skyBlockIsland.members.add(member);
            });

            return skyBlockIsland;
        }

    }

}
