package dev.sbs.api.io.gif;

import dev.sbs.api.builder.ClassBuilder;
import dev.sbs.api.builder.annotation.BuildFlag;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.io.ByteArrayDataInput;
import dev.sbs.api.io.ByteArrayDataOutput;
import dev.sbs.api.util.NumberUtil;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GifSequenceWriter {

    private final @NotNull ConcurrentList<BufferedImage> frames = Concurrent.newList();
    private final @NotNull ImageType imageType;
    private final @NotNull DisposalMethod disposalMethod;
    private final int frameDelay;
    private final boolean userInputRequired;
    private final boolean looping;
    private final boolean transparent;
    private final int transparentColorIndex;

    @Getter(AccessLevel.NONE)
    private ImageWriter writer;
    @Getter(AccessLevel.NONE)
    private ImageWriteParam parameters;
    @Getter(AccessLevel.NONE)
    private ImageTypeSpecifier specifier;
    @Getter(AccessLevel.NONE)
    private IIOMetadata imageMetaData;

    @SneakyThrows
    private @NotNull GifSequenceWriter init() {
        this.writer = ImageIO.getImageWritersByFormatName("gif").next();
        this.parameters = this.writer.getDefaultWriteParam();
        this.specifier = ImageTypeSpecifier.createFromBufferedImageType(this.getImageType().getValue());
        this.imageMetaData = this.writer.getDefaultImageMetadata(this.specifier, this.parameters);

        // Root
        String metaFormatName = this.imageMetaData.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) this.imageMetaData.getAsTree(metaFormatName);

        // Graphic Control Settings
        IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
        graphicsControlExtensionNode.setAttribute("disposalMethod", this.getDisposalMethod().getValue());
        graphicsControlExtensionNode.setAttribute("userInputFlag", String.valueOf(this.isUserInputRequired()));
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", String.valueOf(this.isTransparent()));
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(this.getFrameDelay() / 10));
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", Integer.toString(this.getTransparentColorIndex()));

        // Comments
        IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
        commentsNode.setAttribute("CommentExtension", "Created by CraftedFury");

        // Application Data
        IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");

        // Looping
        child.setUserObject(new byte[] {
            0x1, // Sub-block index (always 1)
            (byte) (this.isLooping() ? 0x0 : 0x1), // Low byte of the loop count (0 = infinite)
            (byte) 0x0 // High byte of the loop count (must be 0)
        });
        appExtensionsNode.appendChild(child);

        this.imageMetaData.setFromTree(metaFormatName, root);
        return this;
    }

    private static @NotNull IIOMetadataNode getNode(@NotNull IIOMetadataNode rootNode, @NotNull String nodeName) {
        for (int i = 0; i < rootNode.getLength(); i++) {
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0)
                return (IIOMetadataNode) rootNode.item(i);
        }

        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return node;
    }

    /**
     * Adds a frame to the GIF sequence.
     *
     * @param image the {@link BufferedImage} representing the frame to be added to the sequence.
     */
    public void addFrame(@NotNull BufferedImage image) {
        this.frames.add(image);
    }

    /**
     * Adds a frame to the GIF sequence at a specified index.
     *
     * @param index the position in the sequence where the frame should be inserted
     * @param image the {@link BufferedImage} representing the frame to be added to the sequence
     * @throws IndexOutOfBoundsException if the index is out of the valid range for the sequence
     */
    public void addFrame(int index, @NotNull BufferedImage image) {
        this.frames.add(index, image);
    }

    /**
     * Converts the frames added to the writer into a byte array representing a GIF sequence.
     * <p>
     * The method processes all the stored frames and outputs the resulting byte array, in GIF format.
     *
     * @return the compiled byte array representing the GIF sequence.
     * @throws IllegalStateException if no frames have been added to the writer.
     */
    @SneakyThrows
    public byte[] toByteArray() {
        if (this.getFrames().isEmpty())
            throw new IllegalStateException("No frames have been added to the writer.");

        // Set Output
        @Cleanup ByteArrayDataOutput dataOutput = new ByteArrayDataOutput();
        @Cleanup ImageOutputStream outputStream = ImageIO.createImageOutputStream(dataOutput);
        this.writer.setOutput(outputStream);
        this.writer.prepareWriteSequence(null);

        // Write Frames
        this.getFrames().forEach(this::writeToSequence);
        this.close();

        return dataOutput.toByteArray();
    }

    /**
     * Converts the frames added to the writer into a single compiled {@link BufferedImage}.
     * <p>
     * The method processes all the stored frames and outputs the resulting image, in GIF format.
     *
     * @return the compiled {@link BufferedImage} representing the GIF sequence.
     * @throws IllegalStateException if no frames have been added to the writer.
     */
    @SneakyThrows
    public @NotNull BufferedImage toImage() {
        @Cleanup ByteArrayDataInput dataInput = new ByteArrayDataInput(this.toByteArray());
        return ImageIO.read(dataInput);
    }

    /**
     * Closes the GifSequenceWriter by finalizing any ongoing write sequence, resetting the writer,
     * and clearing the internal frame data.
     * <p>
     * If the writer is in the middle of a write sequence, it will appropriately end the sequence before resetting.
     * The method ensures proper cleanup of resources and state, making the instance ready to be discarded or reinitialized.
     */
    @SneakyThrows
    public void close() {
        if (this.writer.canWriteSequence())
            this.writer.endWriteSequence();

        this.writer.reset();
        this.frames.clear();
    }


    /**
     * Writes the {@link BufferedImage} to the ongoing GIF sequence.
     *
     * @param image the {@link BufferedImage} to be added to the GIF sequence.
     */
    @SneakyThrows
    private void writeToSequence(@NotNull BufferedImage image) {
        this.writer.writeToSequence(new IIOImage(image, null, this.imageMetaData), this.parameters);
    }

    public static class Builder implements ClassBuilder<GifSequenceWriter> {

        @BuildFlag(nonNull = true)
        private ImageType imageType = ImageType.INT_ARGB;
        @BuildFlag(nonNull = true)
        private DisposalMethod disposalMethod = DisposalMethod.NONE;
        private int frameDelay = 50;
        private boolean requireUserInput = false;
        private boolean looping = true;
        private boolean transparent = false;
        private int transparentColorIndex = 0;

        /**
         * Sets the GIF sequence to loop indefinitely by default.
         *
         * @return the current Builder instance for method chaining
         */
        public Builder isLooping() {
            return this.isLooping(true);
        }

        /**
         * Sets whether the GIF sequence should loop indefinitely or not.
         *
         * @param looping true enables looping and false disables it
         * @return the current Builder instance for method chaining
         */
        public Builder isLooping(boolean looping) {
            this.looping = looping;
            return this;
        }

        /**
         * Sets the GIF sequence to be rendered with transparency.
         *
         * @return the current Builder instance for method chaining
         */
        public Builder isTransparent() {
            return this.isTransparent(true);
        }

        /**
         * Sets whether the GIF sequence should be rendered with transparency.
         *
         * @param transparent true enables transparency and false disables it
         * @return the current Builder instance for method chaining
         */
        public Builder isTransparent(boolean transparent) {
            this.transparent = transparent;
            return this;
        }

        /**
         * Sets the GIF sequence to require user input.
         *
         * @return the current Builder instance for method chaining
         */
        public Builder isUserInputRequired() {
            return this.isUserInputRequired(true);
        }

        /**
         * Sets whether the GIF sequence requires user input.
         *
         * @param requireUserInput true indicates user input is required,
         *                         and false indicates it is not required.
         * @return the current Builder instance for method chaining.
         */
        public Builder isUserInputRequired(boolean requireUserInput) {
            this.requireUserInput = requireUserInput;
            return this;
        }

        /**
         * Sets the image type for the GIF sequence.
         *
         * @param imageType the image type to be applied.
         * @return the current Builder instance for method chaining
         */
        public Builder withImageType(@NotNull ImageType imageType) {
            this.imageType = imageType;
            return this;
        }

        /**
         * Sets the disposal method to be used after a frame is displayed in the GIF sequence.
         *
         * @param disposalMethod the disposal method to be applied.
         * @return the current Builder instance for method chaining
         */
        public Builder withDisposalMethod(@NotNull DisposalMethod disposalMethod) {
            this.disposalMethod = disposalMethod;
            return this;
        }

        /**
         * Sets the delay between frames in the GIF sequence.
         * <p>
         * The provided frame delay will be constrained to a minimum of 100 milliseconds
         * and a maximum of {@link Integer#MAX_VALUE} milliseconds.
         *
         * @param frameDelay the delay between frames in milliseconds
         * @return the current Builder instance for method chaining
         */
        public Builder withFrameDelay(int frameDelay) {
            this.frameDelay = NumberUtil.ensureRange(frameDelay, 100, Integer.MAX_VALUE);
            return this;
        }

        /**
         * Sets the transparent color index for the GIF sequence to be rendered as transparent.
         *
         * @param colorIndex the index of the color that should be treated as transparent
         * @return the current Builder instance for method chaining
         */
        public Builder withTransparentColorIndex(int colorIndex) {
            this.transparentColorIndex = colorIndex;
            return this;
        }

        @Override
        public @NotNull GifSequenceWriter build() {
            return new GifSequenceWriter(
                this.imageType,
                this.disposalMethod,
                this.frameDelay,
                this.requireUserInput,
                this.looping,
                this.transparent,
                this.transparentColorIndex
            ).init();
        }

    }

}
