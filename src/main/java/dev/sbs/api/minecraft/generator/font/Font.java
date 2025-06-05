package dev.sbs.api.minecraft.generator.font;

import dev.sbs.api.minecraft.generator.exception.FontException;
import dev.sbs.api.util.SystemUtil;
import lombok.Cleanup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public interface Font {

    @NotNull java.awt.Font getActual();

    @NotNull Style getStyle();

    float getSize();

    /**
     * Initializes a font by loading it from the specified resource path and applying the given font size.
     *
     * @param resourcePath the relative path to the font file resource
     * @param size the desired font size to apply to the loaded font
     * @return the {@link java.awt.Font} object initialized with the specified resource and size
     * @throws FontException if the font file cannot be loaded or is in an invalid format
     */
    static @NotNull java.awt.Font initFont(@NotNull String resourcePath, float size) throws FontException {
        try {
            @Cleanup InputStream inputStream = SystemUtil.getResource(resourcePath);
            java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream).deriveFont(size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (IOException | FontFormatException ex) {
            throw new FontException(ex, resourcePath);
        }
    }

    @Getter
    @RequiredArgsConstructor
    enum Style {

        PLAIN(0),
        BOLD(1),
        ITALIC(2),
        BOLD_ITALIC(3);

        public final int id;

        /**
         * Returns the Style corresponding to the specified id.
         * <p>
         * If no matching Style is found, the default Style {@link #PLAIN} is returned.
         *
         * @param id the identifier of the Style to be retrieved
         * @return the Style corresponding to the specified id, or {@link #PLAIN} if no match is found
         */
        public static @NotNull Style of(int id) {
            for (Style style : values()) {
                if (style.getId() == id)
                    return style;
            }

            return PLAIN;
        }

    }

}
