package gg.sbs.api.hypixel;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import gg.sbs.api.SimplifiedAPI;
import gg.sbs.api.util.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import gg.sbs.api.hypixel.skyblock.Location;
import gg.sbs.api.hypixel.skyblock.Skyblock;
import gg.sbs.api.nbt_old.NbtCompound;
import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;
import gg.sbs.api.util.concurrent.ConcurrentMap;
import gg.sbs.api.util.concurrent.ConcurrentSet;
import gg.sbs.api.util.concurrent.linked.ConcurrentLinkedMap;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public final class SkyblockIsland {

	private UUID profile_id;
	private ConcurrentList<Member> members = Concurrent.newList();
	private Banking banking;
	private Member currentMember;
	String cute_name;

	private SkyblockIsland() { }

	public Collection getCollection(Skyblock.Skill type) {
		Collection collection = new Collection(type);

		for (Member profile : this.members) {
			Collection profileCollection = profile.getCollection(type);
			profileCollection.items.forEach(entry -> collection.items.put(entry.getKey(), Math.max(collection.items.getOrDefault(entry.getKey(), 0), entry.getValue())));
			collection.unlocked.putAll(profileCollection.unlocked);
		}

		return collection;
	}

	public Banking getBanking() {
		return this.banking;
	}

	public Member getCurrentMember() {
		return this.currentMember;
	}

	public Minion getMinion(Skyblock.Minion type) {
		Minion minion = new Minion(type);

		for (Member profile : this.members) {
			Minion profileMinion = profile.getMinion(type);
			minion.unlocked.addAll(profileMinion.unlocked);
		}

		return minion;
	}

	public Member getMember(UUID profileId) {
		for (Member profile : this.members) {
			if (profile.getUniqueId().equals(profileId))
				return profile;
		}

		throw new IllegalArgumentException(FormatUtil.format("Profile ID {0} is not part of Skyblock Island {1}!", profileId, this.getIslandId()));
	}

	public ConcurrentList<Member> getMembers() {
		return this.members;
	}

	public UUID getIslandId() {
		return this.profile_id;
	}

	public String getCuteName() {
		return this.cute_name;
	}

	public boolean hasMember(UUID profileId) {
		for (Member member : this.getMembers()) {
			if (member.getUniqueId().equals(profileId))
				return true;
		}

		return false;
	}

	public void setCurrentMember(UUID profileId) {
		this.currentMember = this.getMember(profileId);
	}

	public static class Member {

		private UUID uniqueId;
		private long first_join; // Unix Timestamp (in milliseconds)
		private long first_join_hub; // Skyblock Timestamp
		private long last_death; // Skyblock Timestamp
		private int death_count;
		private long last_save; // Unix Timestamp (in milliseconds)
		private double coin_purse;
		private int fishing_treasure_caught;
		private int fairy_souls_collected;
		private int fairy_souls;
		private int fairy_exchanges;
		private int wardrobe_equipped_slot;

		private ConcurrentList<String> tutorial;
		private ConcurrentList<String> visited_zones;
		private ConcurrentList<String> achievement_spawned_island_types;

		private ConcurrentLinkedMap<String, Objective> objectives;
		private ConcurrentLinkedMap<String, Quest> quests;
		private ConcurrentMap<String, Double> stats;
		private ConcurrentList<String> crafted_generators;
		private ConcurrentLinkedMap<String, Integer> sacks_counts;
		private ConcurrentLinkedMap<String, SlayerBoss> slayer_bosses;
		private ConcurrentList<PetInfo> pets;
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

		private Member() { }

		public ConcurrentList<String> getAchievementSpawnedIslandTypes() {
			return this.achievement_spawned_island_types;
		}

		public Collection getCollection(Skyblock.Skill type) {
			Collection collection = new Collection(type);
			ConcurrentList<Skyblock.Collection> items = Skyblock.Collection.getItems(type);

			if (this.collection != null) {
				for (Skyblock.Collection item : items) {
					collection.items.put(item, this.collection.getOrDefault(item.getName(), 0));

					List<String> unlocked = this.unlocked_coll_tiers.stream().filter(tier -> tier.matches(FormatUtil.format("^{0}_[\\d]+$", item.getName()))).collect(Collectors.toList());
					unlocked.forEach(tier -> {
						int current = collection.unlocked.getOrDefault(item, 0);
						collection.unlocked.put(item, Math.max(current, Integer.parseInt(tier.replace(FormatUtil.format("{0}_", item.getName()), ""))));
					});
				}
			}

			return collection;
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

		public MemberInfo getInfo() {
			return new MemberInfo(this);
		}

		public Minion getMinion(Skyblock.Minion type) {
			Minion minion = new Minion(type);

			if (this.crafted_generators != null) {
				minion.unlocked.addAll(
						this.crafted_generators.stream()
								.filter(item -> item.matches(FormatUtil.format("^{0}_[\\d]+$", type.name())))
								.map(item -> Integer.parseInt(item.replace(FormatUtil.format("{0}_", type.name()), "")))
								.collect(Collectors.toList())
				);
			}

			return minion;
		}

		public ConcurrentMap<String, Double> getMissingStats() {
			return this.getFilteredStats(false);
		}

		public ConcurrentLinkedMap<String, Objective> getObjectives() {
			return this.getObjectives(null);
		}

		public ConcurrentLinkedMap<String, Objective> getObjectives(BasicObjective.Status status) {
			return this.objectives.stream().filter(entry -> status == null || entry.getValue().getStatus() == status).sorted(Comparator.comparingLong(o -> o.getValue().getCompleted())).collect(Concurrent.toLinkedMap());
		}

		public ConcurrentList<PetInfo> getPets() {
			return this.pets;
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
			return this.quests.stream().filter(entry -> status == null || entry.getValue().getStatus() == status).sorted(Comparator.comparingLong(o -> o.getValue().getCompleted())).collect(Concurrent.toLinkedMap());
		}

		public Skill getSkill(Skyblock.Skill skill) {
			double experience = (double)new Reflection(Member.class).getValue(FormatUtil.format("experience_skill_{0}", skill.name().toLowerCase()), this);
			return new Skill(skill, experience);
		}

		public Slayer getSlayer(Skyblock.Slayer type) {
			return new Slayer(type, this.slayer_bosses.get(type.getName()));
		}

		public ConcurrentMap<Stat, Double> getStats() {
			return this.getFilteredStats(true).entrySet().stream().collect(Concurrent.toMap(entry -> Stat.valueOf(entry.getKey().toUpperCase()), Map.Entry::getValue));
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

		public ConcurrentList<String> getTutorial() {
			return this.tutorial;
		}

		public UUID getUniqueId() {
			return this.uniqueId;
		}

		public ConcurrentList<String> getVisitedZones() {
			return this.visited_zones;
		}

	}

	public static class MemberInfo {

		private final Member member;

		MemberInfo(Member member) {
			this.member = member;
		}

		public int getDeathCount() {
			return this.member.death_count;
		}

		public int getEquippedWardrobeSlot() {
			return this.member.wardrobe_equipped_slot;
		}

		public int getFairyExchanges() {
			return this.member.fairy_exchanges;
		}

		public int getFairySoulsCollected() {
			return this.member.fairy_souls_collected;
		}

		public int getFairySoulsUncollected() {
			return this.member.fairy_souls;
		}

		public long getFirstJoin() {
			return this.member.first_join;
		}

		public long getFirstJoinHub() {
			return (Skyblock.START_DATE + this.member.first_join_hub) * 1000;
		}

		public long getFirstJoinHubSb() {
			return this.member.first_join_hub;
		}

		public long getLastDeath() {
			return (Skyblock.START_DATE + this.member.last_death) * 1000;
		}

		public long getLastDeathSb() {
			return this.member.last_death;
		}

		public long getLastSave() {
			return this.member.last_save;
		}

		public double getPurse() {
			return this.member.coin_purse;
		}

		public double getSkillAverage() {
			ConcurrentList<Skyblock.Skill> skills = Arrays.stream(Skyblock.Skill.values()).filter(skill -> !skill.isCosmetic()).collect(Concurrent.toList());
			double total = skills.stream().mapToInt(skill -> this.member.getSkill(skill).getLevel()).sum();
			return total / skills.size();
		}

	}

	public static class Quest extends BasicObjective {

		private long activated_at;
		private long activated_at_sb;
		private long completed_at_sb;

		private Quest() { }

		public long getActivated() {
			return this.activated_at;
		}

		public long getActivatedSb() {
			return this.activated_at_sb;
		}

		public long getCompletedSb() {
			return this.completed_at_sb;
		}

	}

	public static class Objective extends BasicObjective {

		private int progress;

		private Objective() { }

		public int getProgress() {
			return this.progress;
		}

	}

	public static class BasicObjective {

		private Status status;
		private long completed_at;

		private BasicObjective() { }

		public long getCompleted() {
			return this.completed_at;
		}

		public Status getStatus() {
			return this.status;
		}

		public enum Status {

			ACTIVE,
			COMPLETE;

			public String getName() {
				return WordUtil.capitalizeFully(this.name());
			}

		}


	}

	public enum Stat {

		// PLAYER
		HIGHEST_CRITICAL_DAMAGE(Category.PLAYER),
		PET_MILESTONE_SEA_CREATURES_KILLED(Category.PLAYER),
		PET_MILESTONE_ORES_MINED(Category.PLAYER, Location.DEEP_CAVERNS),
		KILLS(Category.PLAYER),
		KILLS_PLAYER(Category.PLAYER),
		KILLS_RUNIC(Category.PLAYER),
		KILLS_FOREST_ISLAND_BAT(Category.PLAYER, Location.YOUR_ISLAND, Skyblock.Entity.BAT),
		DEATHS(Category.PLAYER),
		DEATHS_VOID(Category.PLAYER),
		DEATHS_FALL(Category.PLAYER),
		DEATHS_FIRE(Category.PLAYER),
		DEATHS_WITHER(Category.PLAYER),
		DEATHS_UNKNOWN(Category.PLAYER),
		DEATHS_PLAYER(Category.PLAYER),
		DEATHS_DROWNING(Category.PLAYER),
		DEATHS_CACTUS(Category.PLAYER),
		DEATHS_SUFFOCATION(Category.PLAYER),

		// GENERAL
		KILLS_ZOMBIE(Category.GENERAL, Location.NONE, Skyblock.Entity.ZOMBIE),
		KILLS_SKELETON(Category.GENERAL, Location.NONE, Skyblock.Entity.SKELETON),
		KILLS_CREEPER(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.CREEPER),
		KILLS_SPIDER(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.SPIDER),
		KILLS_WITCH(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.WITCH),
		KILLS_CAVE_SPIDER(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.CAVE_SPIDER),
		KILLS_RANDOM_SLIME(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.SLIME),
		DEATHS_ZOMBIE(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.ZOMBIE),
		DEATHS_SKELETON(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.SKELETON),
		DEATHS_CREEPER(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.CREEPER),
		DEATHS_SPIDER(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.SPIDER),
		DEATHS_WITCH(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.WITCH),
		DEATHS_CAVE_SPIDER(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.CAVE_SPIDER),
		DEATHS_RANDOM_SLIME(Category.GENERAL, Location.YOUR_ISLAND, Skyblock.Entity.SLIME),

		// HUB
		KILLS_ZOMBIE_VILLAGER(Category.HUB, Location.GRAVEYARD, Skyblock.Entity.ZOMBIE_VILLAGER),
		KILLS_NIGHT_RESPAWNING_SKELETON(Category.HUB, Location.HIGH_LEVEL, Skyblock.Entity.SKELETON),
		DEATHS_ZOMBIE_VILLAGER(Category.HUB, Location.GRAVEYARD, Skyblock.Entity.ZOMBIE_VILLAGER),
		DEATHS_NIGHT_RESPAWNING_SKELETON(Category.HUB, Location.HIGH_LEVEL, Skyblock.Entity.SKELETON),

		// MINIONS
		KILLS_GENERATOR_GHAST(Category.MINIONS, Location.YOUR_ISLAND, Skyblock.Entity.GHAST),
		KILLS_GENERATOR_MAGMA_CUBE(Category.MINIONS, Location.YOUR_ISLAND, Skyblock.Entity.MAGMA_CUBE),
		KILLS_GENERATOR_SLIME(Category.MINIONS, Location.YOUR_ISLAND, Skyblock.Entity.SLIME),
		DEATHS_GENERATOR_GHAST(Category.MINIONS, Location.YOUR_ISLAND, Skyblock.Entity.GHAST),
		DEATHS_GENERATOR_MAGMA_CUBE(Category.MINIONS, Location.YOUR_ISLAND, Skyblock.Entity.MAGMA_CUBE),
		DEATHS_GENERATOR_SLIME(Category.MINIONS, Location.YOUR_ISLAND, Skyblock.Entity.SLIME),

		// BOSSES
		KILLS_BROOD_MOTHER_CAVE_SPIDER(Category.BOSSES, Location.SPIDERS_DEN, Skyblock.Entity.BROOD_MOTHER_CAVE_SPIDER),
		KILLS_BROOD_MOTHER_SPIDER(Category.BOSSES, Location.SPIDERS_DEN, Skyblock.Entity.BROOD_MOTHER_SPIDDER),
		KILLS_CORRUPTED_PROTECTOR(Category.BOSSES, Location.THE_END, Skyblock.Entity.CORRUPTED_PROTECTOR),
		KILLS_MAGMA_CUBE_BOSS(Category.BOSSES, Location.BLAZING_FORTRESS, Skyblock.Entity.MAGMA_CUBE_BOSS),
		DEATHS_BROOD_MOTHER_CAVE_SPIDER(Category.BOSSES, Location.SPIDERS_DEN, Skyblock.Entity.BROOD_MOTHER_CAVE_SPIDER),
		DEATHS_BROOD_MOTHER_SPIDER(Category.BOSSES, Location.SPIDERS_DEN, Skyblock.Entity.BROOD_MOTHER_SPIDDER),
		DEATHS_CORRUPTED_PROTECTOR(Category.BOSSES, Location.THE_END, Skyblock.Entity.CORRUPTED_PROTECTOR),
		DEATHS_MAGMA_CUBE_BOSS(Category.BOSSES, Location.BLAZING_FORTRESS, Skyblock.Entity.MAGMA_CUBE_BOSS),

		// DRAGONS
		ENDER_CRYSTALS_DESTROYED(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.ENDER_CRYSTAL),
		DEATHS_WISE_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.WISE_DRAGON),
		DEATHS_PROTECTOR_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.PROTECTOR_DRAGON),
		DEATHS_YOUNG_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.YOUNG_DRAGON),
		DEATHS_OLD_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.OLD_DRAGON),
		DEATHS_STRONG_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.STRONG_DRAGON),
		DEATHS_UNSTABLE_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.UNSTABLE_DRAGON),
		DEATHS_SUPERIOR_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.SUPERIOR_DRAGON),
		KILLS_PROTECTOR_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.PROTECTOR_DRAGON),
		KILLS_UNSTABLE_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.UNSTABLE_DRAGON),
		KILLS_STRONG_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.STRONG_DRAGON),
		KILLS_OLD_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.OLD_DRAGON),
		KILLS_WISE_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.WISE_DRAGON),
		KILLS_YOUNG_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.YOUNG_DRAGON),
		KILLS_SUPERIOR_DRAGON(Category.DRAGONS, Location.DRAGONS_NEST, Skyblock.Entity.SUPERIOR_DRAGON),

		// RACE
		END_RACE_BEST_TIME(Category.RACE, Location.THE_END),
		FORAGING_RACE_BEST_TIME(Category.RACE, Location.SPRUCE_WOODS),
		CHICKEN_RACE_BEST_TIME(Category.RACE, Location.JERRYS_WORKSHOP),
		CHICKEN_RACE_BEST_TIME_2(Category.RACE, Location.JERRYS_WORKSHOP),
		DUNGEON_HUB_GIANT_MUSHROOM_ANYTHING_NO_RETURN_BEST_TIME(Category.RACE, Location.DUNGEON_HUB),
		DUNGEON_HUB_GIANT_MUSHROOM_NOTHING_NO_RETURN_BEST_TIME(Category.RACE, Location.DUNGEON_HUB),
		DUNGEON_HUB_GIANT_MUSHROOM_NO_PEARLS_NO_RETURN_BEST_TIME(Category.RACE, Location.DUNGEON_HUB),
		DUNGEON_HUB_GIANT_MUSHROOM_NO_ABILITIES_NO_RETURN_BEST_TIME(Category.RACE, Location.DUNGEON_HUB),

		// DUNGEONS
		KILLS_CELLAR_SPIDER(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_CRYPT_DREADLORD(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_CRYPT_LURKER(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_CRYPT_SOULEATER(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_CRYPT_TANK_ZOMBIE(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_CRYPT_UNDEAD(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_LONELY_SPIDER(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_PARASITE(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_SCARED_SKELETON(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_SKELETON_GRUNT(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_SKELETON_SOLDIER(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_SKELETON_MASTER(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_SKELETOR(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_SNIPER_SKELETON(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_ZOMBIE_GRUNT(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_ZOMBIE_KNIGHT(Category.DUNGEONS, Location.THE_CATACOMBS),
		KILLS_ZOMBIE_SOLDIER(Category.DUNGEONS, Location.THE_CATACOMBS),
		DEATHS_CRYPT_LURKER(Category.DUNGEONS, Location.THE_CATACOMBS),
		DEATHS_SKELETON_MASTER(Category.DUNGEONS, Location.THE_CATACOMBS),
		DEATHS_SKELETOR(Category.DUNGEONS, Location.THE_CATACOMBS),
		DEATHS_ZOMBIE_GRUNT(Category.DUNGEONS, Location.THE_CATACOMBS),

		// DUNGEON SPECIAL
		KILLS_BLAZE_HIGHER_OR_LOWER(Category.DUNGEON_SPECIAL, Location.THE_CATACOMBS),
		KILLS_DUNGEON_SECRET_BAT(Category.DUNGEON_SPECIAL, Location.THE_CATACOMBS),
		KILLS_DUNGEON_RESPAWNING_SKELETON(Category.DUNGEON_SPECIAL, Location.THE_CATACOMBS),
		KILLS_DUNGEON_RESPAWNING_SKELETON_SKULL(Category.DUNGEON_SPECIAL, Location.THE_CATACOMBS),
		DEATHS_TRAP(Category.DUNGEON_SPECIAL, Location.THE_CATACOMBS),

		// DUNGEON BOSSES
		KILLS_SCARF(Category.DUNGEON_BOSSES, Location.THE_CATACOMBS),
		DEATHS_SCARF(Category.DUNGEON_BOSSES, Location.THE_CATACOMBS),

		// DUNGEON MINIBOSSES
		KILLS_BONZO_SUMMON_UNDEAD(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_DEATHMITE(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_DIAMOND_GUY(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_KING_MIDAS(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_LOST_ADVENTURER(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_PROFESSOR_GUARDIAN_SUMMON(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_SCARF_ARCHER(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_SCARF_MAGE(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_SCARF_PRIEST(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_SCARF_WARRIOR(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_SHADOW_ASSASSIN(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_WATCHER_BONZO(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		KILLS_WATCHER_SUMMON_UNDEAD(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		DEATHS_DEATHMITE(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		DEATHS_DIAMOND_GUY(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		DEATHS_KING_MIDAS(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		DEATHS_LOST_ADVENTURER(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		DEATHS_PROFESSOR_MAGE_GUARDIAN(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		DEATHS_SHADOW_ASSASSIN(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),
		DEATHS_WATCHER_SUMMON_UNDEAD(Category.DUNGEON_MINIBOSSES, Location.THE_CATACOMBS),

		// DUNGEON ROYALS
		KILLS_CRYPT_UNDEAD_ALEXANDER(Category.DUNGEON_ROYALS, Location.THE_CATACOMBS),
		KILLS_CRYPT_UNDEAD_BERNHARD(Category.DUNGEON_ROYALS, Location.THE_CATACOMBS),
		KILLS_CRYPT_UNDEAD_CHRISTIAN(Category.DUNGEON_ROYALS, Location.THE_CATACOMBS),
		KILLS_CRYPT_UNDEAD_FRIEDRICH(Category.DUNGEON_ROYALS, Location.THE_CATACOMBS),
		KILLS_CRYPT_UNDEAD_MARIUS(Category.DUNGEON_ROYALS, Location.THE_CATACOMBS),
		KILLS_CRYPT_UNDEAD_NICHOLAS(Category.DUNGEON_ROYALS, Location.THE_CATACOMBS),
		KILLS_CRYPT_UNDEAD_PIETER(Category.DUNGEON_ROYALS, Location.THE_CATACOMBS),
		KILLS_CRYPT_UNDEAD_VALENTIN(Category.DUNGEON_ROYALS, Location.THE_CATACOMBS),

		// THE BARN
		KILLS_PIG(Category.THE_BARN, Location.THE_BARN, Skyblock.Entity.PIG),
		KILLS_COW(Category.THE_BARN, Location.THE_BARN, Skyblock.Entity.COW),
		KILLS_CHICKEN(Category.THE_BARN, Location.THE_BARN, Skyblock.Entity.CHICKEN),

		// MUSHROOM ISLAND
		KILLS_SHEEP(Category.MUSHROOM_DESERT, Location.MUSHROOM_DESERT, Skyblock.Entity.SHEEP),
		KILLS_RABBIT(Category.MUSHROOM_DESERT, Location.MUSHROOM_DESERT, Skyblock.Entity.RABBIT),

		// DEEP CAVERNS
		KILLS_LAPIS_ZOMBIE(Category.DEEP_CAVERNS, Location.LAPIS_QUARRY, Skyblock.Entity.LAPIS_ZOMBIE),
		KILLS_EMERALD_SLIME(Category.DEEP_CAVERNS, Location.SLIMEHILL, Skyblock.Entity.EMERALD_SLIME),
		KILLS_DIAMOND_ZOMBIE(Category.DEEP_CAVERNS, Location.OBSIDIAN_SANCTUARY, Skyblock.Entity.DIAMOND_ZOMBIE),
		KILLS_DIAMOND_SKELETON(Category.DEEP_CAVERNS, Location.OBSIDIAN_SANCTUARY, Skyblock.Entity.DIAMOND_SKELETON),
		KILLS_REDSTONE_PIGMAN(Category.DEEP_CAVERNS, Location.PIGMAN_DEN, Skyblock.Entity.REDSTONE_PIGMAN),
		KILLS_INVISIBLE_CREEPER(Category.DEEP_CAVERNS, Location.GUNPOWDER_MINES, Skyblock.Entity.SNEAKY_CREEPER),
		DEATHS_INVISIBLE_CREEPER(Category.DEEP_CAVERNS, Location.GUNPOWDER_MINES, Skyblock.Entity.SNEAKY_CREEPER),
		DEATHS_DIAMOND_ZOMBIE(Category.DEEP_CAVERNS, Location.OBSIDIAN_SANCTUARY, Skyblock.Entity.DIAMOND_ZOMBIE),
		DEATHS_DIAMOND_SKELETON(Category.DEEP_CAVERNS, Location.OBSIDIAN_SANCTUARY, Skyblock.Entity.DIAMOND_SKELETON),
		DEATHS_EMERALD_SLIME(Category.DEEP_CAVERNS, Location.SLIMEHILL, Skyblock.Entity.EMERALD_SLIME),
		DEATHS_LAPIS_ZOMBIE(Category.DEEP_CAVERNS, Location.LAPIS_QUARRY, Skyblock.Entity.LAPIS_ZOMBIE),
		DEATHS_REDSTONE_PIGMAN(Category.DEEP_CAVERNS, Location.PIGMAN_DEN, Skyblock.Entity.REDSTONE_PIGMAN),

		// SPIDERS DEN
		KILLS_SPLITTER_SPIDER_SILVERFISH(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SILVERFISH),
		KILLS_SPLITTER_SPIDER(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SPLITTER_SPIDER),
		KILLS_DASHER_SPIDER(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.DASHER_SPIDER),
		KILLS_WEAVER_SPIDER(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.WEAVER_SPIDER),
		KILLS_JOCKEY_SHOT_SILVERFISH(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SILVERFISH),
		KILLS_JOCKEY_SKELETON(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.JOCKEY_SKELETON),
		KILLS_SPIDER_JOCKEY(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SPIDER_JOCKEY),
		KILLS_SILVERFISH(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SILVERFISH),
		KILLS_SLIME(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SLIME),
		KILLS_RESPAWNING_SKELETON(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SKELETON),
		DEATHS_RESPAWNING_SKELETON(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SKELETON),
		DEATHS_SLIME(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SLIME),
		DEATHS_JOCKEY_SHOT_SILVERFISH(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SILVERFISH),
		DEATHS_DASHER_SPIDER(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.DASHER_SPIDER),
		DEATHS_SPLITTER_SPIDER(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SPLITTER_SPIDER),
		DEATHS_WEAVER_SPIDER(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.WEAVER_SPIDER),
		DEATHS_SPIDER_JOCKEY(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SPIDER_JOCKEY),
		DEATHS_SPLITTER_SPIDER_SILVERFISH(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SILVERFISH),
		DEATHS_SILVERFISH(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.SILVERFISH),
		DEATHS_JOCKEY_SKELETON(Category.SPIDERS_DEN, Location.SPIDERS_DEN, Skyblock.Entity.JOCKEY_SKELETON),

		// BLAZING FORTRESS
		KILLS_WITHER_SKELETON(Category.BLAZING_FORTRESS, Location.BLAZING_FORTRESS, Skyblock.Entity.WITHER_SKELETON),
		KILLS_MAGMA_CUBE(Category.BLAZING_FORTRESS, Location.BLAZING_FORTRESS, Skyblock.Entity.MAGMA_CUBE),
		KILLS_BLAZE(Category.BLAZING_FORTRESS, Location.BLAZING_FORTRESS, Skyblock.Entity.BLAZE),
		KILLS_PIGMAN(Category.BLAZING_FORTRESS, Location.BLAZING_FORTRESS, Skyblock.Entity.ZOMBIE_PIGMAN),
		KILLS_GHAST(Category.BLAZING_FORTRESS, Location.BLAZING_FORTRESS, Skyblock.Entity.GHAST),
		DEATHS_BLAZE(Category.BLAZING_FORTRESS, Location.BLAZING_FORTRESS, Skyblock.Entity.BLAZE),
		DEATHS_WITHER_SKELETON(Category.BLAZING_FORTRESS, Location.BLAZING_FORTRESS, Skyblock.Entity.WITHER_SKELETON),
		DEATHS_MAGMA_CUBE(Category.BLAZING_FORTRESS, Location.BLAZING_FORTRESS, Skyblock.Entity.MAGMA_CUBE),
		DEATHS_GHAST(Category.BLAZING_FORTRESS, Location.BLAZING_FORTRESS, Skyblock.Entity.GHAST),
		DEATHS_PIGMAN(Category.BLAZING_FORTRESS, Location.BLAZING_FORTRESS, Skyblock.Entity.ZOMBIE_PIGMAN),

		// THE END
		KILLS_ENDERMAN(Category.THE_END, Location.THE_END, Skyblock.Entity.ENDERMAN),
		KILLS_ENDERMITE(Category.THE_END, Location.THE_END, Skyblock.Entity.ENDERMITE),
		KILLS_WATCHER(Category.THE_END, Location.THE_END, Skyblock.Entity.WATCHER),
		KILLS_ZEALOT_ENDERMAN(Category.THE_END, Location.THE_END, Skyblock.Entity.ZEALOT),
		KILLS_OBSIDIAN_WITHER(Category.THE_END, Location.THE_END, Skyblock.Entity.OBSIDIAN_DEFENDER),
		DEATHS_ENDERMAN(Category.THE_END, Location.THE_END, Skyblock.Entity.ENDERMAN),
		DEATHS_ENDERMITE(Category.THE_END, Location.THE_END, Skyblock.Entity.ENDERMITE),
		DEATHS_WATCHER(Category.THE_END, Location.THE_END, Skyblock.Entity.WATCHER),
		DEATHS_ZEALOT_ENDERMAN(Category.THE_END, Location.THE_END, Skyblock.Entity.ZEALOT),
		DEATHS_OBSIDIAN_WITHER(Category.THE_END, Location.THE_END, Skyblock.Entity.OBSIDIAN_DEFENDER),

		// FISHING
		ITEMS_FISHED(Category.FISHING),
		ITEMS_FISHED_NORMAL(Category.FISHING),
		ITEMS_FISHED_TREASURE(Category.FISHING),
		ITEMS_FISHED_LARGE_TREASURE(Category.FISHING),
		SHREDDER_BAIT(Category.FISHING),
		SHREDDER_FISHED(Category.FISHING),

		// FISHING KILLS
		KILLS_CHICKEN_DEEP(Category.FISHING_KILLS, Skyblock.Entity.MONSTER_OF_THE_DEEP),
		KILLS_POND_SQUID(Category.FISHING_KILLS, Skyblock.Entity.SQUID),
		KILLS_SEA_GUARDIAN(Category.FISHING_KILLS, Skyblock.Entity.SEA_GUARDIAN),
		KILLS_SEA_ARCHER(Category.FISHING_KILLS, Skyblock.Entity.SEA_ARCHER),
		KILLS_ZOMBIE_DEEP(Category.FISHING_KILLS, Skyblock.Entity.MONSTER_OF_THE_DEEP),
		KILLS_CATFISH(Category.FISHING_KILLS, Skyblock.Entity.CATFISH),
		KILLS_DEEP_SEA_GOLEM(Category.FISHING_KILLS, Skyblock.Entity.DEEP_SEA_PROTECTOR),
		KILLS_FROZEN_STEVE(Category.FISHING_KILLS, Skyblock.Entity.FROZEN_STEVE),
		KILLS_FROSTY_THE_SNOWMAN(Category.FISHING_KILLS, Skyblock.Entity.FROSTY_THE_SNOWMAN),
		KILLS_YETI(Category.FISHING_KILLS, Skyblock.Entity.YETI),
		KILLS_GRINCH(Category.FISHING_KILLS, Skyblock.Entity.GRINCH),
		KILLS_SEA_LEECH(Category.FISHING_KILLS, Skyblock.Entity.SEA_LEECH),
		KILLS_NIGHT_SQUID(Category.FISHING_KILLS, Skyblock.Entity.NIGHT_SQUID),
		KILLS_GUARDIAN_DEFENDER(Category.FISHING_KILLS, Skyblock.Entity.GUARDIAN_DEFENDER),
		KILLS_DEEP_SEA_PROTECTOR(Category.FISHING_KILLS, Skyblock.Entity.DEEP_SEA_PROTECTOR),
		KILLS_WATER_HYDRA(Category.FISHING_KILLS, Skyblock.Entity.WATER_HYDRA),
		KILLS_CARROT_KING(Category.FISHING_KILLS, Skyblock.Entity.CARROT_KING),
		KILLS_SEA_WITCH(Category.FISHING_KILLS, Skyblock.Entity.SEA_WITCH),
		KILLS_SKELETON_EMPEROR(Category.FISHING_KILLS, Skyblock.Entity.SEA_EMPEROR),
		KILLS_GUARDIAN_EMPEROR(Category.FISHING_KILLS, Skyblock.Entity.SEA_EMPEROR),
		KILLS_GUARDIAN_BOSS(Category.FISHING_KILLS, Skyblock.Entity.GUARDIAN_DEFENDER),
		KILLS_SEA_WALKER(Category.FISHING_KILLS, Skyblock.Entity.SEA_WALKER),

		// FISHING DEATHS
		DEATHS_IRON_GOLEM(Category.FISHING_DEATHS, Skyblock.Entity.DEEP_SEA_PROTECTOR),
		DEATHS_FROZEN_STEVE(Category.FISHING_DEATHS, Skyblock.Entity.FROZEN_STEVE),
		DEATHS_YETI(Category.FISHING_DEATHS, Skyblock.Entity.YETI),
		DEATHS_FROSTY_THE_SNOWMAN(Category.FISHING_DEATHS, Skyblock.Entity.FROSTY_THE_SNOWMAN),
		DEATHS_SNOWMAN(Category.FISHING_DEATHS, Skyblock.Entity.FROSTY_THE_SNOWMAN),
		DEATHS_CARROT_KING(Category.FISHING_DEATHS, Skyblock.Entity.CARROT_KING),
		DEATHS_GUARDIAN_EMPEROR(Category.FISHING_DEATHS, Skyblock.Entity.SEA_EMPEROR),
		DEATHS_GUARDIAN_DEFENDER(Category.FISHING_DEATHS, Skyblock.Entity.GUARDIAN_DEFENDER),
		DEATHS_SEA_LEECH(Category.FISHING_DEATHS, Skyblock.Entity.SEA_LEECH),
		DEATHS_DEEP_SEA_PROTECTOR(Category.FISHING_DEATHS, Skyblock.Entity.DEEP_SEA_PROTECTOR),
		DEATHS_WATER_HYDRA(Category.FISHING_DEATHS, Skyblock.Entity.WATER_HYDRA),
		DEATHS_CATFISH(Category.FISHING_DEATHS, Skyblock.Entity.CATFISH),
		DEATHS_GUARDIAN(Category.FISHING_DEATHS, Skyblock.Entity.GUARDIAN_DEFENDER),
		DEATHS_SEA_WITCH(Category.FISHING_DEATHS, Skyblock.Entity.SEA_WITCH),
		DEATHS_SEA_GUARDIAN(Category.FISHING_DEATHS, Skyblock.Entity.SEA_GUARDIAN),
		DEATHS_ZOMBIE_DEEP(Category.FISHING_DEATHS, Skyblock.Entity.MONSTER_OF_THE_DEEP),
		DEATHS_SEA_ARCHER(Category.FISHING_DEATHS, Skyblock.Entity.SEA_ARCHER),
		DEATHS_SKELETON_EMPEROR(Category.FISHING_DEATHS, Skyblock.Entity.SEA_EMPEROR),
		DEATHS_SEA_WALKER(Category.FISHING_DEATHS, Skyblock.Entity.SEA_WALKER),
		DEATHS_OCELOT(Category.FISHING_DEATHS, Skyblock.Entity.CATFISH),

		// SPOOKY FESTIVAL
		KILLS_HORSEMAN_BAT(Category.SPOOKY_FESTIVAL, Location.NONE, Skyblock.Entity.BAT),
		KILLS_HORSEMAN_ZOMBIE(Category.SPOOKY_FESTIVAL, Location.NONE, Skyblock.Entity.ZOMBIE),
		KILLS_HORSEMAN_HORSE(Category.SPOOKY_FESTIVAL, Location.NONE, Skyblock.Entity.SKELETON_HORSE),
		KILLS_HEADLESS_HORSEMAN(Category.SPOOKY_FESTIVAL, Location.NONE, Skyblock.Entity.HEADLESS_HORSEMAN),
		KILLS_BAT_PINATA(Category.SPOOKY_FESTIVAL, Location.NONE, Skyblock.Entity.BAT_PINATA),
		DEATHS_HORSEMAN_BAT(Category.SPOOKY_FESTIVAL, Location.NONE, Skyblock.Entity.BAT),
		DEATHS_HORSEMAN_ZOMBIE(Category.SPOOKY_FESTIVAL, Location.NONE, Skyblock.Entity.ZOMBIE),
		DEATHS_HORSEMAN_HORSE(Category.SPOOKY_FESTIVAL, Location.NONE, Skyblock.Entity.SKELETON_HORSE),
		DEATHS_HEADLESS_HORSEMAN(Category.SPOOKY_FESTIVAL, Location.NONE, Skyblock.Entity.HEADLESS_HORSEMAN),

		// SLAYER
		KILLS_VICIOUS_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.VICIOUS_WOLF),
		KILLS_RUIN_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.WOLF),
		KILLS_PACK_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.PACK_ENFORCER),
		KILLS_ALPHA_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.SVEN_ALPHA),
		KILLS_HOWLING_SPIRIT(Category.SLAYER, Location.HOWLING_CAVE, Skyblock.Entity.HOWLING_SPIRIT),
		KILLS_PACK_SPIRIT(Category.SLAYER, Location.HOWLING_CAVE, Skyblock.Entity.PACK_SPIRIT),
		KILLS_SOUL_OF_THE_ALPHA(Category.SLAYER, Location.HOWLING_CAVE, Skyblock.Entity.SOUL_OF_THE_ALPHA),
		KILLS_TARANTULA_SPIDER(Category.SLAYER, Location.SPIDERS_DEN, Skyblock.Entity.TARANTULA_BROODFATHER),
		KILLS_REVENANT_ZOMBIE(Category.SLAYER, Location.COAL_MINE, Skyblock.Entity.REVENANT_HORROR),
		KILLS_UNBURRIED_ZOMBIE(Category.SLAYER, Location.COAL_MINE, Skyblock.Entity.CRYPT_GHOUL),
		KILLS_VORACIOUS_SPIDER(Category.SLAYER, Location.SPIDERS_DEN, Skyblock.Entity.VORACIOUS_SPIDER),
		KILLS_OLD_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.OLD_WOLF),
		DEATHS_VORACIOUS_SPIDER(Category.SLAYER, Location.SPIDERS_DEN, Skyblock.Entity.VORACIOUS_SPIDER),
		DEATHS_REVENANT_ZOMBIE(Category.SLAYER, Location.COAL_MINE, Skyblock.Entity.REVENANT_HORROR),
		DEATHS_TARANTULA_SPIDER(Category.SLAYER, Location.SPIDERS_DEN, Skyblock.Entity.TARANTULA_BROODFATHER),
		DEATHS_PACK_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.PACK_ENFORCER),
		DEATHS_VICIOUS_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.VICIOUS_WOLF),
		DEATHS_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.WOLF),
		DEATHS_RUIN_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.WOLF),
		DEATHS_OLD_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.OLD_WOLF),
		DEATHS_ALPHA_WOLF(Category.SLAYER, Location.RUINS, Skyblock.Entity.SVEN_ALPHA),
		DEATHS_SOUL_OF_THE_ALPHA(Category.SLAYER, Location.HOWLING_CAVE, Skyblock.Entity.SOUL_OF_THE_ALPHA),
		DEATHS_HOWLING_SPIRIT(Category.SLAYER, Location.HOWLING_CAVE, Skyblock.Entity.HOWLING_SPIRIT),
		DEATHS_PACK_SPIRIT(Category.SLAYER, Location.HOWLING_CAVE, Skyblock.Entity.PACK_SPIRIT),
		DEATHS_UNBURRIED_ZOMBIE(Category.SLAYER, Location.COAL_MINE, Skyblock.Entity.CRYPT_GHOUL),

		// JERRY'S WORKSHOP
		GIFTS_GIVEN(Category.JERRYS_WORKSHOP, Location.JERRYS_WORKSHOP),
		GIFTS_RECEIVED(Category.JERRYS_WORKSHOP, Location.JERRYS_WORKSHOP),
		MOST_WINTER_SNOWBALLS_HIT(Category.JERRYS_WORKSHOP, Location.JERRYS_WORKSHOP),
		MOST_WINTER_DAMAGE_DEALT(Category.JERRYS_WORKSHOP, Location.JERRYS_WORKSHOP),
		MOST_WINTER_MAGMA_DAMAGE_DEALT(Category.JERRYS_WORKSHOP, Location.JERRYS_WORKSHOP),
		MOST_WINTER_CANNONBALLS_HIT(Category.JERRYS_WORKSHOP, Location.JERRYS_WORKSHOP),

		KILLS_LIQUID_HOT_MAGMA(Category.JERRYS_WORKSHOP, Location.JERRYS_WORKSHOP, Skyblock.Entity.LIQUID_HOT_MAGMA),
		KILLS_FIREBALL_MAGMA_CUBE(Category.JERRYS_WORKSHOP, Location.JERRYS_WORKSHOP, Skyblock.Entity.MAGMA_CUBE),

		DEATHS_LIQUID_HOT_MAGMA(Category.JERRYS_WORKSHOP, Location.JERRYS_WORKSHOP, Skyblock.Entity.LIQUID_HOT_MAGMA),
		DEATHS_FIREBALL_MAGMA_CUBE(Category.JERRYS_WORKSHOP, Location.JERRYS_WORKSHOP, Skyblock.Entity.MAGMA_CUBE),

		// AUCTION HOUSE
		AUCTIONS_BIDS(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_NO_BIDS(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_WON(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_HIGHEST_BID(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_FEES(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_GOLD_EARNED(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_GOLD_SPENT(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_CREATED(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_COMPLETED(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_BOUGHT_COMMON(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_BOUGHT_UNCOMMON(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_BOUGHT_RARE(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_BOUGHT_EPIC(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_BOUGHT_LEGENDARY(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_BOUGHT_MYTHIC(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_BOUGHT_SPECIAL(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_BOUGHT_VERY_SPECIAL(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_SOLD_COMMON(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_SOLD_UNCOMMON(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_SOLD_RARE(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_SOLD_EPIC(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_SOLD_LEGENDARY(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_SOLD_MYTHIC(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_SOLD_SPECIAL(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE),
		AUCTIONS_SOLD_VERY_SPECIAL(Category.AUCTION_HOUSE, Location.AUCTION_HOUSE);

		private final Category category;
		private final Location location;
		private final Skyblock.Entity entity;

		Stat(Category category) {
			this(category, Location.NONE, null);
		}

		Stat(Category category, Location location) {
			this(category, location, null);
		}

		Stat(Category category, Skyblock.Entity entity) {
			this(category, Location.NONE, entity);
		}

		Stat(Category category, Location location, Skyblock.Entity entity) {
			this.category = category;
			this.location = location;
			this.entity = entity;
		}

		public Category getCategory() {
			return this.category;
		}

		public Skyblock.Entity getEntity() {
			return this.entity;
		}

		public Location getLocation() {
			return this.location;
		}

		public String getName() {
			return WordUtil.capitalizeFully(this.name().replace("_", " "));
		}

		public boolean hasEntity() {
			return this.entity != null;
		}

		public static ConcurrentList<Stat> getStats(Category category) {
			ConcurrentList<Stat> stats = Concurrent.newList();

			for (Stat value : values()) {
				if (value.getCategory() == category)
					stats.add(value);
			}

			return stats;
		}

		public enum Category {

			PLAYER(Items.SKULL, 3, "Pet Milestone"),
			GENERAL,
			HUB(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Location Hub").create()),
			MINIONS(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Crafted Minions").create()),
			BOSSES(Items.SKULL, 1),
			DRAGONS(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Pet Ender Dragon").create()),
			RACE(Blocks.RAIL),
			DUNGEONS(Items.APPLE),
			DUNGEON_SPECIAL(Items.APPLE),
			DUNGEON_BOSSES(Items.APPLE),
			DUNGEON_MINIBOSSES(Items.APPLE),
			DUNGEON_ROYALS(Items.APPLE),
			SPOOKY_FESTIVAL(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Location Hub").create()),
			DEEP_CAVERNS(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Location Deep Caverns").create()),
			MUSHROOM_DESERT(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Location Mushroom Desert").create()),
			THE_BARN(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Location The Barn").create()),
			SLAYER(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Maddox Batphone").create()),
			THE_END(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Location The End").create()),
			BLAZING_FORTRESS(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Location Blazing Fortress").create()),
			SPIDERS_DEN(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Location Spiders Den").create()),
			JERRYS_WORKSHOP(MinecraftAPI.getMiniBlockDatabase().get("Hypixel Location Jerrys Workshop").create(), "Gifts"),
			AUCTION_HOUSE(Blocks.GOLD_BLOCK, "Auctions Bought", "Auctions Sold", "Auctions"),
			FISHING(Items.FISH, "Items Fished", "Shredder"),
			FISHING_KILLS,
			FISHING_DEATHS;

			private final ItemStack itemStack;
			private final ConcurrentList<String> sortNames;

			Category(String... sortNames) {
				this(Items.PAPER, sortNames);
			}

			Category(Block block, String... sortNames) {
				this(block, 0, sortNames);
			}

			Category(Block block, int meta, String... sortNames) {
				this(Item.getItemFromBlock(block), meta, sortNames);
			}

			Category(Item item, String... sortNames) {
				this(item, 0, sortNames);
			}

			Category(Item item, int meta, String... sortNames) {
				this(new ItemStack(item, 1, meta), sortNames);
			}

			Category(ItemStack itemStack, String... sortNames) {
				this.itemStack = itemStack;
				this.sortNames = Concurrent.newList(sortNames);
				this.sortNames.add("Kills");
				this.sortNames.add("Deaths");
			}

			public String getName() {
				return WordUtil.capitalizeFully(this.name().replace("_", " "));
			}

			public ItemStack getItemStack() {
				return this.itemStack;
			}

			public ConcurrentList<String> getSortNames() {
				return this.sortNames;
			}

		}

	}

	public static class Minion {

		private ConcurrentSet<Integer> unlocked = Concurrent.newSet();
		private final Skyblock.Minion type;

		Minion(Skyblock.Minion type) {
			this.type = type;
		}

		public Skyblock.Minion getType() {
			return this.type;
		}

		public ConcurrentSet<Integer> getUnlocked() {
			return this.unlocked;
		}

	}

	public static class Collection {

		private final Skyblock.Skill type;
		private ConcurrentLinkedMap<Skyblock.Collection, Integer> items = Concurrent.newLinkedMap();
		private ConcurrentLinkedMap<Skyblock.Collection, Integer> unlocked = Concurrent.newLinkedMap();

		Collection(Skyblock.Skill type) {
			this.type = type;
		}

		public int getCollected(Skyblock.Collection collection) {
			return this.items.get(collection);
		}

		public ConcurrentLinkedMap<Skyblock.Collection, Integer> getCollected() {
			return this.items;
		}

		public Skyblock.Skill getType() {
			return this.type;
		}

		public int getUnlocked(Skyblock.Collection collection) {
			return this.unlocked.getOrDefault(collection, 0);
		}

		public ConcurrentLinkedMap<Skyblock.Collection, Integer> getUnlocked() {
			return this.unlocked;
		}

	}

	public static class Sack {

		private final Skyblock.Sack type;
		private ConcurrentLinkedMap<Skyblock.Item, Integer> stored = Concurrent.newLinkedMap();

		Sack(Skyblock.Sack type) {
			this.type = type;
		}

		public int getStored(Skyblock.Item item) {
			return this.stored.getOrDefault(item, (this.type.getItems().contains(item) ? 0 : -1));
		}

		public ConcurrentLinkedMap<Skyblock.Item, Integer> getStored() {
			return this.stored;
		}

		public Skyblock.Sack getType() {
			return this.type;
		}

	}

	public static class Skill extends ExperienceCalculator {

		private final Skyblock.Skill type;
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
			return this.getType() == Skyblock.Skill.RUNECRAFTING ? Skyblock.RUNECRAFTING_EXP_SCALE : Skyblock.SKILL_EXP_SCALE;
		}

		@Override
		public int getMaxLevel() {
			return (this.getType() == Skyblock.Skill.RUNECRAFTING ? 24 : 50);
		}

		public Skyblock.Skill getType() {
			return this.type;
		}

	}

	public static class Banking {

		private double balance;
		private ConcurrentList<Transaction> transactions;

		private Banking() { }

		public double getBalance() {
			return this.balance;
		}

		public ConcurrentList<Transaction> getTransactions() {
			return this.transactions;
		}

		public static class Transaction {

			private double amount;
			private double timestamp;
			private Type action;
			private String initiator_name;

			private Transaction() { }

			public Type getAction() {
				return this.action;
			}

			public double getAmount() {
				return this.amount;
			}

			public String getInitiatorName() {
				return this.initiator_name.replace("Â", ""); // API Artifact
			}

			public double getTimestamp() {
				return this.timestamp;
			}

			public enum Type {

				DEPOSIT,
				WITHDRAW

			}

		}

	}

	public static class NbtContent {

		private int type; // Always 0
		private String data;

		private NbtContent() { }

		public byte[] getData() {
			return DataUtil.decode(this.getRawData().toCharArray());
		}

		public NbtCompound getNbtData() {
			return SimplifiedAPI.getNbtFactory().fromBytes(this.getData());
		}

		public String getRawData() {
			return this.data;
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

	public static class PetInfo extends ExperienceCalculator {

		private String type;
		private double exp;
		private boolean active;
		private Skyblock.Item.Rarity tier;
		private String heldItem;
		private int candyUsed;

		private PetInfo() { }

		public int getCandyUsed() {
			return this.candyUsed;
		}

		@Override
		public double getExperience() {
			return this.exp;
		}

		@Override
		public ConcurrentList<Integer> getExperienceTiers() {
			return Skyblock.PET_EXP_TIER_SCALE.get(this.getTier());
		}

		public HeldItem getHeldItem() {
			return this.isHoldingItem() ? new HeldItem(this.heldItem) : null;
		}

		@Override
		public int getMaxLevel() {
			return 100;
		}

		public String getName() {
			return this.type;
		}

		public Skyblock.Pet getPet() {
			try {
				return Skyblock.Pet.valueOf(this.type);
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

		public Skyblock.Item.Rarity getTier() {
			return this.tier;
		}

		public boolean isActive() {
			return this.active;
		}

		public boolean isHoldingItem() {
			return StringUtil.isNotEmpty(this.heldItem);
		}

		public static class HeldItem {

			private final String itemName;
			private final Skyblock.Pet.Item item;
			private final Skyblock.Item.Rarity rarity;

			HeldItem(String heldItem) {
				heldItem = heldItem.replace("PET_ITEM_", "");
				String[] split = StringUtil.split("_", heldItem);
				Skyblock.Item.Rarity rarity = null;//TODO: NULL Skyblock.Item.Rarity.COMMON;
				Skyblock.Pet.Item item = null;
				int end = 0;

				try {
					rarity = Skyblock.Item.Rarity.valueOf(split[split.length - 1]);
					end = 1;
				} catch (Exception ignore) { }

				this.itemName = StringUtil.join("_", split, 0, split.length - end);

				try {
					item = Skyblock.Pet.Item.valueOf(this.itemName);
				} catch (Exception ignore) { }

				this.item = (item != null ? item : Skyblock.Pet.Item.UNKNOWN);
				this.rarity = (rarity != null ? rarity : this.item.getUniqueRarity());
			}

			public Skyblock.Pet.Item getItem() {
				return this.item;
			}

			public String getItemName() {
				return this.itemName;
			}

			public String getPrettyItemName() {
				return WordUtil.capitalizeFully(this.itemName.replace("_", " "));
			}

			public Skyblock.Item.Rarity getRarity() {
				return this.rarity;
			}

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

	static class Deserializer implements JsonDeserializer<SkyblockIsland> {

		// Use a new instance of Gson to avoid infinite recursion to a deserializer.
		@Override
		@SuppressWarnings("unchecked")
		public SkyblockIsland deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc) throws JsonParseException {
			try {
				SkyblockIsland skyblockProfile = new SkyblockIsland();
				JsonObject rootObject = jsonElement.getAsJsonObject();
				skyblockProfile.profile_id = StringUtil.toUUID(rootObject.get("profile_id").getAsString());
				skyblockProfile.cute_name = rootObject.has("cute_name") ? rootObject.get("cute_name").getAsString() : null;
				Gson gson = new Gson();
				skyblockProfile.banking = gson.fromJson(rootObject.getAsJsonObject("banking"), Banking.class);
				Map<String, LinkedTreeMap<String, Object>> members = gson.fromJson(rootObject.getAsJsonObject("members"), Map.class);

				members.forEach((key, value) -> {
					Member profile = gson.fromJson(gson.toJson(value), Member.class);
					profile.uniqueId = StringUtil.toUUID(key);
					skyblockProfile.members.add(profile);
				});

				return skyblockProfile;
			} catch (Exception ex) {
				throw new HypixelApiException(HypixelApiException.Reason.NO_EXISTING_PLAYER, HypixelAPI.Endpoint.PLAYER, "This player has never played Skyblock!", ex);
			}
		}

	}

}