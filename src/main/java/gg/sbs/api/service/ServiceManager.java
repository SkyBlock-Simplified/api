package gg.sbs.api.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import gg.sbs.api.service.exception.RegisteredServiceException;
import gg.sbs.api.service.exception.UnknownServiceException;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentSet;

import java.util.Set;

/**
 * Manager for containing services that assist in instance access.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ServiceManager {

	private final transient Set<ServiceProvider> serviceProviders = Sets.newHashSet();

	/**
	 * Checks if the given service class has a registered instance.
	 *
	 * @param service class type to check.
	 * @return if class has a registered instance
	 */
	public final boolean isRegistered(Class<?> service) {
		Preconditions.checkArgument(service != null, "Service cannot be NULL!");

		for (ServiceProvider provider : this.serviceProviders) {
			if (provider.getService().isAssignableFrom(service))
				return true;
		}

		return false;
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
	public final <T> ServiceProvider<T> getServiceProvider(Class<T> service) throws UnknownServiceException {
		if (this.isRegistered(service)) {
			for (ServiceProvider provider : this.serviceProviders) {
				if (provider.getService().isAssignableFrom(service))
					return provider;
			}
		}

		throw new UnknownServiceException(service);
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
	public final <T> T getProvider(Class<T> service) throws UnknownServiceException {
		return this.getServiceProvider(service).getProvider();
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
	public final <T> ConcurrentSet<T> getProviders(Class<T> service) throws UnknownServiceException {
		ConcurrentSet<T> providers = Concurrent.newSet();

		for (ServiceProvider provider : this.serviceProviders) {
			if (service.isAssignableFrom(provider.getService()))
				providers.add((T)provider.getProvider());
		}

		if (providers.isEmpty())
			throw new UnknownServiceException(service);

		return providers;
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
	public final <T> ConcurrentSet<Class<T>> getServices(Class<T> service) throws UnknownServiceException {
		ConcurrentSet<Class<T>> providers = Concurrent.newSet();

		for (ServiceProvider provider : this.serviceProviders) {
			if (service.isAssignableFrom(provider.getService()))
				providers.add(provider.getService());
		}

		if (providers.isEmpty())
			throw new UnknownServiceException(service);

		return providers;
	}

	/**
	 * Registers an instance for the given service class.
	 *
	 * @param service Service class.
	 * @param instance Instance of service.
	 * @param <T> Type of service.
	 * @throws RegisteredServiceException When the given class already has a registered service.
	 */
	public final <T> void provide(Class<T> service, T instance) throws RegisteredServiceException {
		Preconditions.checkArgument(instance != null, "Instance cannot be NULL!");

		if (this.isRegistered(service))
			throw new RegisteredServiceException(service);

		this.serviceProviders.add(new ServiceProvider<>(service, instance));
	}

}