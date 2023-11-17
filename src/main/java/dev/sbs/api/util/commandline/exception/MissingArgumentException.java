package dev.sbs.api.util.commandline.exception;

import dev.sbs.api.util.commandline.option.Option;
import lombok.Getter;

/**
 * Thrown when an option requiring an argument
 * is not provided with an argument.
 */
@Getter
public class MissingArgumentException extends ParseException {

    /**
     * The option requiring an argument that wasn't provided
     * on the command line.
     */
    private Option option;

    /**
     * Construct a new <code>MissingArgumentException</code>
     * with the specified detail message.
     *
     * @param message the detail message
     */
    public MissingArgumentException(String message) {
        super(message);
    }

    /**
     * Construct a new <code>MissingArgumentException</code>
     * with the specified detail message.
     *
     * @param option the option requiring an argument
     * @since 1.2
     */
    public MissingArgumentException(Option option) {
        this("Missing argument for option: " + option.getKey());
        this.option = option;
    }
}
