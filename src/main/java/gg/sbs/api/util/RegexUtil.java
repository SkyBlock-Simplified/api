package gg.sbs.api.util;

import gg.sbs.api.hypixel.HypixelAPI;
import gg.sbs.api.util.comparator.LastCharCompare;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used for regular expression replacement on strings.
 *
 * @link https://regex101.com/r/kqVPL5/2
 */
public class RegexUtil {

	private static final transient LinkedHashMap<String, String> ORDERED_MESSAGES = new MaxSizeLinkedMap<>(100);
	private static final transient LastCharCompare CODE_COMPARE = new LastCharCompare();
	private static final transient String ALL_PATTERN = "[0-9A-FK-ORa-fk-or]";
	private static final transient Pattern REPLACE_PATTERN = Pattern.compile("&&(?=" + ALL_PATTERN + ")");

	public static final transient String SECTOR_SYMBOL = "\u00a7";
	public static final transient Pattern VANILLA_PATTERN = Pattern.compile(SECTOR_SYMBOL + "+(" + ALL_PATTERN + ")");

	public static final Pattern UUID_REGEX = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
	public static final Pattern SERVER_REGEX = Pattern.compile("([0-9]{2}/[0-9]{2}/[0-9]{2}) (m(?:ini|ega)?[0-9]{1,3}[A-Za-z])");
	public static final Pattern API_KEY_REGEX = Pattern.compile("§aYour new API key is §r§b" + HypixelAPI.Key.REGEX.pattern() + "§r");
	public static final Pattern TELEPORT_REGEX = Pattern.compile("§aWarped from the §r(.*?)§r§a to the §r(.*?)§r§a!§r");
	public static final Pattern MANA_REGEX = Pattern.compile("§aUsed §r§6(.*?)§r§a! §r§b\\(([\\d]+) Mana\\)§r");
	public static final Pattern NUMBERS_SLASHES = Pattern.compile("[^0-9 /]");
	public static final Pattern LETTERS_NUMBERS = Pattern.compile("[^a-z A-Z:0-9/']");

	//public static final transient Pattern LOG_PATTERN = Pattern.compile("\\{(\\{[\\d]+(?:,[^,}]+)*})}");
	//public static final transient Pattern LOG_PATTERN2 = Pattern.compile("(?!.*\\{[^}]*$)\\{\\K(?:\\{[^}]*\\}|[^{}]*)+");
	//public static final transient Pattern LOG_PATTERN3 = Pattern.compile("(?:(?<!')?\\{(?!'))((?:[^{}]*\\{[^{}]*}[^{}]*)*)(?:(?<!')}(?!')?)");

	public static final transient Pattern LOG_PATTERN4 = Pattern.compile("(?<!')?\\{(?!')((?:[^{}]*\\{[^{}]*}[^{}]*)*)(?<!')}(?!')?");
	public static final transient Pattern URL_PATTERN = Pattern.compile("((?:https?://)?[\\w._-]{2,})\\.([a-z]{2,6}(?:/\\S+)?)");

	static {
		CODE_COMPARE.addIgnoreCharacter('r');
	}

	/**
	 * Replaces the given message's {@link #SECTOR_SYMBOL} with a double ampersand (&&).
	 *
	 * @param message The message to replace the SECTOR_SYMBOL's.
	 * @return The cached replaced message.
	 */
	public static String lameColor(String message) {
		if (!ORDERED_MESSAGES.containsKey(message))
			ORDERED_MESSAGES.put(message, message.replace(SECTOR_SYMBOL, "&&"));

		return ORDERED_MESSAGES.get(message);
	}

	/**
	 * Replaces the given message using the given pattern.
	 *
	 * @param message The message to filter.
	 * @param pattern The regular expression pattern.
	 * @return The cached filtered message.
	 */
	public static String replace(String message, Pattern pattern) {
		return replace(message, pattern, "$1");
	}

	/**
	 * Replaces the given message using the given pattern.
	 *
	 * @param message The message to filter.
	 * @param pattern The regular expression pattern.
	 * @param replace The replacement string.
	 * @return The cached filtered message.
	 */
	public static String replace(String message, Pattern pattern, String replace) {
		return pattern.matcher(message).replaceAll(replace);
	}

	/**
	 * Replaces the colors in the given message using the given pattern.
	 *
	 * @param message The message to filter.
	 * @param pattern The regular expression pattern.
	 * @return The cached filtered message.
	 */
	public static String replaceColor(String message, Pattern pattern) {
		if (!ORDERED_MESSAGES.containsKey(message)) {
			Pattern patternx = Pattern.compile(StringUtil.format("(((?:[&{0}]{1}){2})+)([^&{0}]*)", SECTOR_SYMBOL, "{1,2}", ALL_PATTERN));
			String[] parts = StringUtil.split(" ", message);
			String newMessage = message;

			for (String part : parts) {
				Matcher matcher = patternx.matcher(part);
				String newPart = part;

				while (matcher.find()) {
					String[] codes = matcher.group(1).split(StringUtil.format("(?<!&|{0})", SECTOR_SYMBOL));
					Arrays.sort(codes, CODE_COMPARE);
					String replace = StringUtil.format("{0}{1}", StringUtil.implode(codes), matcher.group(3));
					newPart = newPart.replace(matcher.group(0), replace);
				}

				newMessage = newMessage.replace(part, newPart);
			}

			ORDERED_MESSAGES.put(message, newMessage);
		}

		return replace(replace(ORDERED_MESSAGES.get(message), pattern, RegexUtil.SECTOR_SYMBOL + "$1"), REPLACE_PATTERN, "&");
	}

	/**
	 * Strips the given message using the given pattern.
	 *
	 * @param message The message to filter.
	 * @param pattern The regular expression pattern.
	 * @return The cached filtered message.
	 */
	public static String strip(String message, Pattern pattern) {
		return replace(message, pattern, "");
	}

}