package dev.sbs.api.util.helper;

import dev.sbs.api.util.builder.string.StringBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentCollection;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedList;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.collection.search.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public static <T> Predicate<T> distinctByKey(@NotNull Function<? super T, ?> keyExtractor) {
        ConcurrentSet<Object> seen = Concurrent.newSet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static <T> Stream<T> flattenOptional(@NotNull Optional<T> optional) {
        return Stream.ofNullable(optional.orElse(null));
    }

    /**
     * Returns a stream consisting of the results of applying the given function to the elements of
     * {@code stream} and their indices in the stream. For example,
     *
     * <pre>{@code
     * mapWithIndex(
     *     Stream.of("a", "b", "c"),
     *     (str, index, size) -> str + ":" + index + "/" + size)
     * }</pre>
     *
     * <p>would return {@code Stream.of("a:0/3", "b:1/3", "c:2/3")}.
     *
     * <p>The resulting stream is <a
     * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>
     * if and only if {@code stream} was efficiently splittable and its underlying spliterator
     * reported {@link Spliterator#SUBSIZED}. This is generally the case if the underlying stream
     * comes from a data structure supporting efficient indexed random access, typically an array or
     * list.
     *
     * <p>The order of the resulting stream is defined if and only if the order of the original stream
     * was defined.
     */
    public static <T, R> Stream<R> mapWithIndex(@NotNull Stream<T> stream, @NotNull TriFunction<? super T, Long, Long, ? extends R> function) {
        Objects.requireNonNull(stream);
        Objects.requireNonNull(function);
        boolean isParallel = stream.isParallel();
        Spliterator<T> fromSpliterator = stream.spliterator();
        long size = fromSpliterator.estimateSize();

        if (!fromSpliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            Iterator<T> fromIterator = Spliterators.iterator(fromSpliterator);

            return StreamSupport.stream(
                    new Spliterators.AbstractSpliterator<R>(
                        fromSpliterator.estimateSize(),
                        fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED)) {
                        long index = 0;

                        @Override
                        public boolean tryAdvance(Consumer<? super R> action) {
                            if (fromIterator.hasNext()) {
                                action.accept(function.apply(fromIterator.next(), index++, size));
                                return true;
                            }
                            return false;
                        }
                    },
                    isParallel)
                .onClose(stream::close);
        }

        class Splitr extends MapWithIndexSpliterator<Spliterator<T>, R, Splitr> implements Consumer<T> {

            private @Nullable T holder;

            Splitr(Spliterator<T> splitr, long index) {
                super(splitr, index);
            }

            @Override
            public void accept(@NotNull T t) {
                this.holder = t;
            }

            @Override
            public boolean tryAdvance(Consumer<? super R> action) {
                if (fromSpliterator.tryAdvance(this)) {
                    try {
                        // The cast is safe because tryAdvance puts a T into `holder`.
                        action.accept(function.apply(holder, index++, size));
                        return true;
                    } finally {
                        holder = null;
                    }
                }
                return false;
            }

            @Override
            Splitr createSplit(Spliterator<T> from, long i) {
                return new Splitr(from, i);
            }

        }

        return StreamSupport.stream(new Splitr(fromSpliterator, 0), isParallel).onClose(stream::close);
    }

    public static <T> Stream<T> modifyStream(@NotNull Stream<T> stream, @NotNull TriFunction<T, Long, Long, T> modFunction) {
        return mapWithIndex(stream, (value, index, size) -> modFunction.apply(value, index, size));
    }

    public static Stream<String> appendEach(@NotNull Stream<String> stringStream, @NotNull String entryValue) {
        return appendEach(stringStream, entryValue, entryValue);
    }

    public static Stream<String> appendEach(@NotNull Stream<String> stringStream, @NotNull String entryValue, @NotNull String lastEntry) {
        return modifyStream(stringStream, (value, index, size) -> value + (index < size - 1 ? entryValue : lastEntry));
    }

    public static Stream<String> prependEach(@NotNull Stream<String> stringStream, @NotNull String entryValue) {
        return prependEach(stringStream, entryValue, entryValue);
    }

    public static Stream<String> prependEach(@NotNull Stream<String> stringStream, @NotNull String entryValue, @NotNull String lastEntry) {
        return modifyStream(stringStream, (value, index, size) -> (index < size - 1 ? entryValue : lastEntry) + value);
    }

    public static <E> Collector<E, ?, ConcurrentCollection<E>> toConcurrentCollection() {
        return new StreamCollector<>(ConcurrentCollection::new, ConcurrentCollection::add, (left, right) -> { left.addAll(right); return left; }, CHARACTERISTICS);
    }

    public static <E> Collector<E, ?, ConcurrentLinkedList<E>> toConcurrentLinkedList() {
        return new StreamCollector<>(ConcurrentLinkedList::new, ConcurrentLinkedList::add, (left, right) -> { left.addAll(right); return left; }, CHARACTERISTICS);
    }

    public static <E> Collector<E, ?, ConcurrentList<E>> toConcurrentList() {
        return new StreamCollector<>(ConcurrentList::new, ConcurrentList::add, (left, right) -> { left.addAll(right); return left; }, CHARACTERISTICS);
    }

    public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toConcurrentMap() {
        return toConcurrentMap(throwingMerger());
    }

    public static <T, K, V> Collector<T, ?, ConcurrentMap<K, V>> toConcurrentMap(BinaryOperator<V> mergeFunction) {
        Function<? super T, ? extends K> keyMapper = entry -> ((Map.Entry<K, V>) entry).getKey();
        Function<? super T, ? extends V> valueMapper = entry -> ((Map.Entry<K, V>) entry).getValue();
        return toConcurrentMap(keyMapper, valueMapper, mergeFunction);
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
        return toConcurrentLinkedMap(throwingMerger());
    }

    public static <T, K, V> Collector<T, ?, ConcurrentLinkedMap<K, V>> toConcurrentLinkedMap(BinaryOperator<V> mergeFunction) {
        Function<? super T, ? extends K> keyMapper = entry -> ((Map.Entry<K, V>) entry).getKey();
        Function<? super T, ? extends V> valueMapper = entry -> ((Map.Entry<K, V>) entry).getValue();
        return toConcurrentLinkedMap(keyMapper, valueMapper, mergeFunction);
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

    private abstract static class MapWithIndexSpliterator<F extends Spliterator<?>, R extends Object, S extends MapWithIndexSpliterator<F, R, S>> implements Spliterator<R> {

        protected final F fromSpliterator;
        protected long index;

        MapWithIndexSpliterator(F fromSpliterator, long index) {
            this.fromSpliterator = fromSpliterator;
            this.index = index;
        }

        abstract S createSplit(F from, long i);

        @Override
        public S trySplit() {
            Spliterator<?> splitOrNull = fromSpliterator.trySplit();

            if (splitOrNull == null) {
                return null;
            }

            F split = (F) splitOrNull;
            S result = createSplit(split, index);
            this.index += split.getExactSizeIfKnown();
            return result;
        }

        @Override
        public long estimateSize() {
            return fromSpliterator.estimateSize();
        }

        @Override
        public int characteristics() {
            return fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED);
        }

    }

}
