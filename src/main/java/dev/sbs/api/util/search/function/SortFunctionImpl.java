package dev.sbs.api.util.search.function;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class SortFunctionImpl<T, C extends Comparable<C>> implements SortFunction<T, C> {

    @Getter private final FilterFunction<T, C> function;

    @Override
    public C apply(T t) {
        return function.apply(t);
    }

}