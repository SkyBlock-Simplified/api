package dev.sbs.api.data;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.data.sql.SqlSession;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.sort.Graph;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("all")
public final class SessionManager {

    private @NotNull Optional<DataSession<?>> session = Optional.empty();

    /**
     * Connects to a database as defined in the provided {@link DataConfig}.
     *
     * @param model Model classes to locate.
     * @param initFunction Constructor to build {@link DataSession}.
     */
    public <T extends Model> void connect(@NotNull Class<T> model, Function<ConcurrentList<Class<T>>, DataSession<T>> initFunction) {
        if (!this.isActive()) {
            // Create Session
            DataSession<?> session = initFunction.apply(getModels(model));
            this.session = Optional.of(session);

            // Initialize Session
            session.initialize();

            // Cache Repositories
            session.cacheRepositories();
        }
    }

    /**
     * Connects to a database as defined in the provided {@link SqlConfig}.
     *
     * @param sqlConfig Config with database details.
     */
    public void connectSql(@NotNull SqlConfig sqlConfig) {
        this.connect(SqlModel.class, models -> new SqlSession(sqlConfig, models));
    }

    /**
     * Disconnects from a connected database.
     */
    public void disconnect() {
        if (this.isActive())
            this.session.get().shutdown();
    }

    /**
     * Gets the {@link Repository<T>} caching all items of type {@link T}.
     *
     * @param tClass The {@link Model} class to find.
     * @param <T> The type of model.
     * @return The repository of type {@link T}.
     */
    public <T extends Model> @NotNull Repository<T> getRepositoryOf(Class<T> tClass) {
        return this.getSession().getRepositoryOf(tClass);
    }

    /**
     * Gets the active {@link DataSession}.
     */
    public <T extends DataSession<?>> @NotNull T getSession() {
        if (this.isRegistered())
            return (T) this.session.get();
        else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Session has not been initialized!")
                .build();
    }

    private static <T> @NotNull ConcurrentList<Class<T>> getModels(@NotNull Class<T> clazz) {
        return Graph.builder(clazz)
            .withValues(
                Reflection.getResources()
                    .filterPackage(clazz)
                    .getSubtypesOf(clazz)
            )
            .withEdgeFunction(type -> Arrays.stream(type.getDeclaredFields())
                .map(Field::getType)
                .filter(clazz::isAssignableFrom)
                .map(fieldType -> (Class<T>) fieldType)
            )
            .build()
            .topologicalSort();
    }

    public boolean isActive() {
        return this.isRegistered() && this.session.map(DataSession::isActive).orElse(false);
    }

    public boolean isRegistered() {
        return this.session.isPresent();
    }

}
