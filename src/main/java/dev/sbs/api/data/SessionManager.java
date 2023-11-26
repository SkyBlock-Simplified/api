package dev.sbs.api.data;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.util.SimplifiedException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("all")
public final class SessionManager {

    private @NotNull Optional<DataSession<?>> session = Optional.empty();

    /**
     * Connects to a database as defined in the provided {@link DataConfig}.
     *
     * @param dataConfig Config with connection and model details.
     */
    public <T extends Model> void connect(@NotNull DataConfig<T> dataConfig) {
        if (!this.isActive()) {
            // Create Session
            DataSession<T> session = dataConfig.createSession();
            this.session = Optional.of(session);

            // Initialize Session
            session.initialize();

            // Cache Repositories
            session.cacheRepositories();
        }
    }

    /**
     * Disconnects from a connected database.
     */
    public void disconnect() {
        if (this.isActive())
            this.session.get().shutdown();
    }

    /**
     * Gets the {@link Repository<M>} caching all items of type {@link M}.
     *
     * @param tClass The {@link Model} class to find.
     * @param <M> The type of model.
     * @return The repository of type {@link M}.
     */
    public <M extends Model> @NotNull Repository<M> getRepositoryOf(Class<M> tClass) {
        return this.getSession().getRepositoryOf(tClass);
    }

    /**
     * Gets the active {@link DataSession}.
     */
    public <M extends Model, S extends DataSession<M>> @NotNull S getSession() {
        if (this.isRegistered())
            return (S) this.session.get();
        else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Session has not been initialized!")
                .build();
    }

    public boolean isActive() {
        return this.isRegistered() && this.session.map(DataSession::isActive).orElse(false);
    }

    public boolean isRegistered() {
        return this.session.isPresent();
    }

}
