package dev.sbs.api.minecraft.nbt.tags.array;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtUtil;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.primitive.LongTag;
import dev.sbs.api.util.helper.PrimitiveUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * The long array tag (type ID 12) is used for storing {@code long[]} arrays in NBT structures.
 * <br><br>
 * It is not stored as a list of {@link LongTag LongTags}.
 */
public final class LongArrayTag extends ArrayTag<Long> {

    public static final @NotNull LongArrayTag EMPTY = new LongArrayTag(null, new Long[0], false);

    /**
     * Constructs an empty long array.
     */
    public LongArrayTag() {
        this(new LinkedList<>());
    }

    /**
     * Constructs an unnamed long array tag using a {@code List<>} object.
     *
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code long[]} array.
     */
    public LongArrayTag(@NotNull Collection<Long> value) {
        this(null, value);
    }

    /**
     * Constructs a long array tag with a given name, using a List object to determine its {@code long[]} value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code long[]} array.
     */
    public LongArrayTag(@Nullable String name, @NotNull Collection<Long> value) {
        this(name, value.toArray(new Long[0]));
    }

    /**
     * Constructs a long array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code long[]} value.
     */
    public LongArrayTag(@Nullable String name, long[] value) {
        this(name, PrimitiveUtil.wrap(value));
    }

    /**
     * Constructs a long array tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code long[]} value.
     */
    public LongArrayTag(@Nullable String name, Long[] value) {
        this(name, value, true);
    }

    private LongArrayTag(@Nullable String name, Long[] value, boolean updatable) {
        super(TagType.LONG_ARRAY.getId(), name, value, updatable);
    }

    @Override
    public @NotNull LongArrayTag fromJson(@NotNull JsonObject json, int depth) throws IOException {
        JsonArray array = json.getAsJsonArray("value");
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(new Long[array.size()]);

        for (int i = 0; i < array.size(); i++)
            this.getValue()[i] = array.get(i).getAsLong();

        return this;
    }

    @Override
    public @NotNull LongArrayTag read(@NotNull DataInput input, int depth) throws IOException {
        this.setValue(new Long[input.readInt()]);

        for (int i = 0; i < this.size(); i++)
            this.getValue()[i] = input.readLong();

        return this;
    }

    @Override
    public @NotNull JsonObject toJson(int depth) throws IOException {
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
    public @NotNull String toSnbt(int depth, @NotNull SnbtConfig config) {
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
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        output.writeInt(this.size());

        for (long l : this)
            output.writeLong(l);
    }

}
