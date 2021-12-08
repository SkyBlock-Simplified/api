package dev.sbs.api.minecraft.nbt.tags.array;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.snbt.SnbtUtil;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.primitive.IntTag;
import dev.sbs.api.util.Primitives;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The int array tag (type ID 11) is used for storing {@code int[]} arrays in NBT structures.
 * It is not stored as a list of {@link IntTag}s.
 */
public class IntArrayTag extends ArrayTag<Integer> {

    /**
     * Constructs an empty int array.
     */
    public IntArrayTag() {
        this(null, new LinkedList<>());
    }

    /**
     * Constructs an int array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code int[]} value.
     */
    public IntArrayTag(String name, int[] value) {
        this(name, Primitives.wrap(value));
    }

    /**
     * Constructs an int array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code int[]} value.
     */
    public IntArrayTag(String name, Integer[] value) {
        super(name, value);
    }

    /**
     * Constructs an unnamed int array tag using a {@code List<>} object.
     *
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code int[]} array.
     */
    public IntArrayTag(@NonNull List<Integer> value) {
        this(null, value);
    }

    /**
     * Constructs an int array tag with a given name, using a List object to determine its {@code int[]} value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code int[]} array.
     */
    public IntArrayTag(String name, @NonNull List<Integer> value) {
        super(name, value.toArray(new Integer[0]));
    }

    @Override
    public void clear() {
        this.value = new Integer[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntArrayTag that = (IntArrayTag) o;
        return Arrays.equals(this.getValue(), that.getValue());
    }

    @Override
    public IntArrayTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) throws IOException {
        JsonArray array = json.getAsJsonArray("value");
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(new Integer[array.size()]);

        for (int i = 0; i < array.size(); i++)
            this.value[i] = array.get(i).getAsInt();

        return this;
    }

    @Override
    public byte getTypeId() {
        return TagType.INT_ARRAY.getId();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getValue());
    }

    @Override
    public IntArrayTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.setValue(new Integer[input.readInt()]);

        for (int i = 0; i < this.getValue().length; i++)
            this.value[i] = input.readInt();

        return this;
    }

    @Override
    public JsonObject toJson(int depth, TagTypeRegistry registry) throws IOException {
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        for (int i : this)
            array.add(i);

        json.add("value", array);

        return json;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        StringBuilder sb = new StringBuilder("[I;");

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

            sb.append(this.getValue()[i]);
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

        for (int i : this)
            output.writeInt(i);
    }

}
