package dev.sbs.api.util.io.commandline.parser;

import dev.sbs.api.util.io.commandline.CommandLine;
import dev.sbs.api.util.io.commandline.exception.ParseException;
import dev.sbs.api.util.io.commandline.option.Options;

/**
 * A class that implements the <code>CommandLineParser</code> interface
 * can parse a String array according to the {@link Options} specified
 * and return a {@link CommandLine}.
 */
public interface CommandLineParser {

    /**
     * Parse the arguments according to the specified options.
     *
     * @param options the specified Options
     * @param arguments the command line arguments
     * @return the list of atomic option and value tokens
     *
     * @throws ParseException if there are any problems encountered
     * while parsing the command line tokens.
     */
    CommandLine parse(Options options, String[] arguments) throws ParseException;

    /**
     * Parse the arguments according to the specified options and
     * properties.
     *
     * @param options the specified Options
     * @param arguments the command line arguments
     * @param properties command line option name-value pairs
     * @return the list of atomic option and value tokens
     *
     * @throws ParseException if there are any problems encountered
     * while parsing the command line tokens.
     */
    /* To maintain binary compatibility, this is commented out.
       It is still in the abstract Parser class, so most users will
       still reap the benefit.
    CommandLine parse(Options options, String[] arguments, Properties properties)
               throws ParseException;
     */

    /**
     * Parse the arguments according to the specified options.
     *
     * @param options the specified Options
     * @param arguments the command line arguments
     * @param stopAtNonOption if <tt>true</tt> an unrecognized argument stops
     *     the parsing and the remaining arguments are added to the 
     *     {@link CommandLine}s args list. If <tt>false</tt> an unrecognized
     *     argument triggers a ParseException.
     *
     * @return the list of atomic option and value tokens
     * @throws ParseException if there are any problems encountered
     * while parsing the command line tokens.
     */
    CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException;

}
