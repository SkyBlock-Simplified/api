package gg.sbs.api.util.concurrent.atomic;

import gg.sbs.api.reflection.exception.ReflectionException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
			List<E> list = current.getClass().newInstance();
			list.addAll(current);
			return (T) list;
		} catch (Exception ex) {
			throw new ReflectionException("Unable to create new list instance of " + current.getClass().getSimpleName() + "!"); // Cannot use FormatUtil
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

	@Override
	public void sort(Comparator<? super E> comparator) {
		List.super.sort(comparator);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return this.ref.get().subList(fromIndex, toIndex);
	}

}