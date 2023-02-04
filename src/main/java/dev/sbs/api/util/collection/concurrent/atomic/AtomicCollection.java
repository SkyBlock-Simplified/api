package dev.sbs.api.util.collection.concurrent.atomic;

import dev.sbs.api.util.collection.concurrent.iterator.ConcurrentIterator;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("all")
public abstract class AtomicCollection<E, T extends Collection<E>> extends AbstractCollection<E> implements Collection<E>, Serializable {

	protected final @NotNull T ref;
	protected final transient @NotNull Object lock = new Object();

	protected AtomicCollection(@NotNull T ref) {
		this.ref = ref;
	}

	@Override
	public boolean add(@NotNull E element) {
		synchronized (this.lock) {
			return this.ref.add(element);
		}
	}

	public final boolean addAll(@NotNull E... collection) {
		return this.addAll(Arrays.asList(collection));
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends E> collection) {
		synchronized (this.lock) {
			return this.ref.addAll(collection);
		}
	}

	@Override
	public void clear() {
		synchronized (this.lock) {
			this.ref.clear();
		}
	}

	@Override
	public boolean contains(Object item) {
		synchronized (this.lock) {
			return this.ref.contains(item);
		}
	}

	public final <S> boolean contains(@NotNull Function<E, S> function, S value) {
		synchronized (this.lock) {
			for (E element : this.ref) {
				if (Objects.equals(function.apply(element), value))
					return true;
			}
		}

		return false;
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> collection) {
		synchronized (this.lock) {
			return this.ref.containsAll(collection);
		}
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (obj instanceof AtomicCollection) obj = ((AtomicCollection<?, ?>) obj).ref;
		return this.ref.equals(obj);
	}

	@Override
	public final int hashCode() {
		synchronized (this.lock) {
			return this.ref.hashCode();
		}
	}

	@Override
	public final boolean isEmpty() {
		synchronized (this.lock) {
			return this.ref.isEmpty();
		}
	}

	@Override @NotNull
	public Iterator<E> iterator() {
		return new ConcurrentCollectionIterator(this.ref.toArray(), 0);
	}

	public final boolean notContains(Object item) {
		return !this.contains(item);
	}

	@Override
	public Stream<E> parallelStream() {
		return this.ref.parallelStream();
	}

	@Override
	@SuppressWarnings("all")
	public boolean remove(Object element) {
		synchronized (this.lock) {
			return this.ref.remove(element);
		}
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> collection) {
		synchronized (this.lock) {
			return this.ref.removeAll(collection);
		}
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> collection) {
		synchronized (this.lock) {
			return this.ref.retainAll(collection);
		}
	}

	@Override
	public final int size() {
		return this.ref.size();
	}

	@Override
	public @NotNull Stream<E> stream() {
		return this.ref.stream();
	}

	@Override @NotNull
	public Object[] toArray() {
		return this.ref.toArray();
	}

	@Override @NotNull
	@SuppressWarnings("SuspiciousToArrayCall")
	public <U> U[] toArray(@NotNull U[] array) {
		return this.ref.toArray(array);
	}

	/**
	 * A concurrent version of {@link CopyOnWriteArrayList.COWIterator}.
	 */
	protected class ConcurrentCollectionIterator extends ConcurrentIterator<E> {

		protected ConcurrentCollectionIterator(Object[] snapshot, int index) {
			super(snapshot, index);
		}

		@Override
		public void remove() {
			if (this.last < 0)
				throw new IllegalStateException();

			try {
				AtomicCollection.this.remove(this.snapshot[this.last]);
				this.snapshot = AtomicCollection.this.toArray();
				this.cursor = this.last;
				this.last = -1;
			} catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}

	}

}
