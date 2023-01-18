package dev.sbs.api.util.collection.concurrent.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class ConcurrentIterator<E> implements Iterator<E> {

		/** Snapshot of the array */
		protected Object[] snapshot;
		/** Index of element to be returned by subsequent call to next.  */
		protected int cursor;

		protected ConcurrentIterator(Object[] snapshot, int index) {
			this.cursor = index;
			this.snapshot = snapshot;
		}

		@Override
		public boolean hasNext() {
			return this.cursor < this.snapshot.length;
		}

		@Override
		public E next() {
			if (this.hasNext())
				return (E) this.snapshot[this.cursor++];
			else
				throw new NoSuchElementException();
		}

		public abstract void remove();

		@Override
		public void forEachRemaining(Consumer<? super E> action) {
			Objects.requireNonNull(action);
			final int size = this.snapshot.length;
			int i = this.cursor;
			this.cursor = size;

			for (; i < size; i++)
				action.accept((E) this.snapshot[i]);
		}

	}