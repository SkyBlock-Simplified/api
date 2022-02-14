package dev.sbs.api.minecraft.nbt.tags.primitive;

import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The float tag (type ID 5) is used for storing a single-precision 32-bit IEEE 754 floating point value; a Java primitive {@code float}.
 */
public class FloatTag extends NumericalTag<Float> {

    public static final FloatTag EMPTY = new FloatTag() {{ this.setNonUpdatable(); }};

    /**
     * Constructs a float tag with a 0 value.
     */
    public FloatTag() {
        this(0);
    }

    /**
     * Constructs a float tag with a given value.
     *
     * @param value the tag's {@code Number} value, to be converted to {@code float}.
     */
    public FloatTag(@NotNull Number value) {
        this(null, value.floatValue());
    }

    /**
     * Constructs a float tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code float} value.
     */
    public FloatTag(String name, float value) {
        super(name, value);
    }

    @Override
    public byte getTypeId() {
        return TagType.FLOAT.getId();
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeFloat(this.getValue());
    }

    @Override
    public FloatTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.setValue(input.readFloat());
        return this;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        return this.getValue() + "f";
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
    public FloatTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsFloat());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatTag floatTag = (FloatTag) o;
        return Float.compare(floatTag.getValue(), getValue()) == 0;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getValue()).build();
    }

}
