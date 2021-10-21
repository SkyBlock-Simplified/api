package dev.sbs.api.util.concurrent;

import dev.sbs.api.util.concurrent.atomic.AtomicCollection;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A concurrent collection that allows for simultaneous fast reading, iteration and
 * modification utilizing {@link AtomicReference}.
 * <p>
 * The AtomicReference changes the methods that modify the set by replacing the
 * entire set each modification. This allows for maintaining the original speed
 * of {@link HashSet#contains(Object)} and makes it cross-thread-safe.
 *
 * @param <E> type of elements
 */
public class ConcurrentCollection<E> extends AtomicCollection<E, AbstractCollection<E>> {

	/**
	 * Create a new concurrent set.
	 */
	public ConcurrentCollection() {
		super(new ArrayList<>());
	}

	/**
	 * Create a new concurrent set and fill it with the given array.
	 */
	@SafeVarargs
	public ConcurrentCollection(E... array) {
		this(Arrays.asList(array));
	}

	/**
	 * Create a new concurrent set and fill it with the given collection.
	 */
	public ConcurrentCollection(Collection<? extends E> collection) {
		super(new ArrayList<>(collection));
	}

}
