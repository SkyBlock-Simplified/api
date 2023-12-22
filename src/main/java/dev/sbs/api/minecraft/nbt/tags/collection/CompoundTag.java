package dev.sbs.api.minecraft.nbt.tags.collection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.exception.TagTypeRegistryException;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtUtil;
import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.TagRegistry;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.impl.RegistryTag;
import dev.sbs.api.util.builder.string.StringBuilder;
import dev.sbs.api.util.data.tuple.pair.Pair;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * The compound tag (type ID 10) is used for storing an unordered map of any and all named tags.
 * All tags present in a compound must be given a name (key). Every valid NBT data structure is contained entirely within a "root" compound.
 */
@Getter
@SuppressWarnings("unchecked")
public final class CompoundTag extends RegistryTag<Map<String, Tag<?>>> implements Map<String, Tag<?>>, Iterable<Map.Entry<String, Tag<?>>> {

    private final boolean root;

    /**
     * Constructs an empty, unnamed compound tag.
     */
    public CompoundTag() {
        this(false);
    }

    /**
     * Constructs an empty, unnamed compound tag.
     */
    public CompoundTag(boolean root) {
        this(null, root);
    }

    /**
     * Constructs an empty compound tag with a given name.
     *
     * @param name the tag's name.
     */
    public CompoundTag(@Nullable String name) {
        this(name, false);
    }

    /**
     * Constructs an empty compound tag with a given name.
     *
     * @param name the tag's name.
     */
    public CompoundTag(@Nullable String name, boolean root) {
        this(name, new LinkedHashMap<>(), root);
    }

    /**
     * Constructs a compound tag with a given name and {@code Map<>} value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code Map<>} value.
     */
    public CompoundTag(@Nullable String name, @NotNull Map<String, Tag<?>> value) {
        this(name, value, false);
    }

    /**
     * Constructs a compound tag with a given name and {@code Map<>} value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code Map<>} value.
     */
    public CompoundTag(@Nullable String name, @NotNull Map<String, Tag<?>> value, boolean root) {
        super(TagType.COMPOUND.getId(), name, value, true);
        this.root = root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.getValue().clear();
    }

    @Override
    @SuppressWarnings("all")
    public @NotNull CompoundTag clone() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putAll(this);
        return compoundTag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(@NotNull Object key) {
        return this.getValue().containsKey(key);
    }

    public boolean containsListOf(@NotNull String key, byte of) {
        return this.containsType(key, TagType.LIST) && this.getList(key).getListType() == of;
    }

    /**
     * Returns true if this compound contains an entry with a given name (key) and if that entry is of a given tag type, false otherwise.
     *
     * @param key    the name (key) to check for.
     * @param typeId the tag type ID to test for.
     * @return true if this compound contains an entry with a given name (key) and if that entry is of a given tag type, false otherwise.
     */
    public boolean containsType(@NotNull String key, byte typeId) {
        if (!this.containsKey(key))
            return false;

        return this.getTag(key).getTypeId() == typeId;
    }

    public boolean containsType(@NotNull String key, TagType tagType) {
        return this.containsType(key, tagType.getId());
    }

    /**
     * Checks if the path exists in the tree.
     * <p>
     * Every element of the path (except the end) are assumed to be compounds. The
     * retrieval operation will return false if any of them are missing.
     *
     * @param path The path to the entry.
     * @return True if found.
     */
    public boolean containsPath(@NotNull String path) {
        List<String> entries = StringUtil.toList(StringUtil.split(path, "\\."));
        CompoundTag current = this;

        for (String entry : entries) {
            Tag<?> childTag = current.getTag(entry);

            if (childTag == null)
                return false;

            if (!(childTag instanceof CompoundTag))
                return true;

            current = (CompoundTag) childTag;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(@NotNull Object value) {
        return this.values()
            .stream()
            .anyMatch(tagValue -> Objects.equals(((value instanceof Tag<?>) ? tagValue : tagValue.getValue()), value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Set<Map.Entry<String, Tag<?>>> entrySet() {
        return this.getValue().entrySet();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void forEach(@NotNull Consumer<? super Entry<String, Tag<?>>> action) {
        this.getValue().forEach((key, value) -> action.accept(Pair.of(key, value)));
    }

    @Override
    public @NotNull CompoundTag fromJson(@NotNull JsonObject json, int depth, @NotNull TagRegistry registry) throws IOException {
        if (depth > 512)
            throw new IOException("NBT structure too complex (depth > 512).");

        this.clear();
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        Map<String, Tag<?>> tags = new LinkedHashMap<>();
        byte nextTypeId;
        Tag<?> next;

        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("value").entrySet()) {
            JsonObject entryJson = entry.getValue().getAsJsonObject();
            nextTypeId = entryJson.get("type").getAsByte();
            Class<? extends Tag<?>> tagClass = registry.getTypeFromId(nextTypeId).getTagClass();

            try {
                next = registry.instantiate(tagClass);
            } catch (TagTypeRegistryException e) {
                throw new IOException(e);
            }

            if (next instanceof RegistryTag<?> nextRTag)
                nextRTag.fromJson(entryJson, depth + 1, registry);
            else
                next.fromJson(entryJson, depth + 1);

            tags.put(next.getName(), next);
        }

        this.setValue(tags);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag<?> get(Object key) {
        return this.getValue().get(key);
    }

    public <T extends Tag<?>> ListTag<T> getList(@NotNull String key) {
        return this.getTag(key);
    }

    /**
     * Retrieve the value of a given entry in the tree.
     * <p>
     * Every element of the path (except the end) are assumed to be compounds. The
     * retrieval operation will be cancelled if any of them are missing.
     *
     * @param path The path to the entry.
     * @return The value, or NULL if not found.
     */
    public <T extends Tag<?>> T getPath(String path) {
        return this.getPathOrDefault(path, null);
    }

    /**
     * Retrieve the value of a given entry in the tree.
     * <p>
     * Every element of the path (except the end) are assumed to be compounds. The
     * retrieval operation will be cancelled if any of them are missing.
     *
     * @param path The path to the entry.
     * @return The value, or NULL if not found.
     */
    public <T extends Tag<?>> T getPathOrDefault(String path, T defaultValue) {
        T value = defaultValue;

        if (this.containsPath(path)) {
            List<String> entries = StringUtil.toList(StringUtil.split(path, "\\."));
            CompoundTag compoundTag = this.getMap(entries.subList(0, entries.size() - 1), false);
            value = compoundTag.getTag(entries.get(entries.size() - 1));
        }

        return value;
    }

    /**
     * Retrieve a map from a given path.
     *
     * @param path      The path of compounds to look up.
     * @param createNew Whether or not to create new compounds on the way.
     * @return The map at this location.
     */
    private CompoundTag getMap(List<String> path, boolean createNew) {
        CompoundTag current = this;

        for (String entry : path) {
            CompoundTag childTag = current.getTag(entry);

            if (childTag == null) {
                if (!createNew)
                    throw new IllegalArgumentException(String.format("Cannot find '%s' in '%s'.", entry, path));

                current.put(entry, childTag = new CompoundTag());
            }

            current = childTag;
        }

        return current;
    }

    /**
     * Retrieve the map by the given name.
     *
     * @param key The name of the map.
     * @return An existing or new map.
     */
    public CompoundTag getMap(String key) {
        return this.getMap(key, true);
    }

    /**
     * Retrieve the map by the given name.
     *
     * @param key       The name of the map.
     * @param createNew Whether or not to create a new map if its missing.
     * @return An existing map, a new map or null.
     */
    public CompoundTag getMap(String key, boolean createNew) {
        return this.getMap(Collections.singletonList(key), createNew);
    }

    /**
     * Retrieves a tag from this compound with a given name (key).
     *
     * @param key the name whose mapping is to be retrieved from this compound.
     * @param <T> the tag type you believe you are retrieving.
     * @return the value associated with {@code key} as type T.
     */
    public <T extends Tag<?>> T getTag(@NotNull String key) {
        return (T) this.get(key);
    }

    /**
     * Retrieve the value by the given key.
     *
     * @param key The name of the value.
     * @return An existing or null value.
     */
    public <T> T getValue(String key) {
        return this.getValue(key, null);
    }

    /**
     * Retrieve the value by the given key.
     *
     * @param key          The name of the value.
     * @param defaultValue The default value if key doesn't exist.
     * @return An existing or default value.
     */
    public <T> T getValue(String key, T defaultValue) {
        return this.containsKey(key) ? (T) this.get(key).getValue() : defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return this.getValue().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Iterator<Map.Entry<String, Tag<?>>> iterator() {
        return this.getValue().entrySet().iterator();
    }

    /**
     * Returns a {@code Set<>} of all names (keys) currently used within this compound.
     *
     * @return a {@code Set<>} of all names (keys) currently used within this compound.
     */
    @Override
    public @NotNull Set<String> keySet() {
        return this.getValue().keySet();
    }

    public boolean notEmpty() {
        return !this.isEmpty();
    }

    /**
     * Adds a given tag to this compound. The tag must have a name, or NPE is thrown.
     *
     * @param tag the named tag to be added to the compound.
     * @param <T> the type of an existing tag you believe you may be replacing (optional).
     * @return the previous value mapped with the tag's name as type E if provided, or null if there wasn't any.
     * @throws NullPointerException if the tag's name is null.
     */
    @SuppressWarnings("all")
    public <U, T extends Tag<U>> @Nullable T putTag(@NotNull Tag<?> tag) {
        return (T) this.put(tag.getName(), tag);
    }

    /**
     * Adds a given tag to this compound. Be careful, the tag's name is set to the {@code name} parameter automatically.
     *
     * @param key   the tag's name (key).
     * @param value the tag to be added to the compound.
     * @return the previous value mapped with the tag's name as type E if provided, or null if there wasn't any.
     */
    @Override
    public @Nullable Tag<?> put(@NotNull String key, @NotNull Tag<?> value) {
        value.setName(key);
        return this.getValue().put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(@NotNull Map<? extends String, ? extends Tag<?>> map) {
        map.forEach(this::put);
    }

    /**
     * Set the value of an entry at a given location.
     * <p>
     * Every element of the path (except the end) are assumed to be compounds, and will
     * be created if they are missing.
     *
     * @param path  The path to the entry.
     * @param value The new value of this entry.
     * @return This compound, for chaining.
     */
    public CompoundTag putPath(String path, Tag<?> value) {
        List<String> entries = StringUtil.toList(StringUtil.split(path, "\\."));
        CompoundTag map = this.getMap(entries.subList(0, entries.size() - 1), true);
        map.put(entries.get(entries.size() - 1), value);
        // TODO: Save tag data
        return this;
    }

    @Override
    @SuppressWarnings("all")
    public @NotNull CompoundTag read(@NotNull DataInput input, int depth, @NotNull TagRegistry registry) throws IOException {
        if (depth > 512)
            throw new IOException("NBT structure too complex (depth > 512).");

        this.clear();
        byte nextTypeId;
        Tag<?> next;

        while ((nextTypeId = input.readByte()) != 0) {
            Class<? extends Tag<?>> tagClass = registry.getTypeFromId(nextTypeId).getTagClass();

            try {
                next = registry.instantiate(tagClass);
            } catch (TagTypeRegistryException e) {
                throw new IOException(e);
            }

            next.setName(input.readUTF());

            if (next instanceof RegistryTag<?> nextRTag)
                nextRTag.read(input, depth + 1, registry);
            else
                next.read(input, depth + 1);

            this.put(next.getName(), next);
        }

        return this;
    }

    /**
     * Removes a tag from this compound with a given name (key).
     *
     * @param key the key whose mapping is to be removed from this compound.
     * @return the previous value associated with {@code key}.
     */
    @Override
    public Tag<?> remove(Object key) {
        return this.getValue().remove(key);
    }

    /**
     * Remove the value of a given entry in the tree.
     * <p>
     * Every element of the path (except the end) are assumed to be compounds. The
     * retrieval operation will return the last most compound.
     *
     * @param path The path to the entry.
     * @return The last most compound, or this compound if not found..
     */
    public CompoundTag removePath(String path) {
        List<String> entries = StringUtil.toList(StringUtil.split(path, "\\."));
        CompoundTag currentTag = this;

        for (int i = 0; i < entries.size(); i++) {
            String entry = entries.get(i);

            if (i == entries.size() - 1/* && !this.isRootTag(entry)*/)
                currentTag.remove(entry);
            else
                currentTag = currentTag.getTag(entry);
        }

        // TODO: Save tag data
        return currentTag;
    }

    /**
     * Removes a tag from this compound with a given name (key).
     *
     * @param key the name whose mapping is to be removed from this compound.
     * @param <T> the tag type you believe you are removing (optional).
     * @return the previous value associated with {@code key} as type T if provided.
     */
    public <T extends Tag<?>> T removeTag(@NotNull String key) {
        return (T) this.getValue().remove(key);
    }

    /**
     * Remove the value of a given entry.
     *
     * @param key The name of the value.
     * @return The previous value, or NULL if not found.
     */
    public <T> T removeValue(String key) {
        return (T) this.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return this.getValue().size();
    }

    @Override
    public Spliterator<Map.Entry<String, Tag<?>>> spliterator() {
        return this.getValue().entrySet().spliterator();
    }

    @Override
    public @NotNull JsonObject toJson(int depth) throws IOException {
        if (depth > 512)
            throw new IOException("NBT structure too complex (depth > 512).");

        JsonObject json = new JsonObject();
        JsonObject value = new JsonObject();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        for (Tag<?> tag : this.getValue().values()) {
            try {
                value.add(StringUtil.stripToEmpty(tag.getName()), tag.toJson(depth + 1));
            } catch (Exception ex) {
                throw new IOException("Tag not JsonSerializable.", ex);
            }
        }

        json.add("value", value);
        return json;
    }

    @Override
    public @NotNull String toSnbt(int depth, @NotNull SnbtConfig config) {
        if (this.isEmpty())
            return "{}";

        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        if (config.isPrettyPrint())
            sb.append('\n').append(SnbtUtil.multiplyIndent(depth + 1, config));

        for (Tag<?> tag : this.getValue().values()) {
            if (!first) {
                if (config.isPrettyPrint())
                    sb.append(",\n").append(SnbtUtil.multiplyIndent(depth + 1, config));
                else
                    sb.append(',');
            }

            sb.append(SnbtUtil.escape(StringUtil.stripToEmpty(tag.getName())));
            sb.append(String.format(":%s", config.isPrettyPrint() ? " " : ""));
            sb.append(tag.toSnbt(depth + 1, config));

            if (first)
                first = false;
        }

        if (config.isPrettyPrint())
            sb.append("\n").append(SnbtUtil.multiplyIndent(depth, config)).append('}');
        else
            sb.append('}');

        return sb.toString();
    }

    /**
     * Returns all {@link Tag}s contained within this compound.
     *
     * @return all {@link Tag}s contained within this compound.
     */
    @Override
    public @NotNull Collection<Tag<?>> values() {
        return this.getValue().values();
    }

    @Override
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        if (depth > 512)
            throw new IOException("NBT structure too complex (depth > 512).");

        for (Map.Entry<String, Tag<?>> entry : this) {
            Tag<?> tag = entry.getValue();
            output.writeByte(tag.getTypeId());
            output.writeUTF(StringUtil.stripToEmpty(tag.getName()));
            tag.write(output, depth + 1);
        }

        output.writeByte(0);
    }

}
