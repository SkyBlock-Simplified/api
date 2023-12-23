package dev.sbs.api.minecraft.nbt.tags.impl;

import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.TagRegistry;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.IOException;

/**
 * An abstract NBT tag that implements {@link #read} with {@link TagRegistry}.
 */
@Getter
public abstract class RegistryTag<T> extends Tag<T> {

    protected RegistryTag(byte typeId, @Nullable String name, @NotNull T value, boolean modifiable) {
        super(typeId, name, value, modifiable);
    }

    @Override
    public final @NotNull Tag<?> fromJson(@NotNull JsonObject json) throws IOException {
        return this.fromJson(json, 0);
    }

    @Override
    public final @NotNull Tag<?> fromJson(@NotNull JsonObject json, int depth) throws IOException {
        throw new UnsupportedOperationException(String.format("JSON reading is not supported from this method for Tag ('%s')! TagRegistry is required", this.getClass().getSimpleName()));
    }

    /**
     * Deserializes this tag from a given {@code JsonObject}.
     *
     * @param json The {@code JsonObject} to be deserialized.
     * @return this (literally {@code return this;} after deserialization).
     * @throws IOException if any I/O error occurs.
     */
    public final @NotNull Tag<?> fromJson(@NotNull JsonObject json, @NotNull TagRegistry registry) throws IOException {
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
    public abstract @NotNull Tag<?> fromJson(@NotNull JsonObject json, int depth, @NotNull TagRegistry registry) throws IOException;

    @Override
    public final @NotNull Tag<?> read(@NotNull DataInput input, int depth) throws IOException {
        throw new UnsupportedOperationException(String.format("TAG reading is not supported from this method for Tag ('%s')! TagRegistry is required", this.getClass().getSimpleName()));
    }

    /**
     * Reads this tag from a {@link DataInput} stream using the {@link TagRegistry}.
     *
     * @param input The stream to read from.
     * @param depth The current depth of the NBT data structure.
     * @param registry The {@link TagRegistry} to be used in reading.
     * @return this (literally {@code return this;} after reading).
     * @throws IOException if any I/O error occurs.
     */
    public abstract @NotNull Tag<?> read(@NotNull DataInput input, int depth, @NotNull TagRegistry registry) throws IOException;

}