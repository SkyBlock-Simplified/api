package dev.sbs.api.minecraft.nbt.json;

import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.tags.Tag;

import java.io.IOException;

/**
 * Interface for JSON (de)serialization. Must be implemented if your tag will be JSON (de)serializable.
 */
public interface JsonSerializable {

    /**
     * Deserializes this tag from a give {@code JsonObject}.
     *
     * @param json     the {@code JsonObject} to be deserialized.
     * @param registry the {@link TagTypeRegistry} to be used in deserialization.
     * @return this (literally {@code return this;} after deserialization).
     * @throws IOException if any I/O error occurs.
     */
    default Tag<?> fromJson(JsonObject json, TagTypeRegistry registry) throws IOException {
        return this.fromJson(json, 0, registry);
    }

    /**
     * Deserializes this tag from a give {@code JsonObject}.
     *
     * @param json     the {@code JsonObject} to be deserialized.
     * @param depth    the current depth of the NBT data structure.
     * @param registry the {@link TagTypeRegistry} to be used in deserialization.
     * @return this (literally {@code return this;} after deserialization).
     * @throws IOException if any I/O error occurs.
     */
    Tag<?> fromJson(JsonObject json, int depth, TagTypeRegistry registry) throws IOException;

    /**
     * Serializes this tag into a GSON {@code JsonObject}.
     *
     * @param registry the {@link TagTypeRegistry} to be used in serialization.
     * @return the serialized {@code JsonObject}.
     * @throws IOException if any I/O error occurs.
     */
    default JsonObject toJson(TagTypeRegistry registry) throws IOException {
        return this.toJson(0, registry);
    }

    /**
     * Serializes this tag into a GSON {@code JsonObject}.
     *
     * @param depth    the current depth of the NBT data structure.
     * @param registry the {@link TagTypeRegistry} to be used in serialization.
     * @return the serialized {@code JsonObject}.
     * @throws IOException if any I/O error occurs.
     */
    JsonObject toJson(int depth, TagTypeRegistry registry) throws IOException;

}
