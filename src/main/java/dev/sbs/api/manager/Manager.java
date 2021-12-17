package dev.sbs.api.manager;

import com.google.common.base.Preconditions;
import dev.sbs.api.SimplifiedException;
import dev.sbs.api.manager.service.exception.UnknownServiceException;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentSet;

/**
 * Manager for containing providers of class instances.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
public abstract class Manager<P extends Provider> {

    protected final transient ConcurrentSet<P> providers = Concurrent.newSet();

    /**
     * Checks if the given service class has a registered instance.
     *
     * @param service class type to check.
     * @return if class has a registered instance
     */
    public final boolean isRegistered(Class<?> service) {
        Preconditions.checkNotNull(service, "Service cannot be NULL!");

        for (P provider : this.providers) {
            if (provider.getService().isAssignableFrom(service))
                return true;
        }

        return false;
    }

    /**
     * Gets the builder provider for the given service class.
     *
     * @param service class type to get
     * @param <T>     type of service
     * @return service provider for the given class
     * @throws UnknownServiceException When the given service class does not have a registered instance
     * @see #isRegistered(Class)
     */
    public <T> Provider getProvider(Class<T> service) throws UnknownServiceException {
        if (this.isRegistered(service)) {
            for (Provider provider : this.providers) {
                if (provider.getService().isAssignableFrom(service))
                    return provider;
            }
        }

        throw SimplifiedException.builder(UnknownServiceException.class)
            .setMessage(UnknownServiceException.getMessage(service))
            .build();
    }

}
