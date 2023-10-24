package de.lessvoid.nifty.render.image.areaprovider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Parameterizable;
import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The Interface AreaProvider.
 */
public interface AreaProvider extends Parameterizable {
  
  /**
	 * Gets the source area.
	 *
	 * @param renderImage the render image
	 * @return the source area
	 */
  @Nullable
  Box getSourceArea(@Nonnull RenderImage renderImage);

  /**
	 * Gets the native size.
	 *
	 * @param image the image
	 * @return the native size
	 */
  @Nonnull
  Size getNativeSize(@Nonnull NiftyImage image);
}
