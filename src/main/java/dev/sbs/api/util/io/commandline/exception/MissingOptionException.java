package dev.sbs.api.util.io.commandline.exception;

import java.util.Iterator;
import java.util.List;

/**
 * Thrown when a required option has not been provided.
 */
public class MissingOptionException extends ParseException {

    /** The list of missing options and groups */
    private List missingOptions;

    /**
     * Construct a new <code>MissingSelectedException</code>
     * with the specified detail message.
     *
     * @param message the detail message
     */
    public MissingOptionException(String message) {
        super(message);
    }

    /**
     * Constructs a new <code>MissingSelectedException</code> with the
     * specified list of missing options.
     *
     * @param missingOptions the list of missing options and groups
     * @since 1.2
     */
    public MissingOptionException(List missingOptions) {
        this(createMessage(missingOptions));
        this.missingOptions = missingOptions;
    }

    /**
     * Returns the list of options or option groups missing in the command line parsed.
     *
     * @return the missing options, consisting of String instances for simple
     *         options, and OptionGroup instances for required option groups.
     * @since 1.2
     */
    public List getMissingOptions() {
        return missingOptions;
    }

    /**
     * Build the exception message from the specified list of options.
     *
     * @param missingOptions the list of missing options and groups
     * @since 1.2
     */
    private static String createMessage(List<?> missingOptions) {
        StringBuilder buf = new StringBuilder("Missing required option");
        buf.append(missingOptions.size() == 1 ? "" : "s");
        buf.append(": ");

        Iterator<?> it = missingOptions.iterator();
        while (it.hasNext()) {
            buf.append(it.next());
            if (it.hasNext()) {
                buf.append(", ");
            }
        }

        return buf.toString();
    }

}
