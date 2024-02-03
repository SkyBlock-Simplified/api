package dev.sbs.api.util.io.commandline.exception;

import lombok.Getter;

/**
 * Exception thrown during parsing signalling an unrecognized
 * option was seen.
 */
@Getter
public class UnrecognizedOptionException extends ParseException {

    /**
     * The  unrecognized option
     */
    private String option;

    /**
     * Construct a new <code>UnrecognizedArgumentException</code>
     * with the specified detail message.
     *
     * @param message the detail message
     */
    public UnrecognizedOptionException(String message) {
        super(message);
    }

    /**
     * Construct a new <code>UnrecognizedArgumentException</code>
     * with the specified option and detail message.
     *
     * @param message the detail message
     * @param option  the unrecognized option
     * @since 1.2
     */
    public UnrecognizedOptionException(String message, String option) {
        this(message);
        this.option = option;
    }

}
