package gg.sbs.api.nbt.tags.array;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gg.sbs.api.nbt.NbtStringUtils;
import gg.sbs.api.nbt.registry.TagTypeRegistry;
import gg.sbs.api.nbt.snbt.SnbtConfig;
import gg.sbs.api.nbt.tags.TagType;
import gg.sbs.api.nbt.tags.primitive.LongTag;
import gg.sbs.api.util.helper.ArrayUtil;
import gg.sbs.api.util.Primitives;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * The long array tag (type ID 12) is used for storing {@code long[]} arrays in NBT structures.
 * It is not stored as a list of {@link LongTag}s.
 */
@NoArgsConstructor
@AllArgsConstructor
public class LongArrayTag extends ArrayTag<Long> {

    private long[] value;

    /**
     * Constructs a long array tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code long[]} value.
     */
    public LongArrayTag(String name, long[] value) {
        this.setName(name);
        this.setValue(value);
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
     * @param name the tag's name.
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code long[]} array.
     */
    public LongArrayTag(String name, @NonNull List<Long> value) {
        this.setName(name);
        this.setValue(Primitives.unwrap(value.toArray(new Long[0])));
    }

    @Override
    public byte getTypeId() {
        return TagType.LONG_ARRAY.getId();
    }

    @Override
    public long[] getValue() {
        return this.value;
    }

    /**
     * Sets the {@code long[]} value of this long array tag.
     *
     * @param value new {@code long[]} value to be set.
     */
    public void setValue(long[] value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeInt(this.value.length);

        for (long l : this)
            output.writeLong(l);
    }

    @Override
    public LongArrayTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.value = new long[input.readInt()];

        for (int i = 0; i < this.value.length; i++)
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
    public LongArrayTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) throws IOException {
        JsonArray array = json.getAsJsonArray("value");
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.value = new long[array.size()];

        for (int i = 0; i < array.size(); i++) {
            this.value[i] = array.get(i).getAsLong();
        }

        return this;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        StringBuilder sb = new StringBuilder("[L;");

        if (config.isPrettyPrint()) {
            if (this.value.length < config.getInlineThreshold()) {
                sb.append('\n').append(NbtStringUtils.multiplyIndent(depth + 1, config));
            } else {
                sb.append(' ');
            }
        }

        for (int i = 0; i < this.value.length; ++i) {
            if (i != 0) {
                if (config.isPrettyPrint()) {
                    if (this.value.length < config.getInlineThreshold()) {
                        sb.append(",\n").append(NbtStringUtils.multiplyIndent(depth + 1, config));
                    } else {
                        sb.append(", ");
                    }
                } else {
                    sb.append(',');
                }
            }

            sb.append(this.value[i]).append('L');
        }

        if (config.isPrettyPrint() && this.value.length < config.getInlineThreshold())
            sb.append("\n").append(NbtStringUtils.multiplyIndent(depth , config)).append(']');
        else
            sb.append(']');

        return sb.toString();
    }

    @Override
    public int size() {
        return this.value.length;
    }

    @Override
    public Long get(int index) {
        return this.value[index];
    }

    @Override
    public Long set(int index, @NonNull Long element) {
        return this.value[index] = element;
    }

    @Override
    public void insert(int index, @NonNull Long... elements) {
        this.value = ArrayUtil.insert(index, this.value, Primitives.unwrap(elements));
    }

    @Override
    public Long remove(int index) {
        Long previous = this.value[index];
        this.value = ArrayUtil.remove(this.value, index);
        return previous;
    }

    @Override
    public void clear() {
        this.value = new long[0];
    }

    @Override
    public Iterator<Long> iterator() {
        return Arrays.asList(Primitives.wrap(this.value)).iterator();
    }

    @Override
    public void forEach(Consumer<? super Long> action) {
        Arrays.asList(Primitives.wrap(this.value)).forEach(action);
    }

    @Override
    public Spliterator<Long> spliterator() {
        return Arrays.asList(Primitives.wrap(this.value)).spliterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongArrayTag that = (LongArrayTag) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

}
