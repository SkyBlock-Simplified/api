package gg.sbs.api.nbt_old;

import gg.sbs.api.SimplifiedAPI;

/**
 * Represents a root NBT list.
 * See also:
 * <ul>
 *   <li>{@link NbtFactory#createList}</li>
 *   <li>{@link NbtFactory#fromList}</li>
 * </ul>
 */
public final class NbtList<E> extends WrappedList<E> {

	NbtList(Object handle) {
		super(handle, NbtFactory.getDataField(NbtType.TAG_LIST, handle));
	}

	@Override
	public NbtList<E> clone() {
		return SimplifiedAPI.getNbtFactory().createList(this);
	}

}