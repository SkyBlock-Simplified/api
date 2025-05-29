package dev.sbs.api.minecraft.generator.font;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public enum FontStyle {

    PLAIN(0),
    BOLD(1),
    ITALIC(2),
    BOLD_ITALIC(3);

    public final int id;


    /**
     * Returns the FontStyle corresponding to the specified id.
     * <p>
     * If no matching FontStyle is found, the default FontStyle {@code PLAIN} is returned.
     *
     * @param id the identifier of the FontStyle to be retrieved
     * @return the FontStyle corresponding to the specified id, or {@code PLAIN} if no match is found
     */
    public static @NotNull FontStyle of(int id) {
        for (FontStyle style : values()) {
            if (style.getId() == id)
                return style;
        }

        return PLAIN;
    }

}
