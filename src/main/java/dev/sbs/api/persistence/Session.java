package dev.sbs.api.persistence;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.persistence.exception.SessionException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Session<T extends Model> {

    @Getter(AccessLevel.PROTECTED)
    protected final @NotNull ConcurrentMap<Class<? extends T>, Repository<? extends T>> repositories = Concurrent.newMap();
    protected final @NotNull ConcurrentList<Class<T>> models;
    protected boolean active;
    protected boolean cached = false;
    protected long initialization;
    protected long startup;

    protected abstract <U extends T> @NotNull Repository<U> createRepository(@NotNull Class<U> model);

    public final <M extends T, S extends Session<M>> @NotNull S asType(@NotNull Class<S> sessionType) {
        return sessionType.cast(this);
    }

    protected abstract void build();

    public final void cacheRepositories() {
        if (!this.isCached()) {
            this.cached = true;
            long startTime = System.currentTimeMillis();

            for (Class<? extends T> model : this.getModels())
                this.repositories.put(model, this.createRepository(model));

            this.startup = System.currentTimeMillis() - startTime;
        } else
            throw new SessionException("Session has already cached repositories.");
    }

    /**
     * Gets the {@link Repository <M>} caching all items of type {@link M}.
     *
     * @param tClass The {@link Model} class to find.
     * @param <M> The type of model.
     * @return The repository of type {@link M}.
     */
    @SuppressWarnings("unchecked")
    public final <M extends T> @NotNull Repository<M> getRepository(@NotNull Class<M> tClass) {
        if (this.isActive())
            return (Repository<M>) this.repositories.get(tClass);
        else
            throw new SessionException("Session connection is not active.");
    }

    public final void initialize() {
        if (!this.isActive()) {
            long startTime = System.currentTimeMillis();
            this.build();
            this.active = true;
            this.initialization = System.currentTimeMillis() - startTime;
        } else
            throw new SessionException("Session is already initialized.");
    }

    public void shutdown() {
        this.active = false;
        this.repositories.forEach((model, repository) -> repository.getTask().cancel(true));
        this.repositories.clear();
    }

}
