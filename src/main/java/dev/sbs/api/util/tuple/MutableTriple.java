package dev.sbs.api.util.tuple;

/**
 * <p>A mutable triple consisting of three {@code Object} elements.</p>
 *
 * <p>Not #ThreadSafe#</p>
 *
 * @param <L> the left element type
 * @param <M> the middle element type
 * @param <R> the right element type
 */
public class MutableTriple<L, M, R> extends Triple<L, M, R> {

    /**
     * The empty array singleton.
     * <p>
     * Consider using {@link #emptyArray()} to avoid generics warnings.
     * </p>
     */
    public static final MutableTriple<?, ?, ?>[] EMPTY_ARRAY = new MutableTriple[0];

    /**
     * Returns the empty array singleton that can be assigned without compiler warning.
     *
     * @param <L> the left element type
     * @param <M> the middle element type
     * @param <R> the right element type
     * @return the empty array singleton that can be assigned without compiler warning.
     */
    @SuppressWarnings("unchecked")
    public static <L, M, R> MutableTriple<L, M, R>[] emptyArray() {
        return (MutableTriple<L, M, R>[]) EMPTY_ARRAY;
    }

    /**
     * <p>Obtains a mutable triple of three objects inferring the generic types.</p>
     *
     * <p>This factory allows the triple to be created using inference to
     * obtain the generic types.</p>
     *
     * @param <L> the left element type
     * @param <M> the middle element type
     * @param <R> the right element type
     * @param left  the left element, may be null
     * @param middle  the middle element, may be null
     * @param right  the right element, may be null
     * @return a triple formed from the three parameters, not null
     */
    public static <L, M, R> MutableTriple<L, M, R> of(final L left, final M middle, final R right) {
        return new MutableTriple<>(left, middle, right);
    }
    /** Left object */
    public L left;
    /** Middle object */
    public M middle;

    /** Right object */
    public R right;

    /**
     * Create a new triple instance of three nulls.
     */
    public MutableTriple() {
        super();
    }

    /**
     * Create a new triple instance.
     *
     * @param left  the left value, may be null
     * @param middle  the middle value, may be null
     * @param right  the right value, may be null
     */
    public MutableTriple(final L left, final M middle, final R right) {
        super();
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    //-----------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public L getLeft() {
        return left;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public M getMiddle() {
        return middle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public R getRight() {
        return right;
    }

    /**
     * Sets the left element of the triple.
     *
     * @param left  the new value of the left element, may be null
     */
    public void setLeft(final L left) {
        this.left = left;
    }

    /**
     * Sets the middle element of the triple.
     *
     * @param middle  the new value of the middle element, may be null
     */
    public void setMiddle(final M middle) {
        this.middle = middle;
    }

    /**
     * Sets the right element of the triple.
     *
     * @param right  the new value of the right element, may be null
     */
    public void setRight(final R right) {
        this.right = right;
    }
}
