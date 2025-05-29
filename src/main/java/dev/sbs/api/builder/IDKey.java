package dev.sbs.api.builder;

/**
 * Wrap an identity key (System.identityHashCode())
 * so that an object can only be equal() to itself.
 * <p>
 * This is necessary to disambiguate the occasional duplicate
 * identityHashCodes that can occur.
 */
public final class IDKey {

    private final Object value;
    private final int id;

    /**
     * Constructor for IDKey
     * @param _value The value
     */
    public IDKey(final Object _value) {
        // This is the Object hash code
        id = System.identityHashCode(_value);
        // There have been some cases (LANG-459) that return the
        // same identity hash code for different objects.  So
        // the value is also added to disambiguate these cases.
        value = _value;
    }

    /**
     * returns hash code - i.e. the system identity hashcode.
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * checks if instances are equal
     * @param other The other object to compare to
     * @return if the instances are for the same object
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof IDKey idKey)) {
            return false;
        }
        if (id != idKey.id) {
            return false;
        }
        // Note that identity equals is used.
        return value == idKey.value;
    }

}
