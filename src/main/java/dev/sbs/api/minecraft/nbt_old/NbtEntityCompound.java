package dev.sbs.api.minecraft.nbt_old;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * NBT Wrapper for an Entity.
 */
public final class NbtEntityCompound extends WrappedCompound<Entity> {

	NbtEntityCompound(Entity entity, Object handle) {
		super(entity, handle, "ForgeData");
		//this.load();
	}

	/*@Override
	protected void load() {
		if (this.getHandle() != null) {
			NbtCompound compound = SimplifiedAPI.getNbtFactory().fromCompound(this.getHandle());
			this.putAll(compound);
		} else
			this.save();
	}*/

	@Override
	protected void save() {
		this.getWrapped().deserializeNBT((NBTTagCompound)this.getHandle());
	}

}