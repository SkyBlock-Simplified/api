package dev.sbs.api.minecraft.image;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * This class will generate an animated GIF from a sequence of individual images.
 */
public class GifSequenceWriter {

    private final ImageOutputStream output = new MemoryCacheImageOutputStream(new ByteArrayOutputStream());
    private final ImageTypeSpecifier imageType;
    private final ImageWriter gifWriter;
    private final ImageWriteParam imageWriteParam;
    private final IIOMetadata imageMetaData;

    /**
     * Creates a new GifSequenceWriter
     *
     * @param imageType the type of images to be written
     * @param timeBetweenFramesMS the time between frames in miliseconds
     * @param loopContinuously wether the gif should loop repeatedly
     * @throws IIOException if no gif ImageWriters are found
     *
     * @author Elliot Kroo (elliot[at]kroo[dot]net)
     */
    public GifSequenceWriter(ImageTypeSpecifier imageType, int timeBetweenFramesMS, boolean loopContinuously) throws IOException {
        // TODO: http://www.java2s.com/Code/Java/2D-Graphics-GUI/AnimatedGifEncoder.htm
        // TODO: https://stackoverflow.com/questions/51163881/issue-with-converting-an-arraylist-of-bufferedimages-to-a-gif-using-gifsequencew
        // Create the Writer
        this.gifWriter = getWriter();
        this.imageType = imageType;
        this.imageWriteParam = this.gifWriter.getDefaultWriteParam();
        this.imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        this.imageMetaData = this.gifWriter.getDefaultImageMetadata(this.imageType, this.imageWriteParam);

        String metaFormatName = this.imageMetaData.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) this.imageMetaData.getAsTree(metaFormatName);

        IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "TRUE");
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(timeBetweenFramesMS / 10));

        IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
        commentsNode.setAttribute("CommentExtension", "Created by MAH");

        IIOMetadataNode appEntensionsNode = getNode(root, "ApplicationExtensions");
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");
        int loop = loopContinuously ? 0 : 1;
        child.setUserObject(new byte[] { 0x1, (byte) (loop & 0xFF), (byte) ((loop >> 8) & 0xFF)});
        appEntensionsNode.appendChild(child);

        this.imageMetaData.setFromTree(metaFormatName, root);
        this.gifWriter.setOutput(this.output);
        this.gifWriter.prepareWriteSequence(null);
    }

    public void writeToSequence(RenderedImage img) throws IOException {
        this.gifWriter.writeToSequence(new IIOImage(img, null, this.imageMetaData), this.imageWriteParam);
    }

    /**
     * Close this GifSequenceWriter object. This does not close the underlying
     * stream, just finishes off the GIF.
     *
     * @throws IOException if there is a problem writing the last bytes.
     */
    public void close() throws IOException {
        this.gifWriter.endWriteSequence();
    }

    /**
     * Returns the first available GIF ImageWriter using
     * ImageIO.getImageWritersBySuffix("gif").
     *
     * @return a GIF ImageWriter object
     * @throws IIOException if no GIF image writers are returned
     */
    private static ImageWriter getWriter() throws IIOException {
        Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");

        if (!iter.hasNext())
            throw new IIOException("No GIF Image Writers Exist");

        return iter.next();
    }

    /**
     * Returns an existing child node, or creates and returns a new child node (if
     * the requested node does not exist).
     *
     * @param rootNode the <tt>IIOMetadataNode</tt> to search for the child node.
     * @param nodeName the name of the child node.
     *
     * @return the child node, if found or a new node created with the given name.
     */
    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();

        for (int i = 0; i < nNodes; i++) {
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0)
                return((IIOMetadataNode) rootNode.item(i));
        }

        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return(node);
    }

    /**
     * Support invocation from the command line; provide a list of input file names, followed by a single output
     * file name.
     *
     * @param args the names of the image files to be combined into a GIF sequence, folloewd by the output file name.
     *
     * @throws Exception if there is a problem reading the inputs or writing the output.
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 1) {
            // grab the output image type from the first image in the sequence
            BufferedImage firstImage = ImageIO.read(new File(args[0]));

            // create a new BufferedOutputStream with the last argument

            // create a gif sequence with the type of the first image, 1 second
            // between frames, which loops continuously

            // ImageTypeSpecifier.createFromRenderedImage
            // ImageTypeSpecifier.createFromBufferedImageType
            GifSequenceWriter writer = new GifSequenceWriter(ImageTypeSpecifier.createFromBufferedImageType(firstImage.getType()), 1, false);
            GifSequenceWriter writer2 = new GifSequenceWriter(ImageTypeSpecifier.createFromBufferedImageType(firstImage.getType()), 1, true);

            // write out the first image to our sequence...
            writer.writeToSequence(firstImage);
            for (int i=1; i<args.length-1; i++) {
                BufferedImage nextImage = ImageIO.read(new File(args[i]));
                writer.writeToSequence(nextImage);
            }

            //writer.close();
            //output.close();
        } else {
            System.out.println("Usage: java GifSequenceWriter [list of gif files] [output file]");
        }
    }
}