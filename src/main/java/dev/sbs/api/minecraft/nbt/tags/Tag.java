package dev.sbs.api.minecraft.nbt.tags;

import dev.sbs.api.minecraft.nbt.exception.NbtMaxDepthException;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Interface for all NBT tags.</p>
 *
 * <p>All serializing and deserializing methods data track the nesting levels to prevent
 * circular references or malicious data which could, when deserialized, result in thousands
 * of instances causing a denial of service.</p>
 *
 * <p>These methods have a parameter for the maximum nesting depth they are allowed to traverse. A
 * value of {@code 0} means that only the object itself, but no nested objects may be processed.
 * If an instance is nested further than allowed, a {@link NbtMaxDepthException} will be thrown.
 * Providing a negative maximum nesting depth will cause an {@code IllegalArgumentException}
 * to be thrown.</p>
 *
 * <p>Some methods do not provide a parameter to specify the maximum nesting depth, but instead use
 * {@link #DEFAULT_MAX_DEPTH}, which is also the maximum used by Minecraft.</p>
 *
 * @param <T> The type of the contained value.
 * */
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Tag<T> implements Cloneable {

    private @NotNull T value;

    /**
     * The default maximum depth of the NBT structure.
     * */
    public static final int DEFAULT_MAX_DEPTH = 512;

    /**
     * Creates a clone of this Tag.
     * */
    public abstract @NotNull Tag<T> clone();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag<?> tag = (Tag<?>) o;

        return new EqualsBuilder()
            .append(this.getValue(), tag.getValue())
            .build();
    }

    /**
     * Gets the unique ID for this NBT tag type.
     * <br><br>
     * 0 to 12 (inclusive) are reserved.
     */
    public abstract byte getId();

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getValue())
            .build();
    }

    @Override
    public abstract @NotNull String toString();

}
