package dev.sbs.api.util.search.function;

import java.util.function.Function;

public interface SortFunction<T, C extends Comparable<C>> extends Function<T, C> {

    static <T, C extends Comparable<C>> SortFunction<T, C> of(FilterFunction<T, C> function) {
        return new SortFunctionImpl<>(function);
    }

}
