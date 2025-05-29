package dev.sbs.api.minecraft.generator.font;

import dev.sbs.api.minecraft.generator.exception.FontException;
import dev.sbs.api.minecraft.text.segment.ColorSegment;
import dev.sbs.api.util.SystemUtil;
import lombok.Cleanup;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

@Getter
public enum MinecraftFont {

    PLAIN("minecraft/1_Minecraft.otf", FontStyle.PLAIN, 15.5f),
    BOLD("minecraft/3_Minecraft-Bold.otf", FontStyle.BOLD, 20.0f),
    ITALIC("minecraft/2_Minecraft-Italic.otf", FontStyle.ITALIC, 20.5f),
    BOLD_ITALIC("minecraft/4_Minecraft-BoldItalic.otf", FontStyle.BOLD_ITALIC, 20.5f);

    public static final @NotNull Font SANS_SERIF = new Font("", Font.PLAIN, 20);
    private final @NotNull Font font;
    private final @NotNull FontStyle style;
    private final float size;

    MinecraftFont(@NotNull String resourcePath, @NotNull FontStyle style, float size) {
        this.font = initFont(resourcePath, size);
        this.style = style;
        this.size = size;
    }

    /**
     * Initializes a font by loading it from the specified resource path and applying the given font size.
     *
     * @param resourcePath the relative path to the font file resource
     * @param size the desired font size to apply to the loaded font
     * @return the {@link Font} object initialized with the specified resource and size
     * @throws FontException if the font file cannot be loaded or is in an invalid format
     */
    private static @NotNull Font initFont(String resourcePath, float size) throws FontException {
        try {
            @Cleanup InputStream inputStream = SystemUtil.getResource(resourcePath);
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (IOException | FontFormatException ex) {
            throw new FontException(ex, resourcePath);
        }
    }

    /**
     * Retrieves the {@link MinecraftFont} associated with the given {@link FontStyle}.
     * <p>
     * If no matching font is found, the default {@link MinecraftFont#PLAIN} is returned.
     *
     * @param style the {@link FontStyle} to retrieve the corresponding {@link MinecraftFont} for
     * @return the {@link MinecraftFont} corresponding to the given {@link FontStyle}, or {@link MinecraftFont#PLAIN} if none is found
     */
    public static @NotNull MinecraftFont of(@NotNull FontStyle style) {
        for (MinecraftFont font : values()) {
            if (font.getStyle() == style)
                return font;
        }

        return PLAIN;
    }

    /**
     * Retrieves the {@link MinecraftFont} that corresponds to the style described by the given {@link ColorSegment}.
     * <p>
     * If no matching font is found, the default {@link MinecraftFont#PLAIN} is returned.
     *
     * @param segment the {@link ColorSegment} containing the style information (bold and/or italic) to match
     * @return the {@link MinecraftFont} matching the given style, or {@link MinecraftFont#PLAIN} if no match is found
     */
    public static @NotNull MinecraftFont of(@NotNull ColorSegment segment) {
        for (MinecraftFont font : values()) {
            if (font.getStyle().getId() == (segment.isBold() ? 1 : 0) + (segment.isItalic() ? 2 : 0))
                return font;
        }

        return PLAIN;
    }

}
