package de.lessvoid.nifty.render.image.renderstrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * The Class RenderDirectlyStrategy.
 */
public class RenderDirectlyStrategy implements RenderStrategy {

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
  @Override
  public void render(
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final RenderImage image,
      @Nonnull final Box sourceArea,
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color color,
      final float scale) {
    final int centerX = x + width / 2;
    final int centerY = y + height / 2;

    renderDevice.renderImage(
        image,
        sourceArea.getX() + x, sourceArea.getY() + y, sourceArea.getWidth(), sourceArea.getHeight(),
        sourceArea.getX(), sourceArea.getY(), sourceArea.getWidth(), sourceArea.getHeight(),
        color, scale, centerX, centerY);
  }
}
