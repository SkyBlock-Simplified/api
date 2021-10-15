package gg.sbs.api.util.concurrent.unmodifiable;

import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.atomic.AtomicMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A concurrent map that allows for simultaneous fast reading, iteration utilizing {@link AtomicReference}.
 * <p>
 * The AtomicReference changes the methods that modify the map by replacing the
 * entire map each modification. This allows for maintaining the original speed
 * of {@link HashMap#containsKey(Object)} and {@link HashMap#containsValue(Object)} and makes it cross-thread-safe.
 *
 * @param <K> type of keys
 * @param <V> type of values
 */
public class ConcurrentUnmodifiableMap<K, V> extends AtomicMap<K, V, HashMap<K, V>> {

    /**
     * Create a new unmodifiable concurrent map.
     */
    public ConcurrentUnmodifiableMap() {
        super(new HashMap<>());
    }

    /**
     * Create a new unmodifiable concurrent map and fill it with the given map.
     */
    public ConcurrentUnmodifiableMap(Map<? extends K, ? extends V> map) {
        super(new HashMap<>(map));
    }

    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override @NotNull
    public final Set<Entry<K, V>> entrySet() {
        return Concurrent.newUnmodifiableSet(this.ref.get().entrySet());
    }

    @Override
    public final V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void putAll(@NotNull Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final V putIfAbsent(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override @NotNull
    public final Collection<V> values() {
        return Concurrent.newUnmodifiableSet(this.ref.get().values());
    }

}