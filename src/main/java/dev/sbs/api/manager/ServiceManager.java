package dev.sbs.api.manager;

import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.manager.exception.InvalidReferenceException;
import dev.sbs.api.manager.exception.RegisteredReferenceException;
import dev.sbs.api.manager.exception.UnknownReferenceException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import org.jetbrains.annotations.NotNull;

/**
 * Manager for containing services that assist in instance access.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
@SuppressWarnings({ "unchecked" })
public class ServiceManager extends Manager<Class<?>, Object> {

    public ServiceManager() {
        super((entry, service) -> service.isAssignableFrom(entry.getKey()));
    }

    /**
     * Registers an instance for the given service class.
     *
     * @param service Service class.
     * @param instance Instance of service.
     * @param <T> Type of service.
     * @throws RegisteredReferenceException When the given class already has a registered service.
     */
    public final <T> void add(@NotNull Class<T> service, @NotNull T instance) throws RegisteredReferenceException {
        super.add(service, instance);
    }

    /**
     * Registers a repository for the given service class.
     *
     * @param service Service class.
     * @param repository Repository instance of service.
     * @throws RegisteredReferenceException When the given class already has a registered service.
     * @throws InvalidReferenceException When the given repository does not match the given service.
     */
    public final void addRepository(@NotNull Class<? extends Model> service, @NotNull Repository<? extends Model> repository) throws RegisteredReferenceException, InvalidReferenceException {
        if (!service.isAssignableFrom(repository.getType()))
            throw SimplifiedException.of(InvalidReferenceException.class)
                .withMessage(InvalidReferenceException.getMessage(service.getName(), repository))
                .build();

        super.add(service, repository);
    }

    /**
     * Gets the instance for the given service class.
     *
     * @param service Class type to get.
     * @param <T> Type of service.
     * @return Builder Instance for the given class.
     * @throws UnknownReferenceException When the given service class does not have a registered instance.
     * @see #isRegistered
     */
    public final <T> T get(@NotNull Class<T> service) throws UnknownReferenceException {
        return (T) super.get(service);
    }

    /**
     * Gets all values that extend from service.
     *
     * @param service Class type to inherit.
     * @param <T>     Type of service.
     * @return Set of builder instances for the given class.
     * @throws UnknownReferenceException When the given service class does not have any registered instances.
     * @see #isRegistered
     */
    public final <T> ConcurrentSet<T> getAll(@NotNull Class<T> service) throws UnknownReferenceException {
        ConcurrentSet<T> values = this.ref.stream()
            .filter(entry -> service.isAssignableFrom(entry.getKey()))
            .map(entry -> (T) entry.getValue())
            .collect(Concurrent.toSet())
            .toUnmodifiableSet();

        if (values.isEmpty())
            throw SimplifiedException.of(UnknownReferenceException.class)
                .withMessage(UnknownReferenceException.getMessage(service))
                .build();

        return values;
    }

    /**
     * Registers or updates an instance for the given service class.
     *
     * @param service Service class.
     * @param instance Instance of service.
     * @param <T> Type of service.
     */
    public final <T> void replace(@NotNull Class<T> service, @NotNull T instance) {
        super.replace(service, instance);
    }

    /**
     * Updates an instance for the given service class.
     *
     * @param service Service class.
     * @param instance Instance of service.
     * @param <T> Type of service.
     * @throws UnknownReferenceException When the given service class is not registered.
     */
    public final <T> void update(@NotNull Class<T> service, @NotNull T instance) throws UnknownReferenceException {
        if (!this.isRegistered(service))
            throw SimplifiedException.of(UnknownReferenceException.class)
                .withMessage(UnknownReferenceException.getMessage(service))
                .build();

        super.update(service, instance);
    }

}