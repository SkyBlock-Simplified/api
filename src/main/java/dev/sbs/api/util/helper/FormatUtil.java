package dev.sbs.api.util.helper;

import dev.sbs.api.minecraft.text.MinecraftChatFormatting;
import dev.sbs.api.util.MaxSizeLinkedMap;
import org.apache.commons.text.StringSubstitutor;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Format strings using {@link MessageFormat}.
 */
public final class FormatUtil {

    private static final transient LinkedHashMap<String, MessageFormat> MESSAGE_CACHE = new MaxSizeLinkedMap<>(100);
    private static final MinecraftChatFormatting DEFAULT_IMPORTANT = MinecraftChatFormatting.AQUA;
    private static final MinecraftChatFormatting DEFAULT_LOG = MinecraftChatFormatting.GRAY;

    private FormatUtil() { }

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
            String newFormat = RegexUtil.replaceAll(format, RegexUtil.LOG_PATTERN4, replaceFormat);

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
        String result = (messageFormat != null ? messageFormat.format(objects) : format);

        // Handle Placeholders
        if (placeholders != null)
            result = StringSubstitutor.replace(result, placeholders);

        return result;
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
    public static String preformat(String format, MinecraftChatFormatting logColor, Object... objects) {
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
    public static String preformat(String format, MinecraftChatFormatting logColor, MinecraftChatFormatting logImportant, Object... objects) {
        return format(format, true, logColor, logImportant, objects);
    }

}
