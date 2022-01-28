package dev.sbs.api.data.function;

import dev.sbs.api.data.model.Model;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class SortFunctionImpl<T extends Model, C extends Comparable<C>> implements SortFunction<T, C> {

    @Getter private final FilterFunction<T, C> function;

    @Override
    public C apply(T t) {
        return function.apply(t);
    }

}