package dev.sbs.api.data;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.manager.ServiceManager;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public abstract class DataSession<M extends Model> {

    protected final @NotNull ServiceManager serviceManager = new ServiceManager();
    @Getter protected final @NotNull ConcurrentList<Class<M>> models;
    @Getter protected boolean active;
    @Getter protected boolean cached = false;
    @Getter protected long initializationTime;
    @Getter protected long startupTime;

    protected DataSession(@NotNull ConcurrentList<Class<M>> models) {
        this.models = models;
    }

    protected abstract void addRepository(@NotNull Class<? extends M> model);

    protected abstract void build();

    public final void cacheRepositories() {
        if (!this.isCached()) {
            this.cached = true;
            long startTime = System.currentTimeMillis();

            // Provide SqlRepositories
            for (Class<? extends M> model : this.getModels())
                this.addRepository(model);

            this.startupTime = System.currentTimeMillis() - startTime;
        } else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Session has already cached repositories!")
                .build();
    }

    /**
     * Gets the {@link Repository <T>} caching all items of type {@link T}.
     *
     * @param tClass The {@link Model} class to find.
     * @param <T> The type of model.
     * @return The repository of type {@link T}.
     */
    @SuppressWarnings("unchecked")
    public final <T extends Model> Repository<T> getRepositoryOf(Class<T> tClass) {
        if (this.isActive())
            return (Repository<T>) this.serviceManager.get(tClass);
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
