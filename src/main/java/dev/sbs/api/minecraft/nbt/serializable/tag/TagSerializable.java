package dev.sbs.api.minecraft.nbt.serializable.tag;

import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.TagRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Interface for Tag (de)serialization.
 * <br><br>
 * Must be implemented if your tag will be (de)serializable.
 */
@SuppressWarnings("unused")
public interface TagSerializable {

    /**
     * Reads this tag from a {@link DataInput} stream.
     *
     * @param input The stream to read from.
     * @param depth The current depth of the NBT data structure.
     * @return this (literally {@code return this;} after reading).
     * @throws IOException if any I/O error occurs.
     */
    default @NotNull Tag<?> read(@NotNull DataInput input, int depth) throws IOException {
        throw new UnsupportedOperationException(String.format("TAG reading is not implemented from this method for Tag ('%s')!", this.getClass().getSimpleName()));
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
    default @NotNull Tag<?> read(@NotNull DataInput input, int depth, @NotNull TagRegistry registry) throws IOException {
        throw new UnsupportedOperationException(String.format("TAG reading is not implemented from this method for Tag ('%s')!", this.getClass().getSimpleName()));
    }

    /**
     * Writes this tag to a {@link DataOutput} stream.
     *
     * @param output   the stream to write to.
     * @param depth    the current depth of the NBT data structure.
     * @throws IOException if any I/O error occurs.
     */
    default void write(@NotNull DataOutput output, int depth) throws IOException {
        throw new UnsupportedOperationException(String.format("NBT writing is not implemented from this method for Tag ('%s')!", this.getClass().getSimpleName()));
    }

}
