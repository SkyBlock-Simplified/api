package dev.sbs.api.minecraft.nbt.tags;

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
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Defines the 12 standard NBT tag types and their IDs supported by this library, laid out in the Notchian spec.
 */
@Getter
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

    @Getter(AccessLevel.NONE)
    private final @NotNull Integer id;
    private final @NotNull Class<?> javaClass;
    private final @NotNull Class<? extends Tag<?>> tagClass;

    <U, T extends Tag<U>> TagType(@NotNull Integer id, @NotNull Class<? super U> javaClass, @NotNull Class<T> tagClass) {
        this.id = id;
        this.javaClass = javaClass;
        this.tagClass = tagClass;
    }

    public byte getId() {
        return this.id.byteValue();
    }

    public static @NotNull TagType getById(int id) {
        return getById((byte) id);
    }

    public static @NotNull TagType getById(byte id) {
        for (TagType tagType : values()) {
            if (tagType.getId() == id)
                return tagType;
        }

        throw new IllegalArgumentException(String.format("Tag with id '%s' does not exist.", id));
    }

}
