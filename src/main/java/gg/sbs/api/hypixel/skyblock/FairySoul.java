package gg.sbs.api.hypixel.skyblock;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import gg.sbs.api.minecraft.inventory.item.mini.MiniBlock;

import java.util.Arrays;
import java.util.List;

public enum FairySoul {

	HUB_1(-1, 70, -108, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_2(30, 71, -62, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_3(38, 70, -96, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_4(16, 88, -42, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_5(-43, 79, -41, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_6(-56, 76, -65, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_7(-58, 69, -117, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_8(-34, 70, -30, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_9(-71, 78, -82, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_10(21, 81, -17, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_11(-45, 92, -13, Location.VILLAGE, Location.BANK, Location.FLOWER_HOUSE, Location.AUCTION_HOUSE),
	HUB_12(9, 75, 13, Location.MOUNTAIN),
	HUB_13(10, 179, 22, Location.MOUNTAIN),
	HUB_14(22, 132, 25, Location.MOUNTAIN),
	HUB_15(2, 181, 31, Location.MOUNTAIN),
	HUB_16(131, 99, -77, Location.WILDERNESS, Location.COLOSSEUM),
	HUB_17(87, 77, 43, Location.WILDERNESS),
	HUB_18(110, 67, 58, Location.WILDERNESS),
	HUB_19(49, 121, 69, Location.WIZARD_TOWER),
	HUB_20(86, 89, 66, Location.WILDERNESS),
	HUB_21(145, 61, -42, Location.WILDERNESS, Location.COLOSSEUM),
	HUB_22(43, 152, 73, Location.WIZARD_TOWER),
	HUB_23(43, 120, 70, Location.WIZARD_TOWER),
	HUB_24(57, 90, 79, Location.MOUNTAIN, Location.WIZARD_TOWER),
	HUB_25(155, 62, 28, Location.FISHERMANS_HUT),
	HUB_26(40, 108, 78, Location.WIZARD_TOWER),
	HUB_27(48, 78, 81, Location.WIZARD_TOWER),
	HUB_28(162, 46, 69, Location.FISHERMANS_HUT),
	HUB_29(148, 112, 88, Location.FISHERMANS_HUT, Location.WILDERNESS),
	HUB_30(147, 53, 88, Location.FISHERMANS_HUT),
	HUB_31(169, 73, -29, Location.WILDERNESS, Location.COLOSSEUM),
	HUB_32(176, 64, 42, Location.FISHERMANS_HUT),
	HUB_33(113, 102, 106, Location.WILDERNESS),
	HUB_34(132, 144, 114, Location.WILDERNESS),
	HUB_35(149, 116, 115, Location.WILDERNESS),
	HUB_36(96, 106, 121, Location.WILDERNESS),
	HUB_37(111, 120, 127, Location.WILDERNESS),
	HUB_38(138, 66, 129, Location.WILDERNESS),
	HUB_39(82, 61, 196, Location.WILDERNESS),
	HUB_40(-133, 74, 133, Location.RUINS),
	HUB_41(-152, 67, 123, Location.RUINS),
	HUB_42(-48, 76, 49, Location.MOUNTAIN),
	HUB_43(-166, 79, 93, Location.RUINS),
	HUB_44(-52, 161, 43, Location.MOUNTAIN),
	HUB_45(-183, 80, 29, Location.RUINS),
	HUB_46(-191, 102, 109, Location.RUINS),
	HUB_47(-195, 55, 153, Location.RUINS),
	HUB_48(-207, 100, 66, Location.RUINS),
	HUB_49(-214, 103, 89, Location.RUINS),
	HUB_50(-229, 123, 84, Location.RUINS),
	HUB_51(-233, 86, 84, Location.RUINS),
	HUB_52(-252, 132, 51, Location.RUINS),
	HUB_53(-225, 72, -21, Location.FOREST),
	HUB_54(-142, 77, -31, Location.FOREST),
	HUB_55(-259, 114, 85, Location.RUINS),
	HUB_56(-260, 96, 48, Location.RUINS),
	HUB_57(-262, 102, 67, Location.RUINS),
	HUB_58(-208, 70, -80, Location.GRAVEYARD, Location.FOREST),
	HUB_59(-187, 92, -104, Location.GRAVEYARD),
	HUB_60(10, 65, -160, Location.COAL_MINE),
	HUB_61(-86, 74, -117, Location.GRAVEYARD),
	HUB_62(-13, 79, -166, Location.COAL_MINE),
	HUB_63(-15, 61, -166, Location.COAL_MINE),
	HUB_64(34, 72, -162, Location.FARM),
	HUB_65(70, 90, -149, Location.FARM),
	HUB_66(72, 71, -190, Location.FARM),
	HUB_67(-3, 193, 32, Location.MOUNTAIN),
	HUB_68(-32, 71, 21, Location.MOUNTAIN),
	HUB_69(-39, 191, 34, Location.MOUNTAIN),
	HUB_70(-50, 132, 32, Location.MOUNTAIN),
	HUB_71(-56, 115, 28, Location.MOUNTAIN),
	HUB_72(-60, 108, 3, Location.MOUNTAIN),
	HUB_73(180, 63, -15, Location.WILDERNESS, Location.COLOSSEUM),
	HUB_74(-245, 75, 149, Location.RUINS),
	HUB_75(-261, 56, 115, Location.RUINS),
	HUB_76(169, 60, 129, Location.WILDERNESS),
	HUB_77(-247, 74, 125, Location.RUINS),
	HUB_78(-33, 76, -213, Location.COAL_MINE),
	HUB_79(-248, 74, 125, Location.RUINS),
	HUB_81(-92, 59, -138, Location.CATACOMBS_ENTRANCE),

	DUNGEON_HUB_1(1, 134, 75, Location.DUNGEON_HUB),
	DUNGEON_HUB_2(17, 124, -58, Location.DUNGEON_HUB),
	DUNGEON_HUB_3(10, 164, -10, Location.DUNGEON_HUB),
	DUNGEON_HUB_4(-4, 21, -17, Location.DUNGEON_HUB),
	DUNGEON_HUB_5(-55, 82, -10, Location.DUNGEON_HUB),
	DUNGEON_HUB_6(-139, 46, -1, Location.DUNGEON_HUB),
	DUNGEON_HUB_7(14, 60, 99, Location.DUNGEON_HUB),

	/*THE_CATACOMBS_1(143, 82, 70, Location.THE_CATACOMBS),
	THE_CATACOMBS_2(150, 83, 147, Location.THE_CATACOMBS),
	THE_CATACOMBS_3(27, 68, 95, Location.THE_CATACOMBS),
	THE_CATACOMBS_4(31, 68, 35, Location.THE_CATACOMBS),
	THE_CATACOMBS_5(98, 74, 60, Location.THE_CATACOMBS),
	THE_CATACOMBS_6(111, 82, 70, Location.THE_CATACOMBS),
	THE_CATACOMBS_7(34, 74, 60, Location.THE_CATACOMBS),
	THE_CATACOMBS_8(27, 68, 63, Location.THE_CATACOMBS),*/

	FLOATING_ISLANDS_1(-257, 50, -19, Location.BIRCH_PARK),
	FLOATING_ISLANDS_2(-294, 85, 31, Location.BIRCH_PARK),
	FLOATING_ISLANDS_3(-315, 89, -72, Location.BIRCH_PARK),
	FLOATING_ISLANDS_4(-318, 90, 2, Location.SPRUCE_WOODS),
	FLOATING_ISLANDS_5(-357, 99, 79, Location.SPRUCE_WOODS),
	FLOATING_ISLANDS_6(-286, 119, -93, Location.DARK_THICKET),
	FLOATING_ISLANDS_7(-261, 151, -85, Location.DARK_THICKET),
	FLOATING_ISLANDS_8(-370, 112, -118, Location.DARK_THICKET),
	FLOATING_ISLANDS_9(-386, 108, -69, Location.DARK_THICKET),
	FLOATING_ISLANDS_10(-345, 141, -94, Location.SAVANNA_WOODLAND),
	FLOATING_ISLANDS_11(-404, 136, 6, Location.SAVANNA_WOODLAND),
	FLOATING_ISLANDS_12(-382, 131, -25, Location.JUNGLE_ISLAND),
	FLOATING_ISLANDS_13(-388, 107, -61, Location.JUNGLE_ISLAND),
	FLOATING_ISLANDS_14(-454, 120, -58, Location.JUNGLE_ISLAND),
	FLOATING_ISLANDS_15(-408, 122, -92, Location.JUNGLE_ISLAND),
	FLOATING_ISLANDS_16(-471, 132, -125, Location.JUNGLE_ISLAND),
	FLOATING_ISLANDS_17(-450, 113, -87, Location.JUNGLE_ISLAND),
	HOWLING_CAVE_1(-390, 61, -6, Location.HOWLING_CAVE),

	SPIDERS_DEN_1(-245, 71, -210, Location.SPIDERS_DEN),
	SPIDERS_DEN_2(-254, 183, -265, Location.SPIDERS_DEN),
	SPIDERS_DEN_3(-256, 161, -278, Location.SPIDERS_DEN),
	SPIDERS_DEN_4(-254, 215, -277, Location.SPIDERS_DEN),
	SPIDERS_DEN_5(-269, 152, -252, Location.SPIDERS_DEN),
	SPIDERS_DEN_6(-284, 89, -178, Location.SPIDERS_DEN),
	SPIDERS_DEN_7(-291, 97, -277, Location.SPIDERS_DEN),
	SPIDERS_DEN_8(-317, 87, -217, Location.SPIDERS_DEN),
	SPIDERS_DEN_9(-342, 121, -199, Location.SPIDERS_DEN),
	SPIDERS_DEN_10(-335, 81, -277, Location.SPIDERS_DEN),
	SPIDERS_DEN_11(-244, 88, -314, Location.SPIDERS_DEN),
	SPIDERS_DEN_12(-225, 73, -362, Location.SPIDERS_DEN),
	SPIDERS_DEN_13(-215, 95, -339, Location.SPIDERS_DEN),
	SPIDERS_DEN_14(-202, 113, -305, Location.SPIDERS_DEN),
	SPIDERS_DEN_15(-177, 95, -338, Location.SPIDERS_DEN),
	SPIDERS_DEN_16(-152, 79, -343, Location.SPIDERS_DEN),
	SPIDERS_DEN_17(-426, 84, -183, Location.SPIDERS_DEN),

	BLAZING_FORTRESS_1(-314, 96, -402, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_2(-309, 146, -427, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_3(-373, 136, -398, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_4(-336, 91, -447, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_5(-323, 139, -443, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_6(-389, 105, -462, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_7(-379, 119, -478, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_8(-216, 62, -481, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_9(-320, 77, -503, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_10(-300, 75, -545, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_11(-431, 79, -564, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_12(-433, 208, -579, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_13(-236, 84, -592, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_14(-313, 39, -677, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_15(-374, 172, -683, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_16(-306, 107, -691, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_17(-347, 133, -694, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_18(-181, 90, -608, Location.BLAZING_FORTRESS),
	BLAZING_FORTRESS_19(-317, 203, -739, Location.BLAZING_FORTRESS),

	GOLD_MINE_1(-47, 75, -298, Location.GOLD_MINE),
	GOLD_MINE_2(-62, 104, -289, Location.GOLD_MINE),
	GOLD_MINE_3(-37, 78, -308, Location.GOLD_MINE),
	GOLD_MINE_4(17, 86, -312, Location.GOLD_MINE),
	GOLD_MINE_5(21, 36, -320, Location.GOLD_MINE),
	GOLD_MINE_6(-44, 100, -344, Location.GOLD_MINE),
	GOLD_MINE_7(-26, 94, -340, Location.GOLD_MINE),
	GOLD_MINE_8(-1, 80, -337, Location.GOLD_MINE),
	GOLD_MINE_9(19, 57, -341, Location.GOLD_MINE),
	GOLD_MINE_10(-19, 142, -364, Location.GOLD_MINE),
	GOLD_MINE_11(-23, 113, -353, Location.GOLD_MINE),
	GOLD_MINE_12(-11, 76, -395, Location.GOLD_MINE),

	DEEP_CAVERNS_1(3, 152, 85, Location.DEEP_CAVERNS),
	DEEP_CAVERNS_2(18, 74, 74, Location.SLIMEHILL),
	DEEP_CAVERNS_3(-21, 25, 72, Location.DIAMOND_RESERVE),
	DEEP_CAVERNS_4(3, 182, 50, Location.DEEP_CAVERNS),
	DEEP_CAVERNS_5(0, 65, 57, Location.SLIMEHILL),
	DEEP_CAVERNS_6(3, 14, 51, Location.OBSIDIAN_SANCTUARY),
	DEEP_CAVERNS_7(9, 170, 44, Location.DEEP_CAVERNS),
	DEEP_CAVERNS_8(-60, 37, 52, Location.DIAMOND_RESERVE),
	DEEP_CAVERNS_9(-35, 127, 28, Location.LAPIS_QUARRY),
	DEEP_CAVERNS_10(-18, 163, 26, Location.GUNPOWDER_MINES),
	DEEP_CAVERNS_11(44, 98, 23, Location.PIGMAN_DEN),
	DEEP_CAVERNS_12(57, 161, 19, Location.GUNPOWDER_MINES),
	DEEP_CAVERNS_13(29, 149, 14, Location.GUNPOWDER_MINES),
	DEEP_CAVERNS_14(-2, 255, -1, Location.DEEP_CAVERNS),
	DEEP_CAVERNS_15(-40, 72, 0, Location.SLIMEHILL),
	DEEP_CAVERNS_16(-11, 102, -16, Location.PIGMAN_DEN),
	DEEP_CAVERNS_17(-71, 13, 5, Location.OBSIDIAN_SANCTUARY),
	DEEP_CAVERNS_18(-76, 13, 24, Location.OBSIDIAN_SANCTUARY),
	DEEP_CAVERNS_19(-8, 74, -44, Location.SLIMEHILL),
	DEEP_CAVERNS_20(71, 167, -12, Location.DEEP_CAVERNS),
	DEEP_CAVERNS_21(22, 156, -42, Location.GUNPOWDER_MINES),

	BARN_1(155, 23, -204, Location.THE_BARN),
	BARN_2(143, 90, -243, Location.THE_BARN),
	BARN_3(182, 99, -235, Location.THE_BARN),
	BARN_4(96, 96, -287, Location.THE_BARN),
	BARN_5(99, 112, -275, Location.THE_BARN),
	BARN_6(126, 91, -304, Location.THE_BARN),
	BARN_7(183, 99, -305, Location.THE_BARN),

	MUSHROOM_DESERT_1(136, 63, -382, Location.MUSHROOM_DESERT),
	MUSHROOM_DESERT_2(150, 82, -371, Location.MUSHROOM_DESERT),
	MUSHROOM_DESERT_3(169, 73, -383, Location.MUSHROOM_DESERT),
	MUSHROOM_DESERT_4(198, 61, -342, Location.MUSHROOM_DESERT),
	MUSHROOM_DESERT_5(215, 85, -409, Location.MUSHROOM_DESERT),
	MUSHROOM_DESERT_6(217, 94, -366, Location.MUSHROOM_DESERT),
	MUSHROOM_DESERT_7(221, 91, -422, Location.MUSHROOM_DESERT),
	MUSHROOM_DESERT_8(228, 115, -400, Location.MUSHROOM_DESERT),

	END_1(-517, 100, -295, Location.THE_END),
	END_2(-545, 92, -257, Location.THE_END),
	END_3(-492, 81, -275, Location.THE_END),
	END_4(-587, 122, -276, Location.THE_END),
	END_5(-696, 116, -256, Location.THE_END),
	END_6(-748, 106, -284, Location.THE_END, Location.DRAGONS_NEST),
	END_7(-587, 48, -293, Location.THE_END, Location.DRAGONS_NEST),
	END_8(-723, 75, -222, Location.DRAGONS_NEST),
	END_9(-657, 36, -201, Location.THE_END, Location.DRAGONS_NEST),
	END_10(-609, 84, -230, Location.DRAGONS_NEST),
	END_11(-583, 208, -272, Location.THE_END),
	END_12(-492, 21, -175, Location.THE_END),

	JERRYS_WORKSHOP_1(-7, 108, 107, Location.JERRYS_WORKSHOP),
	JERRYS_WORKSHOP_2(-44, 87, 76, Location.JERRYS_WORKSHOP),
	JERRYS_WORKSHOP_3(-95, 77, 20, Location.JERRYS_WORKSHOP),
	JERRYS_WORKSHOP_4(74, 109, -18, Location.JERRYS_WORKSHOP),
	JERRYS_WORKSHOP_5(56, 108, 64, Location.JERRYS_WORKSHOP);

	private static final MiniBlock fairySoul = MinecraftAPI.getMiniBlockDatabase().get("Hypixel Fairy Soul");
	private final List<Location> locations;
	private final int radius = 5;
	private final AxisAlignedBB area;
	private final int x;
	private final int y;
	private final int z;

	FairySoul(int x, int y, int z, Location... locations) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.locations = Arrays.asList(locations);
		this.area = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
	}

	public List<Location> getLocations() {
		return locations;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public BlockPos getPosition() {
		return new BlockPos(this.x, this.y, this.z);
	}

	public boolean isNear(int x, int y, int z) {
		return this.area.contains(new Vec3d(x, y, z));
	}

	public static boolean isNearFairySoul(int x, int y, int z) {
		return getNearbyFairySoul(x, y ,z) != null;
	}

	public static FairySoul getFairySoul(Entity entity) {
		return getFairySoul(entity.getPosition().up());
	}

	public static FairySoul getFairySoul(BlockPos blockPos) {
		for (FairySoul soul : values()) {
			if (soul.x == blockPos.getX() && soul.y == blockPos.getY() && soul.getZ() == blockPos.getZ())
				return soul;
		}

		return null;
	}

	public static boolean hasFairySoul(Entity entity) {
		return hasFairySoul(entity.getPosition().up());
	}

	public static boolean hasFairySoul(BlockPos blockPos) {
		return getFairySoul(blockPos) != null;
	}

	public static boolean isFairySoul(Entity entity) {
		if (entity instanceof EntityArmorStand) {
			EntityArmorStand armorStand = (EntityArmorStand)entity;

			if (armorStand.getTotalArmorValue() == 0) {
				Iterable<ItemStack> armorList = armorStand.getArmorInventoryList();

				for (ItemStack itemStack : armorList) {
					if (!Items.AIR.equals(itemStack.getItem())) {
						if (itemStack.getItemDamage() == 3) {
							if (fairySoul.equals(itemStack))
								return true;
						}
					}
				}
			}
		}

		return false;
	}

	public static FairySoul getNearbyFairySoul(BlockPos blockPos) {
		return getNearbyFairySoul(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public static FairySoul getNearbyFairySoul(int x, int y, int z) {
		for (FairySoul soul : values()) {
			if (soul.isNear(x, y, z))
				return soul;
		}

		return null;
	}

}