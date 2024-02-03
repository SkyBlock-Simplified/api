package dev.sbs.api.util.io.commandline.exception;

/**
 * Base for Exceptions thrown during parsing of a command-line.
 */
public class ParseException extends RuntimeException {

    /**
     * Construct a new <code>ParseException</code>
     * with the specified detail message.
     *
     * @param message the detail message
     */
    public ParseException(String message)
    {
        super(message);
    }

}
