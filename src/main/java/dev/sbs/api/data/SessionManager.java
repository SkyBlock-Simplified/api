package dev.sbs.api.data;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.data.exception.DataException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The {@code SessionManager} class is responsible for managing {@link DataSession} instances,
 * allowing for the establishment, management, and termination of data sessions. It provides methods
 * to work with default or uniquely identified sessions, retrieve repositories, handle initialization,
 * and manage session states.
 * <br><br>
 * All operations involving data sessions, such as creating, reconnecting, or shutting down,
 * are managed centrally through this class.
 * <br><br>
 * Thread safety is ensured through the use of a {@link ConcurrentMap}.
 */
@SuppressWarnings("all")
public final class SessionManager {

    private final @NotNull ConcurrentMap<String, DataSession<?>> sessions = Concurrent.newMap();

    /**
     * Establishes a connection to the data source using the provided {@link DataConfig} instance
     * with the {@link DataConfig#getKey() config key}.
     *
     * @param <T>        The type of {@link Model} that the {@link DataConfig} manages.
     * @param dataConfig The {@link DataConfig} instance containing the configuration
     *                   for creating a session and managing the models.
     */
    public <T extends Model> @NotNull DataSession<T> connect(@NotNull DataConfig<T> dataConfig) {
        if (!this.isActive(dataConfig.getIdentifier())) {
            DataSession<T> session = dataConfig.createSession();
            this.sessions.put(dataConfig.getIdentifier(), session);
            session.initialize();
            session.cacheRepositories();
            return session;
        } else
            throw new DataException("Session with the specified key is already active.");
    }

    /**
     * Disconnects all active sessions managed by the {@code SessionManager} instance.
     * This method iterates over all registered sessions and disconnects each one individually.
     */
    public void disconnect() {
        this.sessions.forEach((key, session) -> this.disconnect(key));
    }

    /**
     * Disconnects the active session associated with the specified key.
     *
     * @param key The unique identifier for the session to be disconnected.
     */
    public void disconnect(@NotNull String key) {
        if (this.isActive(key))
            this.sessions.get(key).shutdown();
    }

    /**
     * Reconnects all registered sessions managed by the {@code SessionManager} instance.
     * This method iterates over all registered sessions and reconnects each one individually.
     */
    public void reconnect() {
        this.sessions.forEach((key, session) -> this.reconnect(key));
    }

    /**
     * Re-establishes the session associated with the specified key, reinitializing its state and recaching its associated repositories.
     *
     * @param key The unique identifier for the session to be reconnected.
     */
    public void reconnect(@NotNull String key) {
        if (this.isRegistered(key)) {
            // Reinitialize Session
            this.sessions.get(key).initialize();

            // Recache Repositories
            this.sessions.get(key).cacheRepositories();
        }
    }

    /**
     * Retrieves the {@link Repository} instance associated with the specified {@link Model} class.
     *
     * @param <M>    The type of the {@link Model}.
     * @param tClass The {@link Class} object representing the type of {@link Model} for which to retrieve the repository.
     * @return The {@link Repository} of type {@link M}.
     * @throws DataException If no session is active or the repository cannot be retrieved.
     */
    public <M extends Model> @NotNull Repository<M> getRepository(@NotNull Class<M> tClass) {
        if (!this.isActive())
            throw new DataException("There are no active sessions.");

        for (Map.Entry<String, DataSession<?>> entry : this.sessions) {
            if (entry.getValue().getServiceManager().isRegistered(tClass))
                return this.getSession(entry.getKey()).getRepositoryOf(tClass);
        }

        throw new DataException("Repository cannot be retrieved.");
    }

    /**
     * Retrieves the {@link DataSession} associated with the specified key.
     *
     * @param <M> The type of the {@link Model}.
     * @param <S> The type of the {@link DataSession}.
     * @param key The unique identifier for the session.
     * @return The {@link DataSession} of type {@link S} associated with the specified key.
     * @throws DataException If the session associated with the specified key has not been initialized.
     */
    public <M extends Model, S extends DataSession<M>> @NotNull S getSession(@NotNull String key) {
        if (this.isRegistered(key))
            return (S) this.sessions.get(key);
        else
            throw new DataException("Session has not been initialized.");
    }

    /**
     * Indicates whether any session is currently active.
     *
     * @return {@code true} if any session is active, otherwise {@code false}.
     */
    public boolean isActive() {
        return this.sessions.stream().anyMatch((key, session) -> this.isActive(key));
    }

    /**
     * Indicates whether the session associated with the specified key is currently active.
     *
     * @param key The unique identifier for the session.
     * @return {@code true} if the session associated with the specified key is active and registered, otherwise {@code false}.
     */
    public boolean isActive(@NotNull String key) {
        return this.isRegistered() && this.sessions.get(key).isActive();
    }

    /**
     * Determines whether any session is registered.
     *
     * @return {@code true} if any session is registered, otherwise {@code false}.
     */
    public boolean isRegistered() {
        return this.sessions.stream().anyMatch((key, session) -> this.isRegistered(key));
    }

    /**
     * Checks if a session associated with the given key is registered.
     *
     * @param key The unique identifier for the session.
     * @return {@code true} if the session with the specified key is registered, otherwise {@code false}.
     */
    public boolean isRegistered(@NotNull String key) {
        return this.sessions.containsKey(key);
    }

}
