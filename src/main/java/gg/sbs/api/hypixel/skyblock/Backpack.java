package gg.sbs.api.hypixel.skyblock;

import net.minecraft.item.ItemStack;
import gg.sbs.api.nbt.NbtCompound;
import gg.sbs.api.util.RegexUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Backpack {

	private static final Pattern BACKPACK_ID_PATTERN = Pattern.compile("([A-Z]+)_BACKPACK");
	private final List<ItemStack> contents;
	private final ItemStack backpack;
	private final Size size;

	private Backpack(ItemStack[] items, ItemStack backpack, Size size) {
		this.contents = Arrays.asList(items);
		this.backpack = backpack;
		this.size = size;
	}

	public List<ItemStack> getContents() {
		return this.contents;
	}

	public ItemStack getBackpack() {
		return this.backpack;
	}

	public String getDisplayName() {
		return RegexUtil.strip(this.backpack.getDisplayName(), RegexUtil.VANILLA_PATTERN);
	}

	public Size getSize() {
		return this.size;
	}

	public static boolean isBackpack(ItemStack itemStack) {
		if (itemStack != null && itemStack.hasTagCompound()) {
			NbtCompound nbtCompound = SimplifiedAPI.getNbtFactory().fromItemStack(itemStack);

			if (nbtCompound.containsKey("ExtraAttributes")) {
				NbtCompound extraAttributes = nbtCompound.get("ExtraAttributes");
				String itemId = extraAttributes.get("id");
				Matcher matcher = BACKPACK_ID_PATTERN.matcher(itemId);
				return matcher.matches() || "NEW_YEAR_CAKE_BAG".equals(itemId);
			}
		}

		return false;
	}

	public static Backpack getFromItem(ItemStack itemStack) {
		if (isBackpack(itemStack)) {
			NbtCompound extraAttributes = SimplifiedAPI.getNbtFactory().fromItemStack(itemStack).get("ExtraAttributes");
			Matcher matcher = BACKPACK_ID_PATTERN.matcher(extraAttributes.get("id"));
			byte[] bytes = null;

			for (String key : extraAttributes.keySet()) {
				if (key.endsWith("backpack_data") || "new_year_cake_bag_data".equals(key)) {
					bytes = extraAttributes.get(key);
					break;
				}
			}

			String backpackType = "NEW_YEAR_CAKE_BAG";
			if (matcher.matches()) backpackType = matcher.group(1);
			Size backpackSize = Size.getSize(backpackType);
			ItemStack[] items = new ItemStack[backpackSize.getTotalSlots()];

			if (bytes != null) {
				NbtCompound nbtBackpack = SimplifiedAPI.getNbtFactory().fromBytes(bytes);
				List<NbtCompound> list = nbtBackpack.get("i");

				if (list.size() != backpackSize.getTotalSlots()) {
					backpackSize = Size.getSize(list.size());
					items = new ItemStack[backpackSize.getTotalSlots()];
				}

				for (int i = 0; i < list.size(); i++) {
					NbtCompound itemNbt = list.get(i);
					items[i] = SimplifiedAPI.getNbtFactory().toItemStack(itemNbt);
				}
			}

			return new Backpack(items, itemStack, backpackSize);
		}

		return null;
	}

	public enum Size {

		SMALL(9),
		MEDIUM(18),
		LARGE(27),
		GREATER(36),
		JUMBO(54),
		NEW_YEAR_CAKE_BAG(54),
		UNKNOWN(-1);

		private final int totalSlots;

		Size(int totalSlots) {
			this.totalSlots = totalSlots;
		}

		public int getTotalSlots() {
			return this.totalSlots;
		}

		static Size getSize(String sizeName) {
			for (Size size : values()) {
				if (size.name().equals(sizeName))
					return size;
			}

			return Size.UNKNOWN;
		}

		static Size getSize(int totalSlots) {
			for (Size size : values()) {
				if (size.getTotalSlots() == totalSlots)
					return size;
			}

			return Size.UNKNOWN;
		}

	}

}