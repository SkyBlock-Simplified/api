package dev.sbs.api.minecraft.nbt.tags;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.sbs.api.minecraft.nbt.exception.TagTypeRegistryException;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.reflection.exception.ReflectionException;
import dev.sbs.api.util.SimplifiedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A registry mapping {@code byte} tag type IDs to tag type classes. Used to register custom-made {@link Tag} types.
 */
public class TagRegistry {

    private final BiMap<Byte, Class<?>> types = HashBiMap.create();
    private final BiMap<Byte, Class<? extends Tag<?>>> tags = HashBiMap.create();

    public TagRegistry() {
        for (TagType tagType : TagType.values())
            this.registerDefault(tagType.getId(), tagType.getJavaClass(), tagType.getTagClass());
    }

    /**
     * Register a custom-made {@link Tag} type with a unique {@code byte} ID.
     * <br><br>
     * IDs 0-12 (inclusive) are reserved and may not be used.
     *
     * @param id The class type's unique ID used in reading and writing.
     * @param tClass The java type class.
     * @param tagClass The tag type class.
     * @throws TagTypeRegistryException If the ID provided is either registered already or is a reserved ID (0-12 inclusive).
     */
    public <U, T extends Tag<U>> void register(int id, @NotNull Class<U> tClass, @NotNull Class<T> tagClass) throws TagTypeRegistryException {
        this.register((byte) id, tClass, tagClass);
    }

    /**
     * Register a custom-made {@link Tag} type with a unique {@code byte} ID.
     * <br><br>
     * IDs 0-12 (inclusive) are reserved and may not be used.
     *
     * @param id The class type's unique ID used in reading and writing.
     * @param tClass The java type class.
     * @param tagClass The tag type class.
     * @throws TagTypeRegistryException If the ID provided is either registered already or is a reserved ID (0-12 inclusive).
     */
    public <U, T extends Tag<U>> void register(byte id, @NotNull Class<U> tClass, @NotNull Class<T> tagClass) throws TagTypeRegistryException {
        this.registerDefault(id, tClass, tagClass);
    }

    /**
     * Register a java class and custom-made tag type with a unique {@code byte} ID.
     * <br><br>
     * IDs 0-12 (inclusive) are reserved and may not be used.
     *
     * @param id The class type's unique ID used in reading and writing.
     * @param tClass The java type class.
     * @param tagClass The tag type class.
     * @throws TagTypeRegistryException If the ID provided is either registered already or is a reserved ID (0-12 inclusive).
     */
    void registerDefault(byte id, @NotNull Class<?> tClass, @NotNull Class<? extends Tag<?>> tagClass) throws TagTypeRegistryException {
        if (id == 0) {
            throw SimplifiedException.of(TagTypeRegistryException.class)
                .withMessage(
                    "Cannot register Tag ('%s', '%s') with ID %s. ID is reserved.",
                    tClass.getSimpleName(), tagClass.getSimpleName(), id
                )
                .build();
        }

        if (this.types.containsKey(id)) {
            throw SimplifiedException.of(TagTypeRegistryException.class)
                .withMessage(
                    "Cannot register Tag ('%s', '%s') with ID %s. ID is already registered as ('%s', '%s').",
                    tClass.getSimpleName(), tagClass.getSimpleName(), id,
                    this.types.get(id).getSimpleName(), this.tags.get(id).getSimpleName()
                )
                .build();
        }

        if (this.types.containsValue(tClass)) {
            byte existing = 0;
            for (Map.Entry<Byte, Class<?>> entry : this.types.entrySet()) {
                if (entry.getValue().equals(tClass))
                    existing = entry.getKey();
            }

            throw SimplifiedException.of(TagTypeRegistryException.class)
                .withMessage(
                    "Cannot register Tag ('%s', '%s') with ID %s. Tag is already registered with ID %s.",
                    tClass.getSimpleName(), tagClass.getSimpleName(), id, existing
                )
                .build();
        }

        this.types.put(id, tClass);
        this.tags.put(id, tagClass);
    }

    /**
     * Unregister a custom-made tag type with a provided tag type ID.
     * <br><br>
     * IDs 0-12 (inclusive) are reserved and may not be unregistered.
     *
     * @param id the ID of the tag type to unregister.
     * @return if the tag type was unregistered successfully.
     */
    public boolean unregisterTagType(byte id) {
        if (id <= 12)
            return false; // Do not unregister reserved tag types

        return this.types.remove(id) != null && this.tags.remove(id) != null;
    }

    /**
     * Returns a java type class value from the registry from a provided {@code byte} ID.
     *
     * @param id the ID of the java type to retrieve.
     * @return a java type class value from the registry from a provided {@code byte} ID.
     */
    public @NotNull Class<?> getTypeClassFromId(byte id) throws TagTypeRegistryException {
        if (!this.types.containsKey(id)) {
            throw SimplifiedException.of(TagTypeRegistryException.class)
                .withMessage("Tag with ID %s is not registered!", id)
                .build();
        }

        return this.types.get(id);
    }

    /**
     * Returns a tag type class value from the registry from a provided {@code byte} ID.
     *
     * @param id the ID of the tag type to retrieve.
     * @return a tag type class value from the registry from a provided {@code byte} ID.
     */
    public @NotNull Class<? extends Tag<?>> getTagClassFromId(byte id) throws TagTypeRegistryException {
        if (!this.tags.containsKey(id)) {
            throw SimplifiedException.of(TagTypeRegistryException.class)
                .withMessage("Tag with ID %s is not registered!", id)
                .build();
        }

        return this.tags.get(id);
    }

    /**
     * Returns a tag type class value from the registry from a provided type {@code Class}.
     *
     * @param tClass the type class of the tag type to retrieve.
     * @return a tag type class value from the registry from a provided type {@code Class}.
     */
    public @NotNull Class<? extends Tag<?>> getTagClassFromTypeClass(@NotNull Class<?> tClass) throws TagTypeRegistryException {
        if (!this.types.containsValue(tClass)) {
            throw SimplifiedException.of(TagTypeRegistryException.class)
                .withMessage("Tag java type '%s' is not registered!", tClass.getSimpleName())
                .build();
        }

        return this.tags.get(this.types.inverse().get(tClass));
    }

    /**
     * Returns a tag type class value from the registry from a provided {@code byte} ID.
     *
     * @param tClass the nbt tag type of the tag id to retrieve.
     * @return a {@code byte} ID from the registry from a provided tag type class value.
     */
    public byte getIdFromTypeClass(@NotNull Class<?> tClass) throws TagTypeRegistryException {
        if (!this.types.containsValue(tClass)) {
            throw SimplifiedException.of(TagTypeRegistryException.class)
                .withMessage("Tag java type '%s' is not registered!", tClass.getSimpleName())
                .build();
        }

        return this.types.inverse().get(tClass);
    }

    /**
     * Returns a tag type class value from the registry from a provided {@code byte} ID.
     *
     * @param tagClass the nbt tag type of the tag id to retrieve.
     * @return a {@code byte} ID from the registry from a provided tag type class value.
     */
    public <U, T extends Tag<U>> byte getIdFromTagClass(@NotNull Class<T> tagClass) throws TagTypeRegistryException {
        if (!this.tags.containsValue(tagClass)) {
            throw SimplifiedException.of(TagTypeRegistryException.class)
                .withMessage("Tag class type '%s' is not registered!", tagClass.getSimpleName())
                .build();
        }

        return this.tags.inverse().get(tagClass);
    }

    /**
     * Returns an empty instance of the given {@link Tag} type, with a {@code null} name and a default (possibly {@code null}) value.
     * Only use this if you really know what you're doing.
     *
     * @param tagClass the tag type to instantiate.
     * @return an empty instance of the tag type provided.
     * @throws TagTypeRegistryException if a reflection error occurs when instantiating the tag.
     */
    public <T extends Tag<?>> @NotNull T instantiate(@NotNull Class<T> tagClass) throws TagTypeRegistryException {
        return this.instantiate(tagClass, null);
    }

    /**
     * Returns an empty instance of the given {@link Tag} type, with a {@code null} name and a default (possibly {@code null}) value.
     * Only use this if you really know what you're doing.
     *
     * @param tagClass the tag type to instantiate.
     * @return an empty instance of the tag type provided.
     * @throws TagTypeRegistryException if a reflection error occurs when instantiating the tag.
     */
    public <T extends Tag<?>> @NotNull T instantiate(@NotNull Class<T> tagClass, @Nullable Object value) throws TagTypeRegistryException {
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
    public <T extends Tag<?>> @NotNull T instantiate(@NotNull Class<T> tagClass, @Nullable String name, @Nullable Object value) throws TagTypeRegistryException {
        try {
            if (value == null)
                return Reflection.of(tagClass).newInstance();
            else
                return Reflection.of(tagClass).newInstance(name, value);
        } catch (ReflectionException ex) {
            throw SimplifiedException.of(TagTypeRegistryException.class)
                .withMessage("Instance of Tag '%s' could not be created.", tagClass.getSimpleName())
                .withCause(ex)
                .build();
        }
    }

}
