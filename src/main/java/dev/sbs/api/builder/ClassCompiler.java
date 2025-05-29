package dev.sbs.api.builder;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a generic interface to dynamically create and configure instances
 * of a specified class type using the compiler design pattern. The created
 * instance must extend from the specified parent type {@code T}.
 *
 * @param <T> The type of the parent object that the created instances must extend.
 */
public interface ClassCompiler<T> {

    <R extends T> @NotNull R build(@NotNull Class<R> tClass);

}
