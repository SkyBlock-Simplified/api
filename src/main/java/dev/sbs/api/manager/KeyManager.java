package dev.sbs.api.manager;

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
        super(keyMatcher);
    }

}
