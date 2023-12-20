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
 * The byte tag (type ID 1) is used for storing an 8-bit signed two's complement integer; a Java primitive {@code byte}.
 */
public final class ByteTag extends NumericalTag<Byte> {

    public static final @NotNull ByteTag EMPTY = new ByteTag(null, 0, false);

    /**
     * Constructs a byte tag with a 0 value.
     */
    public ByteTag() {
        this(0);
    }

    /**
     * Constructs a byte tag with a given value.
     *
     * @param value the tag's value, to be converted to {@code byte}.
     */
    public ByteTag(@NotNull Number value) {
        this(null, value);
    }

    /**
     * Constructs a byte tag with a given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's value, to be converted to {@code byte}.
     */
    public ByteTag(@Nullable String name, @NotNull Number value) {
        this(name, value, true);
    }

    private ByteTag(@Nullable String name, @NotNull Number value, boolean modifiable) {
        super(TagType.BYTE.getId(), name, value.byteValue(), modifiable);
    }

    @Override
    public @NotNull ByteTag fromJson(@NotNull JsonObject json, int depth) throws IOException {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsByte());
        return this;
    }

    @Override
    public @NotNull ByteTag read(@NotNull DataInput input, int depth) throws IOException {
        this.setValue(input.readByte());
        return this;
    }

    @Override
    public @NotNull JsonObject toJson(int depth) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        json.addProperty("value", this.getValue());

        return json;
    }

    @Override
    public @NotNull String toSnbt(int depth, @NotNull SnbtConfig config) {
        return this.getValue() + "b";
    }

    @Override
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        output.writeByte(this.getValue());
    }

}
