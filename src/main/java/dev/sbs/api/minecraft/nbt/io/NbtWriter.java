package dev.sbs.api.minecraft.nbt.io;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.minecraft.nbt.exception.NbtException;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.util.helper.StringUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Used to write root {@link CompoundTag}s using a certain {@link TagTypeRegistry}.
 */
@AllArgsConstructor
public class NbtWriter {

    private @NonNull TagTypeRegistry typeRegistry;

    /**
     * Writes the given root {@link CompoundTag} to a {@link DataOutput} stream.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @param output   the stream to write to.
     * @throws NbtException if any I/O error occurs.
     */
    public void toStream(@NonNull CompoundTag compound, @NonNull DataOutput output) throws NbtException {
        try {
            output.writeByte(TagType.COMPOUND.getId());
            output.writeUTF(StringUtil.isEmpty(compound.getName()) ? "" : compound.getName());
            compound.write(output, 0, this.typeRegistry);
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }

    /**
     * Returns the {@link TagTypeRegistry} currently in use by this writer.
     *
     * @return the {@link TagTypeRegistry} currently in use by this writer.
     */
    @NonNull
    public TagTypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    /**
     * Sets the {@link TagTypeRegistry} currently in use by this writer. Used to utilise custom-made tag types.
     *
     * @param typeRegistry the new {@link TagTypeRegistry} to be set.
     */
    public void setTypeRegistry(@NonNull TagTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

}
