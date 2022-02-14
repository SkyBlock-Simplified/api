package dev.sbs.api.minecraft.nbt.io;

import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.util.helper.StringUtil;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Used to write root {@link CompoundTag}s using a certain {@link TagTypeRegistry}.
 */
@AllArgsConstructor
public class NbtWriter {

    private @NotNull TagTypeRegistry typeRegistry;

    /**
     * Writes the given root {@link CompoundTag} to a {@link DataOutput} stream.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @param output   the stream to write to.
     * @throws IOException if any I/O error occurs.
     */
    public void toStream(@NotNull CompoundTag compound, @NotNull DataOutput output) throws IOException {
        output.writeByte(TagType.COMPOUND.getId());
        output.writeUTF(StringUtil.isEmpty(compound.getName()) ? "" : compound.getName());
        compound.write(output, 0, this.typeRegistry);
    }

    /**
     * Returns the {@link TagTypeRegistry} currently in use by this writer.
     *
     * @return the {@link TagTypeRegistry} currently in use by this writer.
     */
    @NotNull
    public TagTypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    /**
     * Sets the {@link TagTypeRegistry} currently in use by this writer. Used to utilise custom-made tag types.
     *
     * @param typeRegistry the new {@link TagTypeRegistry} to be set.
     */
    public void setTypeRegistry(@NotNull TagTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

}
