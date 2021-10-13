package gg.sbs.api.manager.builder;

import com.google.common.base.Preconditions;
import gg.sbs.api.manager.builder.exception.InvalidBuilderException;
import gg.sbs.api.manager.builder.exception.RegisteredBuilderException;
import gg.sbs.api.manager.builder.exception.UnknownBuilderException;
import gg.sbs.api.manager.builder.implementation.CoreBuilder;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentSet;

import java.lang.reflect.ParameterizedType;

/**
 * Manager for builders that assist in constructing classes.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class BuilderManager {

	private final transient ConcurrentSet<BuilderProvider> builderProviders = Concurrent.newSet();

	/**
	 * Checks if the given service class has a registered builder.
	 *
	 * @param service Class type to check.
	 * @return True if class has a registered builder.
	 */
	public final boolean isRegistered(Class<?> service) {
		Preconditions.checkArgument(service != null, "Service cannot be NULL");

		for (BuilderProvider provider : this.builderProviders) {
			if (provider.getService().isAssignableFrom(service))
				return true;
		}

		return false;
	}

	/**
	 * Gets the builder provider for the given service class.
	 *
	 * @param service Class type to get.
	 * @param <T> Type of service.
	 * @param <B> Type of builder class.
	 * @return Builder provider for the given class.
	 * @throws UnknownBuilderException When the given service class does not have a registered builder.
	 * @see #isRegistered(Class)
	 */
	public final <T, B extends CoreBuilder> BuilderProvider<T, B> getBuilderProvider(Class<T> service) throws UnknownBuilderException {
		if (this.isRegistered(service)) {
			for (BuilderProvider provider : this.builderProviders) {
				if (provider.getService().isAssignableFrom(service))
					return provider;
			}
		}

		throw new UnknownBuilderException(service);
	}

	/**
	 * Gets a new builder instance for the given service class.
	 *
	 * @param service Class type to get.
	 * @param <T> Type of service.
	 * @param <B> Type of builder class.
	 * @return Builder instance for the given class.
	 * @throws UnknownBuilderException When the given service class does not have a registered builder.
	 * @see #isRegistered(Class)
	 */
	public final <T, B extends CoreBuilder> B createBuilder(Class<T> service) throws UnknownBuilderException {
		return (B)this.getBuilderProvider(service).createBuilder();
	}

	/**
	 * Registers a builder for the given service class.
	 *
	 * @param service Service class.
	 * @param builder Builder class.
	 * @param <T> Type of service.
	 * @param <B> Type of builder class.
	 * @throws RegisteredBuilderException When the given service class already has a registered builder.
	 */
	public final <T, B extends CoreBuilder> void provide(Class<T> service, Class<B> builder) throws RegisteredBuilderException {
		Preconditions.checkNotNull(builder, "Builder cannot be NULL");

		if (this.isRegistered(service))
			throw new RegisteredBuilderException(service);


		ParameterizedType superClass = (ParameterizedType) builder.getGenericSuperclass();
		Class<?> tClass = (Class<T>) superClass.getActualTypeArguments()[0];

		if (!tClass.isAssignableFrom(service))
			throw new InvalidBuilderException(service, builder);

		this.builderProviders.add(new BuilderProvider(service, builder));
	}

}