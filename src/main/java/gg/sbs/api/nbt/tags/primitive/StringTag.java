package gg.sbs.api.nbt.tags.primitive;

import com.google.gson.JsonObject;
import gg.sbs.api.nbt.NbtStringUtils;
import gg.sbs.api.nbt.json.JsonSerializable;
import gg.sbs.api.nbt.registry.TagTypeRegistry;
import gg.sbs.api.nbt.snbt.SnbtConfig;
import gg.sbs.api.nbt.snbt.SnbtSerializable;
import gg.sbs.api.nbt.tags.Tag;
import gg.sbs.api.nbt.tags.TagType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * The string tag (type ID 8) is used for storing a UTF-8 encoded {@code String}, prefixed by a length value stored as a 32-bit {@code int}.
 */
@NoArgsConstructor
@AllArgsConstructor
public class StringTag extends Tag implements SnbtSerializable, JsonSerializable {

    private @NonNull String value;

    /**
     * Constructs a string tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code String} value.
     */
    public StringTag(String name, @NonNull String value) {
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public byte getTypeId() {
        return TagType.STRING.getId();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the {@code String} value of this string tag.
     *
     * @param value new {@code String} value to be set.
     */
    public void setValue(@NonNull String value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeUTF(this.value);
    }

    @Override
    public StringTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.value = input.readUTF();
        return this;
    }

    @Override
    public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
        return NbtStringUtils.escapeSnbt(this.value);
    }

    @Override
    public JsonObject toJson(int depth, TagTypeRegistry registry) {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        json.addProperty("value", this.value);
        return json;
    }

    @Override
    public StringTag fromJson(JsonObject json, int depth, TagTypeRegistry registry) {
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        this.value = json.getAsJsonPrimitive("value").getAsString();
        return this;
    }

    @Override
    public String toString() {
        return this.toSnbt(0, new TagTypeRegistry(), new SnbtConfig());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringTag stringTag = (StringTag) o;
        return Objects.equals(value, stringTag.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
