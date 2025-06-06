package dev.sbs.api.math.function;

import lombok.Getter;

/**
 * A class representing a Function which can be used in an expression
 */
@Getter
public abstract class MathFunction {

    protected final int minArguments;
    protected final int maxArguments;
    /**
     * Get the name of the Function
     */
    private final String name;

    /**
     * Create a new Function with a given name that takes a single argument
     *
     * @param name the name of the Function
     */
    public MathFunction(String name) {
        this(name, 1, 1);
    }

    /**
     * Create a new Function with a given name and number of arguments
     *
     * @param name         the name of the Function
     * @param numArguments the number of arguments the function takes
     */
    public MathFunction(String name, int numArguments) {
        this(name, numArguments, numArguments);
    }

    /**
     * Create a new Function with a given name and number of arguments
     *
     * @param name         the name of the Function
     * @param minArguments the minimum number of arguments the function takes
     * @param maxArguments the maximum number of arguments the function takes
     */
    public MathFunction(String name, int minArguments, int maxArguments) {
        if (minArguments < 0 || minArguments > maxArguments)
            throw new IllegalArgumentException(String.format("The number of function arguments can not be less than 0 or more than '%s' for '%s'.", maxArguments, name));

        if (!isValidFunctionName(name))
            throw new IllegalArgumentException("The function name '" + name + "' is invalid");

        this.name = name;
        this.minArguments = minArguments;
        this.maxArguments = maxArguments;
    }

    /**
     * Get the set of characters which are allowed for use in Function names.
     *
     * @return the array of characters allowed
     * @deprecated since 0.4.5 All unicode letters are allowed to be used in function names since 0.4.3. This API
     * Function can be safely ignored. Checks for function name validity can be done using Character.isLetter() et al.
     */
    public static char[] getAllowedFunctionCharacters() {
        char[] chars = new char[53];
        int count = 0;
        for (int i = 65; i < 91; i++) {
            chars[count++] = (char) i;
        }
        for (int i = 97; i < 123; i++) {
            chars[count++] = (char) i;
        }
        chars[count] = '_';
        return chars;
    }

    public static boolean isValidFunctionName(final String name) {
        if (name == null) {
            return false;
        }

        final int size = name.length();

        if (size == 0) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            final char c = name.charAt(i);
            if (Character.isLetter(c) || c == '_') {
                continue;
            } else if (Character.isDigit(c) && i > 0) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Get the number of arguments of a function with fixed arguments length.
     * This function may be called only on functions with a fixed number of arguments and will throw an @UnsupportedOperationException otherwise.
     * When using functions with variable arguments length use @getMaxNumArguments and @getMinNumArguments instead.
     *
     * @return the number of arguments
     */
    public int getNumArguments() {
        if (this.minArguments != this.maxArguments)
            throw new UnsupportedOperationException("Calling getNumArgument() is not supported for var arg functions, please use getMaxNumArguments() or getMinNumArguments()");

        return this.minArguments;
    }

    /**
     * Method that does the actual calculation of the function value given the arguments
     *
     * @param args the set of arguments used for calculating the function
     * @return the result of the function evaluation
     */
    public abstract double apply(double... args);

}
