package dev.sbs.api.util.search.function;

import java.util.function.Function;

public interface SortFunction<T, C extends Comparable<C>> extends Function<T, C> {

    static <T, C extends Comparable<C>> SortFunction<T, C> of(FilterFunction<T, C> function) {
        return new SortFunctionImpl<>(function);
    }

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
    static <T1, T2, T3 extends Comparable<T3>> SortFunction<T1, T3> combine(FilterFunction<T1, T2> first, SortFunction<T2, T3> second) {
        return new SortFunctionImpl<>(first.andThen(second));
    }

}
