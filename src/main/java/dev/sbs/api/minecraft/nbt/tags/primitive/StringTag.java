package dev.sbs.api.minecraft.nbt.tags.primitive;

import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtUtil;
import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The string tag (type ID 8) is used for storing a UTF-8 encoded {@code String}, prefixed by a length value stored as a 32-bit {@code int}.
 */
public final class StringTag extends Tag<String> {

    public static final @NotNull StringTag EMPTY = new StringTag(null, "", false);

    /**
     * Constructs a string tag with an empty value.
     */
    public StringTag() {
        this("");
    }

    /**
     * Constructs a string tag with the given value.
     *
     * @param value the tag's {@code String} value.
     */
    public StringTag(@NotNull String value) {
        this(null, value);
    }

    /**
     * Constructs a string tag with the given name and value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code String} value.
     */
    public StringTag(@Nullable String name, @NotNull String value) {
        this(name, value, true);
    }

    private StringTag(@Nullable String name, @NotNull String value, boolean modifiable) {
        super(TagType.STRING.getId(), name, value, modifiable);
    }

    @Override
    public @NotNull StringTag fromJson(@NotNull JsonObject json, int depth) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsString());
        return this;
    }

    @Override
    public @NotNull StringTag read(@NotNull DataInput input, int depth) throws IOException {
        this.setValue(input.readUTF());
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
        return SnbtUtil.escape(this.getValue());
    }

    @Override
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        output.writeUTF(this.getValue());
    }

}
