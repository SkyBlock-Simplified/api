package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.account.Banking;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.account.CommunityUpgrades;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.objectives.BasicObjective;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.objectives.Objective;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.objectives.Quest;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pets.AutoPet;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pets.Pet;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pets.PetData;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.potions.Potion;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.potions.PotionData;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.Experience;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.NbtContent;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.playerstats.PlayerStats;
import dev.sbs.api.data.model.skyblock.collection_data.collection_items.CollectionItemModel;
import dev.sbs.api.data.model.skyblock.collection_data.collections.CollectionModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_classes.DungeonClassModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeons.DungeonModel;
import dev.sbs.api.data.model.skyblock.minion_data.minions.MinionModel;
import dev.sbs.api.data.model.skyblock.profiles.ProfileModel;
import dev.sbs.api.data.model.skyblock.sack_items.SackItemModel;
import dev.sbs.api.data.model.skyblock.sacks.SackModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.data.Range;
import dev.sbs.api.util.data.mutable.MutableDouble;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.gson.SerializedPath;
import lombok.Getter;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("all")
public class SkyBlockIsland {

    private static final DecimalFormat smallDecimalFormat = new DecimalFormat("#0.#");

    @SerializedName("profile_id")
    @Getter private UUID islandId;
    @SerializedName("last_save")
    private long lastSave;
    @SerializedName("community_upgrades")
    private CommunityUpgrades communityUpgrades;
    private Banking banking;
    @SerializedName("cute_name")
    private String profileName;
    @SerializedName("game_mode")
    private String gameMode;
    ConcurrentLinkedMap<UUID, Member> members = Concurrent.newLinkedMap();

    public Optional<Banking> getBanking() {
        return Optional.ofNullable(this.banking);
    }

    public Collection getCollection(CollectionModel type) {
        Collection collection = new Collection(type);

        for (Member profile : this.getMembers()) {
            Collection profileCollection = profile.getCollection(type);
            profileCollection.collected.forEach(entry -> collection.collected.put(entry.getKey(), Math.max(collection.collected.getOrDefault(entry.getKey(), 0L), entry.getValue())));
            collection.unlocked.putAll(profileCollection.unlocked);
        }

        return collection;
    }

    public Optional<CommunityUpgrades> getCommunityUpgrades() {
        return Optional.ofNullable(communityUpgrades);
    }

    public Optional<SkyBlockDate> getLastSave() {
        return Optional.ofNullable(this.lastSave > 0 ? new SkyBlockDate.RealTime(this.lastSave) : null);
    }

    public Optional<String> getGameMode() {
        return Optional.ofNullable(this.gameMode);
    }

    public Optional<Member> getMember(int index) {
        if (this.members.notEmpty() && Range.between(0, this.members.size()).contains(index))
            return Optional.of(this.getMembers().get(index));

        return Optional.empty();
    }

    public Optional<Member> getMember(UUID uniqueId) {
        if (this.members.containsKey(uniqueId)) {
            Member member = this.members.get(uniqueId);
            member.uniqueId = uniqueId;
            return Optional.of(member);
        }

        return Optional.empty();
    }

    public ConcurrentList<Member> getMembers() {
        return Concurrent.newUnmodifiableList(
            this.members.stream()
                .map(entry -> {
                    Member member = entry.getValue();
                    member.uniqueId = entry.getKey();
                    return member;
                })
                .collect(Concurrent.toList())
        );
    }

    public Minion getMinion(MinionModel minionModel) {
        ConcurrentList<Integer> unlocked = Concurrent.newList();
        this.getMembers().forEach(member -> unlocked.addAll(member.getMinion(minionModel).getUnlocked()));
        return Minion.merge(minionModel, unlocked);
    }

    public ConcurrentList<Minion> getMinions() {
        return SimplifiedApi.getRepositoryOf(MinionModel.class)
            .findAll()
            .stream()
            .map(this::getMinion)
            .collect(Concurrent.toList());
    }

    public PlayerStats getPlayerStats(Member member) {
        return this.getPlayerStats(member, true);
    }

    public PlayerStats getPlayerStats(Member member, boolean calculateBonus) {
        return new PlayerStats(this, member, calculateBonus);
    }

    public Optional<ProfileModel> getProfileModel() {
        return SimplifiedApi.getRepositoryOf(ProfileModel.class).findFirst(ProfileModel::getKey, this.profileName.toUpperCase());
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
        PERSONAL_VAULT,
        EQUIPMENT

    }

    public static class Member {

        // Player Stats
        @Getter private UUID uniqueId;
        @SerializedName("first_join")
        @Getter private SkyBlockDate.RealTime firstJoin;
        @SerializedName("first_join_hub")
        @Getter private SkyBlockDate.SkyBlockTime firstJoinHub;
        @SerializedName("last_death")
        @Getter private SkyBlockDate.SkyBlockTime lastDeath;
        @SerializedName("death_count")
        @Getter private int deathCount;
        @SerializedName("coin_purse")
        @Getter private double purse;
        @SerializedName("fishing_treasure_caught")
        @Getter private int caughtFishingTreasure;
        @SerializedName("wardrobe_equipped_slot")
        @Getter private int equippedWardrobeSlot;
        @SerializedName("personal_bank_upgrade")
        @Getter private int personalBankUpgrade;
        @SerializedName("reaper_peppers_eaten")
        @Getter private int eatenReaperPeppers;
        @SerializedName("favorite_arrow")
        @Getter private String selectedArrow;
        @Getter private int soulflow;

        // Experience
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
        @SerializedName("experience_skill_social2")
        private double experience_skill_social = -1;

        // Fairy Souls
        @SerializedName("fairy_souls_collected")
        private int collectedFairySouls;
        @SerializedName("fairy_souls")
        private int unclaimedFairySouls;
        @SerializedName("fairy_exchanges")
        private int fairyExchanges;

        // Essence
        private int essence_undead;
        private int essence_diamond;
        private int essence_dragon;
        private int essence_gold;
        private int essence_ice;
        private int essence_wither;
        private int essence_spider;
        private int essence_crimson;
        @SerializedName("perks")
        private ConcurrentMap<String, Integer> essencePerks = Concurrent.newMap();

        // Inventory Contents
        private NbtContent inv_armor;
        private NbtContent inv_contents;
        private NbtContent ender_chest_contents;
        private NbtContent fishing_bag;
        private NbtContent quiver;
        private NbtContent potion_bag;
        private NbtContent talisman_bag;
        private NbtContent candy_inventory_contents;
        private NbtContent wardrobe_contents;
        private NbtContent personal_vault_contents;
        private NbtContent equipment_contents;
        private ConcurrentMap<Integer, NbtContent> backpack_contents = Concurrent.newMap();
        private ConcurrentMap<Integer, NbtContent> backpack_icons = Concurrent.newMap();

        // Zones/Islands
        @Getter private ConcurrentList<String> tutorial = Concurrent.newList();
        @Getter private ConcurrentList<String> visited_zones = Concurrent.newList();
        @Getter private ConcurrentList<String> achievement_spawned_island_types = Concurrent.newList();

        // Miscellaneous
        @Getter private ConcurrentMap<String, Double> stats = Concurrent.newMap();
        @SerializedName("temp_stat_buffs")
        @Getter private ConcurrentList<CenturyCake> centuryCakes = Concurrent.newList();
        @Getter private Dungeons dungeons;

        // Potions
        private ConcurrentList<Potion> active_effects = Concurrent.newList();
        private ConcurrentList<Potion> paused_effects = Concurrent.newList();
        private ConcurrentList<String> disabled_potion_effects = Concurrent.newList();

        // Unfiltered Collections
        private ConcurrentLinkedMap<String, Objective> objectives = Concurrent.newLinkedMap();
        private ConcurrentLinkedMap<String, Quest> quests = Concurrent.newLinkedMap();
        private ConcurrentList<String> crafted_generators = Concurrent.newList();
        private ConcurrentLinkedMap<String, Integer> sacks_counts = Concurrent.newLinkedMap();
        private ConcurrentMap<String, Object> harp_quest = Concurrent.newMap();
        private ConcurrentLinkedMap<String, Slayer> slayer_bosses = Concurrent.newLinkedMap();
        private ConcurrentList<String> unlocked_coll_tiers = Concurrent.newList();
        private ConcurrentLinkedMap<String, Long> collection = Concurrent.newLinkedMap();
        private ConcurrentMap<String, Object> accessory_bag_storage = Concurrent.newMap();
        @SerializedPath("forge.forge_processes.forge_1")
        private ConcurrentMap<Integer, ForgeItem> forge_items = Concurrent.newMap();
        private ConcurrentMap<String, Object> trophy_fish = Concurrent.newMap();
        private ConcurrentMap<String, Object> bestiary = Concurrent.newMap();

        // Filtered Data
        private AccessoryBag accessoryBag;
        private MelodyHarp melodyHarp;
        private Experimentation experimentation;
        private Leveling leveling;
        private Mining mining_core;
        private JacobsFarming jacob2;
        private CrimsonIsle nether_island_player_data;
        private ConcurrentList<Pet> pets = Concurrent.newList();
        private AutoPet autopet;
        private Trapper trapper_quest;
        private TrophyFish trophyFish;
        private BestiaryData bestiaryData;

        public Backpacks getBackpacks() {
            return new Backpacks(
                this.backpack_contents,
                this.backpack_icons
            );
        }

        public BestiaryData getBestiary() {
            if (Objects.isNull(this.bestiaryData))
                this.bestiaryData = new BestiaryData(this.bestiary);

            return this.bestiaryData;
        }

        public Collection getCollection(CollectionModel type) {
            Collection collection = new Collection(type);

            // Fill Collection
            SimplifiedApi.getRepositoryOf(CollectionItemModel.class)
                .findAll(CollectionItemModel::getCollection, type)
                .forEach(collectionItemModel -> {
                    collection.collected.put(collectionItemModel, this.collection.getOrDefault(collectionItemModel.getItem().getItemId(), 0L));

                    this.unlocked_coll_tiers.stream()
                        .filter(tier -> tier.matches(String.format("^%s_[\\d]+$", collectionItemModel.getItem().getItemId())))
                        .forEach(tier -> {
                            int current = collection.unlocked.getOrDefault(collectionItemModel, 0);
                            int unlocked = Math.max(current, Integer.parseInt(tier.replace(String.format("%s_", collectionItemModel.getItem().getItemId()), "")));
                            collection.unlocked.put(collectionItemModel, unlocked);
                        });
                });

            return collection;
        }

        public AccessoryBag getAccessoryBag() {
            if (Objects.isNull(this.accessoryBag))
                this.accessoryBag = new AccessoryBag(this.talisman_bag, this.accessory_bag_storage);

            return this.accessoryBag;
        }

        public CrimsonIsle getCrimsonIsle() {
            if (Objects.isNull(this.nether_island_player_data))
                this.nether_island_player_data = new CrimsonIsle();

            return this.nether_island_player_data;
        }

        public Essence getEssence() {
            return new Essence(
                this.essence_undead,
                this.essence_diamond,
                this.essence_dragon,
                this.essence_gold,
                this.essence_ice,
                this.essence_wither,
                this.essence_spider,
                this.essence_crimson,
                this.essencePerks
            );
        }

        public Experimentation getExperimentation() {
            if (Objects.isNull(this.experimentation))
                this.experimentation = new Experimentation();

            return this.experimentation;
        }

        public FairyData getFairyData() {
            return new FairyData(
                this.collectedFairySouls,
                this.unclaimedFairySouls,
                this.fairyExchanges
            );
        }

        public JacobsFarming getJacobsFarming() {
            if (Objects.isNull(this.jacob2))
                this.jacob2 = new JacobsFarming();

            return this.jacob2;
        }

        public Leveling getLeveling() {
            if (Objects.isNull(this.leveling))
                this.leveling = new Leveling();

            return this.leveling;
        }

        public MelodyHarp getMelodyHarp() {
            if (Objects.isNull(this.melodyHarp))
                this.melodyHarp = new MelodyHarp(this.harp_quest);

            return this.melodyHarp;
        }

        public Mining getMining() {
            if (Objects.isNull(this.mining_core))
                this.mining_core = new Mining();

            return this.mining_core;
        }

        public Minion getMinion(MinionModel minionModel) {
            return new Minion(minionModel, this.crafted_generators);
        }

        public ConcurrentList<Minion> getMinions() {
            return SimplifiedApi.getRepositoryOf(MinionModel.class).stream().map(this::getMinion).collect(Concurrent.toList());
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

        public PetData getPetData() {
            return Reflection.of(PetData.class).newInstance(this.pets, this.autopet);
        }

        public PotionData getPotionData() {
            return Reflection.of(PotionData.class).newInstance(this.active_effects, this.paused_effects, this.disabled_potion_effects);
        }

        public ConcurrentLinkedMap<String, Quest> getQuests() {
            return this.getQuests(null);
        }

        public ConcurrentLinkedMap<String, Quest> getQuests(BasicObjective.Status status) {
            return this.quests.stream()
                .filter(entry -> status == null || entry.getValue().getStatus() == status)
                .sorted(Comparator.comparingLong(o -> o.getValue().getCompleted().getRealTime()))
                .collect(Concurrent.toLinkedMap());
        }

        public Skill getSkill(SkillModel skillModel) {
            return new Skill(
                skillModel,
                (double) Reflection.of(Member.class).getValue(String.format("experience_skill_%s", skillModel.getKey().toLowerCase()), this),
                skillModel.getKey().equals("FARMING") ? 10 - this.getJacobsFarming().getPerks().getOrDefault(JacobsFarming.Perk.FARMING_LEVEL_CAP, 0) : 0
            );
        }

        public ConcurrentList<Skill> getSkills() {
            return this.getSkills(true);
        }

        public ConcurrentList<Skill> getSkills(boolean cosmetic) {
            return SimplifiedApi.getRepositoryOf(SkillModel.class)
                .stream()
                .filter(skillModel -> cosmetic || !skillModel.isCosmetic())
                .map(this::getSkill)
                .collect(Concurrent.toList());
        }

        public Slayer getSlayer(SlayerModel slayerModel) {
            Slayer slayer = this.slayer_bosses.getOrDefault(slayerModel.getKey().toLowerCase(), new Slayer());
            slayer.type = slayerModel;
            return slayer;
        }

        public ConcurrentList<Slayer> getSlayers() {
            return SimplifiedApi.getRepositoryOf(SlayerModel.class)
                .stream()
                .map(this::getSlayer)
                .collect(Concurrent.toList());
        }

        public Sack getSack(SackModel sackModel) {
            return new Sack(
                sackModel,
                Concurrent.newUnmodifiableMap(
                    SimplifiedApi.getRepositoryOf(SackItemModel.class)
                        .findAll(SackItemModel::getSack, sackModel)
                        .stream()
                        .map(sackItem -> Pair.of(sackItem, this.sacks_counts.getOrDefault(sackItem.getItem().getItemId(), 0)))
                        .collect(Concurrent.toMap())
                )
            );
        }

        public double getSkillAverage() {
            return this.getSkills(false)
                .stream()
                .mapToDouble(Skill::getLevel)
                .average()
                .orElse(0.0);
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
                    return this.inv_contents;
                case ENDER_CHEST:
                    return this.ender_chest_contents;
                case FISHING:
                    return this.fishing_bag;
                case QUIVER:
                    return this.quiver;
                case POTIONS:
                    return this.potion_bag;
                case ACCESSORIES:
                    return this.talisman_bag;
                case CANDY:
                    return this.candy_inventory_contents;
                case PERSONAL_VAULT:
                    return this.personal_vault_contents;
                case EQUIPMENT:
                    return this.equipment_contents;
                case ARMOR:
                default:
                    return this.inv_armor;
            }
        }

        public ConcurrentMap<Skill, Experience.Weight> getSkillWeight() {
            return SimplifiedApi.getRepositoryOf(SkillModel.class)
                .stream()
                .filter(skillModel -> !skillModel.isCosmetic())
                .map(this::getSkill)
                .map(skill -> Pair.of(skill, skill.getWeight()))
                .collect(Concurrent.toMap());
        }

        public ConcurrentMap<Slayer, Experience.Weight> getSlayerWeight() {
            return SimplifiedApi.getRepositoryOf(SlayerModel.class)
                .stream()
                .map(this::getSlayer)
                .map(slayer -> Pair.of(slayer, slayer.getWeight()))
                .collect(Concurrent.toMap());
        }

        public ConcurrentMap<Dungeon, Experience.Weight> getDungeonWeight() {
            return SimplifiedApi.getRepositoryOf(DungeonModel.class)
                .stream()
                .map(this.getDungeons()::getDungeon)
                .map(dungeon -> Pair.of(dungeon, dungeon.getWeight()))
                .collect(Concurrent.toMap());
        }

        public ConcurrentMap<Dungeon.Class, Experience.Weight> getDungeonClassWeight() {
            return SimplifiedApi.getRepositoryOf(DungeonClassModel.class)
                .stream()
                .map(this.getDungeons()::getClass)
                .map(dungeonClass -> Pair.of(dungeonClass, dungeonClass.getWeight()))
                .collect(Concurrent.toMap());
        }

        public Experience.Weight getTotalWeight() {
            // Load Weights
            Experience.Weight skillWeight = this.getTotalWeight(Member::getSkillWeight);
            Experience.Weight slayerWeight = this.getTotalWeight(Member::getSlayerWeight);
            Experience.Weight dungeonWeight = this.getTotalWeight(Member::getDungeonWeight);
            Experience.Weight dungeonClassWeight = this.getTotalWeight(Member::getDungeonClassWeight);

            return new Experience.Weight(
                skillWeight.getValue() + slayerWeight.getValue() + dungeonWeight.getValue() + dungeonClassWeight.getValue(),
                skillWeight.getOverflow() + slayerWeight.getOverflow() + dungeonWeight.getOverflow() + dungeonClassWeight.getOverflow()
            );
        }

        private Experience.Weight getTotalWeight(Function<Member, ConcurrentMap<?, Experience.Weight>> weightMapFunction) {
            MutableDouble totalWeight = new MutableDouble();
            MutableDouble totalOverflow = new MutableDouble();

            weightMapFunction.apply(this)
                .stream()
                .map(Map.Entry::getValue)
                .forEach(skillWeight -> {
                    totalWeight.add(skillWeight.getValue());
                    totalOverflow.add(skillWeight.getOverflow());
                });

            return new Experience.Weight(totalWeight.get(), totalOverflow.get());
        }

        public Trapper getTrapper() {
            if (Objects.isNull(this.trapper_quest))
                this.trapper_quest = new Trapper();

            return this.trapper_quest;
        }

        public TrophyFish getTrophyFish() {
            if (Objects.isNull(this.trophyFish))
                this.trophyFish = new TrophyFish(this.trophy_fish);

            return this.trophyFish;
        }

        public boolean hasStorage(Storage type) {
            return this.getStorage(type) != null;
        }

    }

}
