package dev.sbs.api.minecraft.nbt.tags.array;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.snbt.SnbtUtil;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.primitive.LongTag;
import dev.sbs.api.util.Primitives;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The long array tag (type ID 12) is used for storing {@code long[]} arrays in NBT structures.
 * It is not stored as a list of {@link LongTag}s.
 */
public class LongArrayTag extends ArrayTag<Long> {

    public static final LongArrayTag EMPTY = new LongArrayTag() {{ this.setNonUpdatable(); }};

    /**
     * Constructs an empty long array.
     */
    public LongArrayTag() {
        this(null, new LinkedList<>());
    }

    /**
     * Constructs a long array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code long[]} value.
     */
    public LongArrayTag(String name, long[] value) {
        this(name, Primitives.wrap(value));
    }

    /**
     * Constructs a long array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code long[]} value.
     */
    public LongArrayTag(String name, Long[] value) {
        super(name, value);
    }

    /**
     * Constructs an unnamed long array tag using a {@code List<>} object.
     *
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code long[]} array.
     */
    public LongArrayTag(@NonNull List<Long> value) {
        this(null, value);
    }

    /**
     * Constructs a long array tag with a given name, using a List object to determine its {@code long[]} value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code long[]} array.
     */
    public LongArrayTag(String name, @NonNull List<Long> value) {
        super(name, value.toArray(new Long[0]));
    }

    @Override
    public void clear() {
        this.value = new Long[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongArrayTag that = (LongArrayTag) o;
        return Arrays.equals(this.getValue(), that.getValue());
    }

    @Override
    public LongArrayTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) throws IOException {
        JsonArray array = json.getAsJsonArray("value");
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(new Long[array.size()]);

        for (int i = 0; i < array.size(); i++) {
            this.value[i] = array.get(i).getAsLong();
        }

        return this;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getValue());
    }

    @Override
    public byte getTypeId() {
        return TagType.LONG_ARRAY.getId();
    }

    @Override
    public LongArrayTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.setValue(new Long[input.readInt()]);

        for (int i = 0; i < this.size(); i++)
            this.value[i] = input.readLong();

        return this;
    }

    @Override
    public JsonObject toJson(int depth, TagTypeRegistry registry) throws IOException {
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        for (long l : this)
            array.add(l);

        json.add("value", array);

        return json;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        StringBuilder sb = new StringBuilder("[L;");

        if (config.isPrettyPrint()) {
            if (this.size() < config.getInlineThreshold()) {
                sb.append('\n').append(SnbtUtil.multiplyIndent(depth + 1, config));
            } else {
                sb.append(' ');
            }
        }

        for (int i = 0; i < this.size(); ++i) {
            if (i != 0) {
                if (config.isPrettyPrint()) {
                    if (this.size() < config.getInlineThreshold()) {
                        sb.append(",\n").append(SnbtUtil.multiplyIndent(depth + 1, config));
                    } else {
                        sb.append(", ");
                    }
                } else {
                    sb.append(',');
                }
            }

            sb.append(this.getValue()[i]).append('L');
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

        for (long l : this)
            output.writeLong(l);
    }

}
