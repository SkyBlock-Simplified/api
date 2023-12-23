package dev.sbs.api.util.builder;

import org.jetbrains.annotations.NotNull;

public interface ClassBuilder<T> {

    <R extends T> @NotNull R build(@NotNull Class<R> tClass);

}
