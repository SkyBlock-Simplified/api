package dev.sbs.api.minecraft.nbt.tags;

import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistryException;
import dev.sbs.api.minecraft.nbt.tags.array.ByteArrayTag;
import dev.sbs.api.minecraft.nbt.tags.array.IntArrayTag;
import dev.sbs.api.minecraft.nbt.tags.array.LongArrayTag;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.collection.ListTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.ByteTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.DoubleTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.FloatTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.IntTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.LongTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.ShortTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.StringTag;
import dev.sbs.api.util.helper.FormatUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Defines the 12 standard NBT tag types and their IDs supported by this library, laid out in the Notchian spec.
 */
@RequiredArgsConstructor
public enum TagType {

    /**
     * ID: 1
     *
     * @see ByteTag
     */
    BYTE(1, Byte.class, ByteTag.class),

    /**
     * ID: 2
     *
     * @see ShortTag
     */
    SHORT(2, Short.class, ShortTag.class),

    /**
     * ID: 3
     *
     * @see IntTag
     */
    INT(3, Integer.class, IntTag.class),

    /**
     * ID: 4
     *
     * @see LongTag
     */
    LONG(4, Long.class, LongTag.class),

    /**
     * ID: 5
     *
     * @see FloatTag
     */
    FLOAT(5, Float.class, FloatTag.class),

    /**
     * ID: 6
     *
     * @see DoubleTag
     */
    DOUBLE(6, Double.class, DoubleTag.class),

    /**
     * ID: 7
     *
     * @see ByteArrayTag
     */
    BYTE_ARRAY(7, Byte[].class, ByteArrayTag.class),

    /**
     * ID: 8
     *
     * @see StringTag
     */
    STRING(8, String.class, StringTag.class),

    /**
     * ID: 9
     *
     * @see ListTag
     */
    LIST(9, List.class, ListTag.class),

    /**
     * ID: 10
     *
     * @see CompoundTag
     */
    COMPOUND(10, Map.class, CompoundTag.class),

    /**
     * ID: 11
     *
     * @see IntArrayTag
     */
    INT_ARRAY(11, Integer[].class, IntArrayTag.class),

    /**
     * ID: 12
     *
     * @see LongArrayTag
     */
    LONG_ARRAY(12, Long[].class, LongArrayTag.class);

    private final Integer id;
    @Getter
    private final Class<?> javaClass;
    @Getter
    @SuppressWarnings("all")
    private final Class<? extends Tag> tagClass;

    public byte getId() {
        return this.id.byteValue();
    }

    public static TagType getById(byte id) {
        for (TagType tagType : values()) {
            if (tagType.getId() == id)
                return tagType;
        }

        throw new IllegalArgumentException(FormatUtil.format("Tag with id ''{0}'' does not exist!", id));
    }

    public static TagType getByType(Class<?> tClass) {
        for (TagType tagType : values()) {
            if (tagType.getJavaClass().isAssignableFrom(tClass))
                return tagType;
        }

        throw new IllegalArgumentException(FormatUtil.format("Tag with type ''{0}'' does not exist!", tClass.getSimpleName()));
    }

    public static void registerAllTypes(TagTypeRegistry registry) {
        try {
            for (TagType tagType : values())
                registry.registerClassType(tagType.getId(), tagType.getJavaClass());
        } catch (TagTypeRegistryException ignore) { }
    }

    @SuppressWarnings("unchecked")
    public static void registerAllTags(TagTypeRegistry registry) {
        try {
            for (TagType tagType : values())
                registry.registerTagType(tagType.getId(), (Class<? extends Tag<?>>) tagType.getTagClass());
        } catch (TagTypeRegistryException ignore) { }
    }

}
