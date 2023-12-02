package dev.sbs.api.manager;

import dev.sbs.api.manager.exception.InsufficientModeException;
import dev.sbs.api.manager.exception.RegisteredReferenceException;
import dev.sbs.api.manager.exception.UnknownReferenceException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Manager for containing permanent key-value pairs.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
@RequiredArgsConstructor
public abstract class Manager<K, V> {

    protected final transient @NotNull ConcurrentMap<K, V> ref = Concurrent.newMap();
    private final transient @NotNull BiFunction<Map.Entry<K, V>, K, Boolean> keyMatcher;
    @Getter private final transient @NotNull Mode mode;

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
     * Removes all existing key-value pairs.
     *
     * @throws InsufficientModeException When the mode isn't {@link Mode#ALL}.
     */
    public final void clear() throws InsufficientModeException {
        if (this.getMode().getLevel() < Mode.ALL.getLevel())
            throw SimplifiedException.of(InsufficientModeException.class)
                .withMessage(InsufficientModeException.getMessage(this.getMode()))
                .build();

        this.ref.clear();
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
     * Removes an existing key-value pair.
     *
     * @param identifier Reference identifier.
     * @throws UnknownReferenceException When the given identifier is not registered.
     * @throws InsufficientModeException When the mode isn't {@link Mode#ALL}.
     */
    public final void remove(@NotNull K identifier) throws InsufficientModeException, UnknownReferenceException {
        if (!this.isRegistered(identifier)) {
            throw SimplifiedException.of(UnknownReferenceException.class)
                .withMessage(UnknownReferenceException.getMessage(identifier))
                .build();
        } else {
            if (this.getMode().getLevel() < Mode.ALL.getLevel())
                throw SimplifiedException.of(InsufficientModeException.class)
                    .withMessage(InsufficientModeException.getMessage(this.getMode()))
                    .build();
        }

        this.ref.remove(identifier);
    }

    /**
     * Updates an existing key-value pair.
     *
     * @param identifier Reference identifier.
     * @param newValue New key value.
     * @throws UnknownReferenceException When the given identifier is not registered.
     * @throws InsufficientModeException When the mode isn't {@link Mode#UPDATE} or higher.
     */
    public final void update(@NotNull K identifier, @NotNull V newValue) throws InsufficientModeException, UnknownReferenceException {
        if (!this.isRegistered(identifier)) {
            throw SimplifiedException.of(UnknownReferenceException.class)
                .withMessage(UnknownReferenceException.getMessage(identifier))
                .build();
        } else {
            if (this.getMode().getLevel() < Mode.UPDATE.getLevel())
                throw SimplifiedException.of(InsufficientModeException.class)
                    .withMessage(InsufficientModeException.getMessage(this.getMode()))
                    .build();
        }

        this.ref.put(identifier, newValue);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Mode {

        /**
         * The manager may only add new items to the registry.
         */
        NORMAL(0, false, false),
        /**
         * The manager may add new items to and update the registry.
         */
        UPDATE(0, true, false),
        /**
         * The manager add new items to, update and remove from the registry.
         */
        ALL(0, true, true);

        private final int level;
        private final boolean updateEnabled;
        private final boolean removeEnabled;

    }

}
