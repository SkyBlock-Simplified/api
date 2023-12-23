package dev.sbs.api.minecraft.nbt.tags.collection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.exception.TagTypeRegistryException;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtUtil;
import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.TagRegistry;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.minecraft.nbt.tags.impl.RegistryTag;
import dev.sbs.api.util.mutable.triple.Triple;
import dev.sbs.api.util.stream.StreamUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The list tag (type ID 9) is used for storing an ordered list of unnamed NBT tags all the same type.
 */
@SuppressWarnings("unchecked")
public final class ListTag<E extends Tag<?>> extends RegistryTag<List<E>> implements List<E> {

    private byte tagTypeId;

    /**
     * Constructs an empty, unnamed list tag.
     */
    public ListTag() {
        this(null);
    }

    /**
     * Constructs an empty list tag with a given name.
     *
     * @param name the tag's name.
     */
    public ListTag(@Nullable String name) {
        this(name, new LinkedList<>());
    }

    /**
     * Constructs a list tag with a given name and {@code List<>} value.
     *
     * @param name  the tag's name.
     * @param value the tag's {@code List<>} value.
     */
    public ListTag(@Nullable String name, @NotNull List<E> value) {
        super(TagType.LIST.getId(), name, value, true);
    }

    /**
     * Appends the specified element to the end of the list. Returns true if added successfully.
     *
     * @param element the element to be added.
     * @return true if added successfully.
     */
    @Override
    public boolean add(@NotNull E element) {
        this.requireModifiable();

        if (this.getValue().isEmpty())
            this.tagTypeId = element.getTypeId();

        if (element.getTypeId() != this.getListType())
            return false;

        return this.getValue().add(element);
    }

    /**
     * Inserts the specified tag at the specified position in this list.
     * Shifts the tag currently at that position and any subsequent tags to the right.
     *
     * @param index   index at which the tag is to be inserted.
     * @param element element to be inserted.
     */
    @Override
    public void add(int index, E element) {
        this.requireModifiable();

        if (this.getValue().isEmpty())
            this.tagTypeId = element.getTypeId();

        if (element.getTypeId() != this.getListType())
            return;

        this.getValue().add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean changed = false;

        for (E element : collection)
            changed |= this.add(element);

        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        int previousSize = this.size();
        int start = -1;

        for (E element : collection)
            this.add(index + ++start, element);

        return this.size() > previousSize;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        int resultSize = this.size() - collection.size();
        this.removeIf(tag -> collection.contains(tag) || collection.contains(tag.getValue()));
        return this.size() == resultSize;
    }

    @Override
    public boolean removeIf(@NotNull Predicate<? super E> filter) {
        this.requireModifiable();
        return this.getValue().removeIf(filter);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        int resultSize = this.size() - collection.size();
        this.removeIf(tag -> !(collection.contains(tag) || collection.contains(tag.getValue())));
        return this.size() == resultSize;
    }

    /**
     * Removes all tags from the list. The list will be empty after this call returns.
     */
    @Override
    public void clear() {
        this.requireModifiable();
        this.tagTypeId = 0;
        this.getValue().clear();
    }

    /**
     * Returns true if this list contains the tag, false otherwise.
     *
     * @param obj the tag to check for.
     * @return true if this list contains the tag, false otherwise.
     */
    @Override
    public boolean contains(Object obj) {
        return this.getValue()
            .stream()
            .anyMatch(tag -> Objects.equals((obj instanceof Tag<?>) ? tag : tag.getValue(), obj));
    }

    /**
     * Returns true if this list contains all tags in the collection, false otherwise.
     *
     * @param collection the values to be checked for.
     * @return true if this list contains all tags in the collection, false otherwise.
     */
    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        if (this.isEmpty() || collection.isEmpty())
            return false;

        boolean isTags = collection.stream().findFirst().orElseThrow() instanceof Tag<?>;

        return this.getValue()
            .stream()
            .allMatch(tag -> collection.contains(isTags ? tag : tag.getValue()));
    }

    @Override
    public void forEach(@NotNull Consumer<? super E> action) {
        for (E tag : this)
            action.accept(tag);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull ListTag<E> fromJson(@NotNull JsonObject json, int depth, @NotNull TagRegistry registry) throws IOException {
        if (depth > 512) {
            throw new IOException("NBT structure too complex (depth > 512).");
        }

        this.clear();
        this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
        byte listTypeId = json.get("listType").getAsByte();

        E nextTag;
        for (JsonElement element : json.getAsJsonArray("value")) {
            Class<? extends Tag<?>> tagClass = registry.getTypeFromId(listTypeId).getTagClass();

            try {
                nextTag = (E) registry.instantiate(tagClass);
            } catch (TagTypeRegistryException e) {
                throw new IOException(e);
            }

            if (nextTag instanceof RegistryTag<?> nextRTag)
                nextRTag.fromJson((JsonObject) element, depth + 1, registry);
            else
                nextTag.fromJson((JsonObject) element, depth + 1);

            this.add(nextTag);
        }

        this.tagTypeId = this.isEmpty() ? 0 : listTypeId;
        return this;
    }

    /**
     * Retrieves a tag value from its index in the list.
     *
     * @param index the index of the tag value to be retrieved.
     * @return the tag value at the specified index.
     */
    @Override
    public @NotNull E get(int index) {
        return this.getValue().get(index);
    }

    /**
     * Returns the tag id of the NBT tag type this list holds.
     *
     * @return the ID of the NBT tag type this list holds.
     */
    public byte getListType() {
        return this.tagTypeId;
    }

    @Override
    public int indexOf(@Nullable Object obj) {
        return StreamUtil.zipWithIndex(this.getValue().stream())
            .filterLeft(tag -> Objects.equals((obj instanceof Tag<?>) ? tag : tag.getValue(), obj))
            .map(Triple::getMiddle)
            .reduce((f, s) -> f)
            .map(Long::intValue)
            .orElse(-1);
    }

    /**
     * Returns true if this list tag is empty, false otherwise.
     *
     * @return true if this list tag is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return this.getValue().isEmpty();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return this.getValue().iterator();
    }

    @Override
    public int lastIndexOf(@Nullable Object obj) {
        return StreamUtil.zipWithIndex(this.getValue().stream())
            .filterLeft(tag -> Objects.equals((obj instanceof Tag<?>) ? tag : tag.getValue(), obj))
            .map(Triple::getMiddle)
            .reduce((f, s) -> s)
            .map(Long::intValue)
            .orElse(-1);
    }

    @Override
    public @NotNull ListIterator<E> listIterator() {
        return this.listIterator(0);
    }

    @Override
    public @NotNull ListIterator<E> listIterator(int index) {
        return this.getValue().listIterator();
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull ListTag<E> read(@NotNull DataInput input, int depth, @NotNull TagRegistry registry) throws IOException {
        if (depth > 512)
            throw new IOException("NBT structure too complex (depth > 512).");

        this.clear();
        byte tagTypeId = input.readByte();
        int length = input.readInt();

        E next;
        for (int i = 0; i < length; i++) {
            Class<? extends Tag<?>> tagClass = registry.getTypeFromId(tagTypeId).getTagClass();

            try {
                next = (E) registry.instantiate(tagClass);
            } catch (TagTypeRegistryException e) {
                throw new IOException(e);
            }

            if (next instanceof RegistryTag<?> nextRTag)
                nextRTag.read(input, depth + 1, registry);
            else
                next.read(input, depth + 1);

            next.setName(null);
            this.getValue().add(next);
        }

        this.tagTypeId = this.isEmpty() ? 0 : tagTypeId;
        return this;
    }

    /**
     * Removes a tag from the list based on the tag's index. Returns the removed tag.
     *
     * @param index the index of the tag to be removed.
     * @return the removed tag.
     */
    @Override
    public @NotNull E remove(int index) {
        this.requireModifiable();
        E previous = this.getValue().remove(index);

        if (this.isEmpty())
            this.tagTypeId = 0;

        return previous;
    }

    /**
     * Removes a given tag from the list. Returns true if removed successfully, false otherwise.
     *
     * @param obj the tag to be removed.
     * @return true if the tag was removed successfully, false otherwise.
     */
    @Override
    public boolean remove(Object obj) {
        this.requireModifiable();
        boolean result = this.removeIf(tag -> Objects.equals((obj instanceof Tag<?>) ? tag : tag.getValue(), obj));

        if (this.isEmpty())
            this.tagTypeId = 0;

        return result;
    }

    @Override
    public E set(int index, @NotNull E element) {
        this.requireModifiable();
        return this.getValue().set(index, element);
    }

    /**
     * Returns the number of elements in this list tag.
     *
     * @return the number of elements in this list tag.
     */
    @Override
    public int size() {
        return this.getValue().size();
    }

    @Override
    public @NotNull Spliterator<E> spliterator() {
        return this.getValue().spliterator();
    }

    @Override
    public @NotNull ListTag<E> subList(int fromIndex, int toIndex) {
        return new ListTag<>(this.getName(), this.getValue().subList(fromIndex, toIndex));
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return this.getValue().toArray();
    }

    @Override
    public <T> @NotNull T @NotNull [] toArray(@NotNull T @NotNull [] array) {
        return this.getValue().toArray(array);
    }

    @Override
    public @NotNull JsonObject toJson(int depth) throws IOException {
        if (depth > 512)
            throw new IOException("NBT structure too complex (depth > 512).");

        JsonObject json = new JsonObject();
        JsonArray value = new JsonArray();
        json.addProperty("type", this.getTypeId());
        json.addProperty("listType", this.getListType());

        if (this.getName() != null)
            json.addProperty("name", this.getName());

        for (E tag : this.getValue()) {
            tag.setName(null);
            value.add(tag.toJson(depth + 1));
        }

        json.add("value", value);
        return json;
    }

    @Override
    public @NotNull String toSnbt(int depth, @NotNull SnbtConfig config) {
        StringBuilder sb = new StringBuilder("[");

        if (config.isPrettyPrint())
            sb.append('\n').append(SnbtUtil.multiplyIndent(depth + 1, config));

        for (int i = 0; i < this.getValue().size(); ++i) {
            if (i != 0) {
                if (config.isPrettyPrint())
                    sb.append(",\n").append(SnbtUtil.multiplyIndent(depth + 1, config));
                else
                    sb.append(',');
            }

            sb.append(this.getValue().get(i).toSnbt(depth + 1, config));
        }

        if (config.isPrettyPrint())
            sb.append("\n").append(SnbtUtil.multiplyIndent(depth, config)).append(']');
        else
            sb.append(']');

        return sb.toString();
    }

    @Override
    public void write(@NotNull DataOutput output, int depth) throws IOException {
        if (depth > 512)
            throw new IOException("NBT structure too complex (depth > 512).");

        output.writeByte(this.tagTypeId);
        output.writeInt(this.getValue().size());

        for (E tag : this.getValue()) {
            tag.write(output, depth + 1);
        }
    }

}
