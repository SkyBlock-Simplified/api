package dev.sbs.api.minecraft.nbt.tags.primitive;

import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The double tag (type ID 6) is used for storing a double-precision 64-bit IEEE 754 floating point value; a Java primitive {@code double}.
 */
public class DoubleTag extends NumericalTag<Double> {

    public static final DoubleTag EMPTY = new DoubleTag() {{ this.setNonUpdatable(); }};

    /**
     * Constructs a double tag with a 0 value.
     */
    public DoubleTag() {
        this(0);
    }

    /**
     * Constructs a double tag with a given value.
     *
     * @param value the tag's {@code Number} value, to be converted to {@code double}.
     */
    public DoubleTag(@NonNull Number value) {
        this(null, value.doubleValue());
    }

    /**
     * Constructs a double tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code double} value.
     */
    public DoubleTag(String name, double value) {
        super(name, value);
    }

    @Override
    public byte getTypeId() {
        return TagType.DOUBLE.getId();
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeDouble(this.getValue());
    }

    @Override
    public DoubleTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.setValue(input.readDouble());
        return this;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        return this.getValue() + "d";
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
    public DoubleTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsDouble());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleTag doubleTag = (DoubleTag) o;
        return Double.compare(doubleTag.getValue(), this.getValue()) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(this.getValue());
        return (int) (temp ^ (temp >>> 32));
    }

}
