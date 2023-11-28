package dev.sbs.api.util.stream.triple;

import dev.sbs.api.util.data.tuple.triple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface TripleStream<L, M, R> {

    // Create

    static <L, M, R> @NotNull TripleStream<L, M, R> of(@NotNull Stream<Triple<L, M, R>> stream) {
        return () -> stream;
    }

    static <L, M, R> @NotNull TripleStream<L, M, R> of(@NotNull Stream<L> stream, @NotNull Function<? super L, ? extends M> middle, @NotNull Function<? super L, ? extends R> right) {
        return () -> stream.map(left -> Triple.of(left, middle.apply(left), right.apply(left)));
    }

    // Entries

    @NotNull Stream<Triple<L, M, R>> entries();

    default @NotNull Stream<L> lefts() {
        return this.entries().map(Triple::getLeft);
    }

    default @NotNull Stream<M> middles() {
        return this.entries().map(Triple::getMiddle);
    }

    default @NotNull Stream<R> rights() {
        return this.entries().map(Triple::getRight);
    }

    default long count() {
        return this.entries().count();
    }

    default @NotNull TripleStream<L, M, R> distinct() {
        return of(this.entries().distinct());
    }

    default @NotNull TripleStream<L, M, R> limit(long maxSize) {
        return of(this.entries().limit(maxSize));
    }

    default @NotNull TripleStream<L, M, R> peek(@NotNull TriConsumer<? super L, ? super M, ? super R> action) {
        return of(this.entries().peek(entry -> action.accept(entry.getLeft(), entry.getMiddle(), entry.getRight())));
    }

    default @NotNull TripleStream<L, M, R> skip(long number) {
        return of(this.entries().skip(number));
    }

    // Filter

    default @NotNull TripleStream<L, M, R> filterLeft(@NotNull Predicate<? super L> mapper) {
        return of(this.entries().filter(entry -> mapper.test(entry.getLeft())));
    }

    default @NotNull TripleStream<L, M, R> filterMiddle(@NotNull Predicate<? super M> mapper) {
        return of(this.entries().filter(entry -> mapper.test(entry.getMiddle())));
    }

    default @NotNull TripleStream<L, M, R> filterRight(@NotNull Predicate<? super R> mapper) {
        return of(this.entries().filter(entry -> mapper.test(entry.getRight())));
    }

    default @NotNull TripleStream<L, M, R> filter(@NotNull TriPredicate<? super L, ? super M, ? super R> mapper) {
        return of(this.entries().filter(entry -> mapper.test(entry.getLeft(), entry.getMiddle(), entry.getRight())));
    }

    // Flatmapping

    default <RL, RM, RR> @NotNull TripleStream<RL, RM, RR> flatMap(@NotNull TriFunction<? super L, ? super M, ? super R, ? extends TripleStream<RL, RM, RR>> mapper) {
        return of(this.entries().flatMap(entry -> mapper.apply(entry.getLeft(), entry.getMiddle(), entry.getRight()).entries()));
    }

    default <RT> @NotNull Stream<RT> flatMapToObj(@NotNull TriFunction<? super L, ? super M, ? super R, ? extends Stream<RT>> mapper) {
        return this.entries().flatMap(entry -> mapper.apply(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    default @NotNull DoubleStream flatMapToDouble(@NotNull TriFunction<? super L, ? super M, ? super R, ? extends DoubleStream> mapper) {
        return this.entries().flatMapToDouble(entry -> mapper.apply(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    default @NotNull IntStream flatMapToInt(@NotNull TriFunction<? super L, ? super M, ? super R, ? extends IntStream> mapper) {
        return this.entries().flatMapToInt(entry -> mapper.apply(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    default @NotNull LongStream flatMapToLong(@NotNull TriFunction<? super L, ? super M, ? super R, ? extends LongStream> mapper) {
        return this.entries().flatMapToLong(entry -> mapper.apply(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    // ForEach

    default void forEach(@NotNull TriConsumer<? super L, ? super M, ? super R> action) {
        this.entries().forEach(entry -> action.accept(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    default void forEachOrdered(@NotNull TriConsumer<? super L, ? super M, ? super R> action) {
        this.entries().forEachOrdered(entry -> action.accept(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    // Mapping

    default <T> @NotNull Stream<T> map(@NotNull TriFunction<? super L, ? super M, ? super R, ? extends T> mapper) {
        return this.entries().map(entry -> mapper.apply(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    default @NotNull DoubleStream mapToDouble(@NotNull TriFunction<? super L, ? super M, ? super R, Double> mapper) {
        return this.entries().mapToDouble(entry -> mapper.apply(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    default @NotNull IntStream mapToInt(@NotNull TriFunction<? super L, ? super M, ? super R, Integer> mapper) {
        return this.entries().mapToInt(entry -> mapper.apply(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    default @NotNull LongStream mapToLong(@NotNull TriFunction<? super L, ? super M, ? super R, Long> mapper) {
        return this.entries().mapToLong(entry -> mapper.apply(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    default <RT> @NotNull TripleStream<RT, M, R> mapLeft(@NotNull Function<? super L, ? extends RT> mapper) {
        return of(this.entries().map(entry -> Triple.of(mapper.apply(entry.getLeft()), entry.getMiddle(), entry.getRight())));
    }

    default <RT> @NotNull TripleStream<L, RT, R> mapMiddle(@NotNull Function<? super M, ? extends RT> mapper) {
        return of(this.entries().map(entry -> Triple.of(entry.getLeft(), mapper.apply(entry.getMiddle()), entry.getRight())));
    }

    default <RT> @NotNull TripleStream<L, M, RT> mapRight(@NotNull Function<? super R, ? extends RT> mapper) {
        return of(this.entries().map(entry -> Triple.of(entry.getLeft(), entry.getMiddle(), mapper.apply(entry.getRight()))));
    }

    // Matching

    default boolean allMatch(@NotNull TriPredicate<? super L, ? super M, ? super R> predicate) {
        return this.entries().allMatch(entry -> predicate.test(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    default boolean anyMatch(@NotNull TriPredicate<? super L, ? super M, ? super R> predicate) {
        return this.entries().anyMatch(entry -> predicate.test(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    default boolean noneMatch(@NotNull TriPredicate<? super L, ? super M, ? super R> predicate) {
        return this.entries().noneMatch(entry -> predicate.test(entry.getLeft(), entry.getMiddle(), entry.getRight()));
    }

    // Minmax

    default @NotNull Optional<Triple<L, M, R>> maxByLeft(@NotNull Comparator<? super L> comparator) {
        return this.entries().max((c1, c2) -> comparator.compare(c1.getLeft(), c2.getLeft()));
    }

    default @NotNull Optional<Triple<L, M, R>> maxByMiddle(@NotNull Comparator<? super M> comparator) {
        return this.entries().max((c1, c2) -> comparator.compare(c1.getMiddle(), c2.getMiddle()));
    }

    default @NotNull Optional<Triple<L, M, R>> maxByRight(@NotNull Comparator<? super R> comparator) {
        return this.entries().max((c1, c2) -> comparator.compare(c1.getRight(), c2.getRight()));
    }

    default @NotNull Optional<Triple<L, M, R>> minByLeft(@NotNull Comparator<? super L> comparator) {
        return this.entries().min((c1, c2) -> comparator.compare(c1.getLeft(), c2.getLeft()));
    }

    default @NotNull Optional<Triple<L, M, R>> minByMiddle(@NotNull Comparator<? super M> comparator) {
        return this.entries().min((c1, c2) -> comparator.compare(c1.getMiddle(), c2.getMiddle()));
    }

    default @NotNull Optional<Triple<L, M, R>> minByRight(@NotNull Comparator<? super R> comparator) {
        return this.entries().min((c1, c2) -> comparator.compare(c1.getRight(), c2.getRight()));
    }

    // Sorting

    default @NotNull TripleStream<L, M ,R> sortedByLeft(@NotNull Comparator<? super L> comparator) {
        return of(this.entries().sorted((c1, c2) -> comparator.compare(c1.getLeft(), c2.getLeft())));
    }

    default @NotNull TripleStream<L, M ,R> sortedByMiddle(@NotNull Comparator<? super M> comparator) {
        return of(this.entries().sorted((c1, c2) -> comparator.compare(c1.getMiddle(), c2.getMiddle())));
    }

    default @NotNull TripleStream<L, M ,R> sortedByRight(@NotNull Comparator<? super R> comparator) {
        return of(this.entries().sorted((c1, c2) -> comparator.compare(c1.getRight(), c2.getRight())));
    }

    // Collect

    /**
     * Performs a <a href="package-summary.html#MutableReduction">mutable
     * reduction</a> operation on the elements of this stream using a
     * {@code Collector}.  A {@code Collector}
     * encapsulates the functions used as arguments to
     * {@link Stream#collect(Supplier, BiConsumer, BiConsumer)}, allowing for reuse of
     * collection strategies and composition of collect operations such as
     * multiple-level grouping or partitioning.
     *
     * <p>If the stream is parallel, and the {@code Collector}
     * is {@link Collector.Characteristics#CONCURRENT concurrent}, and
     * either the stream is unordered or the collector is
     * {@link Collector.Characteristics#UNORDERED unordered},
     * then a concurrent reduction will be performed (see {@link Collector} for
     * details on concurrent reduction.)
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * <p>When executed in parallel, multiple intermediate results may be
     * instantiated, populated, and merged so as to maintain isolation of
     * mutable data structures.  Therefore, even when executed in parallel
     * with non-thread-safe data structures (such as {@code ArrayList}), no
     * additional synchronization is needed for a parallel reduction.
     *
     * @apiNote
     * The following will accumulate strings into a List:
     * <pre>{@code
     *     List<String> asList = stringStream.collect(Collectors.toList());
     * }</pre>
     *
     * <p>The following will classify {@code Person} objects by city:
     * <pre>{@code
     *     Map<String, List<Person>> peopleByCity
     *         = personStream.collect(Collectors.groupingBy(Person::getCity));
     * }</pre>
     *
     * <p>The following will classify {@code Person} objects by state and city,
     * cascading two {@code Collector}s together:
     * <pre>{@code
     *     Map<String, Map<String, List<Person>>> peopleByStateAndCity
     *         = personStream.collect(Collectors.groupingBy(Person::getState,
     *                                                      Collectors.groupingBy(Person::getCity)));
     * }</pre>
     *
     * @param <T> the type of the result
     * @param <A> the intermediate accumulation type of the {@code Collector}
     * @param collector the {@code Collector} describing the reduction
     * @return the result of the reduction
     * @see Stream#collect(Supplier, BiConsumer, BiConsumer)
     * @see Collectors
     */
    default <T, A> T collect(@NotNull Collector<? super Triple<L, M, R>, A, T> collector) {
        return this.entries().collect(collector);
    }

}