package dev.sbs.api.minecraft.nbt.io;

import com.google.gson.JsonObject;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.minecraft.nbt.exception.NbtException;
import dev.sbs.api.minecraft.nbt.tags.TagRegistry;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.helper.DataUtil;
import dev.sbs.api.util.helper.PrimitiveUtil;
import lombok.Cleanup;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Used to read root {@link CompoundTag CompoundTags} using a certain {@link TagRegistry}.
 */
public interface NbtReader {

    /**
     * Decodes an NBT data structure (root {@link CompoundTag}) from a Base64 encoded string.
     *
     * @param encoded the encoded Base64 string to decode.
     * @return the decoded root {@link CompoundTag}.
     * @throws NbtException if any I/O error occurs.
     */
    default @NotNull CompoundTag fromBase64(@NotNull String encoded) throws NbtException {
        return this.fromByteArray(DataUtil.decode(encoded));
    }

    /**
     * Reads an NBT data structure (root {@link CompoundTag}) from a {@code Byte[]} array.
     *
     * @param bytes the {@code Byte[]} array to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws NbtException if any I/O error occurs.
     */
    default @NotNull CompoundTag fromByteArray(Byte[] bytes) throws NbtException {
        return this.fromByteArray(PrimitiveUtil.unwrap(bytes));
    }

    /**
     * Reads an NBT data structure (root {@link CompoundTag}) from a {@code byte[]} array.
     *
     * @param bytes the {@code byte[]} array to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws NbtException if any I/O error occurs.
     */
    default @NotNull CompoundTag fromByteArray(byte[] bytes) throws NbtException {
        try {
            @Cleanup ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            return this.fromStream(byteArrayInputStream);
        } catch (Exception unreported) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(unreported)
                .withMessage(unreported.getMessage())
                .build();
        }
    }

    /**
     * Reads an NBT data structure (root {@link CompoundTag}) from a {@link File}.
     *
     * @param file the file to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws NbtException if any I/O error occurs.
     */
    default @NotNull CompoundTag fromFile(@NotNull File file) throws NbtException {
        try {
            @Cleanup FileInputStream fileInputStream = new FileInputStream(file);
            return this.fromStream(fileInputStream);
        } catch (IOException ex) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ex)
                .withMessage(ex.getMessage())
                .build();
        }
    }

    /**
     * Deserializes an NBT data structure (root {@link CompoundTag}) from a JSON {@link File}.
     *
     * @param file the JSON file to read from.
     * @return the root {@link CompoundTag} deserialized from the JSON file.
     * @throws NbtException if any I/O error occurs.
     */
    default @NotNull CompoundTag fromJson(@NotNull File file) throws NbtException {
        try {
            @Cleanup FileReader reader = new FileReader(file);
            return new CompoundTag(true).fromJson(SimplifiedApi.getGson().fromJson(reader, JsonObject.class), 0, this.getRegistry());
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }

    /**
     * Deserializes an NBT data structure (root {@link CompoundTag}) from a JSON {@link File}.
     *
     * @param json the JSON string to read from.
     * @return the root {@link CompoundTag} deserialized from the JSON file.
     * @throws NbtException if any I/O error occurs.
     */
    default @NotNull CompoundTag fromJson(@NotNull String json) throws NbtException {
        try {
            return new CompoundTag(true).fromJson(SimplifiedApi.getGson().fromJson(json, JsonObject.class), 0, this.getRegistry());
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }

    /**
     * Deserializes an NBT data structure (root {@link CompoundTag}) from a JSON {@link File}.
     *
     * @param json the JSON object to read from.
     * @return the root {@link CompoundTag} deserialized from the JSON file.
     * @throws NbtException if any I/O error occurs.
     */
    default @NotNull CompoundTag fromJson(@NotNull JsonObject json) throws NbtException {
        try {
            return new CompoundTag(true).fromJson(SimplifiedApi.getGson().fromJson(json, JsonObject.class), 0, this.getRegistry());
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }

    /**
     * Reads an NBT data structure (root {@link CompoundTag}) from a {@link DataInput} stream.
     *
     * @param inputStream the stream to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws NbtException if any I/O error occurs.
     */
    default @NotNull CompoundTag fromStream(@NotNull InputStream inputStream) throws NbtException {
        try {
            switch (DataUtil.getCompression(inputStream)) {
                case GZIP:
                    inputStream = new GZIPInputStream(inputStream);
                    break;
                case ZLIB:
                    inputStream = new InflaterInputStream(inputStream);
                    break;
            }

            @Cleanup DataInputStream dataInput = new DataInputStream(new BufferedInputStream(inputStream));
            return this.fromStream((DataInput) dataInput);
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }

    /**
     * Reads a root {@link CompoundTag} from a {@link DataInput} stream.
     *
     * @param dataInput the stream to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws NbtException if any I/O error occurs.
     */
    default @NotNull CompoundTag fromStream(@NotNull DataInput dataInput) throws NbtException {
        try {
            if (dataInput.readByte() != TagType.COMPOUND.getId())
                throw new IOException("Root tag in NBT structure must be a compound tag.");

            CompoundTag result = new CompoundTag(true);
            result.setName(dataInput.readUTF());
            result.read(dataInput, 0, this.getRegistry());

            return result;
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }
    
    @NotNull TagRegistry getRegistry();

}
