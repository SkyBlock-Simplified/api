package dev.sbs.api.util.search.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface SearchFunction<T, R> extends Function<T, R> {

    /**
     * Alows you to traverse down through method references.
     *
     * @param first  The first function in the chain
     * @param second The last function in the chain
     * @param <T1>   The first class to traverse
     * @param <T2>   The second class to traverse
     * @param <T3>   The return type
     * @return The method reference between {@link T1} and {@link T3}
     */
    static <T1, T2, T3> SearchFunction<T1, T3> combine(SearchFunction<T1, T2> first, SearchFunction<T2, T3> second) {
        return first.andThen(second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <V> SearchFunction<T, V> andThen(@NotNull Function<? super R, ? extends V> after) {
        return (T t) -> after.apply(apply(t));
    }

    enum Match {

        ALL,
        ANY

    }

}
