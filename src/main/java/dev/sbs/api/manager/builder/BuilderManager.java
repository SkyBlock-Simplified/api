package dev.sbs.api.manager.builder;

import com.google.common.base.Preconditions;
import dev.sbs.api.SimplifiedException;
import dev.sbs.api.manager.Manager;
import dev.sbs.api.manager.builder.exception.InvalidBuilderException;
import dev.sbs.api.manager.builder.exception.RegisteredBuilderException;
import dev.sbs.api.manager.builder.exception.UnknownBuilderException;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.reflection.exception.ReflectionException;
import dev.sbs.api.util.builder.Builder;
import dev.sbs.api.util.builder.ClassBuilder;
import dev.sbs.api.util.builder.CoreBuilder;

/**
 * Manager for builders that assist in constructing classes.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
@SuppressWarnings({ "unchecked" })
public class BuilderManager extends Manager<BuilderProvider> {

    /**
     * Registers a builder for the given service class.
     *
     * @param service Service class.
     * @param builder Builder class.
     * @param <T>     Type of service.
     * @param <B>     Type of builder class.
     * @throws RegisteredBuilderException When the given service class already has a registered builder.
     */
    public final <T, B extends CoreBuilder> void add(Class<T> service, Class<B> builder) throws RegisteredBuilderException {
        Preconditions.checkNotNull(builder, "Builder cannot be NULL!");

        if (this.isRegistered(service))
            throw SimplifiedException.builder(RegisteredBuilderException.class)
                .setMessage("Builder ''{0}'' is already registered", service.getName())
                .build();

        try {
            Class<?> tClass = Reflection.getSuperClass(builder);

            if (tClass.isAssignableFrom(service)) {
                super.providers.add(new BuilderProvider(service, builder));
                return;
            }
        } catch (ReflectionException ignore) {
        }

        throw SimplifiedException.builder(InvalidBuilderException.class)
            .setMessage("Builder ''{0}'' does not build instances of ''{1}''", builder.getName(), service.getName())
            .build();
    }

    /**
     * Gets a new service instance using the provided builder class.
     *
     * @param service Class type to build.
     * @param <T>     Type of service.
     * @return Builder instance for the given class.
     * @throws UnknownBuilderException When the given service class does not have a registered builder.
     * @see #isRegistered(Class)
     */
    public final <T> T build(Class<T> service) throws UnknownBuilderException {
        BuilderProvider provider = this.getProvider(service);

        if (ClassBuilder.class.isAssignableFrom(provider.getBuilder()))
            return ((ClassBuilder<T>) provider.newInstance()).build(service);

        throw SimplifiedException.builder(InvalidBuilderException.class)
            .setMessage("Builder ''{0}'' does not build instances of ''{1}''", service.getName(), provider.getBuilder().getName())
            .build();
    }

    /**
     * Gets a builder instance for the provided service class.
     *
     * @param service Class type to build.
     * @param <T>     Type of service.
     * @return Builder instance for the given class.
     * @throws UnknownBuilderException When the given service class does not have a registered builder.
     * @see #isRegistered(Class)
     */
    public final <T, B extends Builder<T>> B get(Class<?> service) throws UnknownBuilderException {
        BuilderProvider provider = this.getProvider(service);

        if (Builder.class.isAssignableFrom(provider.getBuilder()))
            return provider.newInstance();

        throw SimplifiedException.builder(InvalidBuilderException.class)
            .setMessage("Builder ''{0}'' does not build instances of ''{1}''", service.getName(), provider.getBuilder().getName())
            .build();
    }

    /**
     * Gets the builder provider for the given service class.
     *
     * @param service Class type to get.
     * @param <T>     Type of service.
     * @return Builder provider for the given class.
     * @throws UnknownBuilderException When the given service class does not have a registered builder.
     * @see #isRegistered(Class)
     */
    @Override
    public final <T> BuilderProvider getProvider(Class<T> service) throws UnknownBuilderException {
        if (this.isRegistered(service)) {
            for (BuilderProvider provider : super.providers) {
                if (provider.getService().isAssignableFrom(service))
                    return provider;
            }
        }

        throw SimplifiedException.builder(UnknownBuilderException.class)
            .setMessage("Builder ''{0}'' has not been registered", service.getName())
            .build();
    }

}
