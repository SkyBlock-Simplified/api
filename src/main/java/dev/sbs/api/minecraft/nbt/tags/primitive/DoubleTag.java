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
 * The double tag (type ID 6) is used for storing a double-precision 64-bit IEEE 754 floating point value; a Java primitive {@code double}.
 */
public final class DoubleTag extends NumericalTag<Double> {

    public static final @NotNull DoubleTag EMPTY = new DoubleTag(null, 0, false);

    /**
     * Constructs a double tag with a 0 value.
     */
    public DoubleTag() {
        this(0);
    }

    /**
     * Constructs a double tag with a given value.
     *
     * @param value the tag's value, to be converted to {@code double}.
     */
    public DoubleTag(@NotNull Number value) {
        this(null, value);
    }

    /**
     * Constructs a double tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's value, to be converted to {@code double}.
     */
    public DoubleTag(@Nullable String name, @NotNull Number value) {
        this(name, value, true);
    }

    private DoubleTag(@Nullable String name, @NotNull Number value, boolean modifiable) {
        super(TagType.DOUBLE.getId(), name, value.doubleValue(), modifiable);
    }

    @Override
    public @NotNull DoubleTag fromJson(@NotNull JsonObject json, int depth) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsDouble());
        return this;
    }

    @Override
    public @NotNull DoubleTag read(@NotNull DataInput input, int depth) throws IOException {
        this.setValue(input.readDouble());
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
        return this.getValue() + "d";
    }

    @Override
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        output.writeDouble(this.getValue());
    }

}
