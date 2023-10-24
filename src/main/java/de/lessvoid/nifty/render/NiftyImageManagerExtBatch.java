package de.lessvoid.nifty.render;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.render.NiftyImageManager.ReferencedCountedImage;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.render.batch.BatchRenderImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The Class NiftyImageManagerExtBatch.
 */
public class NiftyImageManagerExtBatch implements NiftyImageManagerExt<ReferencedCountedImage> {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(NiftyImageManagerExtBatch.class.getName());

  /** The screen ref. */
  @Nonnull
  private final Map<String, Set<ReferencedCountedImageBatch>> screenRef = new HashMap<String,
      Set<ReferencedCountedImageBatch>>();
  
  /** The current screen. */
  @Nullable
  private Screen currentScreen;

  /**
	 * Register image.
	 *
	 * @param screen the screen
	 * @param image  the image
	 */
  @Override
  public void registerImage(@Nonnull final Screen screen, @Nonnull final ReferencedCountedImage image) {
    Set<ReferencedCountedImageBatch> screenList = screenRef.get(screen.getScreenId());
    if (screenList == null) {
      screenList = new HashSet<ReferencedCountedImageBatch>();
      screenRef.put(screen.getScreenId(), screenList);
    }
    final ReferencedCountedImageBatch batchImage = cast(image);
    if (screenList.add(batchImage)) {
      if (log.isLoggable(Level.FINER)) {
        log.finer("[" + screen.getScreenId() + "] now with [" + screenList.size() + "] entries (" + image.getName() +
            ")");
      }
    }
    if (currentScreen != null && currentScreen.getScreenId().equals(screen.getScreenId())) {
      if (!batchImage.isUploaded()) {
        batchImage.upload();
      }
    }
  }

  /**
	 * Unregister image.
	 *
	 * @param reference the reference
	 */
  @Override
  public void unregisterImage(@Nonnull final ReferencedCountedImage reference) {
    final ReferencedCountedImageBatch image = cast(reference);
    image.unload();

    Set<ReferencedCountedImageBatch> screenList = screenRef.get(reference.getScreen().getScreenId());
    if (screenList != null) {
      screenList.remove(image);
    }
  }

  /**
	 * Upload screen images.
	 *
	 * @param screen the screen
	 */
  @Override
  public void uploadScreenImages(@Nonnull final Screen screen) {
    currentScreen = screen;

    // find all ReferencedCountedImage and upload them into the texture atlas (for this screen).
    Set<ReferencedCountedImageBatch> imageList = screenRef.get(screen.getScreenId());
    if (imageList == null) {
      return;
    }

    for (ReferencedCountedImageBatch image : imageList) {
      image.upload();
    }
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
    ((BatchRenderDevice) renderDevice).resetTextureAtlases();

    // we need to mark all images as unloaded
    for (ReferencedCountedImage i : imageSet) {
      cast(i).markAsUnloaded();
    }

    currentScreen = null;
  }

  /**
	 * Cast.
	 *
	 * @param image the image
	 * @return the referenced counted image batch
	 */
  @Nonnull
  private static ReferencedCountedImageBatch cast(@Nonnull final ReferencedCountedImage image) {
    if (image instanceof ReferencedCountedImageBatch) {
      return (ReferencedCountedImageBatch) image;
    }
    throw new IllegalArgumentException("Illegal image type supplied: " + image.getClass().getName() + "expected " +
        ReferencedCountedImageBatch.class.getName());
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
    screenRef.remove(screen.getScreenId());
  }

  /**
	 * Adds the screen info.
	 *
	 * @param result the result
	 */
  @Override
  public void addScreenInfo(@Nonnull final StringBuffer result) {
    if (screenRef.entrySet().isEmpty()) {
      return;
    }
    result.append("\n");
    for (Map.Entry<String, Set<ReferencedCountedImageBatch>> entry : screenRef.entrySet()) {
      result.append("\n[").append(entry.getKey()).append("]\n");
      for (ReferencedCountedImageBatch image : entry.getValue()) {
        result.append(image.toString());
      }
    }
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
	 * @return the referenced counted image batch
	 */
  @Nonnull
  @Override
  public ReferencedCountedImageBatch createReferencedCountedImage(
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final Screen screen,
      @Nonnull final String filename,
      final boolean filterLinear,
      @Nonnull final RenderImage renderImage,
      @Nonnull final String key) {
    return new ReferencedCountedImageBatch(renderDevice, screen, filename, filterLinear, renderImage, key);
  }

  /**
   * A standard implementation of a ReferencedCountedImage without Batch support.
   *
   * @author void
   */
  public static class ReferencedCountedImageBatch implements ReferencedCountedImage {
    
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
	 * Instantiates a new referenced counted image batch.
	 *
	 * @param renderDevice the render device
	 * @param screen       the screen
	 * @param filename     the filename
	 * @param filterLinear the filter linear
	 * @param renderImage  the render image
	 * @param key          the key
	 */
    public ReferencedCountedImageBatch(
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
	 * Upload.
	 */
    public void upload() {
      BatchRenderImage batchRenderImage = (BatchRenderImage) renderImage;
      batchRenderImage.upload();
    }

    /**
	 * Unload.
	 */
    public void unload() {
      BatchRenderImage batchRenderImage = (BatchRenderImage) renderImage;
      batchRenderImage.unload();
    }

    /**
	 * Mark as unloaded.
	 */
    public void markAsUnloaded() {
      BatchRenderImage batchRenderImage = (BatchRenderImage) renderImage;
      batchRenderImage.markAsUnloaded();
    }

    /**
	 * Reload.
	 *
	 * @return the render image
	 */
    @Nonnull
    @Override
    public RenderImage reload() {
      final RenderImage newImage = renderDevice.createImage(filename, filterLinear);
      if (newImage == null) {
        log.severe("Failed to reload image, reloading canceled.");
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
	 * Checks if is uploaded.
	 *
	 * @return true, if is uploaded
	 */
    public boolean isUploaded() {
      if (renderImage instanceof BatchRenderImage) {
        return ((BatchRenderImage) renderImage).isUploaded();
      }
      return false;
    }

    /**
	 * To string.
	 *
	 * @return the string
	 */
    @Nonnull
    @Override
    public String toString() {
      return " - [" + getName() + "] reference count [" + getReferences() + "] uploaded [" + isUploaded() + "]\n";
    }
  }
}
