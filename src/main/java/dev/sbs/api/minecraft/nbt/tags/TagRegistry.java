package dev.sbs.api.minecraft.nbt.tags;

import dev.sbs.api.minecraft.nbt.exception.TagRegistryException;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.reflection.exception.ReflectionException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A registry mapping {@code byte} tag type IDs to tag type classes. Used to register custom-made {@link Tag} types.
 */
public class TagRegistry {

    private final @NotNull ConcurrentMap<Byte, TagType> types = Concurrent.newMap();

    public TagRegistry() {
        for (TagType tagType : TagType.values())
            this.register(tagType);
    }

    /**
     * Register a custom-made {@link Tag} type with a unique {@code int} ID.
     * <br><br>
     * IDs 0-12 (inclusive) are reserved and may not be used.
     *
     * @param id The class type's unique ID used in reading and writing.
     * @param javaClass The java type class.
     * @param tagClass The tag type class.
     * @throws TagRegistryException If the ID provided is either registered already or is a reserved ID (0-12 inclusive).
     */
    public <U, T extends Tag<U>> void register(int id, @NotNull Class<U> javaClass, @NotNull Class<T> tagClass) throws TagRegistryException {
        this.register(TagType.of(id, javaClass, tagClass));
    }

    /**
     * Register a custom-made {@link Tag} type with a unique {@code byte} ID.
     * <br><br>
     * IDs 0-12 (inclusive) are reserved and may not be used.
     *
     * @param id The class type's unique ID used in reading and writing.
     * @param javaClass The java type class.
     * @param tagClass The tag type class.
     * @throws TagRegistryException If the ID provided is either registered already or is a reserved ID (0-12 inclusive).
     */
    public <U, T extends Tag<U>> void register(byte id, @NotNull Class<U> javaClass, @NotNull Class<T> tagClass) throws TagRegistryException {
        this.register(TagType.of(id, javaClass, tagClass));
    }

    /**
     * Register a custom-made {@link Tag} type the given {@link TagType}.
     * <br><br>
     * IDs 0-12 (inclusive) are reserved and may not be used.
     *
     * @param tagType The class type's unique ID used in reading and writing.
     * @throws TagRegistryException If the ID is reserved (0-12 inclusive) or the ID/classes are registered already.
     */
    public void register(@NotNull TagType tagType) throws TagRegistryException {
        if (tagType.getId() == 0) {
            throw SimplifiedException.of(TagRegistryException.class)
                .withMessage(
                    "Cannot register Tag ('%s', '%s') with ID %s. ID is reserved.",
                    tagType.getJavaClass().getSimpleName(), tagType.getTagClass().getSimpleName(), tagType.getId()
                )
                .build();
        }

        ConcurrentList<String> existing = Concurrent.newList();
        for (TagType registeredType : this.types.values()) {
            if (tagType.getId() == registeredType.getId())
                existing.add(String.format("ID is already registered as ('%s', '%s')", registeredType.getJavaClass().getSimpleName(), registeredType.getTagClass().getSimpleName()));

            if (tagType.getJavaClass().equals(registeredType.getJavaClass()))
                existing.add(String.format("Java class is already registered with ('%s')", registeredType.getTagClass().getSimpleName()));

            if (tagType.getTagClass().equals(registeredType.getTagClass()))
                existing.add(String.format("Tag class is already registered with ('%s')", registeredType.getJavaClass().getSimpleName()));

            if (existing.notEmpty())
                break;
        }

        if (existing.notEmpty()) {
            throw SimplifiedException.of(TagRegistryException.class)
                .withMessage(
                    "Cannot register Tag ('%s', '%s') with ID %s. %s",
                    tagType.getJavaClass().getSimpleName(), tagType.getTagClass().getSimpleName(), tagType.getId(), existing
                )
                .build();
        }

        this.types.put(tagType.getId(), tagType);
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

        return this.types.remove(id) != null;
    }

    /**
     * Gets the {@link TagType} from the registry from a provided {@code byte} ID.
     *
     * @param id the ID of the java type to retrieve.
     * @return a type value from the registry from a provided {@code byte} ID.
     */
    public @NotNull TagType getTypeFromId(byte id) throws TagRegistryException {
        if (!this.types.containsKey(id)) {
            throw SimplifiedException.of(TagRegistryException.class)
                .withMessage("Tag with ID %s is not registered!", id)
                .build();
        }

        return this.types.get(id);
    }

    /**
     * Gets the {@link TagType} from the registry from the provided java type {@code Class}.
     *
     * @param javaClass The java type class of the tag type to retrieve.
     * @return a tag type value from the registry from a provided type {@code Class}.
     */
    public @NotNull TagType getTypeFromJavaClass(@NotNull Class<?> javaClass) throws TagRegistryException {
        return this.types.values()
            .stream()
            .filter(tagType -> tagType.getJavaClass().equals(javaClass))
            .findFirst()
            .orElseThrow(() -> SimplifiedException.of(TagRegistryException.class)
                .withMessage("Java type '%s' is not registered!", javaClass.getSimpleName())
                .build()
            );
    }

    /**
     * Gets the {@link TagType} from the registry from the provided tag type {@code Class}.
     *
     * @param tagClass The java type class of the tag type to retrieve.
     * @return a tag type value from the registry from a provided type {@code Class}.
     */
    public <U, T extends Tag<U>> @NotNull TagType getTypeFromTagClass(@NotNull Class<T> tagClass) throws TagRegistryException {
        return this.types.values()
            .stream()
            .filter(tagType -> tagType.getTagClass().equals(tagClass))
            .findFirst()
            .orElseThrow(() -> SimplifiedException.of(TagRegistryException.class)
                .withMessage("Tag type '%s' is not registered!", tagClass.getSimpleName())
                .build()
            );
    }

    /**
     * Gets the {@link TagType} from the registry from the provided java type {@code Class}.
     *
     * @param javaClass The java type of the tag id to retrieve.
     */
    public byte getIdFromJavaClass(@NotNull Class<?> javaClass) throws TagRegistryException {
        return this.getTypeFromJavaClass(javaClass).getId();
    }

    /**
     * Gets the {@link TagType} from the registry from the provided java type {@code Class}.
     *
     * @param tagClass the tag type of the tag id to retrieve.
     */
    public <U, T extends Tag<U>> byte getIdFromTagClass(@NotNull Class<T> tagClass) throws TagRegistryException {
        return this.getTypeFromTagClass(tagClass).getId();
    }

    /**
     * Returns an empty instance of the given {@link Tag} type, with a {@code null} name and a default (possibly {@code null}) value.
     * Only use this if you really know what you're doing.
     *
     * @param tagClass the tag type to instantiate.
     * @return an empty instance of the tag type provided.
     * @throws TagRegistryException if a reflection error occurs when instantiating the tag.
     */
    public <T extends Tag<?>> @NotNull T instantiate(@NotNull Class<T> tagClass) throws TagRegistryException {
        return this.instantiate(tagClass, null);
    }

    /**
     * Returns an empty instance of the given {@link Tag} type, with a {@code null} name and a default (possibly {@code null}) value.
     * Only use this if you really know what you're doing.
     *
     * @param tagClass the tag type to instantiate.
     * @return an empty instance of the tag type provided.
     * @throws TagRegistryException if a reflection error occurs when instantiating the tag.
     */
    public <T extends Tag<?>> @NotNull T instantiate(@NotNull Class<T> tagClass, @Nullable Object value) throws TagRegistryException {
        return this.instantiate(tagClass, null, value);
    }

    /**
     * Returns an empty instance of the given {@link Tag} type, with a {@code null} name and a default (possibly {@code null}) value.
     * Only use this if you really know what you're doing.
     *
     * @param tagClass the tag type to instantiate.
     * @return an empty instance of the tag type provided.
     * @throws TagRegistryException if a reflection error occurs when instantiating the tag.
     */
    public <T extends Tag<?>> @NotNull T instantiate(@NotNull Class<T> tagClass, @Nullable String name, @Nullable Object value) throws TagRegistryException {
        try {
            if (value == null)
                return Reflection.of(tagClass).newInstance();
            else
                return Reflection.of(tagClass).newInstance(name, value);
        } catch (ReflectionException ex) {
            throw SimplifiedException.of(TagRegistryException.class)
                .withMessage("Instance of Tag '%s' could not be created.", tagClass.getSimpleName())
                .withCause(ex)
                .build();
        }
    }

}
