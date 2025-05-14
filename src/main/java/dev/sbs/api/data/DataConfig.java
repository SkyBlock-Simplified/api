package dev.sbs.api.data;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.sort.Graph;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.reflection.Reflection;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;

@Getter
public abstract class DataConfig<T extends Model> {

    private final @NotNull ConcurrentList<Class<T>> models = this.loadModels();
    protected @NotNull Level logLevel = Level.WARN;

    protected abstract @NotNull DataSession<T> createSession();

    public final boolean isLogLevel(@NotNull Level level) {
        return this.getLogLevel().intLevel() >= level.intLevel();
    }

    private @NotNull ConcurrentList<Class<T>> loadModels() {
        return loadModels(Reflection.getSuperClass(this));
    }

    @SuppressWarnings("unchecked")
    protected static <T extends Model> @NotNull ConcurrentList<Class<T>> loadModels(@NotNull Class<T> modelType) {
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

    public final void setLogLevel(@NotNull Level level) {
        this.logLevel = level;
        this.onLogLevelChange(level);
    }

    protected void onLogLevelChange(@NotNull Level level) { }

}
