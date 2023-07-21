package dev.sbs.api.manager;

import dev.sbs.api.manager.exception.RegisteredReferenceException;
import dev.sbs.api.manager.exception.UnknownReferenceException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Manager for containing permanent key-value pairs.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
public abstract class Manager<K, V> {

    protected final transient @NotNull ConcurrentMap<K, V> ref = Concurrent.newMap();
    private final transient @NotNull BiFunction<Map.Entry<K, V>, K, Boolean> keyMatcher;

    protected Manager(@NotNull BiFunction<Map.Entry<K, V>, K, Boolean> entryMatcher) {
        this.keyMatcher = entryMatcher;
    }

    /**
     * Registers a new key-value pair.
     *
     * @param identifier Reference identifier.
     * @param value Key value.
     * @throws RegisteredReferenceException When the given reference is already registered.
     */
    public final void add(@NotNull K identifier, @NotNull V value) throws RegisteredReferenceException {
        if (this.isRegistered(identifier))
            throw SimplifiedException.of(RegisteredReferenceException.class)
                .withMessage(RegisteredReferenceException.getMessage(identifier))
                .build();

        this.ref.put(identifier, value);
    }

    /**
     * Gets the value stored for the given identifier.
     *
     * @param identifier Reference identifier.
     * @throws UnknownReferenceException When the given identifier is not registered.
     * @see #isRegistered
     */
    public final V get(@NotNull K identifier) throws UnknownReferenceException {
        return this.ref.stream()
            .filter(entry -> this.keyMatcher.apply(entry, identifier))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElseThrow(() -> SimplifiedException.of(UnknownReferenceException.class)
                .withMessage(UnknownReferenceException.getMessage(identifier))
                .build()
            );
    }

    /**
     * Checks if the given key has a registered instance.
     *
     * @param identifier Reference identifier.
     */
    public final boolean isRegistered(@NotNull K identifier) {
        return this.ref.stream().anyMatch(entry -> this.keyMatcher.apply(entry, identifier));
    }

    /**
     * Adds or updates a key-value pair.
     *
     * @param identifier Reference identifier.
     * @param newValue New key value.
     */
    public final void replace(@NotNull K identifier, @NotNull V newValue) {
        this.ref.put(identifier, newValue);
    }

    /**
     * Updates an existing key-value pair.
     *
     * @param identifier Reference identifier.
     * @param newValue New key value.
     * @throws UnknownReferenceException When the given identifier is not registered.
     */
    public final void update(@NotNull K identifier, @NotNull V newValue) throws UnknownReferenceException {
        if (!this.isRegistered(identifier))
            throw SimplifiedException.of(UnknownReferenceException.class)
                .withMessage(UnknownReferenceException.getMessage(identifier))
                .build();

        this.ref.put(identifier, newValue);
    }

}
