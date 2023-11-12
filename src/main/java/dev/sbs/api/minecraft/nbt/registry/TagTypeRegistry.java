package dev.sbs.api.minecraft.nbt.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.TagType;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.reflection.exception.ReflectionException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A registry mapping {@code byte} tag type IDs to tag type classes. Used to register custom-made {@link Tag} types.
 */
public class TagTypeRegistry {

    private final BiMap<Byte, Class<?>> typeRegistry = HashBiMap.create();

    {
        TagType.registerAllTypes(this);
    }

    private final BiMap<Byte, Class<? extends Tag<?>>> tagRegistry = HashBiMap.create();

    {
        TagType.registerAllTags(this);
    }

    /**
     * Register a java class type with a unique {@code byte} ID. IDs 0-12 (inclusive) are reserved and may not be used.
     *
     * @param id     the class type's unique ID used in reading and writing.
     * @param tClass the java type class.
     * @throws TagTypeRegistryException if the ID provided is either registered already or is a reserved ID (0-12 inclusive).
     */
    public void registerClassType(byte id, @NotNull Class<?> tClass) throws TagTypeRegistryException {
        if (id == 0)
            throw new TagTypeRegistryException(String.format("Cannot register class type '%s' with ID %s. That ID is reserved.", tClass.getSimpleName(), id));

        if (this.typeRegistry.containsKey(id))
            throw new TagTypeRegistryException(String.format("Cannot register class type '%s' with ID %s. That ID is already registered by the class type '%s'.", tClass, id, this.typeRegistry.get(id).getSimpleName()));

        if (this.typeRegistry.containsValue(tClass)) {
            byte existing = 0;
            for (Map.Entry<Byte, Class<?>> entry : this.typeRegistry.entrySet()) {
                if (entry.getValue().equals(tClass))
                    existing = entry.getKey();
            }

            throw new TagTypeRegistryException(String.format("Class type '%s' already registered under ID %s." + existing, tClass.getSimpleName(), existing));
        }

        this.typeRegistry.put(id, tClass);
    }

    /**
     * Register a custom-made tag type with a unique {@code byte} ID. IDs 0-12 (inclusive) are reserved and may not be used.
     *
     * @param id       the tag type's unique ID used in reading and writing.
     * @param tagClass the tag type class.
     * @throws TagTypeRegistryException if the ID provided is either registered already or is a reserved ID (0-12 inclusive).
     */
    public void registerTagType(byte id, @NotNull Class<? extends Tag<?>> tagClass) throws TagTypeRegistryException {
        if (id == 0)
            throw new TagTypeRegistryException(String.format("Cannot register NBT tag type '%s' with ID %s. That ID is reserved.", tagClass.getSimpleName(), id));

        if (this.tagRegistry.containsKey(id))
            throw new TagTypeRegistryException(String.format("Cannot register NBT tag type '%s' with ID %s. That ID is already registered by the tag type '%s'.", tagClass, id, this.tagRegistry.get(id).getSimpleName()));

        if (this.tagRegistry.containsValue(tagClass)) {
            byte existing = 0;
            for (Map.Entry<Byte, Class<? extends Tag<?>>> entry : this.tagRegistry.entrySet()) {
                if (entry.getValue().equals(tagClass))
                    existing = entry.getKey();
            }

            throw new TagTypeRegistryException(String.format("NBT tag type '%s' already registered under ID %s." + existing, tagClass.getSimpleName(), existing));
        }

        this.tagRegistry.put(id, tagClass);
    }

    /**
     * Unregister a custom-made tag type with a provided tag type ID.
     *
     * @param id the ID of the tag type to unregister.
     * @return if the tag type was unregistered successfully.
     */
    public boolean unregisterTagType(byte id) {
        if (id >= 0 && id <= 12)
            return false; // Do not unregister reserved tag types

        return this.typeRegistry.remove(id) != null && this.tagRegistry.remove(id) != null;
    }

    /**
     * Returns a java type class value from the registry from a provided {@code byte} ID.
     *
     * @param id the ID of the java type to retrieve.
     * @return a java type class value from the registry from a provided {@code byte} ID.
     */
    public Class<?> getTypeClassFromId(byte id) {
        return this.typeRegistry.get(id);
    }

    /**
     * Returns a tag type class value from the registry from a provided {@code byte} ID.
     *
     * @param id the ID of the tag type to retrieve.
     * @return a tag type class value from the registry from a provided {@code byte} ID.
     */
    public Class<? extends Tag<?>> getTagClassFromId(byte id) {
        return this.tagRegistry.get(id);
    }

    /**
     * Returns a tag type class value from the registry from a provided type {@code Class}.
     *
     * @param tClass the type class of the tag type to retrieve.
     * @return a tag type class value from the registry from a provided type {@code Class}.
     */
    public Class<? extends Tag<?>> getTagClassFromTypeClass(Class<?> tClass) {
        return this.tagRegistry.get(this.typeRegistry.inverse().get(tClass));
    }

    /**
     * Returns a tag type class value from the registry from a provided {@code byte} ID.
     *
     * @param tClass the nbt tag type of the tag id to retrieve.
     * @return a {@code byte} ID from the registry from a provided tag type class value.
     */
    public byte getIdFromTypeClass(Class<?> tClass) {
        return this.typeRegistry.inverse().get(tClass);
    }

    /**
     * Returns a tag type class value from the registry from a provided {@code byte} ID.
     *
     * @param tagClass the nbt tag type of the tag id to retrieve.
     * @return a {@code byte} ID from the registry from a provided tag type class value.
     */
    public byte getIdFromTagClass(Class<? extends Tag<?>> tagClass) {
        return this.tagRegistry.inverse().get(tagClass);
    }

    /**
     * Returns an empty instance of the given {@link Tag} type, with a {@code null} name and a default (possibly {@code null}) value.
     * Only use this if you really know what you're doing.
     *
     * @param tagClass the tag type to instantiate.
     * @return an empty instance of the tag type provided.
     * @throws TagTypeRegistryException if a reflection error occurs when instantiating the tag.
     */
    public Tag<?> instantiate(@NotNull Class<? extends Tag<?>> tagClass) throws TagTypeRegistryException {
        try {
            Tag<?> whatIsThis = Reflection.of(tagClass).newInstance();

            try {
                whatIsThis.setRegistry(this);
                return whatIsThis;
            } catch (ClassCastException cce) {
                return null;
            }
        } catch (ReflectionException e) {
            throw new TagTypeRegistryException(String.format("Instance of tag type class '%s' could not be created.", tagClass.getSimpleName()), e);
        }
    }

    /**
     * Returns an empty instance of the given {@link Tag} type, with a {@code null} name and a default (possibly {@code null}) value.
     * Only use this if you really know what you're doing.
     *
     * @param tagClass the tag type to instantiate.
     * @return an empty instance of the tag type provided.
     * @throws TagTypeRegistryException if a reflection error occurs when instantiating the tag.
     */
    public Tag<?> instantiate(@NotNull Class<? extends Tag<?>> tagClass, Object value) throws TagTypeRegistryException {
        return this.instantiate(tagClass, null, value);
    }

    /**
     * Returns an empty instance of the given {@link Tag} type, with a {@code null} name and a default (possibly {@code null}) value.
     * Only use this if you really know what you're doing.
     *
     * @param tagClass the tag type to instantiate.
     * @return an empty instance of the tag type provided.
     * @throws TagTypeRegistryException if a reflection error occurs when instantiating the tag.
     */
    public Tag<?> instantiate(@NotNull Class<? extends Tag<?>> tagClass, String name, Object value) throws TagTypeRegistryException {
        try {
            Tag<?> tag = Reflection.of(tagClass).newInstance(name, value);
            tag.setRegistry(this);
            return tag;
        } catch (ReflectionException e) {
            throw new TagTypeRegistryException(String.format("Instance of tag type class '%s' could not be created.", tagClass.getSimpleName()), e);
        }
    }

}
