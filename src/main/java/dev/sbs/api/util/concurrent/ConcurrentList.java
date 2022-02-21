package dev.sbs.api.util.concurrent;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.util.concurrent.atomic.AtomicList;
import dev.sbs.api.util.search.SearchQuery;
import dev.sbs.api.util.search.function.SortFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
public class ConcurrentList<E> extends AtomicList<E, ArrayList<E>> implements SearchQuery<E, ConcurrentList<E>> {

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
	public ConcurrentList<E> inverse() {
		return Concurrent.newList(super.inverse());
	}

	@Override @NotNull
	public ConcurrentList<E> subList(int fromIndex, int toIndex) {
		return Concurrent.newList(super.subList(fromIndex, toIndex));
	}

	@Override
	public ConcurrentList<E> findAll() throws DataException {
		return this;
	}

	@Override
	public final <C extends Comparable<C>> ConcurrentList<E> sort(SortFunction<E, C> sortFunction) {
		super.sort(sortFunction);
		return this;
	}

}
