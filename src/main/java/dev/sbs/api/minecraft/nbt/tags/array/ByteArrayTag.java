package dev.sbs.api.minecraft.nbt.tags.array;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtUtil;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.primitive.ByteTag;
import dev.sbs.api.util.helper.PrimitiveUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * The byte array tag (type ID 7) is used for storing {@code byte[]} arrays in NBT structures.
 * <br><br>
 * It is not stored as a list of {@link ByteTag ByteTags}.
 */
public final class ByteArrayTag extends ArrayTag<Byte> {

    public static final @NotNull ByteArrayTag EMPTY = new ByteArrayTag(null, new Byte[0], false);

    /**
     * Constructs an empty byte array.
     */
    public ByteArrayTag() {
        this(new LinkedList<>());
    }

    /**
     * Constructs an unnamed byte array tag using a {@code List<>} object.
     *
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code byte[]} array.
     */
    public ByteArrayTag(@NotNull Collection<Byte> value) {
        this(null, value);
    }

    /**
     * Constructs a byte array tag with a given name, using a List object to determine its {@code byte[]} value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code byte[]} array.
     */
    public ByteArrayTag(@Nullable String name, @NotNull Collection<Byte> value) {
        this(name, value.toArray(new Byte[0]));
    }

    /**
     * Constructs a byte array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code byte[]} value.
     */
    public ByteArrayTag(@Nullable String name, byte[] value) {
        this(name, PrimitiveUtil.wrap(value));
    }

    /**
     * Constructs a byte array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code byte[]} value.
     */
    public ByteArrayTag(@Nullable String name, @NotNull Byte[] value) {
        this(name, value, true);
    }

    private ByteArrayTag(@Nullable String name, Byte[] value, boolean modifiable) {
        super(TagType.BYTE_ARRAY.getId(), name, value, modifiable);
    }

    @Override
    public @NotNull ByteArrayTag fromJson(@NotNull JsonObject json, int depth) throws IOException {
        JsonArray array = json.getAsJsonArray("value");
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(new Byte[array.size()]);

        for (int i = 0; i < array.size(); i++)
            this.getValue()[i] = array.get(i).getAsByte();

        return this;
    }

    @Override
    public @NotNull ByteArrayTag read(@NotNull DataInput input, int depth) throws IOException {
        byte[] tmp = new byte[input.readInt()];
        input.readFully(tmp);
        this.setValue(PrimitiveUtil.wrap(tmp));
        return this;
    }

    @Override
    public @NotNull JsonObject toJson(int depth) throws IOException {
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
    public @NotNull String toSnbt(int depth, @NotNull SnbtConfig config) {
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
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        output.writeInt(this.size());
        output.write(PrimitiveUtil.unwrap(this.getValue()));
    }

}
