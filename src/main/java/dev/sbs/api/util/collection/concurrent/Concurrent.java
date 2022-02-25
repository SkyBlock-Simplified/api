package dev.sbs.api.util.collection.concurrent;

import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedList;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedSet;
import dev.sbs.api.util.collection.concurrent.unmodifiable.ConcurrentUnmodifiableCollection;
import dev.sbs.api.util.collection.concurrent.unmodifiable.ConcurrentUnmodifiableList;
import dev.sbs.api.util.collection.concurrent.unmodifiable.ConcurrentUnmodifiableMap;
import dev.sbs.api.util.collection.concurrent.unmodifiable.ConcurrentUnmodifiableSet;
import dev.sbs.api.util.helper.StreamUtil;

import java.util.Collection;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Helper class to build a new concurrent collection instance.
 */
public final class Concurrent {

	public static <E> ConcurrentCollection<E> newCollection() {
		return new ConcurrentCollection<>();
	}

	@SafeVarargs
	public static <E> ConcurrentCollection<E> newCollection(E... array) {
		return new ConcurrentCollection<>(array);
	}

	public static <E> ConcurrentCollection<E> newCollection(Collection<? extends E> collection) {
		return new ConcurrentCollection<>(collection);
	}

	public static <E> ConcurrentDeque<E> newDeque() {
		return new ConcurrentDeque<>();
	}

	@SafeVarargs
	public static <E> ConcurrentDeque<E> newDeque(E... array) {
		return new ConcurrentDeque<>(array);
	}

	public static <E> ConcurrentDeque<E> newDeque(Collection<? extends E> collection) {
		return new ConcurrentDeque<>(collection);
	}

	public static <E> ConcurrentList<E> newList() {
		return new ConcurrentList<>();
	}

	@SafeVarargs
	public static <E> ConcurrentList<E> newList(E... array) {
		return new ConcurrentList<>(array);
	}

	public static <E> ConcurrentList<E> newList(Collection<? extends E> collection) {
		return new ConcurrentList<>(collection);
	}

	@SafeVarargs
	public static <K, V> ConcurrentMap<K, V> newMap(Map.Entry<K, V>... entries) {
		return new ConcurrentMap<>(entries);
	}

	public static <K, V> ConcurrentMap<K, V> newMap(Map<? extends K, ? extends V> map) {
		return new ConcurrentMap<>(map);
	}

	public static <E> ConcurrentQueue<E> newQueue() {
		return new ConcurrentQueue<>();
	}

	@SafeVarargs
	public static <E> ConcurrentQueue<E> newQueue(E... array) {
		return new ConcurrentQueue<>(array);
	}

	public static <E> ConcurrentQueue<E> newQueue(Collection<? extends E> collection) {
		return new ConcurrentQueue<>(collection);
	}

	public static <E> ConcurrentSet<E> newSet() {
		return new ConcurrentSet<>();
	}

	@SafeVarargs
	public static <E> ConcurrentSet<E> newSet(E... array) {
		return new ConcurrentSet<>(array);
	}

	public static <E> ConcurrentSet<E> newSet(Collection<? extends E> collection) {
		return new ConcurrentSet<>(collection);
	}

	public static <E> ConcurrentLinkedList<E> newLinkedList() {
		return new ConcurrentLinkedList<>();
	}

	@SafeVarargs
	public static <E> ConcurrentLinkedList<E> newLinkedList(E... array) {
		return new ConcurrentLinkedList<>(array);
	}

	public static <E> ConcurrentLinkedList<E> newLinkedList(Collection<? extends E> collection) {
		return new ConcurrentLinkedList<>(collection);
	}

	public static <K, V> ConcurrentLinkedMap<K, V> newLinkedMap() {
		return newLinkedMap(-1);
	}

	public static <K, V> ConcurrentLinkedMap<K, V> newLinkedMap(int maxSize) {
		return new ConcurrentLinkedMap<>(maxSize);
	}

	public static <K, V> ConcurrentLinkedMap<K, V> newLinkedMap(Map<? extends K, ? extends V> map) {
		return new ConcurrentLinkedMap<>(map);
	}

	public static <K, V> ConcurrentLinkedMap<K, V> newLinkedMap(Map<? extends K, ? extends V> map, int maxSize) {
		return new ConcurrentLinkedMap<>(map, maxSize);
	}

	public static <E> ConcurrentLinkedSet<E> newLinkedSet() {
		return new ConcurrentLinkedSet<>();
	}

	@SafeVarargs
	public static <E> ConcurrentLinkedSet<E> newLinkedSet(E... array) {
		return new ConcurrentLinkedSet<>(array);
	}

	public static <E> ConcurrentLinkedSet<E> newLinkedSet(Collection<? extends E> collection) {
		return new ConcurrentLinkedSet<>(collection);
	}

	public static <E> ConcurrentUnmodifiableCollection<E> newUnmodifiableCollection() {
		return new ConcurrentUnmodifiableCollection<>();
	}

	@SafeVarargs
	public static <E> ConcurrentUnmodifiableCollection<E> newUnmodifiableCollection(E... array) {
		return new ConcurrentUnmodifiableCollection<>(array);
	}

	public static <E> ConcurrentUnmodifiableCollection<E> newUnmodifiableCollection(Collection<? extends E> collection) {
		return new ConcurrentUnmodifiableCollection<>(collection);
	}

	public static <E> ConcurrentUnmodifiableList<E> newUnmodifiableList() {
		return new ConcurrentUnmodifiableList<>();
	}

	@SafeVarargs
	public static <E> ConcurrentUnmodifiableList<E> newUnmodifiableList(E... array) {
		return new ConcurrentUnmodifiableList<>(array);
	}

	public static <E> ConcurrentUnmodifiableList<E> newUnmodifiableList(Collection<? extends E> collection) {
		return new ConcurrentUnmodifiableList<>(collection);
	}

	@SafeVarargs
	public static <K, V> ConcurrentUnmodifiableMap<K, V> newUnmodifiableMap(Map.Entry<K, V>... entries) {
		return new ConcurrentUnmodifiableMap<>(entries);
	}

	public static <K, V> ConcurrentUnmodifiableMap<K, V> newUnmodifiableMap(Map<? extends K, ? extends V> map) {
		return new ConcurrentUnmodifiableMap<>(map);
	}

	public static <E> ConcurrentUnmodifiableSet<E> newUnmodifiableSet() {
		return new ConcurrentUnmodifiableSet<>();
	}

	@SafeVarargs
	public static <E> ConcurrentUnmodifiableSet<E> newUnmodifiableSet(E... array) {
		return new ConcurrentUnmodifiableSet<>(array);
	}

	public static <E> ConcurrentUnmodifiableSet<E> newUnmodifiableSet(Collection<? extends E> collection) {
		return new ConcurrentUnmodifiableSet<>(collection);
	}

	public static <E> Collector<E, ?, ConcurrentCollection<E>> toCollection() {
		return StreamUtil.toConcurrentCollection();
	}

	public static <E> Collector<E, ?, ConcurrentList<E>> toList() {
		return StreamUtil.toConcurrentList();
	}

	public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toMap() {
		return StreamUtil.toConcurrentMap();
	}

	public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
		return StreamUtil.toConcurrentMap(keyMapper, valueMapper);
	}

	public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
		return StreamUtil.toConcurrentMap(keyMapper, valueMapper, mergeFunction);
	}

	public static <T, K, V, M extends ConcurrentMap<K, V>> Collector<T, ?, M> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction, Supplier<M> mapSupplier) {
		return StreamUtil.toConcurrentMap(keyMapper, valueMapper, mergeFunction, mapSupplier);
	}

	public static <T, K, V> Collector<T, ?, ConcurrentLinkedMap<K, V>> toLinkedMap() {
		return StreamUtil.toConcurrentLinkedMap();
	}

	public static <T, K, V> Collector<T, ?, ConcurrentLinkedMap<K, V>> toLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
		return StreamUtil.toConcurrentLinkedMap(keyMapper, valueMapper);
	}

	public static <T, K, V> Collector<T, ?, ConcurrentLinkedMap<K, V>> toLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
		return StreamUtil.toConcurrentLinkedMap(keyMapper, valueMapper, mergeFunction);
	}

	public static <T, K, V, M extends ConcurrentLinkedMap<K, V>> Collector<T, ?, M> toLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction, Supplier<M> mapSupplier) {
		return StreamUtil.toConcurrentLinkedMap(keyMapper, valueMapper, mergeFunction, mapSupplier);
	}

	public static <E> Collector<E, ?, ConcurrentSet<E>> toSet() {
		return StreamUtil.toConcurrentSet();
	}

}
