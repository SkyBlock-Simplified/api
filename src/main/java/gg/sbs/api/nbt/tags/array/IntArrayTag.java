package gg.sbs.api.nbt.tags.array;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gg.sbs.api.nbt.NbtStringUtils;
import gg.sbs.api.nbt.registry.TagTypeRegistry;
import gg.sbs.api.nbt.snbt.SnbtConfig;
import gg.sbs.api.nbt.tags.TagType;
import gg.sbs.api.nbt.tags.primitive.IntTag;
import gg.sbs.api.util.ArrayUtil;
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
 * The int array tag (type ID 11) is used for storing {@code int[]} arrays in NBT structures.
 * It is not stored as a list of {@link IntTag}s.
 */
@NoArgsConstructor
@AllArgsConstructor
public class IntArrayTag extends ArrayTag<Integer> {

    private int[] value;

    /**
     * Constructs an int array tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code int[]} value.
     */
    public IntArrayTag(String name, int[] value) {
        this.setName(name);
        this.setValue(value);
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
     * @param name the tag's name.
     * @param value the tag's {@code List<>} value, to be converted to a primitive {@code int[]} array.
     */
    public IntArrayTag(String name, @NonNull List<Integer> value) {
        this.setName(name);
        this.setValue(Primitives.unwrap(value.toArray(new Integer[0])));
    }

    @Override
    public byte getTypeId() {
        return TagType.INT_ARRAY.getId();
    }

    @Override
    public int[] getValue() {
        return this.value;
    }

    /**
     * Sets the {@code int[]} value of this int array tag.
     *
     * @param value new {@code int[]} value to be set.
     */
    public void setValue(int[] value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeInt(this.value.length);

        for (int i : this) {
            output.writeInt(i);
        }
    }

    @Override
    public IntArrayTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.value = new int[input.readInt()];

        for (int i = 0; i < this.value.length; i++)
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
    public IntArrayTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) throws IOException {
        JsonArray array = json.getAsJsonArray("value");
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.value = new int[array.size()];

        for (int i = 0; i < array.size(); i++)
            this.value[i] = array.get(i).getAsInt();

        return this;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        StringBuilder sb = new StringBuilder("[I;");

        if (config.isPrettyPrint()) {
            if (this.value.length < config.getInlineThreshold())
                sb.append('\n').append(NbtStringUtils.multiplyIndent(depth + 1, config));
            else
                sb.append(' ');
        }

        for (int i = 0; i < this.value.length; ++i) {
            if (i != 0) {
                if (config.isPrettyPrint()) {
                    if (this.value.length < config.getInlineThreshold())
                        sb.append(",\n").append(NbtStringUtils.multiplyIndent(depth + 1, config));
                    else
                        sb.append(", ");
                } else
                    sb.append(',');
            }

            sb.append(this.value[i]);
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
    public Integer get(int index) {
        return this.value[index];
    }

    @Override
    public Integer set(int index, @NonNull Integer element) {
        return this.value[index] = element;
    }

    @Override
    public void insert(int index, @NonNull Integer... elements) {
        this.value = ArrayUtil.insert(index, this.value, Primitives.unwrap(elements));
    }

    @Override
    public Integer remove(int index) {
        Integer previous = this.value[index];
        this.value = ArrayUtil.remove(this.value, index);
        return previous;
    }

    @Override
    public void clear() {
        this.value = new int[0];
    }

    @Override
    public Iterator<Integer> iterator() {
        return Arrays.asList(Primitives.wrap(this.value)).iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        Arrays.asList(Primitives.wrap(this.value)).forEach(action);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return Arrays.asList(Primitives.wrap(this.value)).spliterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntArrayTag that = (IntArrayTag) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

}
