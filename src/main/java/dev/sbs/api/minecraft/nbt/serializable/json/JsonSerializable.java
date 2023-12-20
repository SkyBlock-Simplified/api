package dev.sbs.api.minecraft.nbt.serializable.json;

import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.TagRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Interface for JSON (de)serialization.
 * <br><br>
 * Must be implemented if your tag will be JSON (de)serializable.
 */
@SuppressWarnings("unused")
public interface JsonSerializable {

    /**
     * Deserializes this tag from a given {@code JsonObject}.
     *
     * @param json The {@code JsonObject} to be deserialized.
     * @return this (literally {@code return this;} after deserialization).
     * @throws IOException if any I/O error occurs.
     */
    default @NotNull Tag<?> fromJson(@NotNull JsonObject json) throws IOException {
        return this.fromJson(json, 0);
    }

    /**
     * Deserializes this tag from a given {@code JsonObject}.
     *
     * @param json The {@code JsonObject} to be deserialized.
     * @param depth The current depth of the NBT data structure.
     * @return this (literally {@code return this;} after deserialization).
     * @throws IOException if any I/O error occurs.
     */
    default @NotNull Tag<?> fromJson(@NotNull JsonObject json, int depth) throws IOException {
        throw new UnsupportedOperationException(String.format("JSON reading is not implemented from this method for Tag ('%s')!", this.getClass().getSimpleName()));
    }

    /**
     * Deserializes this tag from a given {@code JsonObject}.
     *
     * @param json The {@code JsonObject} to be deserialized.
     * @return this (literally {@code return this;} after deserialization).
     * @throws IOException if any I/O error occurs.
     */
    default @NotNull Tag<?> fromJson(@NotNull JsonObject json, @NotNull TagRegistry registry) throws IOException {
        return this.fromJson(json, 0, registry);
    }

    /**
     * Deserializes this tag from a given {@code JsonObject}.
     *
     * @param json The {@code JsonObject} to be deserialized.
     * @param depth The current depth of the NBT data structure.
     * @return this (literally {@code return this;} after deserialization).
     * @throws IOException if any I/O error occurs.
     */
    default @NotNull Tag<?> fromJson(@NotNull JsonObject json, int depth, @NotNull TagRegistry registry) throws IOException {
        throw new UnsupportedOperationException(String.format("JSON reading is not implemented from this method for Tag ('%s')!", this.getClass().getSimpleName()));
    }

    /**
     * Serializes this tag into a GSON {@code JsonObject}.
     *
     * @return The serialized {@code JsonObject}.
     * @throws IOException if any I/O error occurs.
     */
    default @NotNull JsonObject toJson() throws IOException {
        return this.toJson(0);
    }

    /**
     * Serializes this tag into a GSON {@code JsonObject}.
     *
     * @param depth The current depth of the NBT data structure.
     * @return The serialized {@code JsonObject}.
     * @throws IOException if any I/O error occurs.
     */
    default JsonObject toJson(int depth) throws IOException {
        throw new UnsupportedOperationException(String.format("JSON writing is not implemented from this method for Tag ('%s')!", this.getClass().getSimpleName()));
    }

}
