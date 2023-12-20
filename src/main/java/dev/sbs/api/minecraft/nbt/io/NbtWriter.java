package dev.sbs.api.minecraft.nbt.io;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.minecraft.nbt.exception.NbtException;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.helper.DataUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Cleanup;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Used to write root {@link CompoundTag CompoundTags}.
 */
public interface NbtWriter {

    /**
     * Converts the given root {@link CompoundTag} to a Base64 encoded string.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @return the resulting Base64 encoded string.
     * @throws NbtException if any I/O error occurs.
     */
    default @NotNull String toBase64(@NotNull CompoundTag compound) throws NbtException {
        return DataUtil.encodeToString(this.toByteArray(compound));
    }

    /**
     * Converts the given root {@link CompoundTag} to a {@code byte[]} array.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @return the resulting {@code byte[]} array.
     * @throws NbtException if any I/O error occurs.
     */
    default byte[] toByteArray(@NotNull CompoundTag compound) throws NbtException {
        try {
            @Cleanup ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            @Cleanup DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(byteArrayOutputStream));
            this.toStream(compound, dataOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception unreported) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(unreported)
                .withMessage(unreported.getMessage())
                .build();
        }
    }

    /**
     * Writes the given root {@link CompoundTag} to a {@link File} with no compression.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @param file     the file to write to.
     * @throws NbtException if any I/O error occurs.
     */
    default void toFile(@NotNull CompoundTag compound, @NotNull File file) throws NbtException {
        this.toFile(compound, file, DataUtil.CompressionType.NONE);
    }

    /**
     * Writes the given root {@link CompoundTag} to a {@link File} using a certain {@link DataUtil.CompressionType}.
     *
     * @param compound    the NBT structure to write, contained within a {@link CompoundTag}.
     * @param file        the file to write to.
     * @param compression the compression to be applied.
     * @throws NbtException if any I/O error occurs.
     */
    default void toFile(@NotNull CompoundTag compound, @NotNull File file, @NotNull DataUtil.CompressionType compression) throws NbtException {
        try {
            @Cleanup OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

            outputStream = switch (compression) {
                case GZIP -> new GZIPOutputStream(outputStream);
                case ZLIB -> new DeflaterOutputStream(outputStream);
                default -> outputStream;
            };

            this.toStream(compound, new DataOutputStream(outputStream));
        } catch (IOException ex) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ex)
                .withMessage(ex.getMessage())
                .build();
        }

    }

    /**
     * Serializes the given root {@link CompoundTag} to a JSON {@link File}.
     *
     * @param compound the NBT structure to serialize to JSON, contained within a {@link CompoundTag}.
     * @param file     the JSON file to write to.
     * @throws NbtException if any I/O error occurs.
     */
    default void toJson(@NotNull CompoundTag compound, @NotNull File file) throws NbtException {
        try {
            @Cleanup FileWriter writer = new FileWriter(file);
            SimplifiedApi.getGson().toJson(compound.toJson(), writer);
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }

    /**
     * Serializes the given root {@link CompoundTag} to a SNBT (Stringified NBT).
     *
     * @param compound the NBT structure to serialize to SNBT, contained within a {@link CompoundTag}.
     * @return the serialized SNBT string.
     */
    default @NotNull String toSnbt(@NotNull CompoundTag compound) {
        return compound.toSnbt(0, this.getSnbtConfig());
    }

    /**
     * Writes the given root {@link CompoundTag} to a {@link DataOutput} stream.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @param output   the stream to write to.
     * @throws NbtException if any I/O error occurs.
     */
    default void toStream(@NotNull CompoundTag compound, @NotNull DataOutput output) throws NbtException {
        try {
            output.writeByte(TagType.COMPOUND.getId());
            output.writeUTF(StringUtil.isEmpty(compound.getName()) ? "" : compound.getName());
            compound.write(output, 0);
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }

    @NotNull SnbtConfig getSnbtConfig();

}
