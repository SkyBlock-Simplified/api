package dev.sbs.api.minecraft.nbt.tags.array;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtUtil;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.primitive.IntTag;
import dev.sbs.api.util.helper.PrimitiveUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * The int array tag (type ID 11) is used for storing {@code int[]} arrays in NBT structures.
 * <br><br>
 * It is not stored as a list of {@link IntTag IntTags}.
 */
public final class IntArrayTag extends ArrayTag<Integer> {

    public static final @NotNull IntArrayTag EMPTY = new IntArrayTag(null, new Integer[0], false);

    /**
     * Constructs an empty int array.
     */
    public IntArrayTag() {
        this(new LinkedList<>());
    }

    /**
     * Constructs an unnamed int array tag using a {@code List<>} object.
     *
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code int[]} array.
     */
    public IntArrayTag(@NotNull Collection<Integer> value) {
        this(null, value);
    }

    /**
     * Constructs an int array tag with a given name, using a List object to determine its {@code int[]} value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code int[]} array.
     */
    public IntArrayTag(@Nullable String name, @NotNull Collection<Integer> value) {
        this(name, value.toArray(new Integer[0]));
    }

    /**
     * Constructs an int array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code int[]} value.
     */
    public IntArrayTag(@Nullable String name, int... value) {
        this(name, PrimitiveUtil.wrap(value));
    }

    /**
     * Constructs an int array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code int[]} value.
     */
    public IntArrayTag(@Nullable String name, Integer... value) {
        this(name, value, true);
    }

    private IntArrayTag(@Nullable String name, Integer[] value, boolean updatable) {
        super(TagType.INT_ARRAY.getId(), name, value, updatable);
    }

    @Override
    public @NotNull IntArrayTag fromJson(@NotNull JsonObject json, int depth) throws IOException {
        JsonArray array = json.getAsJsonArray("value");
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(new Integer[array.size()]);

        for (int i = 0; i < array.size(); i++)
            this.getValue()[i] = array.get(i).getAsInt();

        return this;
    }

    @Override
    public @NotNull IntArrayTag read(@NotNull DataInput input, int depth) throws IOException {
        this.setValue(new Integer[input.readInt()]);

        for (int i = 0; i < this.getValue().length; i++)
            this.getValue()[i] = input.readInt();

        return this;
    }

    @Override
    public @NotNull JsonObject toJson(int depth) throws IOException {
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
    public @NotNull String toSnbt(int depth, @NotNull SnbtConfig config) {
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
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        output.writeInt(this.size());

        for (int i : this)
            output.writeInt(i);
    }

}
