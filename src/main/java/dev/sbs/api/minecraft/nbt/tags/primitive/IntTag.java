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
 * The int tag (type ID 3) is used for storing a 32-bit signed two's complement integer; a Java primitive {@code int}.
 */
public final class IntTag extends NumericalTag<Integer> {

    public static final @NotNull IntTag EMPTY = new IntTag(null, 0, false);

    /**
     * Constructs an int tag with a 0 value.
     */
    public IntTag() {
        this(0);
    }

    /**
     * Constructs an int tag with a given value.
     *
     * @param value the tag's value, to be converted to {@code int}.
     */
    public IntTag(@NotNull Number value) {
        this(null, value);
    }

    /**
     * Constructs an int tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's value, to be converted to {@code int}.
     */
    public IntTag(@Nullable String name, @NotNull Number value) {
        this(name, value, true);
    }

    private IntTag(@Nullable String name, @NotNull Number value, boolean modifiable) {
        super(TagType.INT.getId(), name, value.intValue(), modifiable);
    }

    @Override
    public @NotNull IntTag fromJson(@NotNull JsonObject json, int depth) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsInt());
        return this;
    }

    @Override
    public @NotNull IntTag read(@NotNull DataInput input, int depth) throws IOException {
        this.setValue(input.readInt());
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
        return Integer.toString(this.getValue());
    }

    @Override
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        output.writeInt(this.getValue());
    }

}
