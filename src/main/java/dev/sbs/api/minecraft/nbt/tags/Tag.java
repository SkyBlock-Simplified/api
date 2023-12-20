package dev.sbs.api.minecraft.nbt.tags;

import dev.sbs.api.minecraft.nbt.serializable.json.JsonSerializable;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtSerializable;
import dev.sbs.api.minecraft.nbt.serializable.tag.TagSerializable;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An abstract NBT tag.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Tag<T> implements TagSerializable, JsonSerializable, SnbtSerializable {

    /**
     * Gets the unique ID for this NBT tag type.
     * <br><br>
     * 0 to 12 (inclusive) are reserved.
     */
    private final byte typeId;
    /**
     * Gets the name (key) of this tag.
     */
    private @Nullable String name;
    /**
     * Gets the value held by this tag.
     */
    private @NotNull T value;
    /**
     * Gets if the name and value are modifiable.
     */
    @Getter(AccessLevel.PROTECTED)
    private boolean modifiable;

    @Override
    public final boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Tag<?> that = (Tag<?>) obj;

        return new EqualsBuilder()
            .append(this.getTypeId(), that.getTypeId())
            .append(this.getName(), that.getName())
            .append(this.getValue(), that.getValue())
            .append(this.isModifiable(), that.isModifiable())
            .build();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder()
            .append(this.getTypeId())
            .append(this.getName())
            .append(this.getValue())
            .build();
    }

    protected final void requireModifiable() {
        if (!this.isModifiable())
            throw new UnsupportedOperationException("This Tag is not modifiable!");
    }

    /**
     * Sets the name (key) of this tag.
     *
     * @param name the new name to be set.
     */
    public final void setName(@Nullable String name) {
        this.requireModifiable();
        this.name = name;
    }

    /**
     * Sets the {@link T} value of this byte tag.
     *
     * @param value new {@link T} value to be set.
     */
    public final void setValue(@NotNull T value) {
        this.requireModifiable();
        this.value = value;
    }

    @Override
    public final @NotNull String toString() {
        return this.getValue().toString();
    }

}
