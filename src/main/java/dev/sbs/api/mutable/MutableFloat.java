package dev.sbs.api.mutable;

/**
 * A mutable {@code float} wrapper.
 * <p>
 * Note that as MutableFloat does not extend Float, it is not treated by String.format as a Float parameter.
 *
 * @see Float
 */
public class MutableFloat extends Number implements Comparable<MutableFloat>, Mutable<Number> {

    /** The mutable value. */
    private float value;

    /**
     * Constructs a new MutableFloat with the default value of zero.
     */
    public MutableFloat() { }

    /**
     * Constructs a new MutableFloat with the specified value.
     *
     * @param value  the initial value to store
     */
    public MutableFloat(final float value) {
        this.value = value;
    }

    /**
     * Constructs a new MutableFloat with the specified value.
     *
     * @param value  the initial value to store, not null
     * @throws NullPointerException if the object is null
     */
    public MutableFloat(final Number value) {
        this.value = value.floatValue();
    }

    /**
     * Constructs a new MutableFloat parsing the given string.
     *
     * @param value  the string to parse, not null
     * @throws NumberFormatException if the string cannot be parsed into a float
     */
    public MutableFloat(final String value) {
        this.value = Float.parseFloat(value);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value as a Float instance.
     *
     * @return the value as a Float, never null
     */
    @Override
    public Float get() {
        return this.value;
    }

    /**
     * Sets the value.
     *
     * @param value  the value to set
     */
    public void set(final float value) {
        this.value = value;
    }

    /**
     * Sets the value from any Number instance.
     *
     * @param value  the value to set, not null
     * @throws NullPointerException if the object is null
     */
    @Override
    public void set(final Number value) {
        this.value = value.floatValue();
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the float value is the special NaN value.
     *
     * @return true if NaN
     */
    public boolean isNaN() {
        return Float.isNaN(value);
    }

    /**
     * Checks whether the float value is infinite.
     *
     * @return true if infinite
     */
    public boolean isInfinite() {
        return Float.isInfinite(value);
    }

    //-----------------------------------------------------------------------
    /**
     * Increments the value.
     *
     */
    public void increment() {
        value++;
    }

    /**
     * Increments this instance's value by 1; this method returns the value associated with the instance
     * immediately prior to the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was incremented
     */
    public float getAndIncrement() {
        final float last = value;
        value++;
        return last;
    }

    /**
     * Increments this instance's value by 1; this method returns the value associated with the instance
     * immediately after the increment operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is incremented
     */
    public float incrementAndGet() {
        value++;
        return value;
    }

    /**
     * Decrements the value.
     */
    public void decrement() {
        value--;
    }

    /**
     * Decrements this instance's value by 1; this method returns the value associated with the instance
     * immediately prior to the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance before it was decremented
     */
    public float getAndDecrement() {
        final float last = value;
        value--;
        return last;
    }

    /**
     * Decrements this instance's value by 1; this method returns the value associated with the instance
     * immediately after the decrement operation. This method is not thread safe.
     *
     * @return the value associated with the instance after it is decremented
     */
    public float decrementAndGet() {
        value--;
        return value;
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a value to the value of this instance.
     *
     * @param operand  the value to add, not null
     */
    public void add(final float operand) {
        this.value += operand;
    }

    /**
     * Adds a value to the value of this instance.
     *
     * @param operand  the value to add, not null
     * @throws NullPointerException if the object is null
     */
    public void add(final Number operand) {
        this.value += operand.floatValue();
    }

    /**
     * Subtracts a value from the value of this instance.
     *
     * @param operand  the value to subtract
     */
    public void subtract(final float operand) {
        this.value -= operand;
    }

    /**
     * Subtracts a value from the value of this instance.
     *
     * @param operand  the value to subtract, not null
     * @throws NullPointerException if the object is null
     */
    public void subtract(final Number operand) {
        this.value -= operand.floatValue();
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @return the value associated with this instance after adding the operand
     */
    public float addAndGet(final float operand) {
        this.value += operand;
        return value;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately after the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws NullPointerException if {@code operand} is null
     * @return the value associated with this instance after adding the operand
     */
    public float addAndGet(final Number operand) {
        this.value += operand.floatValue();
        return value;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @return the value associated with this instance immediately before the operand was added
     */
    public float getAndAdd(final float operand) {
        final float last = value;
        this.value += operand;
        return last;
    }

    /**
     * Increments this instance's value by {@code operand}; this method returns the value associated with the instance
     * immediately prior to the addition operation. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws NullPointerException if {@code operand} is null
     * @return the value associated with this instance immediately before the operand was added
     */
    public float getAndAdd(final Number operand) {
        final float last = value;
        this.value += operand.floatValue();
        return last;
    }

    //-----------------------------------------------------------------------
    // shortValue and byteValue rely on Number implementation
    /**
     * Returns the value of this MutableFloat as an int.
     *
     * @return the numeric value represented by this object after conversion to type int.
     */
    @Override
    public int intValue() {
        return (int) value;
    }

    /**
     * Returns the value of this MutableFloat as a long.
     *
     * @return the numeric value represented by this object after conversion to type long.
     */
    @Override
    public long longValue() {
        return (long) value;
    }

    /**
     * Returns the value of this MutableFloat as a float.
     *
     * @return the numeric value represented by this object after conversion to type float.
     */
    @Override
    public float floatValue() {
        return value;
    }

    /**
     * Returns the value of this MutableFloat as a double.
     *
     * @return the numeric value represented by this object after conversion to type double.
     */
    @Override
    public double doubleValue() {
        return value;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets this mutable as an instance of Float.
     *
     * @return a Float instance containing the value from this mutable, never null
     */
    public Float toFloat() {
        return floatValue();
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object against some other object. The result is {@code true} if and only if the argument is
     * not {@code null} and is a {@code Float} object that represents a {@code float} that has the
     * identical bit pattern to the bit pattern of the {@code float} represented by this object. For this
     * purpose, two float values are considered to be the same if and only if the method
     * {@link Float#floatToIntBits(float)}returns the same int value when applied to each.
     * <p>
     * Note that in most cases, for two instances of class {@code Float},{@code f1} and {@code f2},
     * the value of {@code f1.equals(f2)} is {@code true} if and only if <blockquote>
     *
     * <pre>
     *   f1.floatValue() == f2.floatValue()
     * </pre>
     *
     * </blockquote>
     * <p>
     * also has the value {@code true}. However, there are two exceptions:
     * <ul>
     * <li>If {@code f1} and {@code f2} both represent {@code Float.NaN}, then the
     * {@code equals} method returns {@code true}, even though {@code Float.NaN==Float.NaN} has
     * the value {@code false}.
     * <li>If {@code f1} represents {@code +0.0f} while {@code f2} represents {@code -0.0f},
     * or vice versa, the {@code equal} test has the value {@code false}, even though
     * {@code 0.0f==-0.0f} has the value {@code true}.
     * </ul>
     * This definition allows hashtables to operate properly.
     *
     * @param obj  the object to compare with, null returns false
     * @return {@code true} if the objects are the same; {@code false} otherwise.
     * @see Float#floatToIntBits(float)
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MutableFloat
            && Float.floatToIntBits(((MutableFloat) obj).value) == Float.floatToIntBits(value);
    }

    /**
     * Returns a suitable hash code for this mutable.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return Float.floatToIntBits(value);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this mutable to another in ascending order.
     *
     * @param other  the other mutable to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(final MutableFloat other) {
        return Float.compare(this.value, other.value);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the String value of this mutable.
     *
     * @return the mutable value as a string
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
