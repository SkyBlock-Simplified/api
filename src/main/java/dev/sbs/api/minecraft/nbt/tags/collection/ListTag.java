package dev.sbs.api.minecraft.nbt.tags.collection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistryException;
import dev.sbs.api.minecraft.nbt.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.snbt.SnbtUtil;
import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.ListUtil;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * The list tag (type ID 9) is used for storing an ordered list of unnamed NBT tags all the same type.
 */
@SuppressWarnings("unchecked")
public class ListTag<E extends Tag<?>> extends Tag<List<E>> implements List<E> {

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
	public ListTag(String name) {
		this(name, new LinkedList<>());
	}

	/**
	 * Constructs a list tag with a given name and {@code List<>} value.
	 *
	 * @param name the tag's name.
	 * @param value the tag's {@code List<>} value.
	 */
	public ListTag(String name, @NonNull List<E> value) {
		super(name, value, new TagTypeRegistry());
	}

	private E createTag2(Object element) {
		return (E) this.registry.instantiate(this.registry.getTagClassFromId(this.tagTypeId), element);
	}

	/**
	 * Appends the specified element to the end of the list. Returns true if added successfully.
	 *
	 * @param element the element to be added.
	 * @return true if added successfully.
	 */
	@Override
	public boolean add(@NonNull E element) {
		if (this.value.isEmpty())
			this.tagTypeId = this.registry.getIdFromTypeClass(element.getClass());

		if (this.registry.getIdFromTypeClass(element.getClass()) != this.tagTypeId)
			return false;

		//Tag<E> wrappedElement = this.createTag(element);
		return this.value.add(element);
	}

	/**
	 * Inserts the specified tag at the specified position in this list.
	 * Shifts the tag currently at that position and any subsequent tags to the right.
	 *
	 * @param index index at which the tag is to be inserted.
	 * @param element element to be inserted.
	 */
	@Override
	public void add(int index, E element) {
		if (this.value.isEmpty())
			this.tagTypeId = this.registry.getIdFromTypeClass(element.getClass());

		if (this.registry.getIdFromTypeClass(element.getClass()) != this.tagTypeId)
			return;

		//Tag<E> wrappedElement = this.createTag(element);
		this.value.add(index, element);
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
	public boolean removeAll(@NotNull Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> c) {
		return false;
	}

	/**
	 * Removes all tags from the list. The list will be empty after this call returns.
	 */
	@Override
	public void clear() {
		this.tagTypeId = 0;
		this.value.clear();
	}

	/**
	 * Returns true if this list contains the tag, false otherwise.
	 *
	 * @param obj the tag to check for.
	 * @return true if this list contains the tag, false otherwise.
	 */
	@Override
	public boolean contains(Object obj) {
		return this.value.contains(obj instanceof Tag ? obj : this.createTag2(obj));
	}

	/**
	 * Returns true if this list contains all tags in the collection, false otherwise.
	 *
	 * @param tags the tags to be checked for.
	 * @return true if this list contains all tags in the collection, false otherwise.
	 */
	@Override
	@SuppressWarnings("all")
	public boolean containsAll(@NotNull Collection<?> collection) {
		Object firstElement = collection.stream().findFirst().orElse(null);

		if (firstElement == null)
			return false;

		if (firstElement instanceof Tag)
			return this.value.containsAll(collection);
		else {
			Collection<Tag<?>> tagCollection = Concurrent.newCollection();
			byte tagTypeId = this.registry.getIdFromTypeClass(firstElement.getClass());

			collection.forEach(element -> {
				Tag<?> tag = this.registry.instantiate(this.registry.getTagClassFromId(tagTypeId), element);
				tagCollection.add(tag);
			});

			return this.value.containsAll(tagCollection);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		ListTag<?> that = (ListTag<?>) obj;

		return new EqualsBuilder()
				.append(this.value, that.value)
				.append(this.tagTypeId, that.tagTypeId)
				.build();
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		for (E tag : this)
			action.accept(tag);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ListTag<E> fromJson(JsonObject json, int depth, TagTypeRegistry registry) throws IOException {
		if (depth > 512) {
			throw new IOException("NBT structure too complex (depth > 512).");
		}

		this.clear();
		this.registry = registry;
		this.setName(json.has("name") ? json.getAsJsonPrimitive("name").getAsString() : null);
		byte listTypeId = json.get("listType").getAsByte();

		E nextTag;
		for (JsonElement element : json.getAsJsonArray("value")) {
			Class<? extends Tag<?>> tagClass = registry.getTagClassFromId(listTypeId);

			if (tagClass == null)
				throw new IOException(FormatUtil.format("Tag type with ID {0} not present in tag type registry.", listTypeId));

			try {
				nextTag = (E) registry.instantiate(tagClass);
			} catch (TagTypeRegistryException e) {
				throw new IOException(e);
			}

			nextTag.fromJson((JsonObject) element, depth + 1, registry);
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
	public E get(int index) {
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
	public byte getTypeId() {
		return TagType.LIST.getId();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.value)
				.append(this.tagTypeId)
				.build();
	}

	@Override
	public int indexOf(Object obj) {
		return this.value.indexOf(obj instanceof Tag ? obj : this.createTag2(obj));
	}

	/**
	 * Returns true if this list tag is empty, false otherwise.
	 *
	 * @return true if this list tag is empty, false otherwise.
	 */
	@Override
	public boolean isEmpty() {
		return this.value.isEmpty();
	}

	@Nonnull
	@Override
	public Iterator<E> iterator() {
		return this.getValue().iterator();
	}

	@Override
	public int lastIndexOf(Object obj) {
		return this.getValue().lastIndexOf(obj instanceof Tag ? obj : this.createTag2(obj));
	}

	@NotNull
	@Override
	public ListIterator<E> listIterator() {
		return this.listIterator(0);
	}

	@NotNull
	@Override
	public ListIterator<E> listIterator(int index) {
		return this.getValue().listIterator();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ListTag<E> read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
		if (depth > 512)
			throw new IOException("NBT structure too complex (depth > 512).");

		this.clear();
		byte tagType = input.readByte();
		int length = input.readInt();

		E next;
		for (int i = 0; i < length; i++) {
			Class<? extends Tag<?>> tagClass = registry.getTagClassFromId(tagType);

			if (tagClass == null)
				throw new IOException(FormatUtil.format("Tag type with ID {0} not present in tag type registry.", tagType));

			try {
				next = (E) registry.instantiate(tagClass);
			} catch (TagTypeRegistryException e) {
				throw new IOException(e);
			}

			next.read(input, depth + 1, registry);
			next.setName(null);
			this.value.add(next);
		}

		this.tagTypeId = this.isEmpty() ? 0 : tagType;
		return this;
	}

	/**
	 * Removes a tag from the list based on the tag's index. Returns the removed tag.
	 *
	 * @param index the index of the tag to be removed.
	 * @return the removed tag.
	 */
	@Override
	public E remove(int index) {
		E previous = this.getValue().remove(index);

		if (this.getValue().isEmpty())
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
		boolean result = this.getValue().remove(obj instanceof Tag ? obj : this.createTag2(obj));

		if (ListUtil.isEmpty(this.value))
			this.tagTypeId = 0;

		return result;
	}

	@Override
	public E set(int index, E element) {
		return this.getValue().set(index, this.createTag2(element));
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
	public Spliterator<E> spliterator() {
		return this.getValue().spliterator();
	}

	@NotNull
	@Override
	public ListTag<E> subList(int fromIndex, int toIndex) {
		return new ListTag<>(this.getName(), this.getValue().subList(fromIndex, toIndex));
	}

	@NotNull
	@Override
	public Object[] toArray() {
		return this.getValue().toArray();
	}

	@NotNull
	@Override
	@SuppressWarnings("all")
	public <T> T[] toArray(@NotNull T[] array) {
		return this.getValue().toArray(array);
	}

	@Override
	public JsonObject toJson(int depth, TagTypeRegistry registry) throws IOException {
		if (depth > 512)
			throw new IOException("NBT structure too complex (depth > 512).");

		JsonObject json = new JsonObject();
		JsonArray value = new JsonArray();
		json.addProperty("type", this.getTypeId());
		json.addProperty("listType", this.getListType());

		if (this.getName() != null)
			json.addProperty("name", this.getName());

		for (E tag : this.value) {
			tag.setName(null);
			value.add(tag.toJson(depth + 1, registry));
		}

		json.add("value", value);

		return json;
	}

	@Override
	public String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config) {
		StringBuilder sb = new StringBuilder("[");

		if (config.isPrettyPrint())
			sb.append('\n').append(SnbtUtil.multiplyIndent(depth + 1, config));

		for (int i = 0; i < this.value.size(); ++i) {
			if (i != 0) {
				if (config.isPrettyPrint())
					sb.append(",\n").append(SnbtUtil.multiplyIndent(depth + 1, config));
				else
					sb.append(',');
			}

			sb.append(this.value.get(i).toSnbt(depth + 1, registry, config));
		}

		if (config.isPrettyPrint())
			sb.append("\n").append(SnbtUtil.multiplyIndent(depth , config)).append(']');
		else
			sb.append(']');

		return sb.toString();
	}

	@Override
	public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
		if (depth > 512)
			throw new IOException("NBT structure too complex (depth > 512).");

		output.writeByte(this.tagTypeId);
		output.writeInt(this.value.size());

		for (E tag : this.value)
			tag.write(output, depth + 1, registry);
	}

}
