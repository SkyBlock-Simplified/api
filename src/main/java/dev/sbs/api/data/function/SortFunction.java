package dev.sbs.api.data.function;

import dev.sbs.api.data.model.Model;

import java.util.function.Function;

public interface SortFunction<T extends Model, C extends Comparable<C>> extends Function<T, C> {

    static <T extends Model, C extends Comparable<C>> SortFunction<T, C> of(FilterFunction<T, C> function) {
        return new SortFunctionImpl<>(function);
    }

}
