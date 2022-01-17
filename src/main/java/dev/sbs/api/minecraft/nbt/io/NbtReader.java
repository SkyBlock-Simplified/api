package dev.sbs.api.minecraft.nbt.io;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.minecraft.nbt.exception.NbtException;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.DataInput;
import java.io.IOException;

/**
 * Used to read root {@link CompoundTag}s using a certain {@link TagTypeRegistry}.
 */
@AllArgsConstructor
public class NbtReader {

    private @NonNull TagTypeRegistry typeRegistry;

    /**
     * Reads a root {@link CompoundTag} from a {@link DataInput} stream.
     *
     * @param dataInput the stream to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws NbtException if any I/O error occurs.
     */
    public CompoundTag fromStream(@NonNull DataInput dataInput) throws NbtException {
        try {
            if (dataInput.readByte() != TagType.COMPOUND.getId())
                throw new IOException("Root tag in NBT structure must be a compound tag.");

            CompoundTag result = new CompoundTag(true);
            result.setName(dataInput.readUTF());
            result.read(dataInput, 0, this.typeRegistry);

            return result;
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }

    /**
     * Returns the {@link TagTypeRegistry} currently in use by this reader.
     *
     * @return the {@link TagTypeRegistry} currently in use by this reader.
     */
    @NonNull
    public TagTypeRegistry getTypeRegistry() {
        return this.typeRegistry;
    }

    /**
     * Sets the {@link TagTypeRegistry} currently in use by this reader. Used to utilise custom-made tag types.
     *
     * @param typeRegistry the new {@link TagTypeRegistry} to be set.
     */
    public void setTypeRegistry(@NonNull TagTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

}
