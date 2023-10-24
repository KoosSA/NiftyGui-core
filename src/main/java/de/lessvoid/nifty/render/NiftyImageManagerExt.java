package de.lessvoid.nifty.render;

import java.util.Collection;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.render.NiftyImageManager.ReferencedCountedImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The purpose of this interface is to extract everything that is different in
 * the way the NiftyImageManager needs to do when using a BatchRenderDevice
 * implementation. There will be a *ExtStandard implementation that does nothing
 * and an *ExtBatch implementation that will do additional management based on
 * Nifty Screen.
 *
 * @author void
 * @param <T> the generic type
 */
public interface NiftyImageManagerExt<T extends ReferencedCountedImage> {

  /**
	 * Register image.
	 *
	 * @param screen the screen
	 * @param image  the image
	 */
  void registerImage(@Nonnull Screen screen, @Nonnull T image);

  /**
	 * Unregister image.
	 *
	 * @param reference the reference
	 */
  void unregisterImage(@Nonnull T reference);

  /**
	 * Upload screen images.
	 *
	 * @param screen the screen
	 */
  void uploadScreenImages(@Nonnull Screen screen);

  /**
	 * Unload screen images.
	 *
	 * @param screen       the screen
	 * @param renderDevice the render device
	 * @param imageCache   the image cache
	 */
  void unloadScreenImages(
      @Nonnull Screen screen,
      @Nonnull RenderDevice renderDevice,
      @Nonnull Collection<T> imageCache);

  /**
	 * Screen added.
	 *
	 * @param screen the screen
	 */
  void screenAdded(@Nonnull Screen screen);

  /**
	 * Screen removed.
	 *
	 * @param screen the screen
	 */
  void screenRemoved(@Nonnull Screen screen);

  /**
	 * Adds the screen info.
	 *
	 * @param result the result
	 */
  void addScreenInfo(@Nonnull StringBuffer result);

  /**
	 * Creates the referenced counted image.
	 *
	 * @param renderDevice the render device
	 * @param screen       the screen
	 * @param filename     the filename
	 * @param filterLinear the filter linear
	 * @param renderImage  the render image
	 * @param key          the key
	 * @return the t
	 */
  @Nonnull
  T createReferencedCountedImage(
      @Nonnull RenderDevice renderDevice,
      @Nonnull Screen screen,
      @Nonnull String filename,
      boolean filterLinear,
      @Nonnull RenderImage renderImage,
      @Nonnull String key);

}
