package de.lessvoid.nifty.render.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;

/**
 * The Interface ImageLoader.
 */
public interface ImageLoader {
  
  /**
	 * Gets the image bit depth.
	 *
	 * @return the image bit depth
	 */
  public int getImageBitDepth();
  
  /**
	 * Gets the image height.
	 *
	 * @return the image height
	 */
  public int getImageHeight();
  
  /**
	 * Gets the image width.
	 *
	 * @return the image width
	 */
  public int getImageWidth();
  
  /**
	 * Gets the texture height.
	 *
	 * @return the texture height
	 */
  public int getTextureHeight();
  
  /**
	 * Gets the texture width.
	 *
	 * @return the texture width
	 */
  public int getTextureWidth();

  /**
	 * Load as byte buffer RGBA.
	 *
	 * @param imageStream the image stream
	 * @return the byte buffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Nonnull
  public ByteBuffer loadAsByteBufferRGBA(@Nonnull @WillNotClose final InputStream imageStream) throws IOException;

  /**
	 * Load as byte buffer ARGB.
	 *
	 * @param imageStream          the image stream
	 * @param shouldFlipVertically the should flip vertically
	 * @return the byte buffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Nonnull
  public ByteBuffer loadAsByteBufferARGB(
          @Nonnull @WillNotClose final InputStream imageStream,
          final boolean shouldFlipVertically) throws IOException;

  /**
	 * Load as buffered image.
	 *
	 * @param imageStream the image stream
	 * @return the buffered image
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Nonnull
  public BufferedImage loadAsBufferedImage(@Nonnull @WillNotClose final InputStream imageStream) throws IOException;
}