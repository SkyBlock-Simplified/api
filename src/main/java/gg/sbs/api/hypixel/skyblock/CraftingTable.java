package gg.sbs.api.hypixel.skyblock;

import java.util.Arrays;
import java.util.List;

public final class CraftingTable {

	public enum Slot {

		TOP_LEFT(10),
		TOP_CENTER(11),
		TOP_RIGHT(12),
		MIDDLE_LEFT(19),
		MIDDLE_CENTER(20),
		MIDDLE_RIGHT(21),
		BOTTOM_LEFT(28),
		BOTTOM_CENTER(29),
		BOTTOM_RIGHT(30),
		RESULT(23),
		QUICK_CRAFT_TOP(16),
		QUICK_CRAFT_CENTER(25),
		QUICK_CRAFT_BOTTOM(34);

		private final int slotId;

		Slot(int slotId) {
			this.slotId = slotId;
		}

		public int getSlotId() {
			return this.slotId;
		}

		@Override
		public String toString() {
			return String.valueOf(this.getSlotId());
		}

	}

	public enum Recipe {

		ALL(Slot.TOP_LEFT, Slot.TOP_CENTER, Slot.TOP_RIGHT, Slot.MIDDLE_LEFT, Slot.MIDDLE_RIGHT, Slot.BOTTOM_LEFT, Slot.BOTTOM_CENTER, Slot.BOTTOM_RIGHT, Slot.MIDDLE_CENTER),
		RING(Slot.TOP_LEFT, Slot.TOP_CENTER, Slot.TOP_RIGHT, Slot.MIDDLE_LEFT, Slot.MIDDLE_RIGHT, Slot.BOTTOM_LEFT, Slot.BOTTOM_RIGHT, Slot.BOTTOM_CENTER),
		STAR(Slot.TOP_CENTER, Slot.MIDDLE_LEFT, Slot.BOTTOM_CENTER, Slot.MIDDLE_RIGHT, Slot.MIDDLE_CENTER),
		SINGLE_ROW(Slot.TOP_LEFT, Slot.TOP_CENTER, Slot.TOP_RIGHT),
		DOUBLE_ROW(Slot.TOP_LEFT, Slot.TOP_CENTER, Slot.TOP_RIGHT, Slot.MIDDLE_LEFT, Slot.MIDDLE_CENTER, Slot.MIDDLE_RIGHT),
		TOP_ROW(Slot.TOP_LEFT, Slot.TOP_CENTER, Slot.TOP_RIGHT),
		BOX(Slot.TOP_LEFT, Slot.TOP_CENTER, Slot.MIDDLE_LEFT, Slot.MIDDLE_CENTER),
		MIDDLE2(Slot.MIDDLE_LEFT, Slot.MIDDLE_CENTER),
		CENTER2(Slot.MIDDLE_CENTER, Slot.BOTTOM_CENTER),
		CENTER3(Slot.TOP_CENTER, Slot.MIDDLE_CENTER, Slot.BOTTOM_CENTER),
		TRIANGLE(Slot.MIDDLE_LEFT, Slot.MIDDLE_CENTER, Slot.BOTTOM_LEFT),
		BUCKET(Slot.MIDDLE_LEFT, Slot.BOTTOM_CENTER, Slot.MIDDLE_RIGHT),
		HOPPER(Slot.TOP_LEFT, Slot.MIDDLE_LEFT, Slot.BOTTOM_CENTER, Slot.MIDDLE_RIGHT, Slot.TOP_RIGHT, Slot.MIDDLE_CENTER),
		DIAGONAL(Slot.BOTTOM_LEFT, Slot.MIDDLE_CENTER, Slot.TOP_RIGHT),
		ENCHANT(Slot.MIDDLE_CENTER, Slot.MIDDLE_RIGHT, Slot.BOTTOM_CENTER, Slot.BOTTOM_RIGHT),
		ENCHANT_FISH(Slot.TOP_LEFT, Slot.MIDDLE_CENTER, Slot.MIDDLE_RIGHT, Slot.BOTTOM_CENTER, Slot.BOTTOM_RIGHT),
		SINGLE(Slot.TOP_LEFT),
		NONE;

		private final List<Slot> craftingTableSlots;

		Recipe(Slot... craftingTableSlots) {
			this.craftingTableSlots = Arrays.asList(craftingTableSlots);
		}

		public List<Slot> getCraftingTableSlots() {
			return this.craftingTableSlots;
		}

		public int indexOf(Slot slot) {
			return this.craftingTableSlots.indexOf(slot);
		}

		public int size() {
			return this.craftingTableSlots.size();
		}

	}

}