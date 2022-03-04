package dev.sbs.api.util.collection.concurrent.linked;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.atomic.AtomicList;
import dev.sbs.api.util.collection.search.SearchQuery;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A concurrent list that allows for simultaneously fast reading, iteration and
 * modification utilizing {@link AtomicReference}.
 * <p>
 * The AtomicReference changes the methods that modify the list by replacing the
 * entire list on each modification. This allows for maintaining the original
 * speed of {@link ArrayList#contains(Object)} and makes it cross-thread-safe.
 *
 * @param <E> type of elements
 */
public class ConcurrentLinkedList<E> extends AtomicList<E, LinkedList<E>> implements SearchQuery<E, ConcurrentLinkedList<E>> {

	/**
	 * Create a new concurrent list.
	 */
	public ConcurrentLinkedList() {
		super(new LinkedList<>());
	}

	/**
	 * Create a new concurrent list and fill it with the given array.
	 */
	@SafeVarargs
	public ConcurrentLinkedList(E... array) {
		this(Arrays.asList(array));
	}

	/**
	 * Create a new concurrent list and fill it with the given collection.
	 */
	public ConcurrentLinkedList(Collection<? extends E> collection) {
		super(new LinkedList<>(collection));
	}

	@Override
	public ConcurrentLinkedList<E> findAll() throws DataException {
		return new ConcurrentLinkedList<>(this);
	}

	@Override
	@SuppressWarnings("all")
	public ConcurrentLinkedList<E> sort(@NotNull Function<E, ? extends Comparable>... sortFunctions) {
		super.sort(sortFunctions);
		return this;
	}

	@Override
	public final ConcurrentLinkedList<E> toList(@NotNull Stream<E> stream) throws DataException {
		return stream.collect(Concurrent.toLinkedList());
	}

}
