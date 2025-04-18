package dev.sbs.api.util.io.commandline.option;

import dev.sbs.api.util.io.commandline.exception.AlreadySelectedException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A group of mutually exclusive options.
 *
 * @version $Id: OptionGroup.java 1749596 2016-06-21 20:27:06Z britter $
 */
public class OptionGroup implements Serializable {

    /** hold the options */
    private final Map<String, Option> optionMap = new LinkedHashMap<String, Option>();

    /** the name of the selected option */
    @Getter private String selected;

    /** specified whether this group is required */
    @Getter @Setter
    private boolean required;

    /**
     * Add the specified <code>Option</code> to this group.
     *
     * @param option the option to add to this group
     * @return this option group with the option added
     */
    public OptionGroup addOption(Option option) {
        // key   - option name
        // value - the option
        optionMap.put(option.getKey(), option);

        return this;
    }

    /**
     * @return the names of the options in this group as a 
     * <code>Collection</code>
     */
    public Collection<String> getNames() {
        // the key set is the collection of names
        return optionMap.keySet();
    }

    /**
     * @return the options in this group as a <code>Collection</code>
     */
    public Collection<Option> getOptions() {
        // the values are the collection of options
        return optionMap.values();
    }

    /**
     * Set the selected option of this group to <code>name</code>.
     *
     * @param option the option that is selected
     * @throws AlreadySelectedException if an option from this group has
     * already been selected.
     */
    public void setSelected(Option option) throws AlreadySelectedException {
        if (option == null) {
            // reset the option previously selected
            selected = null;
            return;
        }

        // if no option has already been selected or the 
        // same option is being reselected then set the
        // selected member variable
        if (selected == null || selected.equals(option.getKey())) {
            selected = option.getKey();
        } else {
            throw new AlreadySelectedException(this, option);
        }
    }

    /**
     * Returns the stringified version of this OptionGroup.
     *
     * @return the stringified representation of this group
     */
    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();

        Iterator<Option> iter = getOptions().iterator();

        buff.append("[");

        while (iter.hasNext()) {
            Option option = iter.next();

            if (option.getOpt() != null) {
                buff.append("-");
                buff.append(option.getOpt());
            } else {
                buff.append("--");
                buff.append(option.getLongOpt());
            }

            if (option.getDescription() != null) {
                buff.append(" ");
                buff.append(option.getDescription());
            }

            if (iter.hasNext()) {
                buff.append(", ");
            }
        }

        buff.append("]");

        return buff.toString();
    }

}
