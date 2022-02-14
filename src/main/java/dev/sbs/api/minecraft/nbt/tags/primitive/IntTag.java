package dev.sbs.api.minecraft.nbt.tags.primitive;

import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * The int tag (type ID 3) is used for storing a 32-bit signed two's complement integer; a Java primitive {@code int}.
 */
public class IntTag extends NumericalTag<Integer> {

    public static final IntTag EMPTY = new IntTag() {{ this.setNonUpdatable(); }};

    /**
     * Constructs an int tag with a 0 value.
     */
    public IntTag() {
        this(0);
    }

    /**
     * Constructs an int tag with a given value.
     *
     * @param value the tag's {@code Number} value, to be converted to {@code int}.
     */
    public IntTag(@NotNull Number value) {
        this(null, value.intValue());
    }

    /**
     * Constructs an int tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code int} value.
     */
    public IntTag(String name, int value) {
        super(name, value);
    }

    @Override
    public byte getTypeId() {
        return TagType.INT.getId();
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeInt(this.getValue());
    }

    @Override
    public IntTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.setValue(input.readInt());
        return this;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        return Integer.toString(this.getValue());
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
    public IntTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsInt());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntTag intTag = (IntTag) o;
        return Objects.equals(this.getValue(), intTag.getValue());
    }

    @Override
    public int hashCode() {
        return this.getValue();
    }

}
