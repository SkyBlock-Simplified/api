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
 * The long tag (type ID 4) is used for storing a 64-bit signed two's complement integer; a Java primitive {@code long}.
 */
public final class LongTag extends NumericalTag<Long> {

    public static final @NotNull LongTag EMPTY = new LongTag(null, 0, false);

    /**
     * Constructs a long tag with a 0 value.
     */
    public LongTag() {
        this(0);
    }

    /**
     * Constructs a long tag with a given value.
     *
     * @param value the tag's value, to be converted to {@code long}.
     */
    public LongTag(@NotNull Number value) {
        this(null, value);
    }

    /**
     * Constructs a long tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's value, to be converted to {@code long}.
     */
    public LongTag(@Nullable String name, @NotNull Number value) {
        this(name, value, true);
    }

    private LongTag(@Nullable String name, @NotNull Number value, boolean modifiable) {
        super(TagType.LONG.getId(), name, value.longValue(), modifiable);
    }

    @Override
    public @NotNull LongTag fromJson(@NotNull JsonObject json, int depth) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsLong());
        return this;
    }

    @Override
    public @NotNull LongTag read(@NotNull DataInput input, int depth) throws IOException {
        this.setValue(input.readLong());
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
        return this.getValue() + "L";
    }

    @Override
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        output.writeLong(this.getValue());
    }

}
