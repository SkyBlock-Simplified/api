package dev.sbs.api.util.io.commandline.exception;

import dev.sbs.api.util.io.commandline.option.Option;
import dev.sbs.api.util.io.commandline.option.OptionGroup;
import lombok.Getter;

/**
 * Thrown when more than one option in an option group
 * has been provided.
 *
 * @version $Id: AlreadySelectedException.java 1443102 2013-02-06 18:12:16Z tn $
 */
@Getter
public class AlreadySelectedException extends ParseException {

    /** The option group selected. */
    private OptionGroup group;

    /** The option that triggered the exception.
     * -- GETTER --
     *  Returns the option that was added to the group and triggered the exception.
     *
     * @return the related option
     *
     */
    private Option option;

    /**
     * Construct a new <code>AlreadySelectedException</code>
     * with the specified detail message.
     *
     * @param message the detail message
     */
    public AlreadySelectedException(String message) {
        super(message);
    }

    /**
     * Construct a new <code>AlreadySelectedException</code>
     * for the specified option group.
     *
     * @param group  the option group already selected
     * @param option the option that triggered the exception
     * @since 1.2
     */
    public AlreadySelectedException(OptionGroup group, Option option) {
        this("The option '" + option.getKey() + "' was specified but an option from this group "
                 + "has already been selected: '" + group.getSelected() + "'");
        this.group = group;
        this.option = option;
    }

}
