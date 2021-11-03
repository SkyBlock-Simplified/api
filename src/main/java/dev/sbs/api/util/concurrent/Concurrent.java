package dev.sbs.api.util.concurrent;

import dev.sbs.api.util.concurrent.linked.ConcurrentLinkedList;
import dev.sbs.api.util.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.concurrent.linked.ConcurrentLinkedSet;
import dev.sbs.api.util.concurrent.unmodifiable.ConcurrentUnmodifiableCollection;
import dev.sbs.api.util.concurrent.unmodifiable.ConcurrentUnmodifiableList;
import dev.sbs.api.util.concurrent.unmodifiable.ConcurrentUnmodifiableSet;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.tuple.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Helper class to build a new concurrent collection instance.
 */
public final class Concurrent {

	private static final ConcurrentSet<Collector.Characteristics> CHARACTERISTICS = Concurrent.newSet(Collector.Characteristics.CONCURRENT, Collector.Characteristics.IDENTITY_FINISH);
	private static final ConcurrentSet<Collector.Characteristics> UN_CHARACTERISTICS = Concurrent.newSet(Collector.Characteristics.CONCURRENT, Collector.Characteristics.IDENTITY_FINISH, Collector.Characteristics.UNORDERED);

	@SuppressWarnings("unchecked")
	private static <I, R> Function<I, R> castingIdentity() {
		return i -> (R)i;
	}

	private static <T> BinaryOperator<T> throwingMerger() {
		return (key, value) -> { throw new IllegalStateException(FormatUtil.format("Duplicate key {0}", key)); };
	}

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
		return new ConcurrentCollector<>(ConcurrentCollection::new, ConcurrentCollection::add, (left, right) -> { left.addAll(right); return left; }, CHARACTERISTICS);
	}

	public static <E> Collector<E, ?, ConcurrentList<E>> toList() {
		return new ConcurrentCollector<>(ConcurrentList::new, ConcurrentList::add, (left, right) -> { left.addAll(right); return left; }, CHARACTERISTICS);
	}

	@SuppressWarnings("unchecked")
	public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toMap() {
		Function<? super T, ? extends K> keyMapper = entry -> ((Map.Entry<K, V>)entry).getKey();
		Function<? super T, ? extends V> valueMapper = entry -> ((Map.Entry<K, V>)entry).getValue();
		return toMap(keyMapper, valueMapper);
	}

	public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
		return toMap(keyMapper, valueMapper, throwingMerger(), ConcurrentMap::new);
	}

	public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
		return toMap(keyMapper, valueMapper, mergeFunction, ConcurrentMap::new);
	}

	public static <T, K, V, M extends ConcurrentMap<K, V>> Collector<T, ?, M> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction, Supplier<M> mapSupplier) {
		BiConsumer<M, T> accumulator = (map, element) -> map.merge(keyMapper.apply(element), valueMapper.apply(element), mergeFunction);

		return new ConcurrentCollector<>(mapSupplier, accumulator, (m1, m2) -> {
			m2.forEach((key, value) -> m1.merge(key, value, mergeFunction));
			return m1;
		}, UN_CHARACTERISTICS);
	}

	@SuppressWarnings("unchecked")
	public static <T, K, V> Collector<T, ?, ConcurrentLinkedMap<K, V>> toLinkedMap() {
		Function<? super T, ? extends K> keyMapper = entry -> ((Map.Entry<K, V>)entry).getKey();
		Function<? super T, ? extends V> valueMapper = entry -> ((Map.Entry<K, V>)entry).getValue();
		return toLinkedMap(keyMapper, valueMapper);
	}

	public static <T, K, V> Collector<T, ?, ConcurrentLinkedMap<K, V>> toLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
		return toLinkedMap(keyMapper, valueMapper, throwingMerger(), ConcurrentLinkedMap::new);
	}

	public static <T, K, V> Collector<T, ?, ConcurrentLinkedMap<K, V>> toLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
		return toLinkedMap(keyMapper, valueMapper, mergeFunction, ConcurrentLinkedMap::new);
	}

	public static <T, K, V, M extends ConcurrentLinkedMap<K, V>> Collector<T, ?, M> toLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction, Supplier<M> mapSupplier) {
		BiConsumer<M, T> accumulator = (map, element) -> map.merge(keyMapper.apply(element), valueMapper.apply(element), mergeFunction);

		return new ConcurrentCollector<>(mapSupplier, accumulator, (m1, m2) -> {
			m2.forEach((key, value) -> m1.merge(key, value, mergeFunction));
			return m1;
		}, UN_CHARACTERISTICS);
	}

	public static <E> Collector<E, ?, ConcurrentSet<E>> toSet() {
		return new ConcurrentCollector<>(ConcurrentSet::new, ConcurrentSet::add, (left, right) -> { left.addAll(right); return left; }, UN_CHARACTERISTICS);
	}

	private static class ConcurrentCollector<T, A, R> implements Collector<T, A, R> {

		private final Supplier<A> supplier;
		private final BiConsumer<A, T> accumulator;
		private final BinaryOperator<A> combiner;
		private final Function<A, R> finisher;
		private final Set<Characteristics> characteristics;

		public ConcurrentCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Set<Characteristics> characteristics) {
			this(supplier, accumulator, combiner, castingIdentity(), characteristics);
		}

		public ConcurrentCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A,R> finisher, Set<Characteristics> characteristics) {
			this.supplier = supplier;
			this.accumulator = accumulator;
			this.combiner = combiner;
			this.finisher = finisher;
			this.characteristics = characteristics;
		}

		@Override
		public BiConsumer<A, T> accumulator() {
			return this.accumulator;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return this.characteristics;
		}

		@Override
		public BinaryOperator<A> combiner() {
			return this.combiner;
		}

		@Override
		public Function<A, R> finisher() {
			return this.finisher;
		}

		@Override
		public Supplier<A> supplier() {
			return this.supplier;
		}

	}

}
