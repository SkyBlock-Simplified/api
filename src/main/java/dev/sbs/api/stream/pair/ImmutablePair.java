package dev.sbs.api.stream.pair;

import lombok.Getter;

/**
 * <p>An immutable pair consisting of two {@code Object} elements.</p>
 *
 * <p>Although the implementation is immutable, there is no restriction on the objects
 * that may be stored. If mutable objects are stored in the pair, then the pair
 * itself effectively becomes mutable. The class is also not {@code final}, so a subclass
 * could add undesirable behaviour.</p>
 *
 * <p>#ThreadSafe# if the objects are threadsafe</p>
 *
 * @param <L> the left element type
 * @param <R> the right element type
 */
@Getter
public final class ImmutablePair<L, R> extends Pair<L, R> {

    /**
     * Left object
     */
    public final L left;

    /**
     * Right object
     */
    public final R right;

    /**
     * Create a new pair instance.
     *
     * @param left  the left value, may be null
     * @param right the right value, may be null
     */
    public ImmutablePair(L left, R right) {
        super();
        this.left = left;
        this.right = right;
    }

    /**
     * <p>Obtains an immutable pair of from two objects inferring the generic types.</p>
     *
     * <p>This factory allows the pair to be created using inference to
     * obtain the generic types.</p>
     *
     * @param <L>   the left element type
     * @param <R>   the right element type
     * @param left  the left element, may be null
     * @param right the right element, may be null
     * @return a pair formed from the two parameters, not null
     */
    public static <L, R> ImmutablePair<L, R> of(L left, R right) {
        return new ImmutablePair<>(left, right);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Throws {@code UnsupportedOperationException}.</p>
     *
     * <p>This pair is immutable, so this operation is not supported.</p>
     *
     * @param value the value to set
     * @return never
     * @throws UnsupportedOperationException as this operation is not supported
     */
    @Override
    public R setValue(R value) {
        throw new UnsupportedOperationException();
    }

}
