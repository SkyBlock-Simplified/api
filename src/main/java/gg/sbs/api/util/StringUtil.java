package gg.sbs.api.util;

import com.google.common.base.Joiner;
import gg.sbs.api.mojang.ChatFormatting;

import java.text.MessageFormat;
import java.util.*;

/**
 * A collection of string methods for easy string
 * formatting, concatenation, checking and converting.
 */
public class StringUtil {

	private static final transient LinkedHashMap<String, MessageFormat> MESSAGE_CACHE = new MaxSizeLinkedMap<>(100);
	private static final ChatFormatting DEFAULT_IMPORTANT = ChatFormatting.AQUA;
	private static final ChatFormatting DEFAULT_LOG = ChatFormatting.GRAY;

	/**
	 * <p>Capitalizes all the whitespace separated words in a String.
	 * Only the first character of each word is changed. To convert the
	 * rest of each word to lowercase at the same time,
	 * use {@link #capitalizeFully(String)}.</p>
	 *
	 * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * A <code>null</code> input String returns <code>null</code>.
	 * Capitalization uses the Unicode title case, normally equivalent to
	 * upper case.</p>
	 *
	 * <pre>
	 * StringUtil.capitalize(null)        = null
	 * StringUtil.capitalize("")          = ""
	 * StringUtil.capitalize("i am FINE") = "I Am FINE"
	 * </pre>
	 *
	 * @param str  the String to capitalize, may be null
	 * @return capitalized String, <code>null</code> if null String input
	 * @see #uncapitalize(String)
	 * @see #capitalizeFully(String)
	 */
	public static String capitalize(final String str) {
		return capitalize(str, null);
	}


	/**
	 * <p>Capitalizes all the delimiter separated words in a String.
	 * Only the first character of each word is changed. To convert the
	 * rest of each word to lowercase at the same time,
	 * use {@link #capitalizeFully(String, char[])}.</p>
	 *
	 * <p>The delimiters represent a set of characters understood to separate words.
	 * The first string character and the first non-delimiter character after a
	 * delimiter will be capitalized. </p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * Capitalization uses the Unicode title case, normally equivalent to
	 * upper case.</p>
	 *
	 * <pre>
	 * StringUtil.capitalize(null, *)            = null
	 * StringUtil.capitalize("", *)              = ""
	 * StringUtil.capitalize(*, new char[0])     = *
	 * StringUtil.capitalize("i am fine", null)  = "I Am Fine"
	 * StringUtil.capitalize("i aM.fine", {'.'}) = "I aM.Fine"
	 * </pre>
	 *
	 * @param str the String to capitalize, may be null
	 * @param delimiters set of characters to determine capitalization, null means whitespace
	 * @return capitalized String, <code>null</code> if null String input
	 * @see #uncapitalize(String)
	 * @see #capitalizeFully(String)
	 * @since 2.1
	 */
	public static String capitalize(final String str, final char... delimiters) {
		final int delimLen = delimiters == null ? -1 : delimiters.length;
		if (StringUtil.isEmpty(str) || delimLen == 0) {
			return str;
		}
		final char[] buffer = str.toCharArray();
		boolean capitalizeNext = true;
		for (int i = 0; i < buffer.length; i++) {
			final char ch = buffer[i];
			if (isDelimiter(ch, delimiters)) {
				capitalizeNext = true;
			} else if (capitalizeNext) {
				buffer[i] = Character.toTitleCase(ch);
				capitalizeNext = false;
			}
		}
		return new String(buffer);
	}

	/**
	 * <p>Converts all the whitespace separated words in a String into capitalized words,
	 * that is each word is made up of a titlecase character and then a series of
	 * lowercase characters.</p>
	 *
	 * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * A <code>null</code> input String returns <code>null</code>.
	 * Capitalization uses the Unicode title case, normally equivalent to
	 * upper case.</p>
	 *
	 * <pre>
	 * StringUtil.capitalizeFully(null)        = null
	 * StringUtil.capitalizeFully("")          = ""
	 * StringUtil.capitalizeFully("i am FINE") = "I Am Fine"
	 * </pre>
	 *
	 * @param str  the String to capitalize, may be null
	 * @return capitalized String, <code>null</code> if null String input
	 */
	public static String capitalizeFully(final String str) {
		return capitalizeFully(str, null);
	}

	/**
	 * <p>Converts all the delimiter separated words in a String into capitalized words,
	 * that is each word is made up of a titlecase character and then a series of
	 * lowercase characters.</p>
	 *
	 * <p>The delimiters represent a set of characters understood to separate words.
	 * The first string character and the first non-delimiter character after a
	 * delimiter will be capitalized.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * Capitalization uses the Unicode title case, normally equivalent to
	 * upper case.</p>
	 *
	 * <pre>
	 * StringUtil.capitalizeFully(null, *)            = null
	 * StringUtil.capitalizeFully("", *)              = ""
	 * StringUtil.capitalizeFully(*, null)            = *
	 * StringUtil.capitalizeFully(*, new char[0])     = *
	 * StringUtil.capitalizeFully("i aM.fine", {'.'}) = "I am.Fine"
	 * </pre>
	 *
	 * @param str the String to capitalize, may be null
	 * @param delimiters set of characters to determine capitalization, null means whitespace
	 * @return capitalized String, <code>null</code> if null String input
	 */
	public static String capitalizeFully(String str, final char... delimiters) {
		final int delimLen = delimiters == null ? -1 : delimiters.length;
		if (StringUtil.isEmpty(str) || delimLen == 0) {
			return str;
		}
		str = str.toLowerCase();
		return capitalize(str, delimiters);
	}

	/**
	 * Encodes unicode characters in a string.
	 *
	 * @param value String to encode
	 * @return Encoded unicode version of the string
	 */
	public static String escapeUnicode(String value) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < value.length(); i++) {
			int codePoint = Character.codePointAt(value, i);
			int charCount = Character.charCount(codePoint);

			if (charCount > 1) {
				i += charCount - 1;

				if (i >= value.length())
					throw new IllegalArgumentException("Truncated value unexpectedly!");
			}

			if (codePoint < 128)
				builder.appendCodePoint(codePoint);
			else
				builder.append(String.format("\\u%x", codePoint));
		}

		return builder.toString();
	}

	/**
	 * Fixes the string representation of a UUID to contain dashes.
	 *
	 * @param uniqueId Unique id without dashes.
	 * @return Unique id with dashes.
	 */
	public static String fixUUID(String uniqueId) {
		return RegexUtil.UUID_REGEX.matcher((notEmpty(uniqueId) ? uniqueId : "").replace("-", "")).replaceAll("$1-$2-$3-$4-$5");
	}

	/**
	 * Returns a formatted string using {@link MessageFormat}.
	 *
	 * @param format The string to format objects with
	 * @param objects The objects to be used for replacement
	 * @return The formatted string
	 */
	public static String format(String format, Object... objects) {
		return format(format, null, objects);
	}

	/**
	 * Returns a formatted string using {@link MessageFormat}.
	 *
	 * @param format The string to format objects with
	 * @param logColor The default color for log messages
	 * @param objects The objects to be used for replacement
	 * @return The formatted string
	 */
	public static String format(String format, ChatFormatting logColor, Object... objects) {
		return format(format, logColor, null, objects);
	}

	/**
	 * Returns a formatted string using {@link MessageFormat}.
	 *
	 * @param format The string to format objects with
	 * @param logColor The default color for log messages
	 * @param logImportant The important color for log messages
	 * @param objects The objects to be used for replacement
	 * @return The formatted string
	 */
	public static String format(String format, ChatFormatting logColor, ChatFormatting logImportant, Object... objects) {
		return format(format, false, logColor, logImportant, objects);
	}

	/**
	 * Returns a formatted string using {@link MessageFormat}.
	 *
	 * @param format The string to format objects with
	 * @param prefixColor Prefix string with logColor
	 * @param logColor The default color for log messages
	 * @param logImportant The important color for log messages
	 * @param objects The objects to be used for replacement
	 * @return The formatted string
	 */
	public static String format(String format, boolean prefixColor, ChatFormatting logColor, ChatFormatting logImportant, Object... objects) {
		if (!MESSAGE_CACHE.containsKey(format)) {
			MessageFormat messageFormat = null;
			String replaceFormat = (logImportant != null ? logImportant : "") + "$1" + (logColor != null ? logColor : "");
			String newFormat = (prefixColor ? logColor : "") + RegexUtil.replace(format, RegexUtil.LOG_PATTERN4, replaceFormat);

			try {
				messageFormat = new MessageFormat(newFormat);
			} catch (IllegalArgumentException ilaException) {
				newFormat = newFormat.replaceAll(RegexUtil.LOG_PATTERN4.pattern(), "\\[$1\\]");

				try {
					messageFormat = new MessageFormat(newFormat);
				} catch (IllegalArgumentException ignore) { }
			}

			MESSAGE_CACHE.put(format, messageFormat);
		}

		MessageFormat messageFormat = MESSAGE_CACHE.get(format);
		return (messageFormat != null ? messageFormat.format(objects) : format);
	}

	/**
	 * Gets a concatenated string separated by nothing.
	 *
	 * @param pieces to concatenate into string
	 * @return concatenated string
	 */
	public static String implode(String[] pieces) {
		return implode(toList(pieces));
	}

	/**
	 * Gets a concatenated string separated by nothing.
	 *
	 * @param collection to concatenate into string
	 * @return concatenated string
	 */
	public static String implode(Collection<String> collection) {
		return implode("", collection);
	}

	/**
	 * Gets a concatenated string separated by {@code glue}.
	 *
	 * @param glue to separate pieces with
	 * @param pieces to concatenate into string
	 * @return concatenated string
	 */
	public static String implode(String glue, String[] pieces) {
		return implode(glue, toList(pieces));
	}

	/**
	 * Gets a concatenated string separated by {@code glue}
	 *
	 * @param glue to separate pieces with
	 * @param collection to concatenate into string
	 * @return concatenated string
	 */
	public static String implode(String glue, Collection<String> collection) {
		return implode(glue, collection, 0);
	}

	/**
	 * Gets a concatenated string separated by nothing,
	 * and starts at index {@code start}.
	 *
	 * @param pieces to concatenate into string
	 * @param start index to start concatenating
	 * @return concatenated string
	 */
	public static String implode(String[] pieces, int start) {
		return implode("", toList(pieces), start);
	}

	/**
	 * Gets a concatenated string separated by nothing,
	 * and starts at index {@code start}.
	 *
	 * @param collection to concatenate into string
	 * @param start index to start concatenating
	 * @return concatenated string
	 */
	public static String implode(Collection<String> collection, int start) {
		return implode("", collection, start);
	}

	/**
	 * Gets a concatenated string separated by {@code glue},
	 * and starts at index {@code start}.
	 *
	 * @param glue to separate pieces with
	 * @param pieces to concatenate into string
	 * @param start index to start concatenating
	 * @return concatenated string
	 */
	public static String implode(String glue, String[] pieces, int start) {
		return implode(glue, toList(pieces), start);
	}

	/**
	 * Gets a concatenated string separated by {@code glue},
	 * and starts at index {@code start}.
	 *
	 * @param glue to separate pieces with
	 * @param collection to concatenate into string
	 * @param start index to start concatenating
	 * @return concatenated string
	 */
	public static String implode(String glue, Collection<String> collection, int start) {
		return implode(glue, collection, start, -1);
	}

	/**
	 * Gets a concatenated string separated by nothing,
	 * starts at index {@code start} and ends at index {@code end}.
	 *
	 * @param pieces to concatenate into string
	 * @param start index to start concatenating
	 * @param end index to stop concatenating
	 * @return concatenated string
	 */
	public static String implode(String[] pieces, int start, int end) {
		return implode("", toList(pieces), start, end);
	}

	/**
	 * Gets a concatenated string separated by nothing,
	 * starts at index {@code start} and ends at index {@code end}.
	 *
	 * @param collection to concatenate into string
	 * @param start index to start concatenating
	 * @param end index to stop concatenating
	 * @return concatenated string
	 */
	public static String implode(Collection<String> collection, int start, int end) {
		return implode("", collection, start, end);
	}

	/**
	 * Gets a concatenated string separated by {@code glue},
	 * starts at index {@code start} and ends at index {@code end}.
	 *
	 * @param glue to separate pieces with
	 * @param pieces to concatenate into string
	 * @param start index to start concatenating
	 * @param end index to stop concatenating
	 * @return concatenated string
	 */
	public static String implode(String glue, String[] pieces, int start, int end) {
		return implode(glue, toList(pieces), start, end);
	}

	/**
	 * Gets a concatenated string separated by {@code glue},
	 * starts at index {@code start} and ends at index {@code end}.
	 *
	 * @param glue to separate pieces with
	 * @param collection to concatenate into string
	 * @param start index to start concatenating
	 * @param end index to stop concatenating
	 * @return concatenated string
	 */
	public static String implode(String glue, Collection<String> collection, int start, int end) {
		if (isEmpty(glue)) glue = "";
		if (collection == null || collection.isEmpty()) return "";
		//if (collection == null || collection.isEmpty()) throw new IllegalArgumentException("Collection cannot be empty!");
		if (start < 0) start = 0;
		if (start > collection.size()) throw new IndexOutOfBoundsException(String.format("Cannot access index %d out of %d total pieces!", start, collection.size()));
		if (end < 0) end = collection.size();
		if (end > collection.size()) throw new IndexOutOfBoundsException(String.format("Cannot access index %d out of %d total pieces!", end, collection.size()));
		List<String> pieces = new ArrayList<>(collection);
		List<String> newPieces = new ArrayList<>();

		for (int i = start; i < end; i++)
			newPieces.add(pieces.get(i));

		return Joiner.on(glue).join(newPieces);
	}

	/**
	 * Is the character a delimiter.
	 *
	 * @param ch the character to check
	 * @param delimiters the delimiters
	 * @return true if it is a delimiter
	 */
	private static boolean isDelimiter(final char ch, final char[] delimiters) {
		if (delimiters == null) {
			return Character.isWhitespace(ch);
		}
		for (final char delimiter : delimiters) {
			if (ch == delimiter) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets if the {@code value} is empty or null.
	 *
	 * @param value to check
	 * @return true if empty or null, otherwise false
	 */
	public static boolean isEmpty(CharSequence value) {
		return value == null || "".contentEquals(value) || value.length() == 0;
	}

	/**
	 * Gets if the {@code value} is empty or null.
	 *
	 * @param value to check
	 * @return true if empty or null, otherwise false
	 */
	public static boolean isEmpty(String value) {
		return value == null || "".equals(value) || value.isEmpty();
	}

	/**
	 * Gets if the {@code value} is not empty.
	 *
	 * @param value to check
	 * @return true if not empty or null, otherwise false
	 */
	public static boolean notEmpty(CharSequence value) {
		return !isEmpty(value);
	}

	/**
	 * Gets if the {@code value} is not empty.
	 *
	 * @param value to check
	 * @return true if not empty or null, otherwise false
	 */
	public static boolean notEmpty(String value) {
		return !isEmpty(value);
	}

	/**
	 * Returns a formatted string using {@link MessageFormat}.
	 *
	 * @param format The string to format objects with
	 * @param objects The objects to be used for replacement
	 * @return The formatted string
	 */
	public static String preformat(String format, Object... objects) {
		return format(format, true, DEFAULT_LOG.getColor(), DEFAULT_IMPORTANT.getColor(), objects);
	}

	/**
	 * Returns a formatted string using {@link MessageFormat}.
	 *
	 * @param format The string to format objects with
	 * @param logColor The default color for log messages
	 * @param objects The objects to be used for replacement
	 * @return The formatted string
	 */
	public static String preformat(String format, ChatFormatting logColor, Object... objects) {
		return format(format, true, logColor, DEFAULT_IMPORTANT.getColor(), objects);
	}

	/**
	 * Returns a formatted string using {@link MessageFormat}.
	 *
	 * @param format The string to format objects with
	 * @param logColor The default color for log messages
	 * @param logImportant The important color for log messages
	 * @param objects The objects to be used for replacement
	 * @return The formatted string
	 */
	public static String preformat(String format, ChatFormatting logColor, ChatFormatting logImportant, Object... objects) {
		return format(format, true, logColor, logImportant, objects);
	}

	/**
	 * Repeats the string {@code value} by the passed number of {@code times}.
	 *
	 * @param value The value to repeat.
	 * @param times The number of times to repeat.
	 * @return a repeated string of the specified value
	 */
	public static String repeat(String value, int times) {
		return new String(new char[value.length() * times]).replaceAll("\0", value);
	}

	/**
	 * Gets a split array of the {@code value} using {@code regex}.
	 *
	 * @param regex The delimiting regular expression.
	 * @param value The value to split.
	 * @return a split array using the specified regex
	 */
	public static String[] split(String regex, String value) {
		return isEmpty(value) ? new String[0] : value.split(regex);
	}

	/**
	 * Removes null from {@code value} and will either be an empty
	 * value or the original passed value.
	 *
	 * @param value to safely return
	 * @return value or empty string
	 */
	public static String stripNull(String value) {
		return isEmpty(value) ? "" : value;
	}

	/**
	 * Gets a list of the string array. If the array is empty then an empty list is returned.
	 *
	 * @param array to check
	 * @return string array converted to string list
	 */
	public static List<String> toList(String... array) {
		return new ArrayList<>(Arrays.asList((array == null || array.length == 0) ? new String[] {} : array));
	}

	/**
	 * Converts a string representation (with or without dashes) of a UUID to the {@link UUID} class.
	 *
	 * @param uniqueId Unique id to convert.
	 * @return Converted unique id.
	 */
	public static UUID toUUID(String uniqueId) {
		return UUID.fromString(fixUUID(uniqueId));
	}

	/**
	 * <p>Uncapitalizes all the whitespace separated words in a String.
	 * Only the first character of each word is changed.</p>
	 *
	 * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * A <code>null</code> input String returns <code>null</code>.</p>
	 *
	 * <pre>
	 * StringUtil.uncapitalize(null)        = null
	 * StringUtil.uncapitalize("")          = ""
	 * StringUtil.uncapitalize("I Am FINE") = "i am fINE"
	 * </pre>
	 *
	 * @param str the String to uncapitalize, may be null
	 * @return uncapitalized String, <code>null</code> if null String input
	 * @see #capitalize(String)
	 */
	public static String uncapitalize(final String str) {
		return uncapitalize(str, null);
	}

	/**
	 * <p>Uncapitalizes all the whitespace separated words in a String.
	 * Only the first character of each word is changed.</p>
	 *
	 * <p>The delimiters represent a set of characters understood to separate words.
	 * The first string character and the first non-delimiter character after a
	 * delimiter will be uncapitalized. </p>
	 *
	 * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * A <code>null</code> input String returns <code>null</code>.</p>
	 *
	 * <pre>
	 * StringUtil.uncapitalize(null, *)            = null
	 * StringUtil.uncapitalize("", *)              = ""
	 * StringUtil.uncapitalize(*, null)            = *
	 * StringUtil.uncapitalize(*, new char[0])     = *
	 * StringUtil.uncapitalize("I AM.FINE", {'.'}) = "i AM.fINE"
	 * </pre>
	 *
	 * @param str the String to uncapitalize, may be null
	 * @param delimiters set of characters to determine uncapitalization, null means whitespace
	 * @return uncapitalized String, <code>null</code> if null String input
	 * @see #capitalize(String)
	 */
	public static String uncapitalize(final String str, final char... delimiters) {
		final int delimLen = delimiters == null ? -1 : delimiters.length;
		if (StringUtil.isEmpty(str) || delimLen == 0) {
			return str;
		}
		final char[] buffer = str.toCharArray();
		boolean uncapitalizeNext = true;
		for (int i = 0; i < buffer.length; i++) {
			final char ch = buffer[i];
			if (isDelimiter(ch, delimiters)) {
				uncapitalizeNext = true;
			} else if (uncapitalizeNext) {
				buffer[i] = Character.toLowerCase(ch);
				uncapitalizeNext = false;
			}
		}
		return new String(buffer);
	}

}