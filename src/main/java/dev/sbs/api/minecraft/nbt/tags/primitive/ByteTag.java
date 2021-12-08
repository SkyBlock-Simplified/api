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
 * The byte tag (type ID 1) is used for storing an 8-bit signed two's complement integer; a Java primitive {@code byte}.
 */
public class ByteTag extends NumericalTag<Byte> {

    /**
     * Constructs a byte tag with a 0 value.
     */
    public ByteTag() {
        this(0);
    }

    /**
     * Constructs a byte tag with a given value.
     *
     * @param value the tag's {@code Number} value, to be converted to {@code byte}.
     */
    public ByteTag(@NonNull Number value) {
        this(null, value);
    }

    /**
     * Constructs a byte tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code Number} value, to be converted to {@code byte}.
     */
    public ByteTag(String name, @NonNull Number value) {
        this(name, value.byteValue());
    }

    /**
     * Constructs a byte tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code byte} value.
     */
    public ByteTag(String name, byte value) {
        super(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteTag byteTag = (ByteTag) o;
        return Objects.equals(this.getValue(), byteTag.getValue());
    }

    @Override
    public ByteTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) throws IOException {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsByte());
        return this;
    }

    @Override
    public byte getTypeId() {
        return TagType.BYTE.getId();
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public ByteTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.setValue(input.readByte());
        return this;
    }

    @Override
    public JsonObject toJson(int depth, TagTypeRegistry registry) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        json.addProperty("value", this.getValue());

        return json;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        return this.getValue() + "b";
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeByte(this.getValue());
    }

}
