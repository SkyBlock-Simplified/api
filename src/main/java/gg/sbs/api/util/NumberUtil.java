package gg.sbs.api.util;

import gg.sbs.api.reflection.Reflection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A collection of number utilities to assist in number checking and formatting,
 * random number generating as well as {@link DataUtil#readVarInt(DataInputStream) readVarInt}
 * and {@link DataUtil#writeVarInt(DataOutputStream, int) writeVarInt} used in bukkits network protocols.
 */
public class NumberUtil {

	private final static NumberFormat FORMATTER = NumberFormat.getInstance();
	private final static TreeMap<Integer, String> ROMAN_MAP = new TreeMap<>();
	private final static NavigableMap<Long, String> FORMAT_SUFFIX = new TreeMap<>();

	static {
		ROMAN_MAP.put(1000, "M");
		ROMAN_MAP.put(900, "CM");
		ROMAN_MAP.put(500, "D");
		ROMAN_MAP.put(400, "CD");
		ROMAN_MAP.put(100, "C");
		ROMAN_MAP.put(90, "XC");
		ROMAN_MAP.put(50, "L");
		ROMAN_MAP.put(40, "XL");
		ROMAN_MAP.put(10, "X");
		ROMAN_MAP.put(9, "IX");
		ROMAN_MAP.put(5, "V");
		ROMAN_MAP.put(4, "IV");
		ROMAN_MAP.put(1, "I");

		FORMAT_SUFFIX.put(1_000L, "k");
		FORMAT_SUFFIX.put(1_000_000L, "M");
		FORMAT_SUFFIX.put(1_000_000_000L, "B");
		FORMAT_SUFFIX.put(1_000_000_000_000L, "T");
		FORMAT_SUFFIX.put(1_000_000_000_000_000L, "P");
		FORMAT_SUFFIX.put(1_000_000_000_000_000_000L, "E");
	}


	public static int ceil(double number) {
		int floor = (int)number;
		return (((double)floor == number) ? floor : (floor + (int)(~Double.doubleToRawLongBits(number) >>> 63)));
	}

	public static int floor(double number) {
		int floor = (int)number;
		return (((double)floor == number) ? floor : (floor - (int)(Double.doubleToRawLongBits(number) >>> 63)));
	}

	public static boolean isFinite(double d) {
		return Math.abs(d) <= 1.7976931348623157E308D;
	}

	public static boolean isFinite(float f) {
		return Math.abs(f) <= 3.4028235E38F;
	}

	/**
	 * Gets if {@code value} is a valid number.
	 *
	 * @param value the value to check
	 * @return true if the value can be casted to a number, otherwise false
	 */
	public static boolean isNumber(String value) {
		ParsePosition position = new ParsePosition(0);
		FORMATTER.parse(value, position);
		return value.length() == position.getIndex();
	}

	/**
	 * Gets a truely random number.
	 *
	 * @param minimum the lowest number allowed
	 * @return a random integer between the specified boundaries
	 */
	public static int rand(int minimum) {
		return rand(minimum, Integer.MAX_VALUE);
	}

	/**
	 * Gets a truely random number.
	 *
	 * @param minimum the lowest number allowed
	 * @param maximum the highest number allowed
	 * @return a random integer between the specified boundaries
	 */
	public static int rand(int minimum, int maximum) {
		return ThreadLocalRandom.current().nextInt(minimum, maximum + 1);
	}

	/**
	 * Round to nearest decimal point.
	 *
	 * @param value The number to round up.
	 * @return The rounded number.
	 */
	public static double round(double value) {
		return round(value, 0);
	}

	/**
	 * Round to nearest decimal point.
	 *
	 * @param value The number to round up.
	 * @param precision How many decimal points to keep.
	 * @return The rounded number.
	 */
	public static double round(double value, int precision) {
		int scale = (int)Math.pow(10, precision);
		return (double)Math.round(value * scale) / scale;
	}

	/**
	 * Rounds number up to nearest multipleOf value
	 *
	 * @param number the number to round up
	 * @param multipleOf multiple to round to
	 * @return rounded up version of number
	 */
	public static int roundUp(double number, int multipleOf) {
		return (int)(Math.ceil(number / multipleOf) * multipleOf);
	}

	/**
	 * Rounds number down to nearest multipleOf value
	 *
	 * @param number the number to round down
	 * @param multipleOf multiple to round to
	 * @return rounded down version of number
	 */
	public static int roundDown(double number, int multipleOf) {
		return (int)(Math.floor(number / multipleOf) * multipleOf);
	}

	public static double square(double number) {
		return number * number;
	}

	public static <N extends Number> N to(Object value, Class<N> clazz) {
		Reflection number = new Reflection(clazz);
		String numValue = String.valueOf(value);
		return clazz.cast(number.newInstance(isNumber(numValue) ? numValue : String.valueOf(0)));
	}

	/**
	 * Gets the hexadecimal string of an integer.
	 *
	 * @param number to convert
	 * @return converted byte array as hexadecimal string
	 */
	public static String toHexString(long number) {
		return Long.valueOf(String.valueOf(number), 16).toString();
	}

	/**
	 * Formats a number to have k, M, B, etc.
	 *
	 * @param value The number to format
	 * @return The formatted number
	 */
	public static String format(double value) {
		return format((long)value);
	}

	/**
	 * Formats a number to have k, M, B, etc.
	 *
	 * @param value The number to format
	 * @return The formatted number
	 */
	public static String format(long value) {
		// Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
		if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
		if (value < 0) return "-" + format(-value);
		if (value < 1000) return Long.toString(value); //deal with easy case

		Map.Entry<Long, String> e = FORMAT_SUFFIX.floorEntry(value);
		Long divideBy = e.getKey();
		String suffix = e.getValue();

		long truncated = value / (divideBy / 10); //the number part of the output times 10
		boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
		return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}

	/**
	 * Formats a number to have it's ordinal value as the suffix.
	 *
	 * @param value Number to format.
	 * @return Formatted number.
	 */
	public static String ordinal(int value) {
		String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };

		switch (value % 100) {
			case 11:
			case 12:
			case 13:
				return value + "th";
			default:
				return value + sufixes[value % 10];

		}
	}

	/**
	 * Converts the given number to roman numerals.
	 *
	 * @param number Number to convert to roman numerals
	 * @return Roman numeral of value
	 */
	public static String toRoman(int number) {
		if (number == 0) return "";
		int highest = ROMAN_MAP.floorKey(number);

		if (number == highest)
			return ROMAN_MAP.get(number);

		return ROMAN_MAP.get(highest) + toRoman(number - highest);
	}

	/**
	 * Gets the base 10 representation of the specified hexadecimal string.
	 *
	 * @param hexString Hexadecimal string to convert.
	 * @return Base 10 version.
	 */
	public static Long toLong(String hexString) {
		return Long.parseLong(hexString, 16);
	}

}