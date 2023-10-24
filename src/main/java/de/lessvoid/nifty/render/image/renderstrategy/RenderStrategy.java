package de.lessvoid.nifty.render.image.renderstrategy;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Parameterizable;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * The Interface RenderStrategy.
 */
public interface RenderStrategy extends Parameterizable {
  
  /**
	 * Render.
	 *
	 * @param renderDevice the render device
	 * @param image        the image
	 * @param sourceArea   the source area
	 * @param x            the x
	 * @param y            the y
	 * @param width        the width
	 * @param height       the height
	 * @param color        the color
	 * @param scale        the scale
	 */
  void render(
      @Nonnull RenderDevice renderDevice,
      @Nonnull RenderImage image,
      @Nonnull Box sourceArea,
      int x,
      int y,
      int width,
      int height,
      @Nonnull Color color,
      float scale);
}
