package dev.sbs.api.stream.triple;

import dev.sbs.api.builder.CompareToBuilder;
import dev.sbs.api.builder.EqualsBuilder;
import dev.sbs.api.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * <p>A triple consisting of three elements.</p>
 *
 * <p>This class is an abstract implementation defining the basic API.
 * It refers to the elements as 'left', 'middle' and 'right'.</p>
 *
 * <p>Subclass implementations may be mutable or immutable.
 * However, there is no restriction on the type of the stored objects that may be stored.
 * If mutable objects are stored in the triple, then the triple itself effectively becomes mutable.</p>
 *
 * @param <L> the left element type
 * @param <M> the middle element type
 * @param <R> the right element type
 */
public abstract class Triple<L, M, R> implements Comparable<Triple<L, M, R>> {

    /**
     * <p>Obtains an immutable triple of three objects inferring the generic types.</p>
     *
     * <p>This factory allows the triple to be created using inference to
     * obtain the generic types.</p>
     *
     * @param <L>    the left element type
     * @param <M>    the middle element type
     * @param <R>    the right element type
     * @param left   the left element, may be null
     * @param middle the middle element, may be null
     * @param right  the right element, may be null
     * @return a triple formed from the three parameters, not null
     */
    public static <L, M, R> @NotNull Triple<L, M, R> of(final L left, final M middle, final R right) {
        return new ImmutableTriple<>(left, middle, right);
    }

    /**
     * <p>Compares the triple based on the left element, followed by the middle element,
     * finally the right element.
     * The types must be {@code Comparable}.</p>
     *
     * @param other the other triple, not null
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public final int compareTo(final Triple<L, M, R> other) {
        return new CompareToBuilder()
            .append(this.getLeft(), other.getLeft())
            .append(this.getMiddle(), other.getMiddle())
            .append(this.getRight(), other.getRight())
            .toComparison();
    }

    /**
     * <p>Compares this triple to another based on the three elements.</p>
     *
     * @param o the object to compare to, null returns false
     * @return true if the elements of the triple are equal
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Triple<?, ?, ?> other)) return false;

        return new EqualsBuilder()
            .append(this.getLeft(), other.getLeft())
            .append(this.getMiddle(), other.getMiddle())
            .append(this.getRight(), other.getRight())
            .build();
    }

    /**
     * <p>Gets the left element from this triple.</p>
     *
     * @return the left element, may be null
     */
    public abstract L getLeft();

    /**
     * <p>Gets the middle element from this triple.</p>
     *
     * @return the middle element, may be null
     */
    public abstract M getMiddle();

    /**
     * <p>Gets the right element from this triple.</p>
     *
     * @return the right element, may be null
     */
    public abstract R getRight();

    @Override
    public final int hashCode() {
        return new HashCodeBuilder()
            .append(this.getLeft())
            .append(this.getMiddle())
            .append(this.getRight())
            .build();
    }

    /**
     * <p>Returns a String representation of this triple using the format {@code ($left,$middle,$right)}.</p>
     *
     * @return a string describing this object
     */
    @Override
    public final @NotNull String toString() {
        return String.format("(%s,%s,%s", this.getLeft(), this.getMiddle(), this.getRight());
    }

    /**
     * <p>Formats the receiver using the given format.</p>
     *
     * <p>This uses {@link java.util.Formattable} to perform the formatting. Three variables may
     * be used to embed the left and right elements. Use {@code %1$s} for the left
     * element, {@code %2$s} for the middle and {@code %3$s} for the right element.
     * The default format used by {@code toString()} is {@code (%1$s,%2$s,%3$s)}.</p>
     *
     * @param format the format string, optionally containing {@code %1$s}, {@code %2$s} and {@code %3$s}, not null
     * @return the formatted string
     */
    public final @NotNull String toString(final String format) {
        return String.format(format, getLeft(), getMiddle(), getRight());
    }

}
