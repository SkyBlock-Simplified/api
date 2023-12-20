package dev.sbs.api.minecraft.nbt.tags.primitive;

import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The float tag (type ID 5) is used for storing a single-precision 32-bit IEEE 754 floating point value; a Java primitive {@code float}.
 */
public final class FloatTag extends NumericalTag<Float> {

    public static final @NotNull FloatTag EMPTY = new FloatTag(null, 0, false);

    /**
     * Constructs a float tag with a 0 value.
     */
    public FloatTag() {
        this(0);
    }

    /**
     * Constructs a float tag with a given value.
     *
     * @param value the tag's value, to be converted to {@code float}.
     */
    public FloatTag(@NotNull Number value) {
        this(null, value);
    }

    /**
     * Constructs a float tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's value, to be converted to {@code float}.
     */
    public FloatTag(@Nullable String name, @NotNull Number value) {
        this(name, value, true);
    }

    private FloatTag(@Nullable String name, @NotNull Number value, boolean modifiable) {
        super(TagType.FLOAT.getId(), name, value.floatValue(), modifiable);
    }

    @Override
    public @NotNull FloatTag fromJson(@NotNull JsonObject json, int depth) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsFloat());
        return this;
    }

    @Override
    public @NotNull FloatTag read(@NotNull DataInput input, int depth) throws IOException {
        this.setValue(input.readFloat());
        return this;
    }

    @Override
    public @NotNull JsonObject toJson(int depth) {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        json.addProperty("value", this.getValue());
        return json;
    }

    @Override
    public @NotNull String toSnbt(int depth, @NotNull SnbtConfig config) {
        return this.getValue() + "f";
    }

    @Override
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        output.writeFloat(this.getValue());
    }

}
