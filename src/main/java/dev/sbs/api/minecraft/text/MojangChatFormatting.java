package dev.sbs.api.minecraft.text;

import com.google.common.base.Preconditions;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.RegexUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;

import java.awt.*;
import java.util.regex.Pattern;

public enum MojangChatFormatting {

    BLACK('0', 0x000000),
    DARK_BLUE('1', 0x0000AA),
    DARK_GREEN('2', 0x00AA00),
    DARK_AQUA('3', 0x00AAAA),
    DARK_RED('4', 0xAA0000),
    DARK_PURPLE('5', 0xAA00AA),
    GOLD('6', 0xFFAA00),
    GRAY('7', 0xAAAAAA),
    DARK_GRAY('8', 0x555555),
    BLUE('9', 0x5555FF),
    GREEN('a', 0x55FF55),
    AQUA('b', 0x55FFFF),
    RED('c', 0xFF5555),
    LIGHT_PURPLE('d', 0xFF55FF),
    YELLOW('e', 0xFFFF55),
    WHITE('f', 0xFFFFFF),
    OBFUSCATED('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINED('n', true),
    ITALIC('o', true),
    RESET('r');

    public static final char COLOR_CHAR = '\u00a7';
    @Getter private final char code;
    @Getter private final boolean isFormat;
    private final String toString;
    private final Color color;

    MojangChatFormatting(char code) {
        this(code, -1);
    }

    MojangChatFormatting(char code, int rgb) {
        this(code, false, rgb);
    }

    MojangChatFormatting(char code, boolean isFormat) {
        this(code, isFormat, -1);
    }

    MojangChatFormatting(char code, boolean isFormat, int rgb) {
        this.code = code;
        this.isFormat = isFormat;
        this.toString = new String(new char[]{COLOR_CHAR, code});
        this.color = (this.isColor() ? new Color(rgb) : null);
    }

    /**
     * Get the color represented by the specified code.
     *
     * @param code The code to search for.
     * @return The mapped color, or null if non exists.
     */
    public static MojangChatFormatting getByChar(char code) {
        for (MojangChatFormatting color : values()) {
            if (color.code == code)
                return color;
        }

        return null;
    }

    public Color getColor() {
        Preconditions.checkArgument(this.isColor(), "Format has no color!");
        return this.color;
    }

    public Color getColor(float alpha) {
        return this.getColor((int) alpha);
    }

    public Color getColor(int alpha) {
        return new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), alpha);
    }

    public int getRGB() {
        return this.getColor().getRGB();
    }

    public boolean isColor() {
        return !this.isFormat() && this != RESET;
    }

    public MojangChatFormatting getNextFormat() {
        return this.getNextFormat(ordinal());
    }

    private MojangChatFormatting getNextFormat(int ordinal) {
        int nextColor = ordinal + 1;

        if (nextColor > values().length - 1)
            return values()[0];
        else if (!values()[nextColor].isColor())
            return getNextFormat(nextColor);

        return values()[nextColor];
    }

    /**
     * Strips the given message of all color and format codes
     *
     * @param value String to strip of color
     * @return A copy of the input string, without any coloring
     */
    public static String stripColor(String value) {
        return RegexUtil.strip(StringUtil.defaultString(value), RegexUtil.VANILLA_PATTERN);
    }

    public static String translateAlternateColorCodes(char altColorChar, String value) {
        Pattern replaceAltColor = Pattern.compile(FormatUtil.format("(?<!{0}){0}([0-9a-fk-orA-FK-OR])", altColorChar));
        return RegexUtil.replaceColor(value, replaceAltColor);
    }

    @Override
    public String toString() {
        return this.toString;
    }

}
