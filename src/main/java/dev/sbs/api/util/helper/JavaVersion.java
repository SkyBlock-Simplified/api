package dev.sbs.api.util.helper;

import java.util.Optional;

/**
 * <p>An enum representing all the versions of the Java specification.
 * This is intended to mirror available values from the
 * <em>java.specification.version</em> System property. </p>
 */
public enum JavaVersion {

    JAVA_0_9(1.5f, "0.9"),
    JAVA_1_1(1.1f, "1.1"),
    JAVA_1_2(1.2f, "1.2"),
    JAVA_1_3(1.3f, "1.3"),
    JAVA_1_4(1.4f, "1.4"),
    JAVA_1_5(1.5f, "1.5"),
    JAVA_1_6(1.6f, "1.6"),
    JAVA_1_7(1.7f, "1.7"),
    JAVA_1_8(1.8f, "1.8");

    /**
     * The float value.
     */
    private final float value;
    /**
     * The standard name.
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param value  the float value
     * @param name  the standard name, not null
     */
    JavaVersion(final float value, final String name) {
        this.value = value;
        this.name = name;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Whether this version of Java is at least the version of Java passed in.</p>
     *
     * <p>For example:<br />
     *  {@code myVersion.atLeast(JavaVersion.JAVA_1_4)}<p>
     *
     * @param requiredVersion  the version to check against, not null
     * @return true if this version is equal to or greater than the specified version
     */
    public boolean atLeast(JavaVersion requiredVersion) {
        return this.value >= requiredVersion.value;
    }

    /**
     * Transforms the given string with a Java version number to the
     * corresponding constant of this enumeration class. This method is used
     * internally.
     *
     * @param nom the Java version as string
     * @return the corresponding enumeration constant or <b>null</b> if the
     * version is unknown
     */
    // helper for static importing
    static Optional<JavaVersion> getJavaVersion(final String nom) {
        return get(nom);
    }

    /**
     * Transforms the given string with a Java version number to the
     * corresponding constant of this enumeration class. This method is used
     * internally.
     *
     * @param nom the Java version as string
     * @return the corresponding enumeration constant or <b>null</b> if the
     * version is unknown
     */
    static Optional<JavaVersion> get(final String nom) {
        if ("0.9".equals(nom))
            return Optional.of(JAVA_0_9);
        else if ("1.1".equals(nom))
            return Optional.of(JAVA_1_1);
        else if ("1.2".equals(nom))
            return Optional.of(JAVA_1_2);
        else if ("1.3".equals(nom))
            return Optional.of(JAVA_1_3);
        else if ("1.4".equals(nom))
            return Optional.of(JAVA_1_4);
        else if ("1.5".equals(nom))
            return Optional.of(JAVA_1_5);
        else if ("1.6".equals(nom))
            return Optional.of(JAVA_1_6);
        else if ("1.7".equals(nom))
            return Optional.of(JAVA_1_7);
        else if ("1.8".equals(nom))
            return Optional.of(JAVA_1_8);
        else
            return Optional.empty();
    }

    //-----------------------------------------------------------------------
    /**
     * <p>The string value is overridden to return the standard name.</p>
     *
     * <p>For example, <code>"1.5"</code>.</p>
     *
     * @return the name, not null
     */
    @Override
    public String toString() {
        return this.name;
    }

}
