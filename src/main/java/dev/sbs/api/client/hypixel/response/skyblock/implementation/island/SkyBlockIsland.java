package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.account.Banking;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.account.CommunityUpgrades;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.crimson_isle.CrimsonIsle;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.crimson_isle.TrophyFish;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.dungeon.Dungeon;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.dungeon.DungeonData;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.mining.ForgeItem;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.mining.Mining;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pet.PetData;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats.ProfileStats;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.Experience;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.NbtContent;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.skill.EnhancedSkill;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.skill.Skill;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.weight.Weight;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.data.mutable.MutableDouble;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.gson.SerializedPath;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Getter
public class SkyBlockIsland {

    private static final DecimalFormat smallDecimalFormat = new DecimalFormat("#0.#");

    @SerializedName("profile_id")
    private @NotNull UUID islandId;
    @SerializedName("last_save")
    private @NotNull Optional<SkyBlockDate.RealTime> lastSave = Optional.empty();
    @SerializedName("community_upgrades")
    private @NotNull Optional<CommunityUpgrades> communityUpgrades = Optional.empty();
    private @NotNull Optional<Banking> banking = Optional.empty();
    @SerializedName("game_mode")
    private @NotNull Optional<String> gameMode = Optional.empty();
    @SerializedName("cute_name")
    private @NotNull Optional<String> profileName = Optional.empty();
    private boolean selected;
    private @NotNull ConcurrentLinkedMap<UUID, Member> members = Concurrent.newLinkedMap();

    /*public Collection getCollection(CollectionModel type) {
        Collection collection = new Collection(type);

        this.getMembers().forEach((uniqueId, member) -> {
            Collection profileCollection = member.getCollection(type);
            profileCollection.collected.forEach(entry -> collection.collected.put(entry.getKey(), Math.max(collection.collected.getOrDefault(entry.getKey(), 0L), entry.getValue())));
            collection.unlocked.putAll(profileCollection.unlocked);
        });

        return collection;
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
    }*/

    public @NotNull ProfileStats getPlayerStats(@NotNull Member member) {
        return this.getPlayerStats(member, true);
    }

    public @NotNull ProfileStats getPlayerStats(@NotNull Member member, boolean calculateBonus) {
        return new ProfileStats(this, member, calculateBonus);
    }

    public boolean hasMember(@NotNull UUID uniqueId) {
        return this.getMembers().containsKey(uniqueId);
    }

    @Getter
    @NoArgsConstructor(force = true)
    @RequiredArgsConstructor
    public static class Member {

        @SerializedName("player_id")
        private final @NotNull UUID uniqueId;
        private final Rift rift;
        private final Stats stats;
        private final Bestiary bestiary;
        @SerializedName("accessory_bag_storage")
        private final AccessoryBag accessoryBag;
        private final Leveling leveling;
        @SerializedName("dungeons")
        private final DungeonData dungeonData;
        @SerializedName("nether_island_player_data")
        private final CrimsonIsle crimsonIsle;
        private final Experimentation experimentation;
        private final Mining mining;
        @SerializedName("player_stats")
        private final PlayerStats playerStats;
        @SerializedName("fairy_soul")
        private final FairySouls fairySouls;
        @SerializedName("player_data")
        private final PlayerData playerData;
        private final Currencies currencies;
        private final Slayer slayer;
        @SerializedName("item_data")
        private final ItemSettings itemSettings;
        private final Inventory inventory;
        @SerializedName("pet_data")
        private final PetData petData;
        @SerializedName("jacobs_farming")
        private final JacobsFarming jacobsFarming;
        protected Optional<Quests> quests = Optional.empty();

        // Profile
        @SerializedName("first_join_hub")
        private final SkyBlockDate.SkyBlockTime firstJoinHub;
        @SerializedPath("profile.first_join")
        private final SkyBlockDate.RealTime firstJoin;
        @SerializedPath("profile.personal_bank_upgrade")
        private final int personalBankUpgrade;

        // Maps
        @SerializedName("trophy_fish")
        @Getter(AccessLevel.NONE)
        protected @NotNull ConcurrentMap<String, Object> trophyFishMap = Concurrent.newMap();
        protected @NotNull ConcurrentMap<String, Long> collection = Concurrent.newMap();
        @SerializedPath("objectives.tutorial")
        protected @NotNull ConcurrentList<String> tutorialObjectives = Concurrent.newList();
        @SerializedPath("forge.forge_processes.forge_1")
        protected @NotNull ConcurrentMap<Integer, ForgeItem> forge = Concurrent.newMap();

        /**
         * Wraps this class in a {@link Experience} and {@link Weight} class.
         * <br><br>
         * Requires an active database session.
         */
        public @NotNull EnhancedMember asEnhanced() {
            return new EnhancedMember(this);
        }

        @SuppressWarnings("all")
        public @NotNull AccessoryBag getAccessoryBag() {
            if (this.accessoryBag.getContents() == null)
                Reflection.of(AccessoryBag.class).setValue(NbtContent.class, this.accessoryBag, this.getInventory().getBags().getAccessories());

            return this.accessoryBag;
        }

        public @NotNull TrophyFish getTrophyFish() {
            return Reflection.of(TrophyFish.class).newInstance(this.trophyFishMap);
        }

    }

    @Getter
    public static class EnhancedMember extends Member {

        private EnhancedMember(@NotNull Member member) {
            super(
                member.getUniqueId(),
                member.getRift(),
                member.getStats(),
                member.getBestiary(),
                member.getAccessoryBag(),
                member.getLeveling(),
                member.getDungeonData(),
                member.getCrimsonIsle(),
                member.getExperimentation(),
                member.getMining(),
                member.getPlayerStats(),
                member.getFairySouls(),
                member.getPlayerData(),
                member.getCurrencies(),
                member.getSlayer(),
                member.getItemSettings(),
                member.getInventory(),
                member.getPetData(),
                member.getJacobsFarming(),
                member.getFirstJoinHub(),
                member.getFirstJoin(),
                member.getPersonalBankUpgrade()
            );
            super.quests = member.getQuests();
            super.trophyFishMap = member.trophyFishMap;
            super.collection = member.collection;
            super.tutorialObjectives = member.tutorialObjectives;
            super.forge = member.forge;
        }

        // Dungeons

        public double getDungeonClassAverage() {
            ConcurrentList<Dungeon.Class> dungeonClasses = this.getDungeonData().getClasses();
            return dungeonClasses.stream()
                .map(Dungeon.Class::asEnhanced)
                .mapToDouble(Dungeon.EnhancedClass::getLevel)
                .sum() / dungeonClasses.size();
        }

        public double getDungeonClassExperience() {
            ConcurrentList<Dungeon.Class> dungeonClasses = this.getDungeonData().getClasses();
            return dungeonClasses.stream().mapToDouble(Dungeon.Class::getExperience).sum();
        }

        public double getDungeonClassProgressPercentage() {
            ConcurrentList<Dungeon.Class> dungeonClasses = this.getDungeonData().getClasses();
            return dungeonClasses.stream()
                .map(Dungeon.Class::asEnhanced)
                .mapToDouble(Dungeon.EnhancedClass::getTotalProgressPercentage).sum() / dungeonClasses.size();
        }

        public @NotNull ConcurrentMap<Dungeon.Type, Weight> getDungeonWeight() {
            return Arrays.stream(Dungeon.Type.values())
                .map(type -> Pair.of(
                    type,
                    this.getDungeonData()
                        .getDungeon(type)
                        .asEnhanced()
                        .getWeight()
                ))
                .collect(Concurrent.toMap());
        }

        public @NotNull ConcurrentMap<Dungeon.Class.Type, Weight> getDungeonClassWeight() {
            return Arrays.stream(Dungeon.Class.Type.values())
                .map(type -> Pair.of(
                    type,
                    this.getDungeonData()
                        .getClass(type)
                        .asEnhanced()
                        .getWeight()
                ))
                .collect(Concurrent.toMap());
        }

        // Skills

        public double getSkillAverage() {
            return this.getPlayerData()
                .getSkills(false)
                .stream()
                .map(skill -> skill.asEnhanced(this.getJacobsFarming()))
                .mapToDouble(EnhancedSkill::getLevel)
                .average()
                .orElse(0.0);
        }

        public double getSkillExperience() {
            ConcurrentList<Skill> skills = this.getPlayerData().getSkills(false);
            return skills.stream().mapToDouble(Skill::getExperience).sum();
        }

        public double getSkillProgressPercentage() {
            ConcurrentList<Skill> skills = this.getPlayerData().getSkills(false);
            return skills.stream()
                .map(skill -> skill.asEnhanced(this.getJacobsFarming()))
                .mapToDouble(EnhancedSkill::getTotalProgressPercentage)
                .sum() / skills.size();
        }

        public @NotNull ConcurrentMap<Skill.Type, Weight> getSkillWeight() {
            return this.getPlayerData()
                .getSkills(false)
                .stream()
                .map(skill -> skill.asEnhanced(this.getJacobsFarming()))
                .map(skill -> Pair.of(skill.getType(), skill.getWeight()))
                .collect(Concurrent.toMap());
        }

        // Slayer

        public double getSlayerAverage() {
            ConcurrentList<Slayer.Boss> bosses = this.getSlayer().getBosses();
            return bosses.stream()
                .map(Slayer.Boss::asEnhanced)
                .mapToDouble(Slayer.EnhancedBoss::getLevel)
                .sum() / bosses.size();
        }

        public double getSlayerExperience() {
            return this.getSlayer()
                .getBosses()
                .stream()
                .map(Slayer.Boss::asEnhanced)
                .mapToDouble(Slayer.EnhancedBoss::getExperience).sum();
        }

        public double getSlayerProgressPercentage() {
            ConcurrentList<Slayer.Boss> bosses = this.getSlayer().getBosses();
            return bosses.stream()
                .map(Slayer.Boss::asEnhanced)
                .mapToDouble(Slayer.EnhancedBoss::getTotalProgressPercentage).sum() / bosses.size();
        }

        public @NotNull ConcurrentMap<Slayer.Type, Weight> getSlayerWeight() {
            return Arrays.stream(Slayer.Type.values())
                .map(type -> Pair.of(
                    type,
                    this.getSlayer()
                        .getBoss(type)
                        .asEnhanced()
                        .getWeight()
                ))
                .collect(Concurrent.toMap());
        }

        // Weight

        public @NotNull Weight getTotalWeight() {
            // Load Weights
            Weight skillWeight = this.getTotalWeight(EnhancedMember::getSkillWeight);
            Weight slayerWeight = this.getTotalWeight(EnhancedMember::getSlayerWeight);
            Weight dungeonWeight = this.getTotalWeight(EnhancedMember::getDungeonWeight);
            Weight dungeonClassWeight = this.getTotalWeight(EnhancedMember::getDungeonClassWeight);

            return Weight.of(
                skillWeight.getValue() + slayerWeight.getValue() + dungeonWeight.getValue() + dungeonClassWeight.getValue(),
                skillWeight.getOverflow() + slayerWeight.getOverflow() + dungeonWeight.getOverflow() + dungeonClassWeight.getOverflow()
            );
        }

        private Weight getTotalWeight(Function<EnhancedMember, ConcurrentMap<?, Weight>> weightMapFunction) {
            MutableDouble totalWeight = new MutableDouble();
            MutableDouble totalOverflow = new MutableDouble();

            weightMapFunction.apply(this)
                .stream()
                .map(Map.Entry::getValue)
                .forEach(skillWeight -> {
                    totalWeight.add(skillWeight.getValue());
                    totalOverflow.add(skillWeight.getOverflow());
                });

            return Weight.of(totalWeight.get(), totalOverflow.get());
        }

    }

}
