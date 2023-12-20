package dev.sbs.api.minecraft.nbt.serializable.snbt;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for SNBT serialization.
 * <br><br>
 * Must be implemented if your tag will be SNBT writable.
 */
public interface SnbtSerializable {

    default @NotNull String toSnbt(@NotNull SnbtConfig config) {
        return this.toSnbt(0, config);
    }

    default @NotNull String toSnbt(int depth, @NotNull SnbtConfig config) {
        throw new UnsupportedOperationException(String.format("SNBT writing is not implemented from this method for Tag ('%s')!", this.getClass().getSimpleName()));
    }

}
