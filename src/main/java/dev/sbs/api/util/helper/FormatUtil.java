package dev.sbs.api.util.helper;

import dev.sbs.api.util.MaxSizeLinkedMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.text.StringSubstitutor;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Format strings using {@link MessageFormat}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FormatUtil { // https://regex101.com/r/RFixIy/1

    private static final transient LinkedHashMap<String, MessageFormat> MESSAGE_CACHE = new MaxSizeLinkedMap<>(100);

    //public static final transient Pattern LOG_PATTERN = Pattern.compile("\\{(\\{[\\d]+(?:,[^,}]+)*})}");
    //public static final transient Pattern LOG_PATTERN2 = Pattern.compile("(?!.*\\{[^}]*$)\\{\\K(?:\\{[^}]*\\}|[^{}]*)+");
    //public static final transient Pattern LOG_PATTERN3 = Pattern.compile("(?:(?<!')?\\{(?!'))((?:[^{}]*\\{[^{}]*}[^{}]*)*)(?:(?<!')}(?!')?)");
    public static final transient Pattern LOG_PATTERN4 = Pattern.compile("(?<!')?\\{(?!')((?:[^{}]*\\{[^{}]*}[^{}]*)*)(?<!')}(?!')?");

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
     * Returns a formatted string using {@link MessageFormat} and {@link StringSubstitutor}.
     *
     * @param format The string to format objects with
     * @param objects The objects to be used for replacement
     * @return The formatted string
     */
    public static String format(String format, Map<String, Object> placeholders, Object... objects) {
        if (!MESSAGE_CACHE.containsKey(format)) {
            MessageFormat messageFormat = null;
            String replaceFormat = "$1";
            String newFormat = RegexUtil.replaceAll(format, LOG_PATTERN4, replaceFormat);

            try {
                messageFormat = new MessageFormat(newFormat);
            } catch (IllegalArgumentException ilaException) {
                newFormat = newFormat.replaceAll(LOG_PATTERN4.pattern(), "\\[$1\\]");

                try {
                    messageFormat = new MessageFormat(newFormat);
                } catch (IllegalArgumentException ignore) { }
            }

            MESSAGE_CACHE.put(format, messageFormat);
        }

        MessageFormat messageFormat = MESSAGE_CACHE.get(format);
        String result = (messageFormat != null ? messageFormat.format(objects) : format);

        // Handle Placeholders
        if (placeholders != null)
            result = StringSubstitutor.replace(result, placeholders);

        return result;
    }

}
