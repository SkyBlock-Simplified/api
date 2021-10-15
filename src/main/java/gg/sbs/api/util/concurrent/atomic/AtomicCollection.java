package gg.sbs.api.util.concurrent.atomic;

import gg.sbs.api.reflection.exception.ReflectionException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class AtomicCollection<E, T extends AbstractCollection<E>> extends AbstractCollection<E> implements Collection<E> {

	protected final AtomicReference<T> ref;

	protected AtomicCollection(T type) {
		this.ref = new AtomicReference<>(type);
	}

	@Override
	public boolean add(E element) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newCollection(current);
			boolean result = modified.add(element);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends E> collection) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newCollection(current);
			boolean result = modified.addAll(collection);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public void clear() {
		this.ref.get().clear();
	}

	@Override
	public final boolean contains(Object item) {
		return ref.get().contains(item);
	}

	@Override
	public final boolean containsAll(@NotNull Collection<?> collection) {
		return this.ref.get().containsAll(collection);
	}

	@Override
	public final boolean isEmpty() {
		return this.ref.get().isEmpty();
	}

	@Override @NotNull
	public final Iterator<E> iterator() {
		return this.ref.get().iterator();
	} // Iterator#remove calls AtomicList#remove

	@SuppressWarnings("unchecked")
	private T newCollection(T current) {
		try {
			Collection<E> collection = current.getClass().newInstance();
			collection.addAll(current);
			return (T) collection;
		} catch (Exception ex) {
			throw new ReflectionException("Unable to create new list instance of " + current.getClass().getSimpleName() + "!"); // Cannot use FormatUtil
		}
	}

	@Override
	public final Stream<E> parallelStream() {
		return this.ref.get().parallelStream();
	}

	@Override
	@SuppressWarnings("all")
	public boolean remove(Object element) {
		while (true) {
			T current = this.ref.get();

			if (!current.contains(element))
				return false;

			T modified = this.newCollection(current);
			boolean result = modified.remove(element);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> collection) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newCollection(current);
			boolean result = modified.removeAll(collection);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> collection) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newCollection(current);
			boolean result = modified.retainAll(collection);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public final int size() {
		return this.ref.get().size();
	}

	@Override
	public final Stream<E> stream() {
		return this.ref.get().stream();
	}

	@Override @NotNull
	public final Object[] toArray() {
		return this.ref.get().toArray();
	}

	@Override @NotNull
	@SuppressWarnings("SuspiciousToArrayCall")
	public final <U> U[] toArray(@NotNull U[] array) {
		return this.ref.get().toArray(array);
	}

}