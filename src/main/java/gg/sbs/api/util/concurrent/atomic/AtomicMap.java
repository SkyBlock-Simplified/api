package gg.sbs.api.util.concurrent.atomic;

import gg.sbs.api.reflection.exception.ReflectionException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public abstract class AtomicMap<K, V, M extends AbstractMap<K, V>> extends AbstractMap<K, V> implements Iterable<Map.Entry<K, V>>, Map<K, V> {

	protected final AtomicReference<M> ref;

	/**
	 * Create a new concurrent map.
	 */
	protected AtomicMap(M type) {
		this.ref = new AtomicReference<>(type);
	}

	@Override
	public void clear() {
		this.ref.get().clear();
	}

	@Override
	public final boolean containsKey(Object key) {
		return this.ref.get().containsKey(key);
	}

	@Override
	public final boolean containsValue(Object value) {
		return this.ref.get().containsValue(value);
	}

	@Override @NotNull
	public Set<Entry<K, V>> entrySet() {
		return this.ref.get().entrySet();
	}

	@Override
	public final V get(Object key) {
		return this.ref.get().get(key);
	}

	@Override
	public final V getOrDefault(Object key, V defaultValue) {
		return this.ref.get().getOrDefault(key, defaultValue);
	}

	@Override
	public final boolean isEmpty() {
		return this.ref.get().isEmpty();
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return this.entrySet().iterator();
	}

	@Override @NotNull
	public final Set<K> keySet() {
		return this.ref.get().keySet();
	}

	@SuppressWarnings("unchecked")
	private M newMap(M current) {
		try {
			Map<K, V> map = current.getClass().newInstance();
			map.putAll(current);
			return (M) map;
		} catch (Exception ex) {
			throw new ReflectionException("Unable to create new map instance of " + current.getClass().getSimpleName() + "!"); // Cannot use FormatUtil
		}
	}

	public final Stream<Entry<K, V>> parallelStream() {
		return this.entrySet().parallelStream();
	}

	@Override
	public V put(K key, V value) {
		while (true) {
			M current = this.ref.get();
			M modified = this.newMap(current);
			V old = modified.put(key, value);

			if (this.ref.compareAndSet(current, modified))
				return old;
		}
	}

	@Override
	public void putAll(@NotNull Map<? extends K, ? extends V> map) {
		while (true) {
			M current = this.ref.get();
			M modified = this.newMap(current);
			modified.putAll(map);

			if (this.ref.compareAndSet(current, modified))
				return;
		}
	}

	@Override
	public V putIfAbsent(K key, V value) {
		while (true) {
			M current = this.ref.get();
			M modified = this.newMap(current);

			if (!modified.containsKey(key) || modified.get(key) == null) {
				V old = modified.put(key, value);

				if (this.ref.compareAndSet(current, modified))
					return old;
			} else
				return null;
		}
	}

	@Override
	@SuppressWarnings("all")
	public V remove(Object key) {
		while (true) {
			M current = this.ref.get();

			if (!current.containsKey(key))
				return null;

			M modified = this.newMap(current);
			V old = modified.remove(key);

			if (this.ref.compareAndSet(current, modified))
				return old;
		}
	}

	@Override
	@SuppressWarnings("all")
	public boolean remove(Object key, Object value) {
		while (true) {
			M current = this.ref.get();

			if (!current.containsKey(key))
				return false;

			M modified = this.newMap(current);
			V currentValue = modified.get(key);

			if (Objects.equals(currentValue, value)) {
				modified.remove(key);

				if (this.ref.compareAndSet(current, modified))
					return true;
			} else
				return false;
		}
	}

	@Override
	public final int size() {
		return this.ref.get().size();
	}

	public final Stream<Entry<K, V>> stream() {
		return this.entrySet().stream();
	}

	@Override @NotNull
	public Collection<V> values() {
		return this.ref.get().values();
	}

}