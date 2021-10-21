package dev.sbs.api.util.concurrent.unmodifiable;

import dev.sbs.api.util.concurrent.ConcurrentList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An unmodifiable concurrent list that allows for simultaneous fast reading and iteration utilizing {@link AtomicReference}.
 * <p>
 * The AtomicReference changes the methods that modify the list by replacing the
 * entire list on each modification. This allows for maintaining the original
 * speed of {@link ArrayList#contains(Object)} and makes it cross-thread-safe.
 *
 * @param <E> type of elements
 */
public class ConcurrentUnmodifiableList<E> extends ConcurrentList<E> {

	/**
	 * Create a new unmodifiable concurrent list.
	 */
	public ConcurrentUnmodifiableList() {
		super(new ArrayList<>());
	}

	/**
	 * Create a new unmodifiable concurrent list and fill it with the given array.
	 */
	@SafeVarargs
	public ConcurrentUnmodifiableList(E... array) {
		this(Arrays.asList(array));
	}

	/**
	 * Create a new unmodifiable concurrent list and fill it with the given collection.
	 */
	public ConcurrentUnmodifiableList(Collection<? extends E> collection) {
		super(new ArrayList<>(collection));
	}

	@Override
	public final void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean add(E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean addAll(@NotNull Collection<? extends E> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean addAll(int index, @NotNull Collection<? extends E> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void clear() {
		throw new UnsupportedOperationException();
	}

	@NotNull
	@Override
	public final ListIterator<E> listIterator(int index) {
		return new ListIterator<E>() {

			private final ListIterator<? extends E> atomicIterator = ConcurrentUnmodifiableList.super.ref.get().listIterator(index);

			@Override
			public boolean hasNext() {
				return atomicIterator.hasNext();
			}

			@Override
			public E next() {
				return atomicIterator.next();
			}

			@Override
			public boolean hasPrevious() {
				return atomicIterator.hasPrevious();
			}

			@Override
			public E previous() {
				return atomicIterator.previous();
			}

			@Override
			public int nextIndex() {
				return atomicIterator.nextIndex();
			}

			@Override
			public int previousIndex() {
				return atomicIterator.previousIndex();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void set(E e) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void add(E e) {
				throw new UnsupportedOperationException();
			}

		};
	}

	@Override
	public final E remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean remove(Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean removeAll(@NotNull Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean retainAll(@NotNull Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void sort(Comparator<? super E> comparator) {
		throw new UnsupportedOperationException();
	}

	@Override @Nonnull
	public ConcurrentList<E> subList(int fromIndex, int toIndex) {
		return new ConcurrentUnmodifiableList<>(super.subList(fromIndex, toIndex));
	}

}