package dev.sbs.api.util.collection.concurrent;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.util.collection.concurrent.atomic.AtomicList;
import dev.sbs.api.util.collection.search.SearchQuery;
import dev.sbs.api.util.collection.sort.SortOrder;
import dev.sbs.api.util.helper.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;

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
		super(ListUtil.isEmpty(collection) ? new ArrayList<>() : new ArrayList<>(collection));
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
		return new ConcurrentList<>(this);
	}

	@Override
	@SuppressWarnings("all")
	public ConcurrentList<E> sorted(@NotNull Function<E, ? extends Comparable>... sortFunctions) {
		super.sorted(sortFunctions);
		return this;
	}

	@Override
	@SuppressWarnings("all")
	public ConcurrentList<E> sorted(@NotNull SortOrder sortOrder, Function<E, ? extends Comparable>... functions) {
		super.sorted(sortOrder, functions);
		return this;
	}

	@Override
	public ConcurrentList<E> sorted(Comparator<? super E> comparator) {
		super.sorted(comparator);
		return this;
	}

	@Override
	public final ConcurrentList<E> toList(@NotNull Stream<E> stream) throws DataException {
		return stream.collect(Concurrent.toList());
	}

}
