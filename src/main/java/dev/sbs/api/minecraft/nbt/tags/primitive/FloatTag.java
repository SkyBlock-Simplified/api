package dev.sbs.api.minecraft.nbt.tags.primitive;

import dev.sbs.api.minecraft.nbt.tags.TagType;
import org.jetbrains.annotations.NotNull;

/**
 * {@link TagType#FLOAT} (ID 5) is used for storing a single-precision 32-bit IEEE 754 floating point value; a Java primitive {@code float}.
 */
public class FloatTag extends NumericalTag<Float> {

    public static final @NotNull FloatTag EMPTY = new FloatTag() {
        @Override
        public void setValue(@NotNull Float value) {
            throw new UnsupportedOperationException("This nbt tag is not modifiable.");
        }
    };

    /**
     * Constructs a float tag with a 0 value.
     */
    public FloatTag() {
        this(0);
    }

    /**
     * Constructs a float tag with a given value.
     *
     * @param value the tag's value, to be converted to {@code float}.
     */
    public FloatTag(@NotNull Number value) {
        super(value.floatValue());
    }

    @Override
    public final @NotNull FloatTag clone() {
        return new FloatTag(this.getValue());
    }

    @Override
    public final byte getId() {
        return TagType.FLOAT.getId();
    }

}
