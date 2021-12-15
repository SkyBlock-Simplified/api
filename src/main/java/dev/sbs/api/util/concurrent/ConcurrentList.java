package dev.sbs.api.util.concurrent;

import dev.sbs.api.util.concurrent.atomic.AtomicList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A concurrent list that allows for simultaneous fast reading, iteration and
 * modification utilizing {@link AtomicReference}.
 * <p>
 * The AtomicReference changes the methods that modify the list by replacing the
 * entire list on each modification. This allows for maintaining the original
 * speed of {@link ArrayList#contains(Object)} and makes it cross-thread-safe.
 *
 * @param <E> type of elements
 */
public class ConcurrentList<E> extends AtomicList<E, ArrayList<E>> {

	/**
	 * Create a new concurrent list.
	 */
	public ConcurrentList() {
		super(new ArrayList<>());
	}

	/**
	 * Create a new concurrent list and fill it with the given array.
	 */
	@SafeVarargs
	public ConcurrentList(E... array) {
		this(Arrays.asList(array));
	}

	/**
	 * Create a new concurrent list and fill it with the given collection.
	 */
	public ConcurrentList(Collection<? extends E> collection) {
		super(new ArrayList<>(collection));
	}

	@Override
	public List<E> inverse() {
		return Concurrent.newList(super.inverse());
	}

	@Override @Nonnull
	public ConcurrentList<E> subList(int fromIndex, int toIndex) {
		return Concurrent.newList(super.subList(fromIndex, toIndex));
	}

}
