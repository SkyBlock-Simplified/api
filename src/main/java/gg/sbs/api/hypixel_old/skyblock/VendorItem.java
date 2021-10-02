package gg.sbs.api.hypixel_old.skyblock;

import gg.sbs.api.util.ListUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum VendorItem {

	// Farm Merchant
	WHEAT("FARM_MERCHANT", Skyblock.Item.WHEAT, 149, 149.3),
	CARROT("FARM_MERCHANT", Skyblock.Item.CARROT, 149, 147),
	MELON("FARM_MERCHANT", Skyblock.Item.MELON, 128, 128),
	POTATO("FARM_MERCHANT", Skyblock.Item.POTATO, 149, 149.3),
	SUGAR_CANE("FARM_MERCHANT", Skyblock.Item.SUGAR_CANE, 320),
	PUMPKIN("FARM_MERCHANT", Skyblock.Item.PUMPKIN, 512),
	COCOA_BEANS("FARM_MERCHANT", Skyblock.Item.COCOA_BEANS, 320),
	RED_MUSHROOM("FARM_MERCHANT", Skyblock.Item.RED_MUSHROOM, 768),
	BROWN_MUSHROOM("FARM_MERCHANT", Skyblock.Item.BROWN_MUSHROOM, 768),
	SAND("FARM_MERCHANT", Skyblock.Item.SAND, 256),

	// Mine Merchant
	COAL("MINE_MERCHANT", Skyblock.Item.COAL, 256),
	IRON_INGOT("MINE_MERCHANT", Skyblock.Item.IRON_INGOT, 352),
	GOLD_INGOT("MINE_MERCHANT", Skyblock.Item.GOLD_INGOT, 384),
	GRAVEL("MINE_MERCHANT", Skyblock.Item.GRAVEL, 384),
	COBBLESTONE("MINE_MERCHANT", Skyblock.Item.COBBLESTONE, 192),

	// Builder
	ICE("BUILDER", Skyblock.Item.ICE, 64),
	PACKED_ICE("BUILDER", Skyblock.Item.PACKED_ICE, 576),
	WOOL("BUILDER", Skyblock.Item.WOOL, 384),
	QUARTZ_BLOCK("BUILDER", Skyblock.Item.QUARTZ_BLOCK, 2048),

	// Weaponsmith
	ARROW("WEAPONSMITH", Skyblock.Item.ARROW, 213, 213.3),

	// Fish Merchant
	RAW_FISH("FISH_MERCHANT", Skyblock.Item.RAW_FISH, 1280),
	RAW_SALMON("FISH_MERCHANT", Skyblock.Item.RAW_SALMON, 1920),
	CLOWNFISH("FISH_MERCHANT", Skyblock.Item.CLOWNFISH, 6400),
	PUFFERFISH("FISH_MERCHANT", Skyblock.Item.PUFFERFISH, 2560),

	// Adventurer
	ROTTEN_FLESH("ADVENTURER", Skyblock.Item.ROTTEN_FLESH, 512),
	BONE("ADVENTURER", Skyblock.Item.BONE, 512),
	STRING("ADVENTURER", Skyblock.Item.STRING, 640),
	SLIME_BALL("ADVENTURER", Skyblock.Item.SLIME_BALL, 896),
	GUNPOWDER("ADVENTURER", Skyblock.Item.GUNPOWDER, 640),

	// Lumber Merchant
	OAK_WOOD("LUMBER_MERCHANT", Skyblock.Item.OAK_WOOD, 320),
	BIRCH_WOOD("LUMBER_MERCHANT", Skyblock.Item.BIRCH_WOOD, 320),
	SPRUCE_WOOD("LUMBER_MERCHANT", Skyblock.Item.SPRUCE_WOOD, 320),
	DARK_OAK_WOOD("LUMBER_MERCHANT", Skyblock.Item.DARK_OAK_WOOD, 320),
	ACACIA_WOOD("LUMBER_MERCHANT", Skyblock.Item.ACACIA_WOOD, 320),
	JUNGLE_WOOD("LUMBER_MERCHANT", Skyblock.Item.JUNGLE_WOOD, 320),

	// Alchemist
	NETHER_WART("ALCHEMIST", Skyblock.Item.NETHER_WART, 640),
	SUGAR("ALCHEMIST", Skyblock.Item.SUGAR, 256),
	RABBIT_FOOT("ALCHEMIST", Skyblock.Item.RABBIT_FOOT, 640),
	GLISTERING_MELON("ALCHEMIST", Skyblock.Item.GLISTERING_MELON, 640),
	SPIDER_EYE("ALCHEMIST", Skyblock.Item.SPIDER_EYE, 768),
	BLAZE_POWDER("ALCHEMIST", Skyblock.Item.BLAZE_POWDER, 768),
	GHAST_TEAR("ALCHEMIST", Skyblock.Item.GHAST_TEAR, 12800),
	MAGMA_CREAM("ALCHEMIST", Skyblock.Item.MAGMA_CREAM, 1280),

	// Pat
	FLINT("PAT", Skyblock.Item.FLINT, 384),
	GRAVEL_CHEAP("PAT", Skyblock.Item.GRAVEL, 277, 277.4),

	// Forgers
	IRON_INGOT_CHEAP("IRON_FORGER", Skyblock.Item.IRON_INGOT, 320),
	GOLD_INGOT_CHEAP("GOLD_FORGER", Skyblock.Item.GOLD_INGOT, 352);

	private final String npc;
	private final Skyblock.Item skyblockItem;
	private final double listedPrice;
	private final double actualPrice;

	VendorItem(String npc, Skyblock.Item skyblockItem, double listedPrice) {
		this(npc, skyblockItem, listedPrice, listedPrice);
	}

	VendorItem(String npc, Skyblock.Item skyblockItem, double listedPrice, double actualPrice) {
		this.npc = npc;
		this.skyblockItem = skyblockItem;
		//this.itemName = (StringUtil.isEmpty(itemName) ? name() : FormatUtil.format(itemName, name(), skyblockItem.getItem().getRegistryName().getResourcePath(), skyblockItem.getItemId())).toLowerCase();
		this.listedPrice = listedPrice;
		this.actualPrice = actualPrice;
	}

	public String getNPC() {
		return this.npc;
	}

	public Skyblock.Item getSkyblockItem() {
		return this.skyblockItem;
	}

	public String getItemName() {
		return this.getSkyblockItem().getItemName();
	}

	public String getPrettyItemName() {
		return this.getSkyblockItem().getPrettyItemName();
	}

	public double getListedPrice() {
		return this.listedPrice;
	}

	public double getPricePerItem() {
		return this.actualPrice / 64.0;
	}

	public double getPricePerEnchantedItem() {
		return this.actualPrice * 2.5;
	}

	public double getPricePerStack() {
		return this.actualPrice;
	}
/*
	public static VendorItem getVendorItem(Skyblock.Item item) {
		return Arrays.stream(values()).filter(vendorItem -> vendorItem.getSkyblockItem().getItem().equals(item)).collect(ListUtil.toSingleton());
	}
*/
	public static VendorItem getVendorItem(String itemName) {
		return Arrays.stream(values()).filter(vendorItem -> {
			for (Skyblock.Item skyblockItem : vendorItem.getSkyblockItem().getCollection()) {
				if (skyblockItem.getItemName().equalsIgnoreCase(itemName))
					return true;
			}

			return false;
		}).collect(ListUtil.toSingleton());
	}

	public static VendorItem getVendorItem(String npc, String itemName) {
		return Arrays.stream(values()).filter(vendorItem -> vendorItem.getNPC() == npc).filter(vendorItem -> vendorItem.getSkyblockItem().getCollectionItemNames().contains(itemName)).collect(ListUtil.toSingleton());
	}
/*
	public static VendorItem getVendorItem(ItemStack itemStack) {
		return Arrays.stream(values()).filter(vendorItem -> {
			if (!itemStack.hasTagCompound())
				return false;

			NbtCompound nbt = SimplifiedApi.getNbtFactory().fromItemStack(itemStack);
			boolean idMatch;

			if (!nbt.containsKey("ExtraAttributes"))
				idMatch = true;
			else {
				NbtCompound extraAttributes = nbt.get("ExtraAttributes");
				String itemId = extraAttributes.get("id");
				idMatch = vendorItem.getSkyblockItem().getItemId().equals(itemId);
			}

			return vendorItem.getSkyblockItem().getItem().equals(itemStack.getItem()) && idMatch;
		}).collect(ListUtil.toSingleton());
	}
*/
	public static Set<Skyblock.Item> getVendorItems(String npc) {
		Set<VendorItem> vendorItems =  Arrays.stream(values()).filter(vendorItem -> vendorItem.getNPC() == npc).collect(Collectors.toSet());
		Set<Skyblock.Item> skyblockItems = new HashSet<>();
		vendorItems.forEach(vendorItem -> skyblockItems.addAll(getVendorItems(vendorItem)));
		return skyblockItems;
	}

	public static Set<Skyblock.Item> getVendorItems(VendorItem vendorItem) {
		return Arrays.stream(Skyblock.Item.values()).filter(item -> item.getCollection().contains(vendorItem.getSkyblockItem())).collect(Collectors.toSet());
	}

	public static Set<Skyblock.Item> getVendorItems(Skyblock.Item skyblockItem) {
		return Arrays.stream(Skyblock.Item.values()).filter(item -> item.getCollection().contains(skyblockItem)).collect(Collectors.toSet());
	}

	public static Set<String> getVendors() {
		return Arrays.stream(values()).map(VendorItem::getNPC).collect(Collectors.toSet());
	}

}