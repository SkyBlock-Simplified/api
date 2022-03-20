package dev.sbs.api.manager.service;

import com.google.common.base.Preconditions;
import dev.sbs.api.manager.Manager;
import dev.sbs.api.manager.service.exception.InvalidServiceException;
import dev.sbs.api.manager.service.exception.RegisteredServiceException;
import dev.sbs.api.manager.service.exception.UnknownServiceException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;

/**
 * Manager for containing services that assist in instance access.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
@SuppressWarnings({ "unchecked" })
public class ServiceManager extends Manager<ServiceProvider> {

    /**
     * Registers an instance for the given service class.
     *
     * @param service  Service class.
     * @param instance Instance of service.
     * @param <T>      Type of service.
     * @throws RegisteredServiceException When the given class already has a registered service.
     */
    public final <T> void add(Class<T> service, T instance) throws RegisteredServiceException {
        Preconditions.checkArgument(instance != null, "Instance cannot be NULL!");

        if (this.isRegistered(service))
            throw SimplifiedException.of(RegisteredServiceException.class)
                .withMessage(RegisteredServiceException.getMessage(service))
                .build();

        super.providers.add(new ServiceProvider(service, instance));
    }

    /**
     * Registers an instance for the given service class.
     *
     * @param service  Service class.
     * @param instance Instance of service.
     * @throws RegisteredServiceException When the given class already has a registered service.
     */
    public final void addRaw(Class<?> service, Object instance) throws RegisteredServiceException {
        Preconditions.checkNotNull(instance, "Instance cannot be NULL!");

        if (this.isRegistered(service))
            throw SimplifiedException.of(RegisteredServiceException.class)
                .withMessage(RegisteredServiceException.getMessage(service))
                .build();

        if (!service.isAssignableFrom(instance.getClass()))
            throw SimplifiedException.of(InvalidServiceException.class)
                .withMessage(InvalidServiceException.getMessage(service, instance))
                .build();

        super.providers.add(new ServiceProvider(service, instance));
    }

    /**
     * Gets the instance for the given service class.
     *
     * @param service Class type to get.
     * @param <T>     Type of service.
     * @return Builder Instance for the given class.
     * @throws UnknownServiceException When the given service class does not have a registered instance.
     * @see #isRegistered(Class)
     */
    public final <T> T get(Class<T> service) throws UnknownServiceException {
        return (T) this.getProvider(service).getProvider();
    }

    /**
     * Gets all providers that extend from service.
     *
     * @param service Class type to inherit.
     * @param <T>     Type of service.
     * @return Set of builder instances for the given class.
     * @throws UnknownServiceException When the given service class does not have any registered instances.
     * @see #isRegistered(Class)
     */
    public final <T> ConcurrentSet<T> getAll(Class<T> service) throws UnknownServiceException {
        ConcurrentSet<T> providers = Concurrent.newSet();

        for (ServiceProvider serviceProvider : super.providers) {
            if (service.isAssignableFrom(serviceProvider.getService()))
                providers.add((T) serviceProvider.getProvider());
        }

        if (providers.isEmpty())
            throw SimplifiedException.of(UnknownServiceException.class)
                .withMessage(UnknownServiceException.getMessage(service))
                .build();

        return providers;
    }

    /**
     * Gets the builder provider for the given service class.
     *
     * @param service Class type to get.
     * @param <T>     Type of service.
     * @return Service provider for the given class.
     * @throws UnknownServiceException When the given service class does not have a registered instance.
     * @see #isRegistered(Class)
     */
    @Override
    public final <T> ServiceProvider getProvider(Class<T> service) throws UnknownServiceException {
        return (ServiceProvider) super.getProvider(service);
    }

    /**
     * Gets the instance for the given service class.
     *
     * @param service Class type to get.
     * @param <T>     Type of service.
     * @return Set of service instances for the given class.
     * @throws UnknownServiceException When the given service class does not have a registered instance.
     * @see #isRegistered(Class)
     */
    public final <T> ConcurrentSet<Class<?>> getServices(Class<T> service) throws UnknownServiceException {
        ConcurrentSet<Class<?>> providers = Concurrent.newSet();

        for (ServiceProvider serviceProvider : super.providers) {
            if (service.isAssignableFrom(serviceProvider.getService()))
                providers.add(serviceProvider.getService());
        }

        if (providers.isEmpty())
            throw SimplifiedException.of(UnknownServiceException.class)
                .withMessage(UnknownServiceException.getMessage(service))
                .build();

        return providers;
    }

    /**
     * Replaces an instance for the given service class.
     *
     * @param service  Service class.
     * @param instance New instance of service.
     * @param <T>      Type of service.
     * @throws UnknownServiceException When the given class does not have a registered instance.
     * @see #isRegistered(Class)
     */
    public final <T> void replace(Class<T> service, T instance) throws RegisteredServiceException {
        Preconditions.checkArgument(instance != null, "Instance cannot be NULL!");

        if (this.isRegistered(service)) {
            super.providers.remove(this.getProvider(service));
            this.add(service, instance);
            return;
        }

        throw SimplifiedException.of(UnknownServiceException.class)
            .withMessage(UnknownServiceException.getMessage(service))
            .build();
    }

}
