package dev.sbs.api.data;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.manager.Manager;
import dev.sbs.api.manager.ServiceManager;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DataSession<T extends Model> {

    @Getter(AccessLevel.NONE)
    protected final @NotNull ServiceManager serviceManager = new ServiceManager(Manager.Mode.ALL);
    protected final @NotNull ConcurrentList<Class<T>> models;
    protected boolean active;
    protected boolean cached = false;
    protected long initializationTime;
    protected long startupTime;

    protected abstract <U extends T> void addRepository(@NotNull Class<U> model);

    public final <M extends T, S extends DataSession<M>> @NotNull S asType(@NotNull Class<S> sessionType) {
        return sessionType.cast(this);
    }

    protected abstract void build();

    public final void cacheRepositories() {
        if (!this.isCached()) {
            this.cached = true;
            long startTime = System.currentTimeMillis();

            // Provide SqlRepositories
            for (Class<? extends T> model : this.getModels())
                this.addRepository(model);

            this.startupTime = System.currentTimeMillis() - startTime;
        } else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Session has already cached repositories!")
                .build();
    }

    /**
     * Gets the {@link Repository <M>} caching all items of type {@link M}.
     *
     * @param tClass The {@link Model} class to find.
     * @param <M> The type of model.
     * @return The repository of type {@link M}.
     */
    @SuppressWarnings("unchecked")
    public final <M extends T> Repository<M> getRepositoryOf(Class<M> tClass) {
        if (this.isActive())
            return (Repository<M>) this.serviceManager.get(tClass);
        else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Session connection is not active!")
                .build();
    }

    public final void initialize() {
        if (!this.isActive()) {
            long startTime = System.currentTimeMillis();
            this.build();
            this.active = true;
            this.initializationTime = System.currentTimeMillis() - startTime;
        } else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Session is already initialized!")
                .build();
    }

    public void shutdown() {
        this.active = false;
    }

    public enum Type {

        SQL,
        JSON

    }
}
