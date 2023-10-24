package de.lessvoid.nifty.render;

import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.render.NiftyImageManager.ReferencedCountedImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The Class NiftyImageManagerExtStandard.
 */
public class NiftyImageManagerExtStandard implements NiftyImageManagerExt<ReferencedCountedImage> {

  /**
	 * Register image.
	 *
	 * @param screen the screen
	 * @param image  the image
	 */
  @Override
  public void registerImage(@Nonnull final Screen screen, @Nonnull final ReferencedCountedImage image) {
  }

  /**
	 * Unregister image.
	 *
	 * @param reference the reference
	 */
  @Override
  public void unregisterImage(@Nonnull final ReferencedCountedImage reference) {
  }

  /**
	 * Upload screen images.
	 *
	 * @param screen the screen
	 */
  @Override
  public void uploadScreenImages(@Nonnull final Screen screen) {
  }

  /**
	 * Unload screen images.
	 *
	 * @param screen       the screen
	 * @param renderDevice the render device
	 * @param imageSet     the image set
	 */
  @Override
  public void unloadScreenImages(
      @Nonnull final Screen screen,
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final Collection<ReferencedCountedImage> imageSet) {
  }

  /**
	 * Screen added.
	 *
	 * @param screen the screen
	 */
  @Override
  public void screenAdded(@Nonnull final Screen screen) {
  }

  /**
	 * Screen removed.
	 *
	 * @param screen the screen
	 */
  @Override
  public void screenRemoved(@Nonnull final Screen screen) {
  }

  /**
	 * Adds the screen info.
	 *
	 * @param result the result
	 */
  @Override
  public void addScreenInfo(@Nonnull final StringBuffer result) {
  }

  /**
	 * Creates the referenced counted image.
	 *
	 * @param renderDevice the render device
	 * @param screen       the screen
	 * @param filename     the filename
	 * @param filterLinear the filter linear
	 * @param renderImage  the render image
	 * @param key          the key
	 * @return the referenced counted image
	 */
  @Nonnull
  @Override
  public ReferencedCountedImage createReferencedCountedImage(
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final Screen screen,
      @Nonnull final String filename,
      final boolean filterLinear,
      @Nonnull final RenderImage renderImage,
      @Nonnull final String key) {
    return new ReferencedCountedImageStandard(renderDevice, screen, filename, filterLinear, renderImage, key);
  }

  /**
   * A standard implementation of a ReferencedCountedImage without Batch support.
   *
   * @author void
   */
  public static class ReferencedCountedImageStandard implements ReferencedCountedImage {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(ReferencedCountedImageStandard.class.getName());
    
    /** The render device. */
    @Nonnull
    private final RenderDevice renderDevice;
    
    /** The screen. */
    @Nonnull
    private final Screen screen;
    
    /** The filename. */
    @Nonnull
    private final String filename;
    
    /** The filter linear. */
    private final boolean filterLinear;
    
    /** The key. */
    @Nonnull
    private final String key;
    
    /** The render image. */
    @Nonnull
    private RenderImage renderImage;
    
    /** The references. */
    private int references;

    /**
	 * Instantiates a new referenced counted image standard.
	 *
	 * @param renderDevice the render device
	 * @param screen       the screen
	 * @param filename     the filename
	 * @param filterLinear the filter linear
	 * @param renderImage  the render image
	 * @param key          the key
	 */
    public ReferencedCountedImageStandard(
        @Nonnull final RenderDevice renderDevice,
        @Nonnull final Screen screen,
        @Nonnull final String filename,
        final boolean filterLinear,
        @Nonnull final RenderImage renderImage,
        @Nonnull final String key) {
      this.renderDevice = renderDevice;
      this.screen = screen;
      this.filename = filename;
      this.filterLinear = filterLinear;
      this.key = key;
      this.renderImage = renderImage;
      this.references = 1;
    }

    /**
	 * Reload.
	 *
	 * @return the render image
	 */
    @Nonnull
    @Override
    public RenderImage reload() {
      RenderImage newImage = renderDevice.createImage(filename, filterLinear);
      if (newImage == null) {
        log.warning("Reloading of image failed! Keeping the old image alive.");
      } else {
        renderImage.dispose();
        renderImage = newImage;
      }
      return renderImage;
    }

    /**
	 * Adds the reference.
	 *
	 * @return the render image
	 */
    @Nonnull
    @Override
    public RenderImage addReference() {
      references++;
      return renderImage;
    }

    /**
	 * Removes the reference.
	 *
	 * @return true, if successful
	 */
    @Override
    public boolean removeReference() {
      references--;
      if (references == 0) {
        renderImage.dispose();
        return true;
      }
      return false;
    }

    /**
	 * Gets the references.
	 *
	 * @return the references
	 */
    @Override
    public int getReferences() {
      return references;
    }

    /**
	 * Gets the render image.
	 *
	 * @return the render image
	 */
    @Nonnull
    @Override
    public RenderImage getRenderImage() {
      return renderImage;
    }

    /**
	 * Gets the name.
	 *
	 * @return the name
	 */
    @Nonnull
    @Override
    public String getName() {
      return key;
    }

    /**
	 * Gets the screen.
	 *
	 * @return the screen
	 */
    @Nonnull
    @Override
    public Screen getScreen() {
      return screen;
    }

    /**
	 * To string.
	 *
	 * @return the string
	 */
    @Nonnull
    @Override
    public String toString() {
      return " - [" + getName() + "] reference count [" + getReferences() + "]\n";
    }
  }
}
