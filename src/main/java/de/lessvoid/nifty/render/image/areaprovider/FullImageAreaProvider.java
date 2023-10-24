package de.lessvoid.nifty.render.image.areaprovider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The Class FullImageAreaProvider.
 */
public class FullImageAreaProvider implements AreaProvider {

  /**
	 * Sets the parameters.
	 *
	 * @param parameters the new parameters
	 */
  @Override
  public void setParameters(@Nullable String parameters) {
    if (parameters != null) {
      throw new IllegalArgumentException("Trying to parse [" + this.getClass().getName()
          + "] : expected no parameters, found [" + parameters + "].");
    }
  }

  /**
	 * Gets the source area.
	 *
	 * @param renderImage the render image
	 * @return the source area
	 */
  @Nonnull
  @Override
  public Box getSourceArea(@Nonnull RenderImage renderImage) {
    return new Box(0, 0, renderImage.getWidth(), renderImage.getHeight());
  }

  /**
	 * Gets the native size.
	 *
	 * @param image the image
	 * @return the native size
	 */
  @Nonnull
  @Override
  public Size getNativeSize(@Nonnull NiftyImage image) {
    return new Size(image.getWidth(), image.getHeight());
  }
}
