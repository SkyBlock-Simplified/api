package gg.sbs.api.hypixel_old.skyblock;

import gg.sbs.api.util.WordUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public enum MenuItem {

	POTION_BAG(Items.SKULL, "§aPotion Bag", Skyblock.InventoryType.POTION_BAG),
	QUIVER(Items.SKULL, "§aQuiver", Skyblock.InventoryType.QUIVER),
	ACCESSORY_BAG(Items.SKULL, "§aAccessory Bag", Skyblock.InventoryType.ACCESSORY_BAG),
	FISHING_BAG(Items.SKULL, "§aFishing Bag", Skyblock.InventoryType.FISHING_BAG),
	TRADES(Items.EMERALD, "§aTrades", Skyblock.InventoryType.TRADES),
	PERSONAL_BANK(Items.SKULL, "§aPersonal Bank", Skyblock.InventoryType.PERSONAL_BANK),
	CRAFTING_TABLE_RESULT(Item.getItemFromBlock(Blocks.BARRIER), "§cRecipe Required", null),
	GO_BACK(Items.ARROW, "§aGo Back"),
	QUIVER_ARROW(Items.ARROW, "§8Quiver Arrow"),
	CLOSE(Item.getItemFromBlock(Blocks.BARRIER), "§cClose");

	private final Item item;
	private final String itemName;
	private final Skyblock.InventoryType inventoryType;

	MenuItem(Item item, String itemName) {
		this(item, itemName, null);
	}

	MenuItem(Item item, String itemName, Skyblock.InventoryType inventoryType) {
		this.item = item;
		this.itemName = itemName;
		this.inventoryType = inventoryType;
	}

	public Skyblock.InventoryType getInventoryType() {
		return this.inventoryType;
	}

	public Item getItem() {
		return this.item;
	}

	public String getItemName() {
		return this.itemName;
	}

	public String getPrettyName() {
		return WordUtil.capitalizeFully(this.name().replace("_", " "));
	}

	public static MenuItem getMenuItem(Skyblock.InventoryType inventoryType) {
		if (inventoryType.isSkyblockSubMenu() && !inventoryType.hasCommand()) {
			for (MenuItem menuItem : values()) {
				if (menuItem.getInventoryType() == inventoryType)
					return menuItem;
			}
		}

		return null;
	}

}