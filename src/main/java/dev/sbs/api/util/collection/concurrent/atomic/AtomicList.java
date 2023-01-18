package dev.sbs.api.util.collection.concurrent.atomic;

import dev.sbs.api.util.collection.concurrent.iterator.ConcurrentIterator;
import dev.sbs.api.util.collection.sort.SortOrder;
import dev.sbs.api.util.helper.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public abstract class AtomicList<E, T extends List<E>> extends AtomicCollection<E, T> implements List<E> {

	protected AtomicList(T type) {
		super(type);
	}

	public void add(int index, @NotNull E element) {
		synchronized (this.lock) {
			this.ref.add(index, element);
		}
	}

	@Override
	public boolean addAll(int index, @NotNull Collection<? extends E> collection) {
		synchronized (this.lock) {
			return this.ref.addAll(index, collection);
		}
	}

	@Override
	public final E get(int index) {
		synchronized (this.lock) {
			return this.ref.get(index);
		}
	}

	public final E getFirst() {
		return this.get(0);
	}

	public final E getLast() {
		return this.get(this.size() - 1);
	}

	public final E getOrDefault(int index, E defaultValue) {
		return index < this.size() ? this.get(index) : defaultValue;
	}

	@Override
	public final int indexOf(Object item) {
		synchronized (this.lock) {
			return this.ref.indexOf(item);
		}
	}

	public List<E> inverse() {
		Collections.reverse(this.ref);
		return this;
	}

	@Override
	public final int lastIndexOf(Object item) {
		synchronized (this.lock) {
			return this.ref.lastIndexOf(item);
		}
	}

	@Override @NotNull
	public final ListIterator<E> listIterator() {
		return this.listIterator(0);
	}

	@Override @NotNull
	public ListIterator<E> listIterator(int index) {
		return new ConcurrentListIterator(this.ref.toArray(), index);
	}

	@Override
	public E remove(int index) {
		synchronized (this.lock) {
			return this.ref.remove(index);
		}
	}

	public final E removeFirst() {
		return this.remove(0);
	}

	public final E removeLast() {
		return this.remove(this.size() - 1);
	}

	@Override
	public E set(int index, E element) {
		synchronized (this.lock) {
			return this.ref.set(index, element);
		}
	}

	@SuppressWarnings("all")
	public AtomicList<E, T> sorted(Function<E, ? extends Comparable>... functions) {
		return this.sorted(SortOrder.ASCENDING, functions);
	}

	@SuppressWarnings("all")
	public AtomicList<E, T> sorted(@NotNull SortOrder sortOrder, Function<E, ? extends Comparable>... functions) {
		if (ListUtil.notEmpty(functions)) {
			this.sort((s1, s2) -> {
				Comparator<E> comparator = Comparator.comparing(functions[0]);

				for (int i = 1; i < functions.length; i++)
					comparator = comparator.thenComparing(functions[i]);

				return sortOrder == SortOrder.ASCENDING ? comparator.compare(s1, s2) : comparator.compare(s2, s1);
			});
		}

		return this;
	}

	public AtomicList<E, T> sorted(Comparator<? super E> comparator) {
		this.sort(comparator);
		return this;
	}

	@Override
	public void sort(Comparator<? super E> comparator) {
		List.super.sort(comparator);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		synchronized (this.lock) {
			return this.ref.subList(fromIndex, toIndex);
		}
	}

	/**
	 * A concurrent list version of {@link ConcurrentIterator}.
	 */
	private final class ConcurrentListIterator extends ConcurrentCollectionIterator implements ListIterator<E> {

		private ConcurrentListIterator(Object[] snapshot, int index) {
			super(snapshot, index);
		}

		@Override
		public boolean hasPrevious() {
			return this.cursor > 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E previous() {
			if (this.hasPrevious())
				return (E) this.snapshot[this.last = --this.cursor];
			else
				throw new NoSuchElementException();
		}

		@Override
		public int nextIndex() {
			return this.cursor;
		}

		@Override
		public int previousIndex() {
			return this.cursor - 1;
		}

		@Override
		public void set(E element) {
			if (this.last < 0)
				throw new IllegalStateException();

			try {
				AtomicList.this.set(this.last, element);
				this.snapshot = AtomicList.this.toArray();
			} catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}

		@Override
		public void add(E element) {
			this.snapshot = AtomicList.this.toArray();

			try {
				AtomicList.this.add(this.cursor++, element);
				this.last = -1;
			} catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}

	}

}
