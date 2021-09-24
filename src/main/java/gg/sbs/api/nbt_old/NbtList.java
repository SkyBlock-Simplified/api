package gg.sbs.api.nbt_old;

import gg.sbs.api.SimplifiedAPI;

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
		return SimplifiedAPI.getNbtFactory().createList(this);
	}

}