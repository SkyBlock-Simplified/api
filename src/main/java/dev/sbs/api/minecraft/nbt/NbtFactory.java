package dev.sbs.api.minecraft.nbt;

import com.google.gson.JsonObject;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.minecraft.nbt.exception.NbtException;
import dev.sbs.api.minecraft.nbt.io.NbtReader;
import dev.sbs.api.minecraft.nbt.io.NbtWriter;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.util.CompressionType;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.helper.DataUtil;
import dev.sbs.api.util.helper.PrimitiveUtil;
import lombok.Cleanup;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Standard interface for reading and writing NBT data structures.
 */
public class NbtFactory {

    private final @NotNull NbtWriter writer;
    private final @NotNull NbtReader reader;
    private @NotNull TagTypeRegistry typeRegistry;
    private @NotNull SnbtConfig snbtConfig;

    /**
     * Constructs an instance of this class using a default {@link TagTypeRegistry} (supporting the standard 12 tag types).
     */
    public NbtFactory() {
        this(new TagTypeRegistry());
    }

    /**
     * Constructs an instance of this class using a given {@link TagTypeRegistry}, with a default SnbtConfig.
     *
     * @param typeRegistry the tag type registry to be used, typically containing custom tag entries.
     */
    public NbtFactory(@NotNull TagTypeRegistry typeRegistry) {
        this(typeRegistry, new SnbtConfig());
    }

    /**
     * Constructs an instance of this class using a given {@link TagTypeRegistry} and {@link SnbtConfig}.
     *
     * @param typeRegistry the tag type registry to be used, typically containing custom tag entries.
     * @param snbtConfig   the SNBT config object to be used.
     */
    public NbtFactory(@NotNull TagTypeRegistry typeRegistry, @NotNull SnbtConfig snbtConfig) {
        this.typeRegistry = typeRegistry;
        this.snbtConfig = snbtConfig;
        this.writer = new NbtWriter(typeRegistry);
        this.reader = new NbtReader(typeRegistry);
    }

    /**
     * Decodes an NBT data structure (root {@link CompoundTag}) from a Base64 encoded string.
     *
     * @param encoded the encoded Base64 string to decode.
     * @return the decoded root {@link CompoundTag}.
     * @throws NbtException if any I/O error occurs.
     */
    public CompoundTag fromBase64(@NotNull String encoded) throws NbtException {
        return this.fromByteArray(DataUtil.decode(encoded));
    }

    /**
     * Reads an NBT data structure (root {@link CompoundTag}) from a {@code Byte[]} array.
     *
     * @param bytes the {@code Byte[]} array to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws NbtException if any I/O error occurs.
     */
    public CompoundTag fromByteArray(Byte[] bytes) throws NbtException {
        return this.fromByteArray(PrimitiveUtil.unwrap(bytes));
    }

    /**
     * Reads an NBT data structure (root {@link CompoundTag}) from a {@code byte[]} array.
     *
     * @param bytes the {@code byte[]} array to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws NbtException if any I/O error occurs.
     */
    public CompoundTag fromByteArray(byte[] bytes) throws NbtException {
        try {
            @Cleanup ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            return this.fromStream(byteArrayInputStream);
        } catch (Exception ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
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
    public CompoundTag fromFile(@NotNull File file) throws NbtException {
        try {
            @Cleanup FileInputStream fileInputStream = new FileInputStream(file);
            return this.fromStream(fileInputStream);
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
     * @param file the JSON file to read from.
     * @return the root {@link CompoundTag} deserialized from the JSON file.
     * @throws NbtException if any I/O error occurs.
     */
    public CompoundTag fromJson(@NotNull File file) throws NbtException {
        try {
            @Cleanup FileReader reader = new FileReader(file);
            return new CompoundTag(true).fromJson(SimplifiedApi.getGson().fromJson(reader, JsonObject.class), 0, this.typeRegistry);
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
    public CompoundTag fromJson(@NotNull String json) throws NbtException {
        try {
            return new CompoundTag(true).fromJson(SimplifiedApi.getGson().fromJson(json, JsonObject.class), 0, this.typeRegistry);
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
    public CompoundTag fromJson(@NotNull JsonObject json) throws NbtException {
        try {
            return new CompoundTag(true).fromJson(SimplifiedApi.getGson().fromJson(json, JsonObject.class), 0, this.typeRegistry);
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
    public CompoundTag fromStream(@NotNull InputStream inputStream) throws NbtException {
        try {
            switch (DataUtil.getCompression(inputStream)) {
                case GZIP:
                    inputStream = new GZIPInputStream(inputStream);
                    break;
                case ZLIB:
                    inputStream = new InflaterInputStream(inputStream);
                    break;
            }

            return this.reader.fromStream(new DataInputStream(new BufferedInputStream(inputStream)));
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }

    /**
     * Converts the given root {@link CompoundTag} to a Base64 encoded string.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @return the resulting Base64 encoded string.
     * @throws NbtException if any I/O error occurs.
     */
    public String toBase64(@NotNull CompoundTag compound) throws NbtException {
        return DataUtil.encodeToString(this.toByteArray(compound));
    }

    /**
     * Converts the given root {@link CompoundTag} to a {@code byte[]} array.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @return the resulting {@code byte[]} array.
     * @throws NbtException if any I/O error occurs.
     */
    public byte[] toByteArray(@NotNull CompoundTag compound) throws NbtException {
        try {
            @Cleanup ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            @Cleanup DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(byteArrayOutputStream));
            this.toStream(compound, dataOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
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
    public void toFile(@NotNull CompoundTag compound, @NotNull File file) throws NbtException {
        this.toFile(compound, file, CompressionType.NONE);
    }

    /**
     * Writes the given root {@link CompoundTag} to a {@link File} using a certain {@link CompressionType}.
     *
     * @param compound    the NBT structure to write, contained within a {@link CompoundTag}.
     * @param file        the file to write to.
     * @param compression the compression to be applied.
     * @throws NbtException if any I/O error occurs.
     */
    public void toFile(@NotNull CompoundTag compound, @NotNull File file, @NotNull CompressionType compression) throws NbtException {
        try {
            @Cleanup OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

            switch (compression) {
                case GZIP:
                    outputStream = new GZIPOutputStream(outputStream);
                    break;
                case ZLIB:
                    outputStream = new DeflaterOutputStream(outputStream);
            }

            this.toStream(compound, new DataOutputStream(outputStream));
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
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
    public void toJson(@NotNull CompoundTag compound, @NotNull File file) throws NbtException {
        try {
            @Cleanup FileWriter writer = new FileWriter(file);
            SimplifiedApi.getGson().toJson(compound.toJson(0, this.typeRegistry), writer);
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
    public String toSnbt(@NotNull CompoundTag compound) {
        return compound.toSnbt(0, this.typeRegistry, this.snbtConfig);
    }

    /**
     * Writes the given root {@link CompoundTag} to a provided {@link DataOutput} stream.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @param output   the stream to write to.
     * @throws NbtException if any I/O error occurs.
     */
    public void toStream(@NotNull CompoundTag compound, @NotNull DataOutput output) throws NbtException {
        try {
            this.writer.toStream(compound, output);
        } catch (IOException ioException) {
            throw SimplifiedException.of(NbtException.class)
                .withCause(ioException)
                .withMessage(ioException.getMessage())
                .build();
        }
    }

    /**
     * Returns the {@link SnbtConfig} currently in use by this instance.
     *
     * @return the {@link SnbtConfig} currently in use by this instance.
     */
    @NotNull
    public SnbtConfig getSnbtConfig() {
        return this.snbtConfig;
    }

    /**
     * Returns the {@link TagTypeRegistry} currently in use by this instance.
     *
     * @return the {@link TagTypeRegistry} currently in use by this instance.
     */
    @NotNull
    public TagTypeRegistry getTypeRegistry() {
        return this.typeRegistry;
    }

    /**
     * Sets the {@link SnbtConfig} currently in use by this instance.
     *
     * @param snbtConfig the new {@link SnbtConfig} to be set.
     */
    public void setSnbtConfig(@NotNull SnbtConfig snbtConfig) {
        this.snbtConfig = snbtConfig;
    }

    /**
     * Sets the {@link TagTypeRegistry} currently in use by this instance. Used to utilise custom-made tag types.
     *
     * @param typeRegistry the new {@link TagTypeRegistry} to be set.
     */
    public void setTypeRegistry(@NotNull TagTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
        this.writer.setTypeRegistry(typeRegistry);
        this.reader.setTypeRegistry(typeRegistry);
    }

}
