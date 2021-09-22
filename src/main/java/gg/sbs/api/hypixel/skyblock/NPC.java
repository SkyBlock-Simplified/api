package gg.sbs.api.hypixel.skyblock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import gg.sbs.api.util.ListUtil;
import gg.sbs.api.util.StringUtil;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public enum NPC {

	AUCTION_MASTER(17.5,71,-78.5, "Auction Master", false, Location.VILLAGE, Location.AUCTION_HOUSE),
	BAZAAR(32.5, 71, -78.5, "Bazaar", true, Location.VILLAGE, Location.BAZAAR_ALLEY),
	WARREN(37.5, 71, -84.5, "Warren", true, Location.VILLAGE, Location.BAZAAR_ALLEY),
	BANKER(20.5, 71, -40.5, "Banker", false, Location.VILLAGE, Location.BANK),
	BAKER(34.5, 71, -44.5, "Baker", false, Location.VILLAGE),
	LOBBY_SELECTOR(-9,70,-79, "Lobby Selector", false, Location.VILLAGE),
	LUMBER_MERCHANT(-18.5,70,-90, "Lumber Merchant", true, Location.VILLAGE),
	ADVENTURER(-18.5,70,-77, "Adventurer", true, Location.VILLAGE),
	FISH_MERCHANT(-25.5,70,-77, "Fish Merchant", true, Location.VILLAGE),
	ARMORSMITH(-25.5,70,-90, "Armorsmith", true, Location.VILLAGE),
	BLACKSMITH(-19.5,71,-124.5, "Blacksmith", false, Location.VILLAGE),
	BLACKSMITH_2(-39.5,77,-299.5, "Blacksmith", false, Location.GOLD_MINE),
	FARM_MERCHANT(-7,70,-48.5, "Farm Merchant", true, Location.VILLAGE),
	MINE_MERCHANT(-19,70,-48.5, "Mine Merchant", true, Location.VILLAGE),
	WEAPONSMITH(-19,70,-41.5, "Weaponsmith", false, Location.VILLAGE),
	BUILDER(-7,70,-41.5, "Builder", true, Location.VILLAGE),
	LIBRARIAN(17.5,71,-16.5, "Librarian", true, Location.VILLAGE, Location.LIBRARY),
	MARCO(9.5,71,-14, "Marco", false, Location.VILLAGE, Location.FLOWER_HOUSE),
	ALCHEMIST(-33.5,73,-14.5, "Alchemist", true, Location.VILLAGE),
	PAT(-129.5,73,-98.5, "Pat", true, Location.GRAVEYARD),
	EVENT_MASTER(-61.5,71,-54.5, "Event Master", false, Location.COLOSSEUM, Location.VILLAGE),
	GOLD_FORGER(-27.5,74,-294.5, "Gold Forger", true, Location.GOLD_MINE),
	IRON_FORGER(-1.5,75,-307.5, "Iron Forger", true, Location.GOLD_MINE),
	RUSTY(-20,78,-326, "Rusty", false, Location.GOLD_MINE),
	MADDOX_THE_SLAYER(-87,66,-70, "Maddox The Slayer", false, Location.VILLAGE, Location.TAVERN),
	SIRIUS(91.5,75,176.5, "Sirius", false, Location.WILDERNESS),
	MORT(-68.5, 123.0, 0.5, "Mort", false, Location.DUNGEON_HUB),
	OPHELIA(-63.5, 121.0, -6.5, "Ophelia", true, Location.DUNGEON_HUB),
	MALIK(-63.5, 121.0, 5.5, "Malik", false, Location.DUNGEON_HUB),
	GUILDFORD(-18.5, 86.0, 4.5, "Guildford", false, Location.DUNGEON_HUB);

	private static final int hideRadius = 4;
	private final AxisAlignedBB hideArea;
	private final double x;
	private final double y;
	private final double z;
	private final String name;
	private final boolean isMerchant;
	private final Set<Location> locations;

	NPC(double x, double y, double z, String name, boolean isMerchant, Location... locations) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = name;
		this.isMerchant = isMerchant;
		this.hideArea = new AxisAlignedBB(x - hideRadius, y - hideRadius, z - hideRadius, x + hideRadius, y + hideRadius, z + hideRadius);
		this.locations = EnumSet.copyOf(Arrays.asList(locations));
	}

	public Set<Location> getLocations() {
		return this.locations;
	}

	public String getName() {
		return this.name;
	}

	public Vec3d getPosition() {
		return new Vec3d(x, y, z);
	}

	public Entity getEntity() {
		if (this.getLocations().contains(Skyblock.getLocation())) {
			try {
				return Minecraft.getMinecraft().world.getLoadedEntityList().stream()
						.filter(entity -> !(entity instanceof EntityArmorStand))
						.filter(entity -> (entity.posX == this.x && entity.posY == this.y && entity.posZ == this.z))
						.collect(ListUtil.toSingleton());
			} catch (Exception ignored) { }
		}

		return null;
	}

	public boolean isAtLocation(Location location) {
		return this.locations.contains(location);
	}

	public boolean isMerchant() {
		return this.isMerchant;
	}

	public boolean isNearEntity(Entity entity) {
		if (this.locations.contains(Skyblock.getLocation())) {
			double x = entity.posX;
			double y = entity.posY;
			double z = entity.posZ;

			return this.hideArea.contains(new Vec3d(x, y, z)) && (this.x != x || this.y != y || this.z != z) && !isNPC(entity);
		}

		return false;
	}

	public static NPC getNPC(Entity entity) {
		try {
			for (NPC npc : values()) {
				if (npc.getEntity() != null && npc.getEntity().equals(entity))
					return npc;
			}
		} catch (Exception ignored) { }

		return null;
	}

	public static NPC getNPC(String name) {
		for (NPC npc : values()) {
			if (npc.getName().equalsIgnoreCase(name))
				return npc;

			if (npc.name().equalsIgnoreCase(name))
				return npc;
		}

		return null;
	}

	public static boolean isMerchant(String name) {
		for (NPC npc : values()) {
			if (npc.isMerchant()) {
				if (npc.name().equalsIgnoreCase(name.replaceAll(" ", "_")))
					return true;
			}
		}

		return name.contains("Merchant");
	}

	public static boolean isNearNPC(Entity entity) {
		for (NPC npc : values()) {
			if (npc.isNearEntity(entity))
				return true;
		}

		return false;
	}

	public static boolean isNotNPC(Entity entity) {
		if (entity instanceof EntityOtherPlayerMP) {
			EntityPlayer p = (EntityPlayer)entity;
			Team team = p.getTeam();

			if (team instanceof ScorePlayerTeam) {
				ScorePlayerTeam playerTeam = (ScorePlayerTeam)team;
				String color = playerTeam.getPrefix();
				return StringUtil.notEmpty(color);
			}
		}

		return true;
	}

	public static boolean isNPC(Entity entity) {
		return !isNotNPC(entity);
	}

	public static boolean isNPC(String name) {
		for (NPC npc : values()) {
			if (npc.getName().equalsIgnoreCase(name))
				return true;
		}

		return false;
	}

}