package gg.sbs.api.util.helper;

import gg.sbs.api.minecraft.text.MojangChatFormatting;
import gg.sbs.api.util.MaxSizeLinkedMap;

import java.text.MessageFormat;
import java.util.LinkedHashMap;

/**
 * Format strings using {@link MessageFormat}.
 */
public final class FormatUtil {

    private static final transient LinkedHashMap<String, MessageFormat> MESSAGE_CACHE = new MaxSizeLinkedMap<>(100);
    private static final MojangChatFormatting DEFAULT_IMPORTANT = MojangChatFormatting.AQUA;
    private static final MojangChatFormatting DEFAULT_LOG = MojangChatFormatting.GRAY;

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
     * Returns a formatted string using {@link MessageFormat}.
     *
     * @param format The string to format objects with
     * @param logColor The default color for log messages
     * @param objects The objects to be used for replacement
     * @return The formatted string
     */
    public static String format(String format, MojangChatFormatting logColor, Object... objects) {
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
    public static String format(String format, MojangChatFormatting logColor, MojangChatFormatting logImportant, Object... objects) {
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
    public static String format(String format, boolean prefixColor, MojangChatFormatting logColor, MojangChatFormatting logImportant, Object... objects) {
        if (!MESSAGE_CACHE.containsKey(format)) {
            MessageFormat messageFormat = null;
            String replaceFormat = (logImportant != null ? logImportant : "") + "$1" + (logColor != null ? logColor : "");
            String newFormat = (prefixColor ? logColor : "") + RegexUtil.replaceAll(format, RegexUtil.LOG_PATTERN4, replaceFormat);

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
    public static String preformat(String format, MojangChatFormatting logColor, Object... objects) {
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
    public static String preformat(String format, MojangChatFormatting logColor, MojangChatFormatting logImportant, Object... objects) {
        return format(format, true, logColor, logImportant, objects);
    }

}
