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

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract @NotNull Tag<?> fromJson(@NotNull JsonObject json, int depth, @NotNull TagRegistry registry) throws IOException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract @NotNull Tag<?> read(@NotNull DataInput input, int depth, @NotNull TagRegistry registry) throws IOException;

}