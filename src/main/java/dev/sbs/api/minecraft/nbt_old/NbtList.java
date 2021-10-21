package dev.sbs.api.minecraft.nbt_old;

import dev.sbs.api.SimplifiedApi;

/**
 * Represents a root NBT list.
 * See also:
 * <ul>
 *   <li>{@link NbtFactory_old#createList}</li>
 *   <li>{@link NbtFactory_old#fromList}</li>
 * </ul>
 */
public final class NbtList<E> extends WrappedList<E> {

	NbtList(Object handle) {
		super(handle, NbtFactory_old.getDataField(NbtType.TAG_LIST, handle));
	}

	@Override
	public NbtList<E> clone() {
		return SimplifiedApi.getNbtFactory().createList(this);
	}

}