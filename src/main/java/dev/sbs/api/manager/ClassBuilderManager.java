package dev.sbs.api.manager;

import dev.sbs.api.manager.exception.InsufficientModeException;
import dev.sbs.api.manager.exception.InvalidReferenceException;
import dev.sbs.api.manager.exception.RegisteredReferenceException;
import dev.sbs.api.manager.exception.UnknownReferenceException;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.reflection.exception.ReflectionException;
import dev.sbs.api.util.builder.ClassCompiler;
import org.jetbrains.annotations.NotNull;

/**
 * Manager for class builders that assist in constructing classes.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
@SuppressWarnings({ "unchecked" })
public class ClassBuilderManager extends Manager<Class<?>, Class<? extends ClassCompiler<?>>> {

    public ClassBuilderManager() {
        this(Mode.NORMAL);
    }

    public ClassBuilderManager(@NotNull Mode mode) {
        super((entry, service) -> service.isAssignableFrom(entry.getKey()), mode);
    }

    /**
     * Registers a builder for the given service class.
     *
     * @param service Service class.
     * @param builder Builder class.
     * @param <T>     Type of service.
     * @param <B>     Type of builder class.
     * @throws RegisteredReferenceException When the given service class already has a registered builder.
     */
    public final <T, B extends ClassCompiler<T>> void add(@NotNull Class<T> service, @NotNull Class<B> builder) throws RegisteredReferenceException {
        if (this.isRegistered(service))
            throw new RegisteredReferenceException(service);

        try {
            Class<?> tClass = Reflection.getSuperClass(builder);

            if (tClass.isAssignableFrom(service)) {
                this.ref.put(service, builder);
                return;
            }
        } catch (ReflectionException ignore) { }

        throw new InvalidReferenceException(builder, service);
    }

    /**
     * Gets a new service instance using the provided builder class.
     *
     * @param service Class type to build.
     * @param <T>     Type of service.
     * @return Builder instance for the given class.
     * @throws UnknownReferenceException When the given service class does not have a registered builder.
     * @see #isRegistered(Class)
     */
    public final <T> @NotNull T build(@NotNull Class<T> service) throws UnknownReferenceException {
        Class<? extends ClassCompiler<?>> builder = super.get(service);

        if (ClassCompiler.class.isAssignableFrom(builder))
            return ((ClassCompiler<T>) Reflection.of(builder).newInstance()).build(service);

        throw new InvalidReferenceException(service.getName(), builder);
    }

    /**
     * Gets a builder instance for the provided service class.
     *
     * @param service Class type to build.
     * @return Builder instance for the given class.
     * @throws UnknownReferenceException When the given service class does not have a registered builder.
     * @see #isRegistered(Class)
     */
    public final <T, B extends ClassCompiler<T>> @NotNull B get(@NotNull Class<T> service) throws UnknownReferenceException {
        return (B) Reflection.of(super.get(service)).newInstance();
    }

    /**
     * Removes an instance for the given service class.
     *
     * @param service Service class.
     * @param <T> Type of service.
     * @throws UnknownReferenceException When the given service class is not registered.
     * @throws InsufficientModeException When the mode isn't {@link Mode#ALL}.
     */
    public final <T> void remove(@NotNull Class<T> service) throws InsufficientModeException, UnknownReferenceException {
        super.remove(service);
    }

    /**
     * Updates an instance for the given service class.
     *
     * @param service Service class.
     * @param builder Builder class.
     * @param <T> Type of service.
     * @param <B> Type of builder.
     * @throws UnknownReferenceException When the given service class is not registered.
     * @throws InsufficientModeException When the mode isn't {@link Mode#UPDATE} or higher.
     */
    public final <T, B extends ClassCompiler<T>> void update(@NotNull Class<T> service, @NotNull Class<B> builder) throws InsufficientModeException, UnknownReferenceException {
        super.update(service, builder);
    }

}
