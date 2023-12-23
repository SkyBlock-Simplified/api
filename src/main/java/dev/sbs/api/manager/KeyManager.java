package dev.sbs.api.manager;

import dev.sbs.api.manager.exception.InsufficientModeException;
import dev.sbs.api.manager.exception.RegisteredReferenceException;
import dev.sbs.api.manager.exception.UnknownReferenceException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;

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

    @Override
    public final @NotNull V get(@NotNull K identifier) throws UnknownReferenceException {
        return super.get(identifier);
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
