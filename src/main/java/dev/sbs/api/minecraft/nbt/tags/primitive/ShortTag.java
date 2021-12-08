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
 * The short tag (type ID 2) is used for storing a 16-bit signed two's complement integer; a Java primitive {@code short}.
 */
public class ShortTag extends NumericalTag<Short> {

    /**
     * Constructs a short tag with a 0 value.
     */
    public ShortTag() {
        this(0);
    }

    /**
     * Constructs a short tag with a given value.
     *
     * @param value the tag's {@code Number} value, to be converted to {@code short}.
     */
    public ShortTag(@NonNull Number value) {
        this(null, value);
    }

    /**
     * Constructs a short tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code Number} value, to be converted to {@code short}.
     */
    public ShortTag(String name, @NonNull Number value) {
        this(name, value.shortValue());
    }

    /**
     * Constructs a short tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code short} value.
     */
    public ShortTag(String name, short value) {
        super(name, value);
    }

    @Override
    public byte getTypeId() {
        return TagType.SHORT.getId();
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeShort(this.getValue());
    }

    @Override
    public ShortTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.setValue(input.readShort());
        return this;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        return this.getValue() + "s";
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
    public ShortTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsShort());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortTag shortTag = (ShortTag) o;
        return Objects.equals(getValue(), shortTag.getValue());
    }

    @Override
    public int hashCode() {
        return getValue();
    }

}
