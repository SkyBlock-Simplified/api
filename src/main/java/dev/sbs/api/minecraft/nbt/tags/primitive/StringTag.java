package dev.sbs.api.minecraft.nbt.tags.primitive;

import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.NbtStringUtils;
import dev.sbs.api.minecraft.nbt.json.JsonSerializable;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.snbt.SnbtSerializable;
import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * The string tag (type ID 8) is used for storing a UTF-8 encoded {@code String}, prefixed by a length value stored as a 32-bit {@code int}.
 */
public class StringTag extends Tag<String> implements SnbtSerializable, JsonSerializable {

    /**
     * Constructs a float tag with a 0 value.
     */
    public StringTag() {
        this(null, "");
    }

    /**
     * Constructs a string tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code String} value.
     */
    public StringTag(String name, @NonNull String value) {
        super(name, value, new TagTypeRegistry());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringTag stringTag = (StringTag) o;
        return Objects.equals(getValue(), stringTag.getValue());
    }

    @Override
    public StringTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.setValue(json.getAsJsonPrimitive("value").getAsString());
        return this;
    }

    @Override
    public byte getTypeId() {
        return TagType.STRING.getId();
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public StringTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.setValue(input.readUTF());
        return this;
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
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        return NbtStringUtils.escapeSnbt(this.getValue());
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeUTF(this.getValue());
    }

}
