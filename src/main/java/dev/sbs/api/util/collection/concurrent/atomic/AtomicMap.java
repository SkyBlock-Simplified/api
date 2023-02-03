package dev.sbs.api.util.collection.concurrent.atomic;

import dev.sbs.api.util.collection.concurrent.iterator.ConcurrentIterator;
import dev.sbs.api.util.helper.NumberUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public abstract class AtomicMap<K, V, M extends AbstractMap<K, V>> extends AbstractMap<K, V> implements Iterable<Map.Entry<K, V>>, Map<K, V>, Serializable {

	protected final @NotNull M ref;
	protected final transient @NotNull Object lock = new Object();

	/**
	 * Create a new concurrent map.
	 */
	protected AtomicMap(@NotNull M ref, Map<? extends K, ? extends V> items) {
		if (Objects.nonNull(items)) ref.putAll(items);
		this.ref = ref;
	}

	@Override
	public void clear() {
		synchronized (this.lock) {
			this.ref.clear();
		}
	}

	@Override
	public final boolean containsKey(Object key) {
		synchronized (this.lock) {
			return this.ref.containsKey(key);
		}
	}

	@Override
	public final boolean containsValue(Object value) {
		synchronized (this.lock) {
			return this.ref.containsValue(value);
		}
	}

	@Override
	public @NotNull Set<Entry<K, V>> entrySet() {
		return this.ref.entrySet();
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (obj instanceof AtomicMap<?, ?, ?>) obj = ((AtomicMap<?, ?, ?>) obj).ref;
		return this.ref.equals(obj);
	}

	@Override
	public final V get(Object key) {
		synchronized (this.lock) {
			return this.ref.get(key);
		}
	}

	@Override
	public final V getOrDefault(Object key, V defaultValue) {
		synchronized (this.lock) {
			return this.ref.getOrDefault(key, defaultValue);
		}
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

	@Override
	public final @NotNull Iterator<Entry<K, V>> iterator() {
		return new ConcurrentMapIterator(this.entrySet().toArray(), 0);
	}

	@Override
	public @NotNull Set<K> keySet() {
		return this.ref.keySet();
	}

	public final boolean notEmpty() {
		return !this.isEmpty();
	}

	public final Stream<Entry<K, V>> parallelStream() {
		return this.entrySet().parallelStream();
	}

	@Override
	public V put(K key, V value) {
		synchronized (this.lock) {
			return this.ref.put(key, value);
		}
	}

	public final V put(Map.Entry<K, V> entry) {
		return this.put(entry.getKey(), entry.getValue());
	}

	@Override
	public void putAll(@NotNull Map<? extends K, ? extends V> map) {
		synchronized (this.lock) {
			this.ref.putAll(map);
		}
	}

	@Override
	public V putIfAbsent(K key, V value) {
		synchronized (this.lock) {
			return this.ref.putIfAbsent(key, value);
		}
	}

	@Override
	public V remove(Object key) {
		synchronized (this.lock) {
			return this.ref.remove(key);
		}
	}

	public final V removeOrGet(Object key, V defaultValue) {
		return Optional.ofNullable(this.remove(key)).orElse(defaultValue);
	}

	@Override
	public boolean remove(Object key, Object value) {
		synchronized (this.lock) {
			return this.ref.remove(key, value);
		}
	}

	@Override
	public final int size() {
		return this.ref.size();
	}

	public final Stream<Entry<K, V>> stream() {
		return this.entrySet().stream();
	}

	@Override @NotNull
	public Collection<V> values() {
		synchronized (this.lock) {
			return this.ref.values();
		}
	}

	protected class ConcurrentMapIterator extends ConcurrentIterator<Map.Entry<K, V>> {

		protected ConcurrentMapIterator(Object[] snapshot, int index) {
			super(snapshot, index);
		}

		@Override
		public void remove() {
			AtomicMap.this.remove(this.snapshot[this.cursor]);
			this.snapshot = AtomicMap.this.entrySet().toArray();
			this.cursor = NumberUtil.ensureRange(this.cursor, 0, this.snapshot.length - 1);
		}

	}

}
