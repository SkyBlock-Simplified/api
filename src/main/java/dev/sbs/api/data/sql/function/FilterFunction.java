package dev.sbs.api.data.sql.function;

import dev.sbs.api.data.model.Model;
import lombok.NonNull;

import java.util.Objects;
import java.util.function.Function;

public interface FilterFunction<T extends Model, R> extends Function<T, R> {

    /**
     * Alows you to traverse down through method references.
     *
     * @param first  The first function in the chain
     * @param second The last function in the chain
     * @param <T1>   The first SqlModel to traverse
     * @param <T2>   The second SqlModel to traverse
     * @param <T3>   The return type
     * @return The method reference between {@link T1} and {@link T3}
     */
    static <T1 extends Model, T2 extends Model, T3> FilterFunction<T1, T3> combine(FilterFunction<T1, T2> first, FilterFunction<T2, T3> second) {
        return first.andThen(second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <V> FilterFunction<T, V> andThen(@NonNull Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    enum Match {

        ALL,
        ANY

    }

}
