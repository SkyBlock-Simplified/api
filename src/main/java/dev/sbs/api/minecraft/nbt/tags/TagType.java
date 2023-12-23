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
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.reflection.accessor.FieldAccessor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Defines the 12 standard NBT tag types and their IDs supported by this library.
 */
public interface TagType {

    /**
     * ID: 1
     *
     * @see ByteTag
     */
    TagType BYTE = of(1, Byte.class, ByteTag.class);
    /**
     * ID: 2
     *
     * @see ShortTag
     */
    TagType SHORT = of(2, Short.class, ShortTag.class);
    /**
     * ID: 3
     *
     * @see IntTag
     */
    TagType INT = of(3, Integer.class, IntTag.class);
    /**
     * ID: 4
     *
     * @see LongTag
     */
    TagType LONG = of(4, Long.class, LongTag.class);
    /**
     * ID: 5
     *
     * @see FloatTag
     */
    TagType FLOAT = of(5, Float.class, FloatTag.class);
    /**
     * ID: 6
     *
     * @see DoubleTag
     */
    TagType DOUBLE = of(6, Double.class, DoubleTag.class);
    /**
     * ID: 7
     *
     * @see ByteArrayTag
     */
    TagType BYTE_ARRAY = of(7, Byte[].class, ByteArrayTag.class);
    /**
     * ID: 8
     *
     * @see StringTag
     */
    TagType STRING = of(8, String.class, StringTag.class);
    /**
     * ID: 9
     *
     * @see ListTag
     */
    @SuppressWarnings("unchecked")
    TagType LIST = of(9, List.class, ListTag.class);
    /**
     * ID: 10
     *
     * @see CompoundTag
     */
    TagType COMPOUND = of(10, Map.class, CompoundTag.class);
    /**
     * ID: 11
     *
     * @see IntArrayTag
     */
    TagType INT_ARRAY = of(11, Integer[].class, IntArrayTag.class);
    /**
     * ID: 12
     *
     * @see LongArrayTag
     */
    TagType LONG_ARRAY = of(12, Long[].class, LongArrayTag.class);

    byte getId();

    @NotNull Class<?> getJavaClass();

    @NotNull Class<? extends Tag<?>> getTagClass();

    static <J, T extends Tag<J>> @NotNull TagType of(byte id, @NotNull Class<? super J> javaClass, @NotNull Class<T> tagClass) {
        return of(Integer.valueOf(id), javaClass, tagClass);
    }

    static <J, T extends Tag<J>> @NotNull TagType of(@NotNull Integer id, @NotNull Class<? super J> javaClass, @NotNull Class<T> tagClass) {
        return new Impl(id, javaClass, tagClass);
    }

    static @NotNull TagType[] values() {
        return Reflection.of(TagType.class)
            .getFields().stream()
            .filter(fieldAccessor -> TagType.class.isAssignableFrom(fieldAccessor.getType()))
            .map(FieldAccessor::get)
            .filter(Objects::nonNull)
            .map(TagType.class::cast)
            .toArray(TagType[]::new);
    }

    @Getter
    class Impl implements TagType {

        private final @NotNull Integer id;
        private final @NotNull Class<?> javaClass;
        private final @NotNull Class<? extends Tag<?>> tagClass;

        <J, T extends Tag<J>> Impl(@NotNull Integer id, @NotNull Class<? super J> javaClass, @NotNull Class<T> tagClass) {
            this.id = id;
            this.javaClass = javaClass;
            this.tagClass = tagClass;
        }

        @Override
        public byte getId() {
            return this.id.byteValue();
        }

    }

}
