package dev.sbs.api.util.helper.stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class StreamCollector<T, A, R> implements Collector<T, A, R> {

    @SuppressWarnings("unchecked")
    public static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    private final @NotNull Supplier<A> supplier;
    private final @NotNull BiConsumer<A, T> accumulator;
    private final @NotNull BinaryOperator<A> combiner;
    private final @NotNull Function<A, R> finisher;
    private final @NotNull Set<Characteristics> characteristics;

    public StreamCollector(@NotNull Supplier<A> supplier, @NotNull BiConsumer<A, T> accumulator, @NotNull BinaryOperator<A> combiner, @NotNull Set<Characteristics> characteristics) {
        this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        Collectors.toUnmodifiableMap()
    }

}