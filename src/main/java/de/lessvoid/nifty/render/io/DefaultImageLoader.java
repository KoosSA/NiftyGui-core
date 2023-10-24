package de.lessvoid.nifty.render.io;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;
import javax.imageio.ImageIO;

/**
 * An image data provider that uses ImageIO to retrieve image data in a format suitable for creating OpenGL textures.
 * This implementation is used when formats not natively supported by the library are required.
 *
 * {@inheritDoc}
 *
 * @author Kevin Glass
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class DefaultImageLoader implements ImageLoader {
  
  /** The Constant GL_ALPHA_COLOR_MODEL. */
  @Nonnull
  private static final ColorModel GL_ALPHA_COLOR_MODEL = new ComponentColorModel(ColorSpace.getInstance(ColorSpace
          .CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
  
  /** The image properties. */
  @Nullable
  private ImageProperties imageProperties;

  /**
	 * Gets the image bit depth.
	 *
	 * @return the image bit depth
	 */
  @Override
  public int getImageBitDepth() {
    if (imageProperties == null) {
      throw new IllegalStateException("Image bit depth is not set!");
    }
    return imageProperties.getBitDepth();
  }

  /**
	 * Gets the image width.
	 *
	 * @return the image width
	 */
  @Override
  public int getImageWidth() {
    if (imageProperties == null) {
      throw new IllegalStateException("Image width is not set!");
    }
    return imageProperties.getWidth();
  }

  /**
	 * Gets the image height.
	 *
	 * @return the image height
	 */
  @Override
  public int getImageHeight() {
    if (imageProperties == null) {
      throw new IllegalStateException("Image height is not set!");
    }
    return imageProperties.getHeight();
  }

  /**
	 * Gets the texture width.
	 *
	 * @return the texture width
	 */
  @Override
  public int getTextureWidth() {
    if (imageProperties == null) {
      throw new IllegalStateException("Texture width is not set!");
    }
    return imageProperties.getWidth();
  }

  /**
	 * Gets the texture height.
	 *
	 * @return the texture height
	 */
  @Override
  public int getTextureHeight() {
    if (imageProperties == null) {
      throw new IllegalStateException("Texture height is not set!");
    }
    return imageProperties.getHeight();
  }

  /**
	 * Load as byte buffer RGBA.
	 *
	 * @param imageStream the image stream
	 * @return the byte buffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Override
  @Nonnull
  public ByteBuffer loadAsByteBufferRGBA(@Nonnull @WillNotClose final InputStream imageStream) throws IOException {
    return convertToOpenGlFormat(loadImageFromStream(imageStream), false, false);
  }

  /**
	 * Load as byte buffer ARGB.
	 *
	 * @param imageStream          the image stream
	 * @param shouldFlipVertically the should flip vertically
	 * @return the byte buffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Nonnull
  @Override
  public ByteBuffer loadAsByteBufferARGB(
          @Nonnull @WillNotClose final InputStream imageStream,
          final boolean shouldFlipVertically) throws IOException {
    try {
      return convertToOpenGlFormat(loadImageFromStream(imageStream), shouldFlipVertically, true);
    } catch (IOException e) {
      throw new IOException("Could not load mouse cursor image!", e);
    }
  }

  /**
	 * Load as buffered image.
	 *
	 * @param imageStream the image stream
	 * @return the buffered image
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Nonnull
  @Override
  public BufferedImage loadAsBufferedImage(@Nonnull @WillNotClose InputStream imageStream) throws IOException {
    try {
      return loadImageFromStream(imageStream);
    } catch (IOException e) {
      throw new IOException("Could not load mouse cursor image!", e);
    }
  }

  // Internal implementations

  /**
	 * Load image from stream.
	 *
	 * @param imageStream the image stream
	 * @return the buffered image
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  private BufferedImage loadImageFromStream(@Nonnull @WillNotClose final InputStream imageStream) throws IOException {
    BufferedImage image;
    try {
      image = ImageIO.read(imageStream);
    } catch (IOException e) {
      throw new IOException("Could not load image from stream as a buffered image!", e);
    }
    if (image == null) {
      throw new IOException("Could not load image from stream as a buffered image!");
    }
    return image;
  }

  /**
	 * Convert to open gl format.
	 *
	 * @param originalImage        the original image
	 * @param shouldFlipVertically the should flip vertically
	 * @param shouldUseARGB        the should use ARGB
	 * @return the byte buffer
	 */
  @Nonnull
  private ByteBuffer convertToOpenGlFormat(
          @Nonnull final BufferedImage originalImage,
          final boolean shouldFlipVertically,
          final boolean shouldUseARGB) {
    ImageProperties originalImageProperties = new ImageProperties(
            originalImage.getWidth(),
            originalImage.getHeight(),
            shouldFlipVertically);

    imageProperties = originalImageProperties;
    BufferedImage openGlImage = createImageWithProperties(originalImageProperties);
    Graphics2D openGlImageGraphics = (Graphics2D) openGlImage.getGraphics();
    blankImageForMacOsXCompatibility(openGlImageGraphics, originalImageProperties);
    copyImage(originalImage, openGlImageGraphics, originalImageProperties);
    byte[] rawOpenGlImageData = getRawImageData(openGlImage);

    if (shouldUseARGB) {
      convertImageToARGB(rawOpenGlImageData);
    }

    ByteBuffer openGlImageByteBuffer = createByteBuffer(rawOpenGlImageData);

    // We can't dispose of openGlImage any earlier because it would destroy rawOpenGlImageData.
    disposeImage(openGlImageGraphics);

    return openGlImageByteBuffer;
  }

  /**
	 * Blank image for mac os X compatibility.
	 *
	 * @param imageGraphics   the image graphics
	 * @param imageProperties the image properties
	 */
  private void blankImageForMacOsXCompatibility(
          @Nonnull final Graphics2D imageGraphics,
          @Nonnull final ImageProperties imageProperties) {
    imageGraphics.setColor(new Color(0f, 0f, 0f, 0f));
    imageGraphics.fillRect(0, 0, imageProperties.getWidth(), imageProperties.getHeight());
  }

  /**
	 * Copy image.
	 *
	 * @param sourceImage           the source image
	 * @param destinationGraphics   the destination graphics
	 * @param sourceImageProperties the source image properties
	 */
  private void copyImage(
          @Nonnull final BufferedImage sourceImage,
          @Nonnull final Graphics2D destinationGraphics,
          @Nonnull final ImageProperties sourceImageProperties) {
    if (sourceImageProperties.isFlipped()) {
      destinationGraphics.scale(1, -1);
      destinationGraphics.drawImage(sourceImage, 0, -sourceImageProperties.getHeight(), null);
    } else {
      destinationGraphics.drawImage(sourceImage, 0, 0, null);
    }
  }

  /**
	 * Creates the image with properties.
	 *
	 * @param imageProperties the image properties
	 * @return the buffered image
	 */
  @Nonnull
  private BufferedImage createImageWithProperties(@Nonnull final ImageProperties imageProperties) {
    return new BufferedImage(
            imageProperties.getColorModel(),
            createRasterWithProperties(imageProperties),
            false,
            null);
  }

  /**
	 * Creates the raster with properties.
	 *
	 * @param imageProperties the image properties
	 * @return the writable raster
	 */
  @Nonnull
  private WritableRaster createRasterWithProperties(@Nonnull final ImageProperties imageProperties) {
    return Raster.createInterleavedRaster(
            DataBuffer.TYPE_BYTE,
            imageProperties.getWidth(),
            imageProperties.getHeight(),
            imageProperties.getColorBands(),
            null);
  }

  /**
	 * Creates the byte buffer.
	 *
	 * @param data the data
	 * @return the byte buffer
	 */
  @Nonnull
  private ByteBuffer createByteBuffer(@Nonnull final byte[] data) {
    return (ByteBuffer) ByteBuffer.allocateDirect(data.length)
            .order(ByteOrder.nativeOrder())
            .put(data, 0, data.length)
            .flip();
  }

  /**
	 * Convert image to ARGB.
	 *
	 * @param imageData the image data
	 */
  private void convertImageToARGB(@Nonnull final byte[] imageData) {
    for (int i = 0; i < imageData.length; i += 4) {
      byte rr = imageData[i];
      byte gg = imageData[i + 1];
      byte bb = imageData[i + 2];
      byte aa = imageData[i + 3];
      imageData[i] = bb;
      imageData[i + 1] = gg;
      imageData[i + 2] = rr;
      imageData[i + 3] = aa;
    }
  }

  /**
	 * Gets the raw image data.
	 *
	 * @param image the image
	 * @return the raw image data
	 */
  @Nonnull
  private byte[] getRawImageData(@Nonnull final BufferedImage image) {
    return ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
  }

  /**
	 * Dispose image.
	 *
	 * @param graphics2D the graphics 2 D
	 */
  private void disposeImage(@Nonnull final Graphics2D graphics2D) {
    graphics2D.dispose();
  }

  /**
	 * The Class ImageProperties.
	 */
  private static class ImageProperties {
    
    /** The width. */
    private final int width;
    
    /** The height. */
    private final int height;
    
    /** The is flipped vertically. */
    private final boolean isFlippedVertically;

    /**
	 * Instantiates a new image properties.
	 *
	 * @param width               the width
	 * @param height              the height
	 * @param isFlippedVertically the is flipped vertically
	 */
    private ImageProperties(
            final int width,
            final int height,
            final boolean isFlippedVertically) {
      this.width = width;
      this.height = height;
      this.isFlippedVertically = isFlippedVertically;
    }

    /**
	 * Gets the width.
	 *
	 * @return the width
	 */
    private int getWidth() {
      return width;
    }

    /**
	 * Gets the height.
	 *
	 * @return the height
	 */
    private int getHeight() {
      return height;
    }

    /**
	 * Checks if is flipped.
	 *
	 * @return true, if is flipped
	 */
    private boolean isFlipped() {
      return isFlippedVertically;
    }

    /**
	 * Gets the color model.
	 *
	 * @return the color model
	 */
    private ColorModel getColorModel() {
      return GL_ALPHA_COLOR_MODEL;
    }

    /**
	 * Gets the bit depth.
	 *
	 * @return the bit depth
	 */
    private int getBitDepth() {
      return 32;
    }

    /**
	 * Gets the color bands.
	 *
	 * @return the color bands
	 */
    private int getColorBands() {
      return 4;
    }
  }
}
