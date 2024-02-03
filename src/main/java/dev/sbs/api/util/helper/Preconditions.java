package dev.sbs.api.util.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;

/**
 * Static convenience methods that help a method or constructor check whether it was invoked
 * correctly (that is, whether its <i>preconditions</i> were met).
 *
 * <p>If the precondition is not met, the {@code Preconditions} method throws an unchecked exception
 * of a specified type, which helps the method in which the exception was thrown communicate that
 * its caller has made a mistake. This allows constructs such as
 *
 * <pre>{@code
 * public static double sqrt(double value) {
 *     if (value < 0)
 *         throw new IllegalArgumentException("input is negative: " + value);
 *
 *     // calculate square root
 * }
 * }</pre>
 *
 * <p>to be replaced with the more compact
 *
 * <pre>{@code
 * public static double sqrt(double value) {
 *     checkArgument(value >= 0, "input is negative: %s", value);
 *     // calculate square root
 * }
 * }</pre>
 *
 * <p>so that a hypothetical bad caller of this method, such as:
 *
 * <pre>{@code
 * void exampleBadCaller() {
 *     double d = sqrt(-1.0);
 * }
 * }</pre>
 *
 * <p>would be flagged as having called {@code sqrt()} with an illegal argument.
 * <h3>Performance</h3>
 *
 * <p>Avoid passing message arguments that are expensive to compute; your code will always compute
 * them, even though they usually won't be needed. If you have such arguments, use the conventional
 * if/throw idiom instead.
 *
 * <p>Depending on your message arguments, memory may be allocated for boxing and varargs array
 * creation. However, the methods of this class have a large number of overloads that prevent such
 * allocations in many common cases.
 *
 * <p>The message string is not formatted unless the exception will be thrown, so the cost of the
 * string formatting itself should not be a concern.
 *
 * <p>As with any performance concerns, you should consider profiling your code (in a production
 * environment if possible) before spending a lot of effort on tweaking a particular element.
 * <h3>Other types of preconditions</h3>
 *
 * <p>Not every type of precondition failure is supported by these methods. Continue to throw
 * standard JDK exceptions such as {@link java.util.NoSuchElementException} or {@link
 * UnsupportedOperationException} in the situations they are intended for.
 * <h3>Non-preconditions</h3>
 *
 * <p>It is of course possible to use the methods of this class to check for invalid conditions
 * which are <i>not the caller's fault</i>. Doing so is <b>not recommended</b> because it is
 * misleading to future readers of the code and of stack traces. See <a
 * href="https://github.com/google/guava/wiki/ConditionalFailuresExplained">Conditional failures
 * explained</a> in the Guava User Guide for more advice.
 *
 * <p>{@code Preconditions} uses {@link String#format(String, Object...)} to format error message template strings.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Preconditions {

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression) {
        if (!expression)
            throw new IllegalArgumentException();
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression   a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression, @NotNull String errorMessage) {
        if (!expression)
            throw new IllegalArgumentException(errorMessage);
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression   a boolean expression
     * @param errorMessage a template for the exception message should the check fail. The message is formed by replacing each {@code %s}
     *                     placeholder in the template with an argument. These are matched by position - the first {@code %s} gets {@code args[0]}, etc
     * @param args         the arguments to be substituted into the message template. Arguments are converted to strings using {@link String#valueOf(Object)}.
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression, @NotNull @PrintFormat String errorMessage, @Nullable Object... args) {
        if (!expression)
            throw new IllegalArgumentException(String.format(errorMessage, args));
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(boolean expression) {
        if (!expression)
            throw new IllegalStateException();
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression   a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(boolean expression, @NotNull String errorMessage) {
        if (!expression)
            throw new IllegalStateException(errorMessage);
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression   a boolean expression
     * @param errorMessage a template for the exception message should the check fail. The message is formed by replacing each {@code %s}
     *                     placeholder in the template with an argument. These are matched by position - the first {@code %s} gets {@code args[0]}, etc.
     * @param args         the arguments to be substituted into the message template. Arguments are converted to strings using {@link String#valueOf(Object)}.
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(boolean expression, @NotNull @PrintFormat String errorMessage, @Nullable Object... args) {
        if (!expression)
            throw new IllegalStateException(String.format(errorMessage, args));
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(@Nullable T reference) {
        if (reference == null)
            throw new NullPointerException();

        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference    an object reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(@Nullable T reference, @NotNull String errorMessage) {
        if (reference == null)
            throw new NullPointerException(errorMessage);

        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference    an object reference
     * @param errorMessage a template for the exception message should the check fail. The message is formed by replacing each {@code %s}
     *                     placeholder in the template with an argument. These are matched by position - the first {@code %s} gets {@code args[0]}, etc.
     * @param args         the arguments to be substituted into the message template. Arguments are converted to strings using {@link String#valueOf(Object)}.
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(@Nullable T reference, String errorMessage, @Nullable Object... args) {
        if (reference == null)
            throw new NullPointerException(String.format(errorMessage, args));

        return reference;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size
     * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size  the size of that array, list or string
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws IllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size) {
        return checkElementIndex(index, size, "index");
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size
     * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size  the size of that array, list or string
     * @param desc  the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws IllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size, String desc) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));

        return index;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>position</i> in an array, list or string of
     * size {@code size}. A position index may range from zero to {@code size}, inclusive.
     *
     * @param index a user-supplied index identifying a position in an array, list or string
     * @param size  the size of that array, list or string
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is greater than {@code size}
     * @throws IllegalArgumentException  if {@code size} is negative
     */
    public static int checkPositionIndex(int index, int size) {
        return checkPositionIndex(index, size, "index");
    }

    /**
     * Ensures that {@code index} specifies a valid <i>position</i> in an array, list or string of
     * size {@code size}. A position index may range from zero to {@code size}, inclusive.
     *
     * @param index a user-supplied index identifying a position in an array, list or string
     * @param size  the size of that array, list or string
     * @param desc  the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is greater than {@code size}
     * @throws IllegalArgumentException  if {@code size} is negative
     */
    public static int checkPositionIndex(int index, int size, String desc) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));

        return index;
    }

    /**
     * Ensures that {@code start} and {@code end} specify valid <i>positions</i> in an array, list or
     * string of size {@code size}, and are in order. A position index may range from zero to {@code
     * size}, inclusive.
     *
     * @param start a user-supplied index identifying a starting position in an array, list or string
     * @param end   a user-supplied index identifying an ending position in an array, list or string
     * @param size  the size of that array, list or string
     * @throws IndexOutOfBoundsException if either index is negative or is greater than {@code size},
     *                                   or if {@code end} is less than {@code start}
     * @throws IllegalArgumentException  if {@code size} is negative
     */
    public static void checkPositionIndexes(int start, int end, int size) {
        if (start < 0 || end < start || end > size)
            throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
    }

    private static String badPositionIndexes(int start, int end, int size) {
        if (start < 0 || start > size)
            return badPositionIndex(start, size, "start index");

        if (end < 0 || end > size)
            return badPositionIndex(end, size, "end index");

        // end < start
        return String.format("end index (%s) must not be less than start index (%s)", end, start);
    }

    private static String badElementIndex(int index, int size, String desc) {
        if (index < 0)
            return String.format("%s (%s) must not be negative", desc, index);
        else if (size < 0)
            throw new IllegalArgumentException("negative size: " + size);
        else // index >= size
            return String.format("%s (%s) must be less than size (%s)", desc, index, size);
    }

    private static String badPositionIndex(int index, int size, String desc) {
        if (index < 0)
            return String.format("%s (%s) must not be negative", desc, index);
        else if (size < 0)
            throw new IllegalArgumentException("negative size: " + size);
        else // index > size
            return String.format("%s (%s) must not be greater than size (%s)", desc, index, size);
    }

    /**
     * Ensures the given array is not null and does not contain a null element.
     *
     * @param array The array to check.
     * @param <T> The type of elements.
     * @throws IllegalArgumentException If the given array is null or contains a null element.
     */
    public static <T> void noNullElements(@NotNull T[] array) throws IllegalArgumentException {
        noNullElements(array, "The validated array is NULL!", "The validated array contains NULL element at index %s!");
    }

    /**
     * Ensures the given collection is not null and does not contain a null element.
     *
     * @param collection The collection to check.
     * @param <T> The type of elements.
     * @throws IllegalArgumentException If the given collection is null or contains a null element.
     */
    public static <T> void noNullElements(@NotNull Collection<? extends T> collection) throws IllegalArgumentException {
        noNullElements(collection, "The validated collection is NULL!", "The validated collection contains NULL element at index %s!");
    }

    /**
     * Ensures the given array is not null and does not contain a null element.
     *
     * @param array The array to check.
     * @param <T> The type of elements.
     * @param message The custom message to display if an IllegalArgumentException is thrown.
     * @throws IllegalArgumentException If the given array is null or contains a null element.
     */
    public static <T> void noNullElements(@NotNull T[] array, String message) throws IllegalArgumentException {
        noNullElements(array, message, message);
    }

    /**
     * Ensures the given collection is not null and does not contain a null element.
     *
     * @param collection The collection to check.
     * @param <T> The type of elements.
     * @param message The custom message to display if an IllegalArgumentException is thrown.
     * @throws IllegalArgumentException If the given collection is null or contains a null element.
     */
    public static <T> void noNullElements(@NotNull Collection<? extends T> collection, String message) throws IllegalArgumentException {
        noNullElements(collection, message, message);
    }

    private static <T> void noNullElements(@Nullable T[] array, String message, String elementMessage) throws IllegalArgumentException {
        if (array == null)
            throw new IllegalArgumentException(message);

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null)
                throw new IllegalArgumentException(String.format(elementMessage, i));
        }
    }

    private static <T> void noNullElements(@Nullable Collection<? extends T> collection, String message, String elementMessage) throws IllegalArgumentException {
        if (collection == null)
            throw new IllegalArgumentException(message);

        Iterator<? extends T> iterator = collection.iterator();
        int i = -1;

        while (iterator.hasNext()) {
            T obj = iterator.next();
            i++;

            if (obj == null)
                throw new IllegalArgumentException(String.format(elementMessage, i));
        }
    }

}
