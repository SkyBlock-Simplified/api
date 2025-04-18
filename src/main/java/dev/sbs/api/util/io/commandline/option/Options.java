package dev.sbs.api.util.io.commandline.option;

import dev.sbs.api.util.StringUtil;
import dev.sbs.api.util.io.commandline.CommandLine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Main entry-point into the library.
 * <p>
 * Options represents a collection of {@link Option} objects, which
 * describe the possible options for a command-line.
 * <p>
 * It may flexibly parse long and short options, with or without
 * values.  Additionally, it may parse only a portion of a commandline,
 * allowing for flexible multi-stage parsing.
 *
 * @see CommandLine
 */
public class Options implements Serializable {

    /** a map of the options with the character key */
    private final Map<String, Option> shortOpts = new LinkedHashMap<String, Option>();

    /** a map of the options with the long key */
    private final Map<String, Option> longOpts = new LinkedHashMap<String, Option>();

    /** a map of the required options */
    // N.B. This can contain either a String (addOption) or an OptionGroup (addOptionGroup)
    // TODO this seems wrong
    private final List<Object> requiredOpts = new ArrayList<Object>();

    /** a map of the option groups */
    private final Map<String, OptionGroup> optionGroups = new LinkedHashMap<String, OptionGroup>();

    /**
     * Add the specified option group.
     *
     * @param group the OptionGroup that is to be added
     * @return the resulting Options instance
     */
    public Options addOptionGroup(OptionGroup group) {
        if (group.isRequired()) {
            requiredOpts.add(group);
        }

        for (Option option : group.getOptions()) {
            // an Option cannot be required if it is in an
            // OptionGroup, either the group is required or
            // nothing is required
            option.setRequired(false);
            addOption(option);

            optionGroups.put(option.getKey(), group);
        }

        return this;
    }

    /**
     * Lists the OptionGroups that are members of this Options instance.
     *
     * @return a Collection of OptionGroup instances.
     */
    public Collection<OptionGroup> getOptionGroups() {
        return new HashSet<>(optionGroups.values());
    }

    /**
     * Add an option that only contains a short name.
     *
     * <p>
     * The option does not take an argument.
     * </p>
     *
     * @param opt Short single-character name of the option.
     * @param description Self-documenting description
     * @return the resulting Options instance
     * @since 1.3
     */
    public Options addOption(String opt, String description) {
        addOption(opt, null, false, description);
        return this;
    }

    /**
     * Add an option that only contains a short-name.
     *
     * <p>
     * It may be specified as requiring an argument.
     * </p>
     *
     * @param opt Short single-character name of the option.
     * @param hasArg flag signally if an argument is required after this option
     * @param description Self-documenting description
     * @return the resulting Options instance
     */
    public Options addOption(String opt, boolean hasArg, String description) {
        addOption(opt, null, hasArg, description);
        return this;
    }

    /**
     * Add an option that contains a short-name and a long-name.
     *
     * <p>
     * It may be specified as requiring an argument.
     * </p>
     *
     * @param opt Short single-character name of the option.
     * @param longOpt Long multi-character name of the option.
     * @param hasArg flag signally if an argument is required after this option
     * @param description Self-documenting description
     * @return the resulting Options instance
     */
    public Options addOption(String opt, String longOpt, boolean hasArg, String description) {
        addOption(new Option(opt, longOpt, hasArg, description));
        return this;
    }

    /**
     * Add an option that contains a short-name and a long-name.
     *
     * <p>
     * The added option is set as required. It may be specified as requiring an argument. This method is a shortcut for:
     * </p>
     *
     * <pre>
     * <code>
     * Options option = new Option(opt, longOpt, hasArg, description);
     * option.setRequired(true);
     * options.add(option);
     * </code>
     * </pre>
     *
     * @param opt Short single-character name of the option.
     * @param longOpt Long multi-character name of the option.
     * @param hasArg flag signally if an argument is required after this option
     * @param description Self-documenting description
     * @return the resulting Options instance
     * @since 1.4
     */
    public Options addRequiredOption(String opt, String longOpt, boolean hasArg, String description) {
        Option option = new Option(opt, longOpt, hasArg, description);
        option.setRequired(true);
        addOption(option);
        return this;
    }

    /**
     * Adds an option instance
     *
     * @param opt the option that is to be added
     * @return the resulting Options instance
     */
    public Options addOption(Option opt) {
        String key = opt.getKey();

        // add it to the long option list
        if (opt.hasLongOpt()) {
            longOpts.put(opt.getLongOpt(), opt);
        }

        // if the option is required add it to the required list
        if (opt.isRequired()) {
            if (requiredOpts.contains(key)) {
                requiredOpts.remove(key);
            }
            requiredOpts.add(key);
        }

        shortOpts.put(key, opt);

        return this;
    }

    /**
     * Retrieve a read-only list of options in this set
     *
     * @return read-only Collection of {@link Option} objects in this descriptor
     */
    public Collection<Option> getOptions() {
        return Collections.unmodifiableCollection(helpOptions());
    }

    /**
     * Returns the Options for use by the HelpFormatter.
     *
     * @return the List of Options
     */
    List<Option> helpOptions() {
        return new ArrayList<Option>(shortOpts.values());
    }

    /**
     * Returns the required options.
     *
     * @return read-only List of required options
     */
    public List getRequiredOptions() {
        return Collections.unmodifiableList(requiredOpts);
    }

    /**
     * Retrieve the {@link Option} matching the long or short name specified.
     *
     * <p>
     * The leading hyphens in the name are ignored (up to 2).
     * </p>
     *
     * @param opt short or long name of the {@link Option}
     * @return the option represented by opt
     */
    public Option getOption(String opt) {
        opt = StringUtil.stripStart(opt, "-");

        if (shortOpts.containsKey(opt)) {
            return shortOpts.get(opt);
        }

        return longOpts.get(opt);
    }

    /**
     * Returns the options with a long name starting with the name specified.
     *
     * @param opt the partial name of the option
     * @return the options matching the partial name specified, or an empty list if none matches
     * @since 1.3
     */
    public List<String> getMatchingOptions(String opt) {
        opt = StringUtil.stripStart(opt, "-");

        List<String> matchingOpts = new ArrayList<String>();

        // for a perfect match return the single option only
        if (longOpts.containsKey(opt)) {
            return Collections.singletonList(opt);
        }

        for (String longOpt : longOpts.keySet()) {
            if (longOpt.startsWith(opt)) {
                matchingOpts.add(longOpt);
            }
        }

        return matchingOpts;
    }

    /**
     * Returns whether the named {@link Option} is a member of this {@link Options}.
     *
     * @param opt short or long name of the {@link Option}
     * @return true if the named {@link Option} is a member of this {@link Options}
     */
    public boolean hasOption(String opt) {
        opt = StringUtil.stripStart(opt, "-");

        return shortOpts.containsKey(opt) || longOpts.containsKey(opt);
    }

    /**
     * Returns whether the named {@link Option} is a member of this {@link Options}.
     *
     * @param opt long name of the {@link Option}
     * @return true if the named {@link Option} is a member of this {@link Options}
     * @since 1.3
     */
    public boolean hasLongOption(String opt) {
        opt = StringUtil.stripStart(opt, "-");

        return longOpts.containsKey(opt);
    }

    /**
     * Returns whether the named {@link Option} is a member of this {@link Options}.
     *
     * @param opt short name of the {@link Option}
     * @return true if the named {@link Option} is a member of this {@link Options}
     * @since 1.3
     */
    public boolean hasShortOption(String opt) {
        opt = StringUtil.stripStart(opt, "-");

        return shortOpts.containsKey(opt);
    }

    /**
     * Returns the OptionGroup the <code>opt</code> belongs to.
     *
     * @param opt the option whose OptionGroup is being queried.
     * @return the OptionGroup if <code>opt</code> is part of an OptionGroup, otherwise return null
     */
    public OptionGroup getOptionGroup(Option opt) {
        return optionGroups.get(opt.getKey());
    }

    /**
     * Dump state, suitable for debugging.
     *
     * @return Stringified form of this object
     */
    @Override
    public String toString() {

        String buf = "[ Options: [ short " +
            shortOpts +
            " ] [ long " +
            longOpts +
            " ]";

        return buf;
    }

}
