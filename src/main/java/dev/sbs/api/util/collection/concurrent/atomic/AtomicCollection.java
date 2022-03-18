package dev.sbs.api.util.collection.concurrent.atomic;

import dev.sbs.api.reflection.exception.ReflectionException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("all")
public abstract class AtomicCollection<E, T extends AbstractCollection<E>> extends AbstractCollection<E> implements Collection<E>, Serializable {

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

	public final boolean addAll(@NotNull E... collection) {
		return this.addAll(Arrays.asList(collection));
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
	public boolean contains(Object item) {
		return this.ref.get().contains(item);
	}

	public final <S> boolean contains(Function<E, S> function, S value) {
		T current = this.ref.get();

		for (E element : current) {
			if (Objects.equals(function.apply(element), value))
				return true;
		}

		return false;
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> collection) {
		return this.ref.get().containsAll(collection);
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (obj instanceof AtomicCollection) obj = ((AtomicCollection<?, ?>) obj).ref.get();
		return this.ref.get().equals(obj);
	}

	@Override
	public final int hashCode() {
		return new HashCodeBuilder()
			.append(this.ref.get())
			.build();
	}

	@Override
	public final boolean isEmpty() {
		return this.ref.get().isEmpty();
	}

	@Override @NotNull
	public Iterator<E> iterator() {
		return this.ref.get().iterator();
	} // Iterator#remove calls AtomicList#remove

	@SuppressWarnings("unchecked")
	private T newCollection(T current) {
		try {
			Collection<E> collection = current.getClass().newInstance();
			collection.addAll(current);
			return (T) collection;
		} catch (Exception ex) {
			throw SimplifiedException.of(ReflectionException.class)
				.withMessage("Unable to create new list instance of " + current.getClass().getSimpleName() + "!") // Cannot use FormatUtil
				.withCause(ex)
				.build();
		}
	}

	public final boolean notContains(Object item) {
		return !this.contains(item);
	}

	@Override
	public Stream<E> parallelStream() {
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
	public Stream<E> stream() {
		return this.ref.get().stream();
	}

	@Override @NotNull
	public Object[] toArray() {
		return this.ref.get().toArray();
	}

	@Override @NotNull
	@SuppressWarnings("SuspiciousToArrayCall")
	public <U> U[] toArray(@NotNull U[] array) {
		return this.ref.get().toArray(array);
	}

}
