package dev.sbs.api.minecraft.nbt;

import com.google.gson.JsonObject;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.minecraft.nbt.io.NbtReader;
import dev.sbs.api.minecraft.nbt.io.NbtWriter;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.util.CompressionType;
import dev.sbs.api.util.Primitives;
import dev.sbs.api.util.helper.DataUtil;
import lombok.Cleanup;
import lombok.NonNull;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Standard interface for reading and writing NBT data structures.
 */
public class NbtFactory {

    private final @NonNull NbtWriter writer;
    private final @NonNull NbtReader reader;
    private @NonNull TagTypeRegistry typeRegistry;
    private @NonNull SnbtConfig snbtConfig;

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
    public NbtFactory(@NonNull TagTypeRegistry typeRegistry) {
        this(typeRegistry, new SnbtConfig());
    }

    /**
     * Constructs an instance of this class using a given {@link TagTypeRegistry} and {@link SnbtConfig}.
     *
     * @param typeRegistry the tag type registry to be used, typically containing custom tag entries.
     * @param snbtConfig   the SNBT config object to be used.
     */
    public NbtFactory(@NonNull TagTypeRegistry typeRegistry, @NonNull SnbtConfig snbtConfig) {
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
     * @throws IOException if any I/O error occurs.
     */
    public CompoundTag fromBase64(@NonNull String encoded) throws IOException {
        return this.fromByteArray(DataUtil.decode(encoded));
    }

    /**
     * Reads an NBT data structure (root {@link CompoundTag}) from a {@code Byte[]} array.
     *
     * @param bytes the {@code Byte[]} array to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws IOException if any I/O error occurs.
     */
    public CompoundTag fromByteArray(Byte[] bytes) throws IOException {
        return this.fromByteArray(Primitives.unwrap(bytes));
    }

    /**
     * Reads an NBT data structure (root {@link CompoundTag}) from a {@code byte[]} array.
     *
     * @param bytes the {@code byte[]} array to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws IOException if any I/O error occurs.
     */
    public CompoundTag fromByteArray(byte[] bytes) throws IOException {
        @Cleanup ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return this.fromStream(byteArrayInputStream);
    }

    /**
     * Reads an NBT data structure (root {@link CompoundTag}) from a {@link File}.
     *
     * @param file the file to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws IOException if any I/O error occurs.
     */
    public CompoundTag fromFile(@NonNull File file) throws IOException {
        @Cleanup FileInputStream fileInputStream = new FileInputStream(file);
        return this.fromStream(fileInputStream);
    }

    /**
     * Deserializes an NBT data structure (root {@link CompoundTag}) from a JSON {@link File}.
     *
     * @param file the JSON file to read from.
     * @return the root {@link CompoundTag} deserialized from the JSON file.
     * @throws IOException if any I/O error occurs.
     */
    public CompoundTag fromJson(@NonNull File file) throws IOException {
        @Cleanup FileReader reader = new FileReader(file);
        return new CompoundTag(true).fromJson(SimplifiedApi.getGson().fromJson(reader, JsonObject.class), 0, this.typeRegistry);
    }

    /**
     * Reads an NBT data structure (root {@link CompoundTag}) from a {@link DataInput} stream.
     *
     * @param inputStream the stream to read from.
     * @return the root {@link CompoundTag} read from the stream.
     * @throws IOException if any I/O error occurs.
     */
    public CompoundTag fromStream(@NonNull InputStream inputStream) throws IOException {
        switch (DataUtil.getCompression(inputStream)) {
            case GZIP:
                inputStream = new GZIPInputStream(inputStream);
                break;
            case ZLIB:
                inputStream = new InflaterInputStream(inputStream);
                break;
        }

        return this.reader.fromStream(new DataInputStream(new BufferedInputStream(inputStream)));
    }

    /**
     * Converts the given root {@link CompoundTag} to a Base64 encoded string.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @return the resulting Base64 encoded string.
     * @throws IOException if any I/O error occurs.
     */
    public String toBase64(@NonNull CompoundTag compound) throws IOException {
        return DataUtil.encodeToString(this.toByteArray(compound));
    }

    /**
     * Converts the given root {@link CompoundTag} to a {@code byte[]} array.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @return the resulting {@code byte[]} array.
     * @throws IOException if any I/O error occurs.
     */
    public byte[] toByteArray(@NonNull CompoundTag compound) throws IOException {
        @Cleanup ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        @Cleanup DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(byteArrayOutputStream));
        this.toStream(compound, dataOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Writes the given root {@link CompoundTag} to a {@link File} with no compression.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @param file     the file to write to.
     * @throws IOException if any I/O error occurs.
     */
    public void toFile(@NonNull CompoundTag compound, @NonNull File file) throws IOException {
        this.toFile(compound, file, CompressionType.NONE);
    }

    /**
     * Writes the given root {@link CompoundTag} to a {@link File} using a certain {@link CompressionType}.
     *
     * @param compound    the NBT structure to write, contained within a {@link CompoundTag}.
     * @param file        the file to write to.
     * @param compression the compression to be applied.
     * @throws IOException if any I/O error occurs.
     */
    public void toFile(@NonNull CompoundTag compound, @NonNull File file, @NonNull CompressionType compression) throws IOException {
        @Cleanup OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

        switch (compression) {
            case GZIP:
                outputStream = new GZIPOutputStream(outputStream);
                break;
            case ZLIB:
                outputStream = new DeflaterOutputStream(outputStream);
        }

        this.toStream(compound, new DataOutputStream(outputStream));
    }

    /**
     * Serializes the given root {@link CompoundTag} to a JSON {@link File}.
     *
     * @param compound the NBT structure to serialize to JSON, contained within a {@link CompoundTag}.
     * @param file     the JSON file to write to.
     * @throws IOException if any I/O error occurs.
     */
    public void toJson(@NonNull CompoundTag compound, @NonNull File file) throws IOException {
        @Cleanup FileWriter writer = new FileWriter(file);
        SimplifiedApi.getGson().toJson(compound.toJson(0, this.typeRegistry), writer);
    }

    /**
     * Serializes the given root {@link CompoundTag} to a SNBT (Stringified NBT).
     *
     * @param compound the NBT structure to serialize to SNBT, contained within a {@link CompoundTag}.
     * @return the serialized SNBT string.
     */
    public String toSnbt(@NonNull CompoundTag compound) {
        return compound.toSnbt(0, this.typeRegistry, this.snbtConfig);
    }

    /**
     * Writes the given root {@link CompoundTag} to a provided {@link DataOutput} stream.
     *
     * @param compound the NBT structure to write, contained within a {@link CompoundTag}.
     * @param output   the stream to write to.
     * @throws IOException if any I/O error occurs.
     */
    public void toStream(@NonNull CompoundTag compound, @NonNull DataOutput output) throws IOException {
        this.writer.toStream(compound, output);
    }

    /**
     * Returns the {@link SnbtConfig} currently in use by this instance.
     *
     * @return the {@link SnbtConfig} currently in use by this instance.
     */
    @NonNull
    public SnbtConfig getSnbtConfig() {
        return this.snbtConfig;
    }

    /**
     * Returns the {@link TagTypeRegistry} currently in use by this instance.
     *
     * @return the {@link TagTypeRegistry} currently in use by this instance.
     */
    @NonNull
    public TagTypeRegistry getTypeRegistry() {
        return this.typeRegistry;
    }

    /**
     * Sets the {@link SnbtConfig} currently in use by this instance.
     *
     * @param snbtConfig the new {@link SnbtConfig} to be set.
     */
    public void setSnbtConfig(@NonNull SnbtConfig snbtConfig) {
        this.snbtConfig = snbtConfig;
    }

    /**
     * Sets the {@link TagTypeRegistry} currently in use by this instance. Used to utilise custom-made tag types.
     *
     * @param typeRegistry the new {@link TagTypeRegistry} to be set.
     */
    public void setTypeRegistry(@NonNull TagTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
        this.writer.setTypeRegistry(typeRegistry);
        this.reader.setTypeRegistry(typeRegistry);
    }

}
