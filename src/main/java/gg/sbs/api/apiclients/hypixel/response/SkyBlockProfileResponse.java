package gg.sbs.api.apiclients.hypixel.response;

import com.google.gson.annotations.SerializedName;
import gg.sbs.api.hypixel_old.skyblock.Skyblock;
import gg.sbs.api.nbt.NbtFactory;
import gg.sbs.api.nbt.tags.collection.CompoundTag;
import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.util.*;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;
import gg.sbs.api.util.concurrent.ConcurrentMap;
import gg.sbs.api.util.concurrent.ConcurrentSet;
import gg.sbs.api.util.concurrent.linked.ConcurrentLinkedMap;
import lombok.Getter;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class SkyBlockProfileResponse {

    @Getter private boolean success;
    @SerializedName("profile")
    @Getter private SkyBlockIsland island;

    public static class Island2 {

        @SerializedName("profile_id")
        private String islandId;
        private ConcurrentMap<String, Member> members;
        @SerializedName("community_upgrades")
        private CommunityUpgrades communityUpgrades;
        private Banking banking;
        @SerializedName("cute_name")
        private ProfileName profileName;
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
            return Optional.of(this.banking);
        }

        public Collection getCollection(Skyblock.Skill type) {
            Collection collection = new Collection(type);

            for (Member profile : this.getMembers()) {
                Collection profileCollection = profile.getCollection(type);
                profileCollection.collected.forEach(entry -> collection.collected.put(entry.getKey(), Math.max(collection.collected.getOrDefault(entry.getKey(), 0), entry.getValue())));
                collection.unlocked.putAll(profileCollection.unlocked);
            }

            return collection;
        }

        public Optional<CommunityUpgrades> getCommunityUpgrades() {
            return Optional.of(communityUpgrades);
        }

        public Optional<Member> getCurrentMember() {
            return Optional.of(this.currentMember);
        }

        public Optional<Member> getMember(UUID uniqueId) {
            this.cacheMembers();

            for (Member profile : this.members.values()) {
                if (profile.getUniqueId().equals(uniqueId))
                    return Optional.of(profile);
            }

            return Optional.empty();
        }

        public ConcurrentSet<Member> getMembers() {
            this.cacheMembers();
            return Concurrent.newSet(this.members.values());
        }

        public Optional<ProfileName> getProfileName() {
            return Optional.of(this.profileName);
        }

        public Minion getMinion(Skyblock.Minion type) {
            Minion minion = new Minion(type);

            for (Member member : this.getMembers()) {
                Minion profileMinion = member.getMinion(type);
                minion.unlocked.addAll(profileMinion.unlocked);
            }

            return minion;
        }

        public UUID getIslandId() {
            return StringUtil.toUUID(this.islandId);
        }

        public boolean hasMember(UUID uniqueId) {
            return this.getMember(uniqueId).isPresent();
        }

        public void setCurrentMember(Member currentMember) {
            this.currentMember = currentMember;
        }

        public static class Member {

            // TODO: Missing CuteName

            @Getter private UUID uniqueId;
            @SerializedName("first_join")
            @Getter private Instant firstJoin; // Unix Timestamp (in milliseconds)
            @SerializedName("first_join_hub")
            @Getter private long firstJoinHub; // TODO: SkyBlockTime
            @SerializedName("last_death")
            @Getter private long lastDeath; // TODO: SkyBlockTime
            @SerializedName("death_count")
            @Getter private int deathCount;
            @SerializedName("last_save")
            @Getter private Instant lastSave; // Unix Timestamp (in milliseconds)
            @SerializedName("coin_purse")
            @Getter private double purse;
            @SerializedName("fishing_treasure_caught")
            @Getter private int caughtFishingTreasure;
            @SerializedName("fairy_souls_collected")
            @Getter private int collectedFairySouls;
            @SerializedName("fairy_souls")
            @Getter private int fairySouls;
            @SerializedName("fairy_exchanges")
            @Getter private int fairyExchanges;
            @SerializedName("wardrobe_equipped_slot")
            @Getter private int equippedWardrobeSlot;

            @Getter private ConcurrentList<String> tutorial;
            @SerializedName("visited_zones")
            @Getter private ConcurrentList<String> visited_zones;
            @SerializedName("achievement_spawned_island_types")
            @Getter private ConcurrentList<String> achievement_spawned_island_types;

            @Getter private ConcurrentList<PetInfo> pets;
            @Getter private ConcurrentMap<String, Double> stats;
            private ConcurrentLinkedMap<String, Objective> objectives;
            private ConcurrentLinkedMap<String, Quest> quests;
            @SerializedName("crafted_generators")
            private ConcurrentList<String> craftedMinions;
            private ConcurrentLinkedMap<String, Integer> sacks_counts;
            private ConcurrentLinkedMap<String, SlayerBoss> slayer_bosses;
            private ConcurrentList<String> unlocked_coll_tiers;
            private ConcurrentLinkedMap<String, Integer> collection;

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

            private NbtContent inv_armor;
            private NbtContent inv_contents;
            private NbtContent ender_chest_contents;
            private NbtContent fishing_bag;
            private NbtContent quiver;
            private NbtContent potion_bag;
            private NbtContent talisman_bag;
            private NbtContent candy_inventory_contents;
            private NbtContent wardrobe_contents;

            public long getFirstJoinHub2() {
                return (Skyblock.START_DATE + this.firstJoinHub) * 1000; // TODO: SkyBlockTime
            }

            public long getLastDeath2() {
                return (Skyblock.START_DATE + this.lastDeath) * 1000; // TODO: SkyBlockTime
            }

            public Collection getCollection(Skyblock.Skill type) {
                Collection collection = new Collection(type);
                ConcurrentList<Skyblock.Collection> items = Skyblock.Collection.getItems(type);

                if (this.collection != null) {
                    for (Skyblock.Collection item : items) {
                        collection.collected.put(item, this.collection.getOrDefault(item.getName(), 0));

                        List<String> unlocked = this.unlocked_coll_tiers.stream().filter(tier -> tier.matches(FormatUtil.format("^{0}_[\\d]+$", item.getName()))).collect(Collectors.toList());
                        unlocked.forEach(tier -> {
                            int current = collection.unlocked.getOrDefault(item, 0);
                            collection.unlocked.put(item, Math.max(current, Integer.parseInt(tier.replace(FormatUtil.format("{0}_", item.getName()), ""))));
                        });
                    }
                }

                return collection;
            }

            public Minion getMinion(Skyblock.Minion type) {
                Minion minion = new Minion(type);

                if (this.craftedMinions != null) {
                    minion.unlocked.addAll(
                            this.craftedMinions.stream()
                                    .filter(item -> item.matches(FormatUtil.format("^{0}_[\\d]+$", type.name())))
                                    .map(item -> Integer.parseInt(item.replace(FormatUtil.format("{0}_", type.name()), "")))
                                    .collect(Collectors.toList())
                    );
                }

                return minion;
            }

            public ConcurrentList<Minion> getMinions() {
                return Arrays.stream(Skyblock.Minion.values()).map(this::getMinion).collect(Concurrent.toList());
            }

            public ConcurrentLinkedMap<String, Objective> getObjectives() {
                return this.getObjectives(null);
            }

            public ConcurrentLinkedMap<String, Objective> getObjectives(BasicObjective.Status status) {
                return this.objectives.stream().filter(entry -> status == null || entry.getValue().getStatus() == status).sorted(Comparator.comparingLong(o -> o.getValue().getCompleted().getEpochSecond())).collect(Concurrent.toLinkedMap());
            }

            public int getPetScore() {
                int petScore = 0;

                if (ListUtil.notEmpty(this.getPets())) {
                    for (PetInfo petInfo : this.getPets())
                        petScore += petInfo.getTier().ordinal() + 1;
                }

                return petScore;
            }

            public ConcurrentLinkedMap<String, Quest> getQuests() {
                return this.getQuests(null);
            }

            public ConcurrentLinkedMap<String, Quest> getQuests(BasicObjective.Status status) {
                return this.quests.stream().filter(entry -> status == null || entry.getValue().getStatus() == status).sorted(Comparator.comparingLong(o -> o.getValue().getCompleted().getEpochSecond())).collect(Concurrent.toLinkedMap());
            }

            public Skill getSkill(Skyblock.Skill skill) {
                double experience = (double)new Reflection(Member.class).getValue(FormatUtil.format("experience_skill_{0}", skill.name().toLowerCase()), this);
                return new Skill(skill, experience);
            }

            public Slayer getSlayer(Skyblock.Slayer type) {
                return new Slayer(type, this.slayer_bosses.get(type.getName()));
            }

            public Sack getSack(Skyblock.Sack type) {
                Sack collection = new Sack(type);
                ConcurrentList<Skyblock.Item> items = type.getItems();

                if (this.sacks_counts != null) {
                    for (Skyblock.Item item : items)
                        collection.stored.put(item, this.sacks_counts.getOrDefault(item.getItemName(), 0));
                }

                return collection;
            }

            public double getSkillAverage() {
                ConcurrentList<Skyblock.Skill> skills = Arrays.stream(Skyblock.Skill.values()).filter(skill -> !skill.isCosmetic()).collect(Concurrent.toList());
                double total = skills.stream().mapToInt(skill -> this.getSkill(skill).getLevel()).sum();
                return total / skills.size();
            }

            public NbtContent getStorage(Storage type) {
                switch (type) {
                    case INVENTORY:
                        return inv_contents;
                    case ENDER_CHEST:
                        return ender_chest_contents;
                    case FISHING:
                        return fishing_bag;
                    case QUIVER:
                        return quiver;
                    case POTIONS:
                        return potion_bag;
                    case TALISMANS:
                        return talisman_bag;
                    case CANDY:
                        return candy_inventory_contents;
                    case ARMOR:
                    default:
                        return inv_armor;
                }
            }
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

        public static class Banking {

            @Getter private double balance;
            @Getter private ConcurrentList<Transaction> transactions;

            public static class Transaction {

                @Getter private double amount;
                @Getter private Instant timestamp;
                @Getter private Action action;
                @SerializedName("initiator_name")
                @Getter private String initiatorName;

                public enum Action {

                    WITHDRAW,
                    DEPOSIT

                }

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

        public static class Quest extends BasicObjective {

            @SerializedName("activated_at")
            @Getter private long activated;
            private long activated_at_sb; // TODO: SkyBlockTime
            private long completed_at_sb; // TODO: SkyBlockTime

            private Quest() { }

            public long getActivatedSb() {
                return this.activated_at_sb;
            }

            public long getCompletedSb() {
                return this.completed_at_sb;
            }

        }

        public static class Objective extends BasicObjective {

            @Getter private int progress;

            private Objective() { }

        }

        public static class BasicObjective {

            @Getter private Status status;
            @SerializedName("completed_at")
            @Getter private Instant completed;

            private BasicObjective() { }

            public enum Status {

                ACTIVE,
                COMPLETE;

                public String getName() {
                    return WordUtil.capitalizeFully(this.name());
                }

            }

        }

        public static class Collection {

            @Getter private final Skyblock.Skill type;
            @SerializedName("items")
            @Getter private ConcurrentLinkedMap<Skyblock.Collection, Integer> collected = Concurrent.newLinkedMap();
            @Getter private ConcurrentLinkedMap<Skyblock.Collection, Integer> unlocked = Concurrent.newLinkedMap();

            Collection(Skyblock.Skill type) {
                this.type = type;
            }

            public int getCollected(Skyblock.Collection collection) {
                return this.collected.get(collection);
            }

            public int getUnlocked(Skyblock.Collection collection) {
                return this.unlocked.getOrDefault(collection, 0);
            }

        }

        public static class CommunityUpgrades {

            @SerializedName("currently_upgrading")
            private UpgradeState.Upgrade currentlyUpgrading;
            @SerializedName("upgrade_states")
            @Getter private ConcurrentList<UpgradeState> upgradeStates;

            public Optional<UpgradeState.Upgrade> getCurrentlyUpgrading() {
                return Optional.of(this.currentlyUpgrading);
            }

            public static class UpgradeState {

                @Getter private Upgrade upgrade;
                @Getter private int tier;
                @SerializedName("started_ms")
                @Getter private Instant started;
                @SerializedName("started_by")
                @Getter private String startedBy;
                @SerializedName("claimed_ms")
                @Getter private Instant claimed;
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

        public static class Minion {

            @Getter private ConcurrentSet<Integer> unlocked = Concurrent.newSet();
            @Getter private final Skyblock.Minion type;

            Minion(Skyblock.Minion type) {
                this.type = type;
            }

        }

        public static class NbtContent {

            private int type; // Always 0
            @SerializedName("data")
            @Getter private String rawData;

            private NbtContent() { }

            public byte[] getData() {
                return DataUtil.decode(this.getRawData().toCharArray());
            }

            public CompoundTag getNbtData() throws IOException {
                return new NbtFactory().fromByteArray(this.getData()); // TODO: NBT
            }

        }

        public static class PetInfo extends ExperienceCalculator {

            @SerializedName("type")
            @Getter private String name;
            @SerializedName("exp")
            @Getter private double experience;
            @Getter private boolean active;
            @Getter private Skyblock.Item.Rarity tier;
            @Getter private int candyUsed;
            private String heldItem;

            private PetInfo() { }

            @Override
            public ConcurrentList<Integer> getExperienceTiers() {
                return Skyblock.PET_EXP_TIER_SCALE.get(this.getTier());
            }

            public Optional<String> getHeldItem() {
                return StringUtil.isNotEmpty(this.heldItem) ? Optional.of(this.heldItem) : Optional.empty(); // TODO: SQL
            }

            @Override
            public int getMaxLevel() {
                return 100;
            }

            public Skyblock.Pet getPet() {
                try {
                    return Skyblock.Pet.valueOf(this.name);
                } catch (Exception ex) {
                    return null;
                }
            }

            public String getPrettyName() {
                return WordUtil.capitalizeFully(this.getName().replace("_", " "));
            }

            @Override
            public int getStartingLevel() {
                return 1;
            }

            public boolean isHoldingItem() {
                return this.getHeldItem().isPresent();
            }

        }

        public static class Sack {

            @Getter private final Skyblock.Sack type;
            @Getter private ConcurrentLinkedMap<Skyblock.Item, Integer> stored = Concurrent.newLinkedMap();

            Sack(Skyblock.Sack type) {
                this.type = type;
            }

            public int getStored(Skyblock.Item item) {
                return this.stored.getOrDefault(item, (this.type.getItems().contains(item) ? 0 : -1));
            }

        }

        public static class Skill extends ExperienceCalculator {

            @Getter private final Skyblock.Skill type;
            private final double experience;

            Skill(Skyblock.Skill type, double experience) {
                this.type = type;
                this.experience = experience;
            }

            @Override
            public double getExperience() {
                return Math.max(this.experience, 0);
            }

            @Override
            public ConcurrentList<Integer> getExperienceTiers() {
                return this.getType() == Skyblock.Skill.RUNECRAFTING ? Skyblock.RUNECRAFTING_EXP_SCALE : Skyblock.SKILL_EXP_SCALE; // TODO: Experience Table Loading
            }

            @Override
            public int getMaxLevel() {
                return (this.getType() == Skyblock.Skill.RUNECRAFTING ? 24 : 50); // TODO: Max Skill Loading
            }

        }

        public static class Slayer extends ExperienceCalculator {

            private final Reflection slayerBossRef = new Reflection(SlayerBoss.class);
            private final ConcurrentMap<Integer, Boolean> claimed = Concurrent.newMap();
            private final ConcurrentMap<Integer, Integer> kills = Concurrent.newMap();
            private final Skyblock.Slayer type;
            private final int experience;

            private Slayer(Skyblock.Slayer type, SlayerBoss slayerBoss) {
                this.type = type;
                this.experience = slayerBoss.xp;

                for (Map.Entry<String, Boolean> entry : slayerBoss.claimed_levels.entrySet())
                    this.claimed.put(Integer.parseInt(entry.getKey().replace("level_", "")), entry.getValue());

                for (int i = 0; i < 4; i++)
                    this.kills.put(i + 1, (int)slayerBossRef.getValue(FormatUtil.format("boss_kills_tier_{0}", i), slayerBoss));
            }

            @Override
            public double getExperience() {
                return this.experience;
            }

            @Override
            public ConcurrentList<Integer> getExperienceTiers() {
                return Skyblock.SLAYER_EXP_SCALE.get(this.getType());
            }

            public int getKills(int tier) {
                return this.kills.getOrDefault(tier, -1);
            }

            @Override
            public int getMaxLevel() {
                return 9;
            }

            public Skyblock.Slayer getType() {
                return this.type;
            }

            public boolean isClaimed(int level) {
                return this.claimed.get(level);
            }

        }

        public enum Storage {

            ARMOR,
            INVENTORY,
            ENDER_CHEST,
            FISHING,
            QUIVER,
            POTIONS,
            TALISMANS,
            CANDY

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
            private int xp;

        }

    }

}