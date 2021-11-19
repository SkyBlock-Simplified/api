package dev.sbs.api.minecraft.nbt.client;

/*
 * NBT Wrapper for an Entity.
 *
public final class NbtTileEntityCompound extends WrappedCompound<TileEntity> {

	NbtTileEntityCompound(TileEntity tileEntity, Object handle) {
		super(tileEntity, handle, "ForgeData");
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

	/*@Override
	protected void save() {
		this.getWrapped().deserializeNBT((NBTTagCompound)this.getHandle());
	}

}*/