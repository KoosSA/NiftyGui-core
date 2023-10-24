package de.lessvoid.nifty.render.batch.spi;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.render.batch.spi.BatchRenderBackend.Image;

/**
 * A factory for creating Image objects.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface ImageFactory {
  
  /**
	 * You must return a non-null instance of your
	 * {@link de.lessvoid.nifty.render.batch.spi.BatchRenderBackend.Image}
	 * implementation, even if buffer is null.
	 *
	 * @param buffer      the buffer
	 * @param imageWidth  the image width
	 * @param imageHeight the image height
	 * @return the image
	 */
  @Nonnull
  public Image create(@Nullable final ByteBuffer buffer, final int imageWidth, final int imageHeight);

  /**
	 * Get the image data in {@link java.nio.ByteBuffer} format.
	 *
	 * @param image the image
	 * @return the byte buffer
	 */
  @Nullable
  public ByteBuffer asByteBuffer(@Nullable final Image image);
}
