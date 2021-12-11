package dev.sbs.api.minecraft.nbt.tags.array;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.snbt.SnbtUtil;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.primitive.ByteTag;
import dev.sbs.api.util.Primitives;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The byte array tag (type ID 7) is used for storing {@code byte[]} arrays in NBT structures.
 * It is not stored as a list of {@link ByteTag}s.
 */
public class ByteArrayTag extends ArrayTag<Byte> {

    public static final ByteArrayTag EMPTY = new ByteArrayTag() {{ this.setNonUpdatable(); }};

    /**
     * Constructs an empty byte array.
     */
    public ByteArrayTag() {
        this(null, new LinkedList<>());
    }

    /**
     * Constructs a byte array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code byte[]} value.
     */
    public ByteArrayTag(String name, byte[] value) {
        this(name, Primitives.wrap(value));
    }

    /**
     * Constructs a byte array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code byte[]} value.
     */
    public ByteArrayTag(String name, Byte[] value) {
        super(name, value);
    }

    /**
     * Constructs an unnamed byte array tag using a {@code List<>} object.
     *
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code byte[]} array.
     */
    public ByteArrayTag(@NonNull List<Byte> value) {
        this(null, value);
    }

    /**
     * Constructs a byte array tag with a given name, using a List object to determine its {@code byte[]} value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code byte[]} array.
     */
    public ByteArrayTag(String name, @NonNull List<Byte> value) {
        super(name, value.toArray(new Byte[0]));
    }

    @Override
    public void clear() {
        this.value = new Byte[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteArrayTag that = (ByteArrayTag) o;
        return Arrays.equals(getValue(), that.getValue());
    }

    @Override
    public ByteArrayTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) throws IOException {
        JsonArray array = json.getAsJsonArray("value");
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(new Byte[array.size()]);

        for (int i = 0; i < array.size(); i++)
            this.value[i] = array.get(i).getAsByte();

        return this;
    }

    @Override
    public byte getTypeId() {
        return TagType.BYTE_ARRAY.getId();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getValue());
    }

    @Override
    public ByteArrayTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        byte[] tmp = new byte[input.readInt()];
        input.readFully(tmp);
        this.setValue(Primitives.wrap(tmp));
        return this;
    }

    @Override
    public JsonObject toJson(int depth, TagTypeRegistry registry) throws IOException {
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        for (byte b : this)
            array.add(b);

        json.add("value", array);

        return json;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        StringBuilder sb = new StringBuilder("[B;");

        if (config.isPrettyPrint()) {
            if (this.size() < config.getInlineThreshold())
                sb.append('\n').append(SnbtUtil.multiplyIndent(depth + 1, config));
            else
                sb.append(' ');
        }

        for (int i = 0; i < this.size(); ++i) {
            if (i != 0) {
                if (config.isPrettyPrint()) {
                    if (this.size() < config.getInlineThreshold())
                        sb.append(",\n").append(SnbtUtil.multiplyIndent(depth + 1, config));
                    else
                        sb.append(", ");
                } else
                    sb.append(',');
            }

            sb.append(this.getValue()[i]).append('B');
        }

        if (config.isPrettyPrint() && this.size() < config.getInlineThreshold())
            sb.append("\n").append(SnbtUtil.multiplyIndent(depth, config)).append(']');
        else
            sb.append(']');

        return sb.toString();
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeInt(this.size());
        output.write(Primitives.unwrap(this.getValue()));
    }

}
