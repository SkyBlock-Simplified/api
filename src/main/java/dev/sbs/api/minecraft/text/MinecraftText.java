package dev.sbs.api.minecraft.text;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.minecraft.text.segment.ColorSegment;
import dev.sbs.api.minecraft.text.segment.LineSegment;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.builder.string.StringBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.data.Range;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.ResourceUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

public class MinecraftText {

    private static final int PIXEL_SIZE = 2;
    private static final int START_XY = PIXEL_SIZE * 5;
    private static final int Y_INCREMENT = PIXEL_SIZE * 10;
    @Getter private static final @NotNull ConcurrentList<Font> minecraftFonts;
    private static final Font sansSerif;

    // Current Settings
    @Getter(AccessLevel.PRIVATE)
    private final Graphics2D graphics;
    @Getter private final int alpha;
    @Getter private final ConcurrentList<LineSegment> lines;
    @Getter private BufferedImage image;
    @Getter private ChatFormat currentColor;
    private Font currentFont;

    // Positioning & Size
    private int locationX = START_XY;
    private int locationY = START_XY + PIXEL_SIZE * 2 + Y_INCREMENT / 2;
    private int largestWidth = 0;

    static {
        sansSerif = new Font("SansSerif", Font.PLAIN, 20);
        minecraftFonts = Concurrent.newUnmodifiableList(
            initFont("/minecraft/1_Minecraft.otf", 15.5f),
            initFont("/minecraft/3_Minecraft-Bold.otf", 20.0f),
            initFont("/minecraft/2_Minecraft-Italic.otf", 20.5f),
            initFont("/minecraft/4_Minecraft-BoldItalic.otf", 20.5f)
        );

        // Register Minecraft Fonts
        getMinecraftFonts().forEach(GraphicsEnvironment.getLocalGraphicsEnvironment()::registerFont);
    }

    private MinecraftText(int defaultWidth, ConcurrentList<LineSegment> lines, ChatFormat defaultColor, int alpha) {
        this.alpha = alpha;
        this.lines = lines.toUnmodifiableList();
        this.graphics = this.initG2D(defaultWidth, this.lines.size() * Y_INCREMENT + START_XY + PIXEL_SIZE * 4);
        this.currentColor = defaultColor;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates an image, then initialized a Graphics2D object from that image.
     *
     * @return G2D object
     */
    private Graphics2D initG2D(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Draw Primary Background
        Graphics2D graphics = this.getImage().createGraphics();
        graphics.setColor(new Color(18, 3, 18, this.getAlpha()));
        graphics.fillRect(PIXEL_SIZE * 2, PIXEL_SIZE * 2, width - PIXEL_SIZE * 4, height - PIXEL_SIZE * 4);

        return graphics;
    }

    /**
     * Crops the image down to closely fit the size taken up.
     */
    public void cropImage() {
        this.image = this.getImage().getSubimage(0, 0, this.largestWidth + START_XY, this.getImage().getHeight());
    }

    /**
     * Creates the inner and outer purple borders around the image.
     */
    public void drawBorders() {
        final int width = this.getImage().getWidth();
        final int height = this.getImage().getHeight();

        // Draw Darker Purple Border Around Purple Border
        this.getGraphics().setColor(new Color(18, 3, 18, this.getAlpha()));
        this.getGraphics().fillRect(0, PIXEL_SIZE, PIXEL_SIZE, height - PIXEL_SIZE * 2);
        this.getGraphics().fillRect(PIXEL_SIZE, 0, width - PIXEL_SIZE * 2, PIXEL_SIZE);
        this.getGraphics().fillRect(width - PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE, height - PIXEL_SIZE * 2);
        this.getGraphics().fillRect(PIXEL_SIZE, height - PIXEL_SIZE, width - PIXEL_SIZE * 2, PIXEL_SIZE);

        // Draw Purple Border
        this.getGraphics().setColor(new Color(37, 0, 94, this.getAlpha()));
        this.getGraphics().drawRect(PIXEL_SIZE, PIXEL_SIZE, width - PIXEL_SIZE * 2 - 1, height - PIXEL_SIZE * 2 - 1);
        this.getGraphics().drawRect(PIXEL_SIZE + 1, PIXEL_SIZE + 1, width - PIXEL_SIZE * 3 - 1, height - PIXEL_SIZE * 3 - 1);
    }

    /**
     * Draws the lines on the image.
     */
    public void drawLines() {
        for (LineSegment line : this.getLines()) {
            for (ColorSegment itemObject : line.getSegments()) {
                // Change Fonts and Color
                this.currentFont = getMinecraftFonts().get((itemObject.isBold() ? 1 : 0) + (itemObject.isItalic() ? 2 : 0));
                this.getGraphics().setFont(this.currentFont);
                this.currentColor = itemObject.getColor().orElse(ChatFormat.GRAY);

                StringBuilder subWord = new StringBuilder();
                String displayingLine = itemObject.getText();

                // iterating through all the indexes of the current line until there is a character which cannot be displayed
                for (int charIndex = 0; charIndex < displayingLine.length(); charIndex++) {
                    char character = displayingLine.charAt(charIndex);

                    if (!this.currentFont.canDisplay(character)) {
                        this.drawString(subWord.toString());
                        subWord.setLength(0);
                        this.drawSymbol(character);
                        continue;
                    }

                    // Prevent Monospace
                    subWord.append(character);
                }

                this.drawString(subWord.toString());
            }

            this.updatePositionAndSize(this.getLines().indexOf(line) == 0);
        }
    }

    /**
     * Draws a symbol on the image, and updates the pointer location.
     *
     * @param symbol The symbol to draw.
     */
    private void drawSymbol(char symbol) {
        this.drawString(Character.toString(symbol), sansSerif);
    }

    /**
     * Draws a string at the current location, and updates the pointer location.
     *
     * @param value The value to draw.
     */
    private void drawString(@NotNull String value) {
        this.drawString(value, this.currentFont);
    }

    private void drawString(@NotNull String value, @NotNull Font font) {
        // Change Font
        this.getGraphics().setFont(font);

        // Draw Drop Shadow Text
        this.getGraphics().setColor(this.currentColor.getBackgroundColor());
        this.getGraphics().drawString(value, this.locationX + 2, this.locationY + 2);

        // Draw Text
        this.getGraphics().setColor(this.currentColor.getColor());
        this.getGraphics().drawString(value, this.locationX, this.locationY);

        // Update Draw Pointer Location
        this.locationX += font.getStringBounds(value, this.getGraphics().getFontRenderContext()).getWidth();

        // Reset Font
        this.getGraphics().setFont(this.currentFont);
    }

    /**
     * Initializes a font.
     *
     * @param path The path to the font in the resources' folder.
     *
     * @return The initialized font.
     */
    @Nullable
    private static Font initFont(String path, float size) {
        try {
            try (InputStream inputStream = ResourceUtil.getResource(path)) {
                return Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(size);
            }
        } catch (IOException | FontFormatException ex) {
            SimplifiedApi.getLog(MinecraftText.class).error(FormatUtil.format("Unable to load font from file ''{0}''!", path), ex);
        }

        return null;
    }

    /**
     * Draws the Lines, Crops the Image and Draws the Borders.
     */
    public MinecraftText render() {
        this.drawLines();
        this.cropImage();
        this.drawBorders();
        return this;
    }

    public InputStream toStream() {
        try {
            return new FileInputStream(this.toFile());
        } catch (FileNotFoundException fnfex) {
            throw SimplifiedException.wrapNative(fnfex).build();
        }
    }

    public File toFile() {
        try {
            File tempFile = new File(UUID.randomUUID().toString(), ".png");
            ImageIO.write(this.getImage(), "png", tempFile);
            return tempFile;
        } catch (IOException ioex) {
            throw SimplifiedException.wrapNative(ioex).build();
        }
    }

    /**
     * Moves the pointer to draw on the next line.
     *
     * @param increaseGap Increase number of pixels between the next line
     */
    private void updatePositionAndSize(boolean increaseGap) {
        this.locationY += Y_INCREMENT + (increaseGap ? PIXEL_SIZE * 2 : 0);
        this.largestWidth = Math.max(this.locationX, this.largestWidth);
        this.locationX = START_XY;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder implements dev.sbs.api.util.builder.Builder<MinecraftText> {

        private ConcurrentList<LineSegment> lines = Concurrent.newList();
        private int defaultWidth;
        private ChatFormat defaultColor = ChatFormat.GRAY;
        private int alpha = 255;

        public Builder withAlpha(int value) {
            this.alpha = Range.between(0, 255).fit(value);
            return this;
        }

        public Builder withDefaultColor(@NotNull ChatFormat chatFormatting) {
            this.defaultColor = chatFormatting;
            return this;
        }

        public Builder withDefaultWidth(int value) {
            this.defaultWidth = value;
            return this;
        }

        public Builder withLines(@NotNull LineSegment... lines) {
            return this.withLines(Arrays.asList(lines));
        }

        public Builder withLines(@NotNull Iterable<LineSegment> lines) {
            lines.forEach(this.lines::add);
            return this;
        }

        public Builder withSegments(@NotNull ColorSegment... segments) {
            return this.withSegments(Arrays.asList(segments));
        }

        public Builder withSegments(@NotNull Iterable<ColorSegment> segments) {
            this.lines.add(LineSegment.builder().withSegments(segments).build());
            return this;
        }

        @Override
        public MinecraftText build() {
            return new MinecraftText(
                this.defaultWidth,
                this.lines,
                this.defaultColor,
                this.alpha
            );
        }

    }

}