package dev.sbs.api.util.helper.stream;

import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.data.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface PairStream<K, V> {

    static <K, V> @NotNull PairStream<K, V> of(@NotNull Map<K, V> map) {
        return of(map.entrySet().stream());
    }

    static <K, V> @NotNull PairStream<K, V> of(@NotNull Stream<Map.Entry<K, V>> stream) {
        return () -> stream;
    }

    static <K, V> @NotNull PairStream<K, V> of(@NotNull Stream<K> stream, Function<? super K, ? extends V> function) {
        return () -> stream.map(key -> Pair.of(key, function.apply(key)));
    }

    default @NotNull PairStream<K, V> distinct() {
        return of(this.entries().distinct());
    }

    default @NotNull PairStream<K, V> peek(@NotNull BiConsumer<? super K, ? super V> action) {
        return of(this.entries().peek(entry -> action.accept(entry.getKey(), entry.getValue())));
    }

    default @NotNull PairStream<K, V> skip(long number) {
        return of(this.entries().skip(number));
    }

    default @NotNull PairStream<K, V> limit(long maxSize) {
        return of(this.entries().limit(maxSize));
    }

    default @NotNull PairStream<K, V> filterKey(@NotNull Predicate<? super K> mapper) {
        return of(this.entries().filter(entry -> mapper.test(entry.getKey())));
    }

    default @NotNull PairStream<K, V> filterValue(@NotNull Predicate<? super V> mapper) {
        return of(this.entries().filter(entry -> mapper.test(entry.getValue())));
    }

    default @NotNull PairStream<K, V> filter(@NotNull BiPredicate<? super K, ? super V> mapper) {
        return of(this.entries().filter(entry -> mapper.test(entry.getKey(), entry.getValue())));
    }

    default <R> PairStream<R, V> mapKey(@NotNull Function<? super K,? extends R> mapper) {
        return of(this.entries().map(entry -> Pair.of(mapper.apply(entry.getKey()), entry.getValue())));
    }

    default <R> @NotNull PairStream<K, R> mapValue(@NotNull Function<? super V,? extends R> mapper) {
        return of(this.entries().map(entry -> Pair.of(entry.getKey(), mapper.apply(entry.getValue()))));
    }

    default <R> @NotNull Stream<R> map(@NotNull BiFunction<? super K, ? super V,? extends R> mapper) {
        return this.entries().map(entry -> mapper.apply(entry.getKey(), entry.getValue()));
    }

    default @NotNull DoubleStream mapToDouble(@NotNull ToDoubleBiFunction<? super K, ? super V> mapper) {
        return this.entries().mapToDouble(entry -> mapper.applyAsDouble(entry.getKey(), entry.getValue()));
    }

    default @NotNull IntStream mapToInt(@NotNull ToIntBiFunction<? super K, ? super V> mapper) {
        return this.entries().mapToInt(entry -> mapper.applyAsInt(entry.getKey(), entry.getValue()));
    }

    default @NotNull LongStream mapToLong(@NotNull ToLongBiFunction<? super K, ? super V> mapper) {
        return this.entries().mapToLong(entry -> mapper.applyAsLong(entry.getKey(), entry.getValue()));
    }

    default <RK, RV> @NotNull PairStream<RK, RV> flatMap(@NotNull BiFunction<? super K, ? super V, ? extends PairStream<RK, RV>> mapper) {
        return of(this.entries().flatMap(entry -> mapper.apply(entry.getKey(), entry.getValue()).entries()));
    }

    default <R> @NotNull Stream<R> flatMapToObj(@NotNull BiFunction<? super K, ? super V,? extends Stream<R>> mapper) {
        return this.entries().flatMap(entry -> mapper.apply(entry.getKey(), entry.getValue()));
    }

    default @NotNull DoubleStream flatMapToDouble(@NotNull BiFunction<? super K, ? super V,? extends DoubleStream> mapper) {
        return this.entries().flatMapToDouble(entry -> mapper.apply(entry.getKey(), entry.getValue()));
    }

    default @NotNull IntStream flatMapToInt(@NotNull BiFunction<? super K, ? super V,? extends IntStream> mapper) {
        return this.entries().flatMapToInt(entry -> mapper.apply(entry.getKey(), entry.getValue()));
    }

    default @NotNull LongStream flatMapToLong(@NotNull BiFunction<? super K, ? super V,? extends LongStream> mapper) {
        return this.entries().flatMapToLong(entry -> mapper.apply(entry.getKey(), entry.getValue()));
    }

    default @NotNull PairStream<K, V> sortedByKey(@NotNull Comparator<? super K> comparator) {
        return of(this.entries().sorted(Map.Entry.comparingByKey(comparator)));
    }

    default @NotNull PairStream<K, V> sortedByValue(@NotNull Comparator<? super V> comparator) {
        return of(this.entries().sorted(Map.Entry.comparingByValue(comparator)));
    }

    default boolean allMatch(@NotNull BiPredicate<? super K,? super V> predicate) {
        return this.entries().allMatch(entry -> predicate.test(entry.getKey(), entry.getValue()));
    }

    default boolean anyMatch(@NotNull BiPredicate<? super K,? super V> predicate) {
        return this.entries().anyMatch(entry -> predicate.test(entry.getKey(), entry.getValue()));
    }

    default boolean noneMatch(@NotNull BiPredicate<? super K,? super V> predicate) {
        return this.entries().noneMatch(entry -> predicate.test(entry.getKey(), entry.getValue()));
    }

    default long count() {
        return this.entries().count();
    }

    @NotNull Stream<Map.Entry<K, V>> entries();

    default @NotNull Stream<K> keys() {
        return this.entries().map(Map.Entry::getKey);
    }

    default @NotNull Stream<V> values() {
        return this.entries().map(Map.Entry::getValue);
    }

    default @NotNull Optional<Map.Entry<K, V>> maxByKey(@NotNull Comparator<? super K> comparator) {
        return this.entries().max(Map.Entry.comparingByKey(comparator));
    }

    default @NotNull Optional<Map.Entry<K, V>> maxByValue(@NotNull Comparator<? super V> comparator) {
        return this.entries().max(Map.Entry.comparingByValue(comparator));
    }

    default @NotNull Optional<Map.Entry<K, V>> minByKey(@NotNull Comparator<? super K> comparator) {
        return this.entries().min(Map.Entry.comparingByKey(comparator));
    }

    default @NotNull Optional<Map.Entry<K, V>> minByValue(@NotNull Comparator<? super V> comparator) {
        return this.entries().min(Map.Entry.comparingByValue(comparator));
    }

    default void forEach(BiConsumer<? super K, ? super V> action) {
        this.entries().forEach(entry -> action.accept(entry.getKey(), entry.getValue()));
    }

    default void forEachOrdered(BiConsumer<? super K, ? super V> action) {
        this.entries().forEachOrdered(entry -> action.accept(entry.getKey(), entry.getValue()));
    }

    default @NotNull ConcurrentMap<K, V> toConcurrentMap() {
        return this.entries().collect(Concurrent.toMap());
    }

    default @NotNull ConcurrentMap<K, V> toConcurrentMap(@NotNull BinaryOperator<V> mergeFunction) {
        return this.entries().collect(Concurrent.toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction));
    }

    default @NotNull ConcurrentMap<K, V> toConcurrentMap(Function<? super Map.Entry<K, V>, ? extends K> keyMapper, Function<? super Map.Entry<K, V>, ? extends V> valueMapper) {
        return this.entries().collect(Concurrent.toMap(keyMapper, valueMapper));
    }

    default @NotNull ConcurrentMap<K, V> toConcurrentMap(Function<? super Map.Entry<K, V>, ? extends K> keyMapper, Function<? super Map.Entry<K, V>, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
        return this.entries().collect(Concurrent.toMap(keyMapper, valueMapper, mergeFunction));
    }

    default @NotNull ConcurrentMap<K, V> toConcurrentMap(Function<? super Map.Entry<K, V>, ? extends K> keyMapper, Function<? super Map.Entry<K, V>, ? extends V> valueMapper, BinaryOperator<V> mergeFunction, Supplier<ConcurrentMap<K, V>> mapSupplier) {
        return this.entries().collect(Concurrent.toMap(keyMapper, valueMapper, mergeFunction, mapSupplier));
    }

    default @NotNull ConcurrentLinkedMap<K, V> toConcurrentLinkedMap() {
        return this.entries().collect(Concurrent.toLinkedMap());
    }

    default @NotNull ConcurrentLinkedMap<K, V> toConcurrentLinkedMap(@NotNull BinaryOperator<V> mergeFunction) {
        return this.entries().collect(Concurrent.toLinkedMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction));
    }

    default @NotNull ConcurrentLinkedMap<K, V> toConcurrentLinkedMap(Function<? super Map.Entry<K, V>, ? extends K> keyMapper, Function<? super Map.Entry<K, V>, ? extends V> valueMapper) {
        return this.entries().collect(Concurrent.toLinkedMap(keyMapper, valueMapper));
    }

    default @NotNull ConcurrentLinkedMap<K, V> toConcurrentLinkedMap(Function<? super Map.Entry<K, V>, ? extends K> keyMapper, Function<? super Map.Entry<K, V>, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
        return this.entries().collect(Concurrent.toLinkedMap(keyMapper, valueMapper, mergeFunction));
    }

    default @NotNull ConcurrentLinkedMap<K, V> toConcurrentLinkedMap(Function<? super Map.Entry<K, V>, ? extends K> keyMapper, Function<? super Map.Entry<K, V>, ? extends V> valueMapper, BinaryOperator<V> mergeFunction, Supplier<ConcurrentLinkedMap<K, V>> mapSupplier) {
        return this.entries().collect(Concurrent.toLinkedMap(keyMapper, valueMapper, mergeFunction, mapSupplier));
    }

    default @NotNull ConcurrentMap<K, V> toConcurrentUnmodifiableMap() {
        return this.entries().collect(Concurrent.toUnmodifiableMap());
    }

    default @NotNull ConcurrentMap<K, V> toConcurrentUnmodifiableMap(@NotNull BinaryOperator<V> mergeFunction) {
        return this.entries().collect(Concurrent.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction));
    }

    default @NotNull ConcurrentMap<K, V> toConcurrentUnmodifiableMap(Function<? super Map.Entry<K, V>, ? extends K> keyMapper, Function<? super Map.Entry<K, V>, ? extends V> valueMapper) {
        return this.entries().collect(Concurrent.toUnmodifiableMap(keyMapper, valueMapper));
    }

    default @NotNull ConcurrentMap<K, V> toConcurrentUnmodifiableMap(Function<? super Map.Entry<K, V>, ? extends K> keyMapper, Function<? super Map.Entry<K, V>, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
        return this.entries().collect(Concurrent.toUnmodifiableMap(keyMapper, valueMapper, mergeFunction));
    }

    default @NotNull ConcurrentMap<K, V> toConcurrentUnmodifiableMap(Function<? super Map.Entry<K, V>, ? extends K> keyMapper, Function<? super Map.Entry<K, V>, ? extends V> valueMapper, BinaryOperator<V> mergeFunction, Supplier<ConcurrentMap<K, V>> mapSupplier) {
        return this.entries().collect(Concurrent.toUnmodifiableMap(keyMapper, valueMapper, mergeFunction, mapSupplier));
    }

}