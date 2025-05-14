package dev.sbs.api.manager;

import dev.sbs.api.manager.exception.InsufficientModeException;
import dev.sbs.api.manager.exception.RegisteredReferenceException;
import dev.sbs.api.manager.exception.UnknownReferenceException;
import dev.sbs.api.mutable.pair.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Manager for containing key-value associations.
 * <p>
 * This should be used in an API, and only once across all projects.
 */
public class KeyManager<K, V> extends Manager<K, V> {

    public KeyManager(@NotNull BiFunction<Map.Entry<K, V>, K, Boolean> keyMatcher) {
        this(keyMatcher, Mode.NORMAL);
    }

    public KeyManager(@NotNull BiFunction<Map.Entry<K, V>, K, Boolean> keyMatcher, @NotNull Mode mode) {
        super(keyMatcher, mode);
    }

    @Override
    public final void add(@NotNull K identifier, @NotNull V value) throws RegisteredReferenceException {
        super.add(identifier, value);
    }

    public final void add(@NotNull K identifier, @NotNull Optional<V> value) throws RegisteredReferenceException {
        value.ifPresent(v -> this.add(identifier, v));
    }

    public final void add(@NotNull Pair<K, Optional<V>> keypair) throws RegisteredReferenceException {
        keypair.getValue().ifPresent(value -> this.add(keypair.getKey(), value));
    }

    @Override
    public final @NotNull V get(@NotNull K identifier) throws UnknownReferenceException {
        return super.get(identifier);
    }

    @Override
    public final @NotNull Optional<V> getOptional(@NotNull K identifier) {
        return super.getOptional(identifier);
    }

    public final @NotNull Supplier<Optional<V>> getSupplier(@NotNull K identifier) {
        return () -> super.getOptional(identifier);
    }

    @Override
    public final void remove(@NotNull K identifier) throws InsufficientModeException, UnknownReferenceException {
        super.remove(identifier);
    }

    @Override
    public final void update(@NotNull K identifier, @NotNull V newValue) throws InsufficientModeException, UnknownReferenceException {
        super.update(identifier, newValue);
    }

}
