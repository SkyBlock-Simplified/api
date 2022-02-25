package dev.sbs.api.util.helper;

import dev.sbs.api.util.builder.string.StringBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentCollection;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.collection.search.function.TriFunction;
import dev.sbs.api.util.data.tuple.Pair;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("all")
public class StreamUtil {

    private static final ConcurrentSet<Collector.Characteristics> CHARACTERISTICS = Concurrent.newSet(Collector.Characteristics.CONCURRENT, Collector.Characteristics.IDENTITY_FINISH);
    private static final ConcurrentSet<Collector.Characteristics> UN_CHARACTERISTICS = Concurrent.newSet(Collector.Characteristics.CONCURRENT, Collector.Characteristics.IDENTITY_FINISH, Collector.Characteristics.UNORDERED);

    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R)i;
    }

    private static <T> BinaryOperator<T> throwingMerger() {
        return (key, value) -> { throw new IllegalStateException(FormatUtil.format("Duplicate key {0}!", key)); };
    }

    public static <T> Stream<T> modifyStream(Stream<T> stringStream, TriFunction<Integer, T, Integer, T> modFunction) {
        AtomicInteger counter = new AtomicInteger();

        return stringStream.map(value -> Pair.of(counter.getAndIncrement(), value))
            .map(pair -> modFunction.apply(pair.getKey(), pair.getValue(), counter.get()));
    }

    public static Stream<String> appendEach(Stream<String> stringStream, String entryValue) {
        return appendEach(stringStream, entryValue, entryValue);
    }

    public static Stream<String> appendEach(Stream<String> stringStream, String entryValue, String lastEntry) {
        return modifyStream(stringStream, (index, value, size) -> value + (index < size - 1 ? entryValue : lastEntry));
    }

    public static Stream<String> prependEach(Stream<String> stringStream, String entryValue) {
        return prependEach(stringStream, entryValue, entryValue);
    }

    public static Stream<String> prependEach(Stream<String> stringStream, String entryValue, String lastEntry) {
        return modifyStream(stringStream, (index, value, size) -> (index < size - 1 ? entryValue : lastEntry) + value);
    }

    public static <E> Collector<E, ?, ConcurrentCollection<E>> toConcurrentCollection() {
        return new StreamCollector<>(ConcurrentCollection::new, ConcurrentCollection::add, (left, right) -> { left.addAll(right); return left; }, CHARACTERISTICS);
    }

    public static <E> Collector<E, ?, ConcurrentList<E>> toConcurrentList() {
        return new StreamCollector<>(ConcurrentList::new, ConcurrentList::add, (left, right) -> { left.addAll(right); return left; }, CHARACTERISTICS);
    }

    public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toConcurrentMap() {
        Function<? super T, ? extends K> keyMapper = entry -> ((Map.Entry<K, V>)entry).getKey();
        Function<? super T, ? extends V> valueMapper = entry -> ((Map.Entry<K, V>)entry).getValue();
        return toConcurrentMap(keyMapper, valueMapper);
    }

    public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toConcurrentMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return toConcurrentMap(keyMapper, valueMapper, throwingMerger(), ConcurrentMap::new);
    }

    public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toConcurrentMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
        return toConcurrentMap(keyMapper, valueMapper, mergeFunction, ConcurrentMap::new);
    }

    public static <T, K, V, M extends ConcurrentMap<K, V>> Collector<T, ?, M> toConcurrentMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction, Supplier<M> mapSupplier) {
        BiConsumer<M, T> accumulator = (map, element) -> map.merge(keyMapper.apply(element), valueMapper.apply(element), mergeFunction);

        return new StreamCollector<>(mapSupplier, accumulator, (m1, m2) -> {
            m2.forEach((key, value) -> m1.merge(key, value, mergeFunction));
            return m1;
        }, UN_CHARACTERISTICS);
    }

    public static <T, K, V> Collector<T, ?, ConcurrentLinkedMap<K, V>> toConcurrentLinkedMap() {
        Function<? super T, ? extends K> keyMapper = entry -> ((Map.Entry<K, V>)entry).getKey();
        Function<? super T, ? extends V> valueMapper = entry -> ((Map.Entry<K, V>)entry).getValue();
        return toConcurrentLinkedMap(keyMapper, valueMapper);
    }

    public static <T, K, V> Collector<T, ?, ConcurrentLinkedMap<K, V>> toConcurrentLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return toConcurrentLinkedMap(keyMapper, valueMapper, throwingMerger(), ConcurrentLinkedMap::new);
    }

    public static <T, K, V> Collector<T, ?, ConcurrentLinkedMap<K, V>> toConcurrentLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
        return toConcurrentLinkedMap(keyMapper, valueMapper, mergeFunction, ConcurrentLinkedMap::new);
    }

    public static <T, K, V, M extends ConcurrentLinkedMap<K, V>> Collector<T, ?, M> toConcurrentLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction, Supplier<M> mapSupplier) {
        BiConsumer<M, T> accumulator = (map, element) -> map.merge(keyMapper.apply(element), valueMapper.apply(element), mergeFunction);

        return new StreamCollector<>(mapSupplier, accumulator, (m1, m2) -> {
            m2.forEach((key, value) -> m1.merge(key, value, mergeFunction));
            return m1;
        }, UN_CHARACTERISTICS);
    }

    public static <E> Collector<E, ?, ConcurrentSet<E>> toConcurrentSet() {
        return new StreamCollector<>(ConcurrentSet::new, ConcurrentSet::add, (left, right) -> { left.addAll(right); return left; }, UN_CHARACTERISTICS);
    }

    public static <E, A> Collector<E, ?, StringBuilder> toStringBuilder() {
        return toStringBuilder(true);
    }

    public static <E, A> Collector<E, ?, StringBuilder> toStringBuilder(boolean newLine) {
        return new StreamCollector<>(
            StringBuilder::new,
            newLine ? StringBuilder::appendln : StringBuilder::append,
            (left, right) -> {
                if (newLine)
                    left.appendln(right);
                else
                    left.append(right);

                return left;
            },
            CHARACTERISTICS
        );
    }

    private static class StreamCollector<T, A, R> implements Collector<T, A, R> {

        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        public StreamCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }

        public StreamCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A,R> finisher, Set<Characteristics> characteristics) {
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
