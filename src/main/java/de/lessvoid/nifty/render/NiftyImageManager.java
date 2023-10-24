package de.lessvoid.nifty.render;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.NiftyStopwatch;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The Class NiftyImageManager.
 */
public class NiftyImageManager {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(NiftyImageManager.class.getName());
  
  /** The render device. */
  @Nonnull
  private final RenderDevice renderDevice;
  
  /** The image cache. */
  @Nonnull
  private final Map<String, ReferencedCountedImage> imageCache = new HashMap<String, ReferencedCountedImage>();
  
  /** The back reference. */
  @Nonnull
  private final Map<RenderImage, ReferencedCountedImage> backReference = new HashMap<RenderImage,
      ReferencedCountedImage>();
  
  /** The ext. */
  @Nonnull
  private final NiftyImageManagerExt<ReferencedCountedImage> ext;

  /**
	 * Instantiates a new nifty image manager.
	 *
	 * @param renderDevice the render device
	 */
  public NiftyImageManager(@Nonnull final RenderDevice renderDevice) {
    this.renderDevice = renderDevice;
    this.ext = getExtImpl(renderDevice);
  }

  /**
	 * Register image.
	 *
	 * @param filename     the filename
	 * @param filterLinear the filter linear
	 * @param screen       the screen
	 * @return the render image
	 */
  @Nullable
  public RenderImage registerImage(
      @Nonnull final String filename,
      final boolean filterLinear,
      @Nonnull final Screen screen) {
    ReferencedCountedImage image = addImage(filename, filterLinear, screen);
    if (image == null) {
      return null;
    }
    ext.registerImage(screen, image);
    return image.getRenderImage();
  }

  /**
	 * Unregister image.
	 *
	 * @param image the image
	 */
  public void unregisterImage(@Nonnull final RenderImage image) {
    if (backReference.containsKey(image)) {
      ReferencedCountedImage reference = backReference.get(image);
      if (removeImage(reference)) {
        ext.unregisterImage(reference);
      }
    }
  }

  /**
	 * Upload screen images.
	 *
	 * @param screen the screen
	 */
  public void uploadScreenImages(@Nonnull final Screen screen) {
    log.fine(">>> uploadScreenImages [" + screen.getScreenId() + "] start");
    NiftyStopwatch.start();

    ext.uploadScreenImages(screen);

    long time = NiftyStopwatch.stop();
    if (log.isLoggable(Level.FINE)) {
      log.fine("{" + String.format("%d", time) + " ms} <<< uploadScreenImages [" + screen.getScreenId() + "]");
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("{" + String.format("%d", time) + " ms} <<< uploadScreenImages [" + screen.getScreenId() + "] " +
          getInfoString());
    }
  }

  /**
	 * Unload screen images.
	 *
	 * @param screen the screen
	 */
  public void unloadScreenImages(@Nonnull final Screen screen) {
    log.fine(">>> unloadScreenImages [" + screen.getScreenId() + "] start");
    NiftyStopwatch.start();

    ext.unloadScreenImages(screen, renderDevice, imageCache.values());

    long time = NiftyStopwatch.stop();
    if (log.isLoggable(Level.FINE)) {
      log.fine("{" + String.format("%d", time) + " ms} <<< unloadScreenImages [" + screen.getScreenId() + "]");
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("{" + String.format("%d", time) + " ms} <<< unloadScreenImages [" + screen.getScreenId() + "] " +
          getInfoString());
    }
  }

  /**
	 * Screen added.
	 *
	 * @param screen the screen
	 */
  public void screenAdded(@Nonnull final Screen screen) {
    ext.screenAdded(screen);

    if (log.isLoggable(Level.FINER)) {
      log.finer("screenAdded [" + screen.getScreenId() + "] " + getInfoString());
    }
  }

  /**
	 * Screen removed.
	 *
	 * @param screen the screen
	 */
  public void screenRemoved(@Nonnull final Screen screen) {
    ext.screenRemoved(screen);

    if (log.isLoggable(Level.FINER)) {
      log.finer("screenRemoved [" + screen.getScreenId() + "] " + getInfoString());
    }
  }

  /**
	 * Reload.
	 *
	 * @param image the image
	 * @return the render image
	 */
  @Nonnull
  public RenderImage reload(@Nonnull final RenderImage image) {
    if (backReference.containsKey(image)) {
      return backReference.get(image).reload();
    }
    return image;
  }

  /**
	 * Gets the info string.
	 *
	 * @return the info string
	 */
  @Nonnull
  public String getInfoString() {
    StringBuffer result = new StringBuffer();
    result.append(imageCache.size()).append(" entries in cache and ").append(backReference.size())
        .append(" backreference entries.");
    ext.addScreenInfo(result);
    return result.toString();
  }

  /**
	 * Gets the ext impl.
	 *
	 * @param renderer the renderer
	 * @return the ext impl
	 */
  @Nonnull
  private NiftyImageManagerExt<ReferencedCountedImage> getExtImpl(final RenderDevice renderer) {
    if (renderer instanceof BatchRenderDevice) {
      return new NiftyImageManagerExtBatch();
    }
    return new NiftyImageManagerExtStandard();
  }

  /**
	 * Builds the name.
	 *
	 * @param filename     the filename
	 * @param filterLinear the filter linear
	 * @return the string
	 */
  @Nonnull
  private static String buildName(@Nonnull final String filename, final boolean filterLinear) {
    return filename + "|" + Boolean.toString(filterLinear);
  }

  /**
	 * Adds the image.
	 *
	 * @param filename     the filename
	 * @param filterLinear the filter linear
	 * @param screen       the screen
	 * @return the referenced counted image
	 */
  @Nullable
  private ReferencedCountedImage addImage(
      @Nonnull final String filename,
      final boolean filterLinear,
      @Nonnull final Screen screen) {
    String key = buildName(filename, filterLinear);
    if (imageCache.containsKey(key)) {
      ReferencedCountedImage existingImage = imageCache.get(key);
      existingImage.addReference();
      if (log.isLoggable(Level.FINER)) {
        log.finer("[" + screen.getScreenId() + "][" + key + "] refcount++ [" + existingImage.getReferences() + "]");
      }
      return existingImage;
    }
    NiftyStopwatch.start();

    RenderImage renderImage = renderDevice.createImage(filename, filterLinear);
    if (renderImage == null) {
      return null;
    }
    ReferencedCountedImage newImage =
        ext.createReferencedCountedImage(renderDevice, screen, filename, filterLinear, renderImage, key);
    backReference.put(renderImage, newImage);
    imageCache.put(key, newImage);

    NiftyStopwatch.stop("imageManager.getImage(" + filename + ")");
    return newImage;
  }

  /**
	 * Removes the image.
	 *
	 * @param reference the reference
	 * @return true, if successful
	 */
  private boolean removeImage(@Nonnull final ReferencedCountedImage reference) {
    Screen screen = reference.getScreen();
    if (reference.removeReference()) {
      imageCache.remove(reference.getName());
      backReference.remove(reference.getRenderImage());
      if (log.isLoggable(Level.FINER)) {
        log.finer("[" + (screen.getScreenId()) + "][" + reference.getName() + "] refcount-- [" + reference
            .getReferences() + "] => DISPOSED");
      }
      return true;
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("[" + (screen.getScreenId()) + "][" + reference.getName() + "] refcount-- [" + reference
          .getReferences() + "]");
    }
    return false;
  }

  /**
	 * The Interface ReferencedCountedImage.
	 */
  public interface ReferencedCountedImage {
    
    /**
	 * Reload.
	 *
	 * @return the render image
	 */
    @Nonnull
    RenderImage reload();

    /**
	 * Adds the reference.
	 *
	 * @return the render image
	 */
    @Nonnull
    RenderImage addReference();

    /**
	 * Removes the reference.
	 *
	 * @return true, if successful
	 */
    boolean removeReference();

    /**
	 * Gets the references.
	 *
	 * @return the references
	 */
    int getReferences();

    /**
	 * Gets the render image.
	 *
	 * @return the render image
	 */
    @Nonnull
    RenderImage getRenderImage();

    /**
	 * Gets the name.
	 *
	 * @return the name
	 */
    @Nonnull
    String getName();

    /**
	 * Gets the screen.
	 *
	 * @return the screen
	 */
    @Nonnull
    Screen getScreen();
  }
}
