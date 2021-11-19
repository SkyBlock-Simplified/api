package dev.sbs.api.minecraft.nbt.tags.primitive;

import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * The long tag (type ID 4) is used for storing a 64-bit signed two's complement integer; a Java primitive {@code long}.
 */
public class LongTag extends NumericalTag<Long> {

    /**
     * Constructs a long tag with a 0 value.
     */
    public LongTag() {
        this(0);
    }

    /**
     * Constructs a long tag with a given value.
     *
     * @param value the tag's {@code Number} value, to be converted to {@code long}.
     */
    public LongTag(@NonNull Number value) {
        this(null, value.longValue());
    }

    /**
     * Constructs a long tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code long} value.
     */
    public LongTag(String name, long value) {
        super(name, value);
    }

    @Override
    public byte getTypeId() {
        return TagType.LONG.getId();
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeLong(this.getValue());
    }

    @Override
    public LongTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.setValue(input.readLong());
        return this;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        return this.getValue() + "L";
    }

    @Override
    public JsonObject toJson(int depth, TagTypeRegistry registry) {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        json.addProperty("value", this.getValue());

        return json;
    }

    @Override
    public LongTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsLong());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongTag longTag = (LongTag) o;
        return Objects.equals(getValue(), longTag.getValue());
    }

    @Override
    public int hashCode() {
        return (int) (getValue() ^ (getValue() >>> 32));
    }

}
