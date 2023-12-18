package dev.sbs.api.util.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * <p>
 * Helpers for {@code java.lang.System}.
 * </p>
 * <p>
 * If a system property cannot be read due to security restrictions, the corresponding field in this class will be set
 * to {@code null} and a message will be written to {@code System.err}.
 * </p>
 * <p>
 * These values are initialized when the class is loaded. If {@link System#setProperty(String,String)} or
 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, that property will be out of
 * sync with the cached values.
 * </p>
 * <p>
 * #ThreadSafe#
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemUtil {

    /**
     * The System property key for the user home directory.
     */
    private static final String USER_HOME_KEY = "user.home";

    /**
     * The System property key for the user directory.
     */
    private static final String USER_DIR_KEY = "user.dir";

    /**
     * The System property key for the Java IO temporary directory.
     */
    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";

    /**
     * The System property key for the Java home directory.
     */
    private static final String JAVA_HOME_KEY = "java.home";

    /**
     * The {@code file.encoding} System Property. Such as {@code Cp1252}.
     */
    public static final String FILE_ENCODING = getSystemProperty("file.encoding");

    /**
     * The {@code file.separator} System Property. File separator (<code>&quot;/&quot;</code> on UNIX).
     */
    public static final String FILE_SEPARATOR = getSystemProperty("file.separator");

    /**
     * The {@code java.awt.headless} System Property. The value of this property is the String {@code "true"} or {@code "false"}.
     */
    private static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");

    /**
     * The {@code java.home} System Property. Java installation directory.
     */
    public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);

    /**
     * The {@code java.io.tmpdir} System Property. Default temp file path.
     */
    public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);

    /**
     * The {@code java.class.path} System Property. Java class path.
     */
    public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");

    /**
     * The {@code java.library.path} System Property. List of paths to search when loading libraries.
     */
    public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");

    /**
     * The {@code java.runtime.name} System Property. Java Runtime Environment name.
     */
    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");

    /**
     * The {@code java.runtime.version} System Property. Java Runtime Environment version.
     */
    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");

    /**
     * The {@code java.specification.name} System Property. Java Runtime Environment specification name.
     */
    public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");

    /**
     * The {@code java.specification.vendor} System Property. Java Runtime Environment specification vendor.
     */
    public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");

    /**
     * The {@code java.specification.version} System Property. Java Runtime Environment specification version.
     */
    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");

    /**
     * The {@code java.util.prefs.PreferencesFactory} System Property. A class name.
     */
    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory");

    /**
     * The {@code java.vendor} System Property. Java vendor-specific string.
     */
    public static final String JAVA_VENDOR = getSystemProperty("java.vendor");

    /**
     * The {@code java.vendor.url} System Property. Java vendor URL.
     */
    public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");

    /**
     * The {@code java.version} System Property. Java version number.
     */
    public static final String JAVA_VERSION = getSystemProperty("java.version");

    /**
     * The {@code java.vm.info} System Property. Java Virtual Machine implementation info.
     */
    public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");

    /**
     * The {@code java.vm.name} System Property. Java Virtual Machine implementation name.
     */
    public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");

    /**
     * The {@code java.vm.specification.name} System Property. Java Virtual Machine specification name.
     */
    public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");

    /**
     * The {@code java.vm.specification.vendor} System Property. Java Virtual Machine specification vendor.
     */
    public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");

    /**
     * The {@code java.vm.specification.version} System Property. Java Virtual Machine specification version.
     */
    public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");

    /**
     * The {@code java.vm.vendor} System Property. Java Virtual Machine implementation vendor.
     */
    public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");

    /**
     * The {@code java.vm.version} System Property. Java Virtual Machine implementation version.
     */
    public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");

    /**
     * The {@code line.separator} System Property. Line separator (<code>&quot;\n&quot;</code> on UNIX).
     */
    public static final String LINE_SEPARATOR = getSystemProperty("line.separator");

    /**
     * <p>
     * The {@code path.separator} System Property. Path separator (<code>&quot;:&quot;</code> on UNIX).
     * </p>
     */
    public static final String PATH_SEPARATOR = getSystemProperty("path.separator");

    /**
     * The {@code user.dir} System Property. User's current working directory.
     */
    public static final String USER_DIR = getSystemProperty(USER_DIR_KEY);

    /**
     * The {@code user.home} System Property. User's home directory.
     */
    public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);

    /**
     * The {@code user.language} System Property. User's language code, such as {@code "en"}.
     */
    public static final String USER_LANGUAGE = getSystemProperty("user.language");

    /**
     * The {@code user.name} System Property. User's account name.
     */
    public static final String USER_NAME = getSystemProperty("user.name");

    /**
     * The {@code user.timezone} System Property. For example: {@code "America/Los_Angeles"}.
     */
    public static final String USER_TIMEZONE = getSystemProperty("user.timezone");

    /**
     * <p>
     * Gets the Java home directory as a {@code File}.
     * </p>
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess} method doesn't allow
     * access to the specified system property.
     * @see System#getProperty(String)
     */
    public static File getJavaHome() {
        return new File(System.getProperty(JAVA_HOME_KEY));
    }

    /**
     * <p>
     * Gets the Java IO temporary directory as a {@code File}.
     * </p>
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess} method doesn't allow
     * access to the specified system property.
     * @see System#getProperty(String)
     */
    public static File getJavaIoTmpDir() {
        return new File(System.getProperty(JAVA_IO_TMPDIR_KEY));
    }

    /**
     * <p>
     * Gets the user directory as a {@code File}.
     * </p>
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess} method doesn't allow
     * access to the specified system property.
     * @see System#getProperty(String)
     */
    public static File getUserDir() {
        return new File(System.getProperty(USER_DIR_KEY));
    }

    /**
     * <p>
     * Gets the user home directory as a {@code File}.
     * </p>
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess} method doesn't allow
     * access to the specified system property.
     * @see System#getProperty(String)
     */
    public static File getUserHome() {
        return new File(System.getProperty(USER_HOME_KEY));
    }

    /**
     * Returns whether the {@link #JAVA_AWT_HEADLESS} value is {@code true}.
     *
     * @return {@code true} if {@code JAVA_AWT_HEADLESS} is {@code "true"}, {@code false} otherwise.
     * @see #JAVA_AWT_HEADLESS
     */
    public static boolean isJavaAwtHeadless() {
        return JAVA_AWT_HEADLESS != null && JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString());
    }

    @SneakyThrows
    public static @NotNull InetAddress getPreferredAddress() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress();
        }
    }

    /**
     * <p>
     * Gets a System property, defaulting to {@code null} if the property cannot be read.
     * </p>
     * <p>
     * If a {@code SecurityException} is caught, the return value is {@code null} and a message is written to
     * {@code System.err}.
     * </p>
     *
     * @param property the system property name
     * @return the system property value or {@code null} if a security problem occurs
     */
    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException ex) {
            // we are not allowed to look at this property
            System.err.println("Caught a SecurityException reading the system property '" + property
                                   + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }

}
