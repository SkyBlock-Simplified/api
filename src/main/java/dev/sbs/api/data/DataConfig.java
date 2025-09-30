package dev.sbs.api.data;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.sort.Graph;
import dev.sbs.api.reflection.Reflection;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface DataConfig<T extends Model> {

    @NotNull DataSession<T> createSession();

    default @NotNull ConcurrentList<Class<T>> detectModels() {
        return detectModels(Reflection.getSuperClass(this));
    }

    @SuppressWarnings("unchecked")
    static <T extends Model> @NotNull ConcurrentList<Class<T>> detectModels(@NotNull Class<T> modelType) {
        return Graph.builder(modelType)
            .withValues(
                Reflection.getResources()
                    .filterPackage(modelType)
                    .getTypesOf(modelType)
                    .stream()
                    .collect(Concurrent.toUnmodifiableList())
            )
            .withEdgeFunction(type -> Arrays.stream(type.getDeclaredFields())
                .map(Field::getType)
                .filter(modelType::isAssignableFrom) // Relies on modelType
                .map(fieldType -> (Class<T>) fieldType)
            )
            .build()
            .topologicalSort();
    }

    @NotNull String getIdentifier();

    @NotNull Level getLogLevel();

    @NotNull ConcurrentList<Class<T>> getModels();

    @NotNull DataType getType();

    default boolean isLogLevel(@NotNull Level level) {
        return this.getLogLevel().intLevel() >= level.intLevel();
    }

    void setLogLevel(@NotNull Level level);

}
