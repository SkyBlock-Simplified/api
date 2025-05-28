package dev.sbs.api.util.io.gif;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
//@SuppressWarnings("all")
public enum ImageType {
    
    /**
     * Represents a customized image and the fallback return value for the {@link BufferedImage#getType()}
     * method.
     */
    CUSTOM(BufferedImage.TYPE_CUSTOM),

    /**
     * Represents an image with 8-bit RGB color components packed into
     * integer pixels.
     * <p>
     * The image has a {@link DirectColorModel} without
     * alpha.
     * <p>
     * When data with non-opaque alpha is stored in an image of this type,
     * the color data must be adjusted to a non-premultiplied form
     * and the alpha discarded, as described in the
     * {@link AlphaComposite} documentation.
     */
    INT_RGB(BufferedImage.TYPE_INT_RGB),

    /**
     * Represents an image with 8-bit RGBA color components packed into
     * integer pixels.
     * <p>
     * The image has a {@link DirectColorModel}
     * with alpha.
     * <p>
     * The color data in this image is considered not to be
     * premultiplied with alpha. When this type is used as the
     * {@code BufferedImage#imageType} argument to a {@link BufferedImage}
     * constructor, the created image is consistent with images
     * created in the JDK1.1 and earlier releases.
     */
    INT_ARGB(BufferedImage.TYPE_INT_ARGB),

    /**
     * Represents an image with 8-bit RGBA color components packed into
     * integer pixels.
     * <p>
     * The image has a {@link DirectColorModel} with alpha.
     * <p>
     * The color data in this image is considered to be
     * premultiplied with alpha.
     */
    INT_ARGB_PRE(BufferedImage.TYPE_INT_ARGB_PRE),

    /**
     * Represents an image with 8-bit RGB color components, corresponding
     * to a Windows- or Solaris-style BGR color model, with the colors
     * Blue, Green, and Red packed into integer pixels.
     * <p>
     * The image has a {@link DirectColorModel} with no alpha.
     * <p>
     * When data with non-opaque alpha is stored in an image of this type,
     * the color data must be adjusted to a non-premultiplied form
     * and the alpha discarded, as described in the
     * {@link AlphaComposite} documentation.
     */
    INT_BGR(BufferedImage.TYPE_INT_BGR),

    /**
     * Represents an image with 8-bit RGB color components, corresponding
     * to a Windows-style BGR color model) with the colors Blue, Green,
     * and Red stored in 3 bytes.
     * <p>
     * The image has a {@link ComponentColorModel} with no alpha.
     * <p>
     * When data with non-opaque alpha is stored in an image of this type,
     * the color data must be adjusted to a non-premultiplied form
     * and the alpha discarded, as described in the
     * {@link AlphaComposite} documentation.
     */
    THREE_BYTE_BGR(BufferedImage.TYPE_3BYTE_BGR),

    /**
     * Represents an image with 8-bit RGBA color components with the colors
     * Blue, Green, and Red stored in 3 bytes and 1 byte of alpha.
     * <p>
     * The image has a {@link ComponentColorModel} with alpha.
     * <p>
     * The color data in this image is considered not to be premultiplied with
     * alpha. The byte data is interleaved in a single byte array in the order
     * A, B, G, R from lower to higher byte addresses within each pixel.
     */
    FOUR_BYTE_ABGR(BufferedImage.TYPE_4BYTE_ABGR),

    /**
     * Represents an image with 8-bit RGBA color components with the colors
     * Blue, Green, and Red stored in 3 bytes and 1 byte of alpha.
     * <p>
     * The image has a {@link ComponentColorModel} with alpha.
     * <p>
     * The color data in this image is considered to be premultiplied with
     * alpha. The byte data is interleaved in a single byte array in the order
     * A, B, G, R from lower to higher byte addresses within each pixel.
     */
    FOUR_BYTE_ABGR_PRE(BufferedImage.TYPE_4BYTE_ABGR_PRE),

    /**
     * Represents an image with 5-6-5 RGB color components (5-bits red,
     * 6-bits green, 5-bits blue).
     * <p>
     * This image has a {@link DirectColorModel} with no alpha.
     * <p>
     * When data with non-opaque alpha is stored in an image of this type,
     * the color data must be adjusted to a non-premultiplied form and the
     * alpha discarded, as described in the {@link AlphaComposite} documentation.
     */
    USHORT_565_RGB(BufferedImage.TYPE_USHORT_565_RGB),

    /**
     * Represents an image with 5-5-5 RGB color components (5-bits red,
     * 5-bits green, 5-bits blue).
     * <p>
     * This image has a {@link DirectColorModel} with no alpha.
     * <p>
     * When data with non-opaque alpha is stored in an image of this type,
     * the color data must be adjusted to a non-premultiplied form and the
     * alpha discarded, as described in the {@link AlphaComposite} documentation.
     */
    USHORT_555_RGB(BufferedImage.TYPE_USHORT_555_RGB),

    /**
     * Represents a unsigned byte grayscale image, non-indexed.
     * <p>
     * This image has a {@link ComponentColorModel} with a CS_GRAY {@link ColorSpace}.
     * <p>
     * When data with non-opaque alpha is stored in an image of this type,
     * the color data must be adjusted to a non-premultiplied form and the
     * alpha discarded, as described in the {@link AlphaComposite} documentation.
     */
    BYTE_GRAY(BufferedImage.TYPE_BYTE_GRAY),

    /**
     * Represents an unsigned short grayscale image, non-indexed).
     * <p>
     * This image has a {@link ComponentColorModel} with a CS_GRAY {@link ColorSpace}.
     * <p>
     * When data with non-opaque alpha is stored in an image of this type,
     * the color data must be adjusted to a non-premultiplied form and the
     * alpha discarded, as described in the {@link AlphaComposite} documentation.
     */
    USHORT_GRAY(BufferedImage.TYPE_USHORT_GRAY),

    /**
     * Represents an opaque byte-packed 1, 2, or 4 bit image.
     * <p>
     * The image has an {@link IndexColorModel} without alpha.
     * <p>
     * When this type is used as the {@code BufferedImage#imageType} argument to the
     * {@link BufferedImage} constructor that takes an {@code BufferedImage#imageType}
     * argument but no {@link ColorModel} argument, a 1-bit image is created with an
     * {@link IndexColorModel} with two colors in the default sRGB {@link ColorSpace}:
     * {0,&nbsp;0,&nbsp;0} and {255,&nbsp;255,&nbsp;255}.
     * <p>
     * Images with 2 or 4 bits per pixel may be constructed via the
     * {@link BufferedImage} constructor that takes a {@link ColorModel}
     * argument by supplying a {@link ColorModel} with an appropriate map size.
     * <p>
     * Images with 8 bits per pixel should use the image types
     * {@link #BYTE_INDEXED} or {@link #BYTE_GRAY}
     * depending on their {@link ColorModel}.
     * <p>
     * When color data is stored in an image of this type, the closest color in
     * the colormap is determined by the {@link IndexColorModel} and the resulting
     * index is stored. Approximation and loss of alpha or color components
     * can result, depending on the colors in the {@link IndexColorModel} colormap.
     */
    BYTE_BINARY(BufferedImage.TYPE_BYTE_BINARY),

    /**
     * Represents an indexed byte image.
     * <p>
     * When this type is used as the {@code BufferedImage#imageType} argument to the
     * {@link BufferedImage} constructor that takes an {@code BufferedImage#imageType}
     * argument but no {@link ColorModel} argument, an {@link IndexColorModel} is
     * created with a 256-color 6/6/6 color cube palette with the rest of the colors
     * from 216-255 populated by grayscale values in the default sRGB ColorSpace.
     * <p>
     * When color data is stored in an image of this type, the closest color in
     * the colormap is determined by the {@link IndexColorModel} and the resulting
     * index is stored. Approximation and loss of alpha or color components
     * can result, depending on the colors in the {@link IndexColorModel} colormap.
     */
    BYTE_INDEXED(BufferedImage.TYPE_BYTE_INDEXED),;
    
    private final int value;
    
    public static @NotNull ImageType of(int value) {
        return Arrays.stream(values())
            .filter(type -> type.getValue() == value)
            .findFirst()
            .orElse(CUSTOM);
    }
    
}
