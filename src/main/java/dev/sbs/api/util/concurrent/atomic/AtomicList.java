package dev.sbs.api.util.concurrent.atomic;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.reflection.exception.ReflectionException;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.search.function.SortFunction;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public abstract class AtomicList<E, T extends AbstractList<E>> extends AtomicCollection<E, T> implements List<E> {

	protected AtomicList(T type) {
		super(type);
	}

	public void add(int index, E element) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newList(current);
			modified.add(index, element);

			if (this.ref.compareAndSet(current, modified))
				return;
		}
	}

	@Override
	public boolean add(E element) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newList(current);
			boolean result = modified.add(element);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends E> collection) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newList(current);
			boolean result = modified.addAll(collection);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public boolean addAll(int index, @NotNull Collection<? extends E> collection) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newList(current);
			boolean result = modified.addAll(index, collection);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public final E get(int index) {
		return this.ref.get().get(index);
	}

	@Override
	public final int indexOf(Object item) {
		return this.ref.get().indexOf(item);
	}

	public List<E> inverse() {
		T list = this.newList(this.ref.get());
		Collections.reverse(list);
		return list;
	}

	@Override
	public final int lastIndexOf(Object item) {
		return this.ref.get().lastIndexOf(item);
	}

	@Override @NotNull
	public final ListIterator<E> listIterator() {
		return this.listIterator(0);
	}

	@Override @NotNull
	public ListIterator<E> listIterator(int index) {
		return this.ref.get().listIterator(index);
	}

	@SuppressWarnings("unchecked")
	private T newList(T current) {
		try {
			List<E> list = Reflection.of(current.getClass()).newInstance();
			list.addAll(current);
			return (T) list;
		} catch (Exception ex) {
			throw SimplifiedException.of(ReflectionException.class)
				.withMessage("Unable to create new list instance of " + current.getClass().getSimpleName() + "!") // Cannot use FormatUtil
				.withCause(ex)
				.build();
		}
	}

	@Override
	public E remove(int index) {
		while (true) {
			T current = this.ref.get();

			if (index >= current.size())
				return null;

			T modified = this.newList(current);
			E old = modified.remove(index);

			if (this.ref.compareAndSet(current, modified))
				return old;
		}
	}

	@Override
	@SuppressWarnings("all")
	public boolean remove(Object element) {
		while (true) {
			T current = this.ref.get();

			if (!current.contains(element))
				return false;

			T modified = this.newList(current);
			boolean result = modified.remove(element);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> collection) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newList(current);
			boolean result = modified.removeAll(collection);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> collection) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newList(current);
			boolean result = modified.retainAll(collection);

			if (this.ref.compareAndSet(current, modified))
				return result;
		}
	}

	public void reverse() {
		Collections.reverse(this);
	}

	@Override
	public E set(int index, E element) {
		while (true) {
			T current = this.ref.get();
			T modified = this.newList(current);
			E old = modified.set(index, element);

			if (this.ref.compareAndSet(current, modified))
				return old;
		}
	}

	@SuppressWarnings("unchecked")
	public <C extends Comparable<C>> AtomicList<E, T> sort(@NotNull SortFunction<E, C>... sortFunctions) {
		if (ListUtil.notEmpty(sortFunctions)) {
			this.sort((s1, s2) -> {
				Comparator<E> comparator = Comparator.comparing(sortFunctions[0]);

				for (int i = 1; i < sortFunctions.length; i++)
					comparator = comparator.thenComparing(sortFunctions[i]);

				return comparator.compare(s1, s2);
			});
		}

		return this;
	}

	@Override
	public void sort(Comparator<? super E> comparator) {
		List.super.sort(comparator);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return this.ref.get().subList(fromIndex, toIndex);
	}

}
