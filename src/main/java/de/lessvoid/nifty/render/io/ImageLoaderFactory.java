package de.lessvoid.nifty.render.io;

import javax.annotation.Nonnull;

/**
 * A factory for creating ImageLoader objects.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class ImageLoaderFactory {
  
  /**
	 * Creates a new ImageLoader object.
	 *
	 * @param imageFilename the image filename
	 * @return the image loader
	 */
  public static ImageLoader createImageLoader(@Nonnull final String imageFilename) {
    return imageFilename.endsWith(".tga") ? new TGAImageLoader() : new DefaultImageLoader();
  }
}
