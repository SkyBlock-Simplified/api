package dev.sbs.api.data;

import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.manager.Manager;
import dev.sbs.api.manager.ServiceManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DataSession<T extends Model> {

    @Getter(AccessLevel.PROTECTED)
    protected final @NotNull ServiceManager serviceManager = new ServiceManager(Manager.Mode.ALL);
    protected final @NotNull ConcurrentList<Class<T>> models;
    protected final @NotNull DataType type;
    protected boolean active;
    protected boolean cached = false;
    protected long initialization;
    protected long startup;

    protected abstract <U extends T> void addRepository(@NotNull Class<U> model);

    public final <M extends T, S extends DataSession<M>> @NotNull S asType(@NotNull Class<S> sessionType) {
        return sessionType.cast(this);
    }

    protected abstract void build();

    public final void cacheRepositories() {
        if (!this.isCached()) {
            this.cached = true;
            long startTime = System.currentTimeMillis();

            for (Class<? extends T> model : this.getModels())
                this.addRepository(model);

            this.startup = System.currentTimeMillis() - startTime;
        } else
            throw new DataException("Session has already cached repositories.");
    }

    /**
     * Gets the {@link Repository <M>} caching all items of type {@link M}.
     *
     * @param tClass The {@link Model} class to find.
     * @param <M> The type of model.
     * @return The repository of type {@link M}.
     */
    @SuppressWarnings("unchecked")
    public final <M extends T> @NotNull Repository<M> getRepositoryOf(@NotNull Class<M> tClass) {
        if (this.isActive())
            return (Repository<M>) this.serviceManager.get(tClass);
        else
            throw new DataException("Session connection is not active.");
    }

    public final void initialize() {
        if (!this.isActive()) {
            long startTime = System.currentTimeMillis();
            this.build();
            this.active = true;
            this.initialization = System.currentTimeMillis() - startTime;
        } else
            throw new DataException("Session is already initialized.");
    }

    public void shutdown() {
        this.active = false;
    }

}
