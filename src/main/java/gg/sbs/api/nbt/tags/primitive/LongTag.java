package gg.sbs.api.nbt.tags.primitive;

import com.google.gson.JsonObject;
import gg.sbs.api.nbt.api.registry.TagTypeRegistry;
import gg.sbs.api.nbt.api.snbt.SnbtConfig;
import gg.sbs.api.nbt.tags.TagType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The long tag (type ID 4) is used for storing a 64-bit signed two's complement integer; a Java primitive {@code long}.
 */
@NoArgsConstructor
@AllArgsConstructor
public class LongTag extends NumericalTag<Long> {
    private long value;

    /**
     * Constructs a long tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code long} value.
     */
    public LongTag(String name, long value) {
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public byte getTypeId() {
        return TagType.LONG.getId();
    }

    @Override
    public Long getValue() {
        return this.value;
    }

    /**
     * Sets the {@code long} value of this long tag.
     *
     * @param value new {@code long} value to be set.
     */
    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeLong(this.value);
    }

    @Override
    public LongTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.value = input.readLong();
        return this;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        return this.value + "L";
    }

    @Override
    public JsonObject toJson(int depth, TagTypeRegistry registry) {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        json.addProperty("value", this.value);

        return json;
    }

    @Override
    public LongTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.value = json.getAsJsonPrimitive("value").getAsLong();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongTag longTag = (LongTag) o;
        return value == longTag.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

}
