package gg.sbs.api.manager.service;

import com.google.common.base.Preconditions;
import gg.sbs.api.manager.Manager;
import gg.sbs.api.manager.service.exception.InvalidServiceException;
import gg.sbs.api.manager.service.exception.RegisteredServiceException;
import gg.sbs.api.manager.service.exception.UnknownServiceException;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentSet;

/**
 * Manager for containing services that assist in instance access.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
@SuppressWarnings({"unchecked"})
public class ServiceManager extends Manager<ServiceProvider> {

	/**
	 * Registers an instance for the given service class.
	 *
	 * @param service Service class.
	 * @param instance Instance of service.
	 * @param <T> Type of service.
	 * @throws RegisteredServiceException When the given class already has a registered service.
	 */
	public final <T> void add(Class<T> service, T instance) throws RegisteredServiceException {
		Preconditions.checkArgument(instance != null, "Instance cannot be NULL");

		if (this.isRegistered(service))
			throw new RegisteredServiceException(service);

		super.providers.add(new ServiceProvider(service, instance));
	}

	/**
	 * Registers an instance for the given service class.
	 *
	 * @param service Service class.
	 * @param instance Instance of service.
	 * @throws RegisteredServiceException When the given class already has a registered service.
	 */
	public final void addRaw(Class<?> service, Object instance) throws RegisteredServiceException {
		Preconditions.checkNotNull(instance, "Instance cannot be NULL");

		if (this.isRegistered(service))
			throw new RegisteredServiceException(service);

		if (!service.isAssignableFrom(instance.getClass()))
			throw new InvalidServiceException(service, instance);

		super.providers.add(new ServiceProvider(service, instance));
	}

	/**
	 * Gets the instance for the given service class.
	 *
	 * @param service class type to get
	 * @param <T> type of service
	 * @return builder instance for the given class
	 * @throws UnknownServiceException When the given service class does not have a registered instance
	 * @see #isRegistered(Class)
	 */
	public final <T> T get(Class<T> service) throws UnknownServiceException {
		return (T) this.getProvider(service).getProvider();
	}

	/**
	 * Gets all providers that extend from service.
	 *
	 * @param service class type to inherit
	 * @param <T> type of service
	 * @return set of builder instances for the given class
	 * @throws UnknownServiceException When the given service class does not have any registered instances
	 * @see #isRegistered(Class)
	 */
	public final <T> ConcurrentSet<T> getAll(Class<T> service) throws UnknownServiceException {
		ConcurrentSet<T> providers = Concurrent.newSet();

		for (ServiceProvider provider : super.providers) {
			if (service.isAssignableFrom(provider.getService()))
				providers.add((T)provider.getProvider());
		}

		if (providers.isEmpty())
			throw new UnknownServiceException(service);

		return providers;
	}

	/**
	 * Gets the builder provider for the given service class.
	 *
	 * @param service class type to get
	 * @param <T> type of service
	 * @return service provider for the given class
	 * @throws UnknownServiceException When the given service class does not have a registered instance
	 * @see #isRegistered(Class)
	 */
	@Override
	public final <T> ServiceProvider getProvider(Class<T> service) throws UnknownServiceException {
		return (ServiceProvider) super.getProvider(service);
	}

	/**
	 * Gets the instance for the given service class.
	 *
	 * @param service class type to get
	 * @param <T> type of service
	 * @return set of service instances for the given class
	 * @throws UnknownServiceException When the given service class does not have a registered instance
	 * @see #isRegistered(Class)
	 */
	public final <T> ConcurrentSet<Class<?>> getServices(Class<T> service) throws UnknownServiceException {
		ConcurrentSet<Class<?>> providers = Concurrent.newSet();

		for (ServiceProvider provider : super.providers) {
			if (service.isAssignableFrom(provider.getService()))
				providers.add(provider.getService());
		}

		if (providers.isEmpty())
			throw new UnknownServiceException(service);

		return providers;
	}

}