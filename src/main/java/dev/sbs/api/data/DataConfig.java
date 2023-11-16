package dev.sbs.api.data;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.ehcache.core.Ehcache;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DataConfig<T extends Model> {

    protected final transient ConcurrentList<Class<T>> models = Concurrent.newList();
    @Getter protected @NotNull Level loggingLevel = Level.WARN;

    protected Class<T> addDatabaseModel(@NotNull Class<T> model) {
        this.models.add(model);
        Configurator.setLevel(String.format("%s-%s", Ehcache.class, model.getName()), this.getLoggingLevel());
        return model;
    }

    public final @NotNull ConcurrentList<Class<? extends T>> getModels() {
        return Concurrent.newUnmodifiableList(this.models);
    }

    public final boolean isLoggingLevel(@NotNull Level level) {
        return level.intLevel() >= this.loggingLevel.intLevel();
    }

    public final void setLoggingLevel(@NotNull Level level) {
        this.loggingLevel = level;
        this.onLoggingLevelChange(level);
    }

    protected void onLoggingLevelChange(@NotNull Level level) { }

}
