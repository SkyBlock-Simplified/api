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
 * The short tag (type ID 2) is used for storing a 16-bit signed two's complement integer; a Java primitive {@code short}.
 */
public final class ShortTag extends NumericalTag<Short> {

    public static final @NotNull ShortTag EMPTY = new ShortTag(null, 0, false);

    /**
     * Constructs a short tag with a 0 value.
     */
    public ShortTag() {
        this(0);
    }

    /**
     * Constructs a short tag with a given value.
     *
     * @param value the tag's value, to be converted to {@code short}.
     */
    public ShortTag(@NotNull Number value) {
        this(null, value);
    }

    /**
     * Constructs a short tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's value, to be converted to {@code short}.
     */
    public ShortTag(@Nullable String name, @NotNull Number value) {
        this(name, value, true);
    }

    private ShortTag(@Nullable String name, @NotNull Number value, boolean modifiable) {
        super(TagType.SHORT.getId(), name, value.shortValue(), modifiable);
    }

    @Override
    public @NotNull ShortTag fromJson(@NotNull JsonObject json, int depth) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsShort());
        return this;
    }

    @Override
    public @NotNull ShortTag read(@NotNull DataInput input, int depth) throws IOException {
        this.setValue(input.readShort());
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
        return this.getValue() + "s";
    }

    @Override
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        output.writeShort(this.getValue());
    }

}
