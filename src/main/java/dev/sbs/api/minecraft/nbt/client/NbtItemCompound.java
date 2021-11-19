package dev.sbs.api.minecraft.nbt.client;

/*
 * NBT Wrapper for an ItemStack.
 *
public final class NbtItemCompound extends WrappedCompound<ItemStack> {

	NbtItemCompound(ItemStack item, Object handle) {
		super(item, handle, "tag");
	}

	@Override
	protected void save() {
		if (!Items.AIR.equals(this.getWrapped().getItem())) {
			this.getWrapped().deserializeNBT((NBTTagCompound)this.getHandle());

			// These aren't written by deserializeNBT, and Mojang stores the incorrect types...
			byte count = this.get("Count");
			short damage = this.get("Damage");
			this.getWrapped().setCount(count);
			this.getWrapped().getDisplayName();
			this.getWrapped().setItemDamage(damage);
		}
	}

}*/