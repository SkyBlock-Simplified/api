package dev.sbs.api.minecraft.nbt.tags;

import dev.sbs.api.minecraft.nbt.json.JsonSerializable;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.snbt.SnbtSerializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * An abstract NBT tag.
 */
@AllArgsConstructor
public abstract class Tag<T> implements SnbtSerializable, JsonSerializable {

    /**
     * Returns the name (key) of this tag.
     */
    @Getter
    protected String name;
    /**
     * Returns the value held by this tag.
     */
    @Getter
    @NonNull
    protected T value;
    @Setter
    protected TagTypeRegistry registry;
    private boolean updatable;

    /**
     * Returns a unique ID for this NBT tag type. 0 to 12 (inclusive) are reserved.
     *
     * @return a unique ID for this NBT tag type.
     */
    public abstract byte getTypeId();

    /**
     * Reads this tag from a {@link DataInput} stream.
     *
     * @param input    the stream to read from.
     * @param depth    the current depth of the NBT data structure.
     * @param registry the {@link TagTypeRegistry} to be used in reading.
     * @return this (literally {@code return this;} after reading).
     * @throws IOException if any I/O error occurs.
     */
    public abstract Tag<?> read(DataInput input, int depth, TagTypeRegistry registry) throws IOException;

    /**
     * Sets the name (key) of this tag.
     *
     * @param name the new name to be set.
     */
    public final void setName(String name) {
        if (this.updatable)
            this.name = name;
    }

    protected final void setNonUpdatable() {
        this.updatable = false;
    }

    /**
     * Sets the {@link T} value of this byte tag.
     *
     * @param value new {@link T} value to be set.
     */
    public final void setValue(@NonNull T value) {
        if (this.updatable)
            this.value = value;
    }

    /**
     * Writes this tag to a {@link DataOutput} stream.
     *
     * @param output   the stream to write to.
     * @param depth    the current depth of the NBT data structure.
     * @param registry the {@link TagTypeRegistry} to be used in writing.
     * @throws IOException if any I/O error occurs.
     */
    public abstract void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException;

    @Override
    public final String toString() {
        return this.toSnbt(0, new TagTypeRegistry(), new SnbtConfig());
    }

}
