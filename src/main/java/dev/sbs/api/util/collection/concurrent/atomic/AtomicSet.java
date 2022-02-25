package dev.sbs.api.util.collection.concurrent.atomic;

import dev.sbs.api.reflection.exception.ReflectionException;
import dev.sbs.api.util.SimplifiedException;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Set;

public abstract class AtomicSet<E, T extends AbstractSet<E>> extends AtomicCollection<E, T> implements Set<E> {

	protected AtomicSet(T type) {
		super(type);
	}

	@Override
	public boolean add(E element) {
		while (true) {
			T current = this.ref.get();

			if (current.contains(element))
				return false;

			T modified = this.newSet(current);
			boolean result = modified.add(element);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends E> collection) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newSet(current);
			boolean result = modified.addAll(collection);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@SuppressWarnings("unchecked")
	private T newSet(T current) {
		try {
			Set<E> set = current.getClass().newInstance();
			set.addAll(current);
			return (T) set;
		} catch (Exception ex) {
			throw SimplifiedException.of(ReflectionException.class)
				.withMessage("Unable to create new list instance of " + current.getClass().getSimpleName() + "!") // Cannot use FormatUtil
				.withCause(ex)
				.build();
		}
	}

	@Override
	@SuppressWarnings("all")
	public boolean remove(Object item) {
		while (true) {
			T current = this.ref.get();

			if (!current.contains(item))
				return false;

			T modified = this.newSet(current);
			boolean result = modified.remove(item);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> collection) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newSet(current);
			boolean result = modified.removeAll(collection);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> collection) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newSet(current);
			boolean result = modified.retainAll(collection);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

}
