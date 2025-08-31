package dev.sbs.api.data;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.data.exception.DataException;
import org.jetbrains.annotations.NotNull;

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

    private static final @NotNull String DEFAULT_KEY = "default";
    private final @NotNull ConcurrentMap<String, DataSession<?>> sessions = Concurrent.newMap();

    /**
     * Establishes a connection to the data source using the provided {@link DataConfig} instance
     * with the default key.
     *
     * @param <T>        The type of {@link Model} that the {@link DataConfig} manages.
     * @param dataConfig The {@link DataConfig} instance containing the configuration
     *                   for creating a session and managing the models.
     */
    public <T extends Model> void connect(@NotNull DataConfig<T> dataConfig) {
        this.connect(DEFAULT_KEY, dataConfig);
    }

    /**
     * Establishes a connection to the data source using the specified key and the provided {@link DataConfig} instance.
     *
     * @param <T>        The type of {@link Model} that the {@link DataConfig} manages.
     * @param key        The unique identifier for the session.
     * @param dataConfig The {@link DataConfig} instance containing the configuration
     *                   for creating a session and managing the models.
     */
    public <T extends Model> void connect(@NotNull String key, @NotNull DataConfig<T> dataConfig) {
        if (!this.isActive(key)) {
            // Create Session
            DataSession<T> session = dataConfig.createSession();
            this.sessions.put(key, session);

            // Initialize Session
            session.initialize();

            // Cache Repositories
            session.cacheRepositories();
        }
    }

    /**
     * Disconnects the active session associated with the default key.
     */
    public void disconnect() {
        this.disconnect(DEFAULT_KEY);
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
     * Disconnects all active sessions managed by the {@code SessionManager} instance.
     * This method iterates over all registered sessions and disconnects each one individually.
     */
    public void disconnectAll() {
        this.sessions.forEach((key, session) -> this.disconnect(key));
    }

    /**
     * Re-establishes the session associated with the default key, reinitializing its state and recaching its associated repositories.
     */
    public void reconnect() {
        this.reconnect(DEFAULT_KEY);
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
     * Reconnects all registered sessions managed by the {@code SessionManager} instance.
     * This method iterates over all registered sessions and reconnects each one individually.
     */
    public void reconnectAll() {
        this.sessions.forEach((key, session) -> this.reconnect(key));
    }

    /**
     * Retrieves the {@link Repository} instance associated with the specified {@link Model} class.
     *
     * @param <M>    The type of the {@link Model}.
     * @param tClass The {@link Class} object representing the type of {@link Model} for which to retrieve the repository.
     * @return The {@link Repository} of type {@link M}.
     * @throws DataException If the session is not active or the repository cannot be retrieved.
     */
    public <M extends Model> @NotNull Repository<M> getRepository(@NotNull Class<M> tClass) {
        return this.getSession().getRepositoryOf(tClass);
    }

    /**
     * Retrieves the default {@link DataSession} associated with the default key.
     *
     * @param <M> The type of the {@link Model}.
     * @param <S> The type of the {@link DataSession}.
     * @return The default {@link DataSession} of type {@link S} associated with the default key.
     * @throws DataException If the session has not been initialized.
     */
    public <M extends Model, S extends DataSession<M>> @NotNull S getSession() {
        return this.getSession(DEFAULT_KEY);
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
     * Indicates whether the default session is currently active.
     *
     * @return {@code true} if the default session is active, otherwise {@code false}.
     */
    public boolean isActive() {
        return this.isActive(DEFAULT_KEY);
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
     * Determines whether the default session is registered.
     *
     * @return {@code true} if the default session is registered, otherwise {@code false}.
     */
    public boolean isRegistered() {
        return this.isRegistered(DEFAULT_KEY);
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
