package gg.sbs.api.nbt;

abstract class WrappedCompound<W> extends NbtCompound {

	private final W wrapped;

	WrappedCompound(W wrapped, Object handle, String storageKey) {
		super(handle, true, storageKey);
		this.wrapped = wrapped;
	}

	protected final W getWrapped() {
		return this.wrapped;
	}

}