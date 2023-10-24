package de.lessvoid.nifty.render.image.renderstrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * The Class RepeatStrategy.
 */
public class RepeatStrategy implements RenderStrategy {

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
      @Nonnull RenderDevice renderDevice, @Nonnull RenderImage image, @Nonnull Box sourceArea, int x, int y, int width,
      int height, @Nonnull Color color, float scale) {
    int centerX = x + width / 2;
    int centerY = y + height / 2;

    int endX = x + width;
    int endY = y + height;

    int tileY = y;
    while (tileY < endY) {
      int tileHeight = Math.min(sourceArea.getHeight(), endY - tileY);

      int tileX = x;
      while (tileX < endX) {
        int tileWidth = Math.min(sourceArea.getWidth(), endX - tileX);

        renderDevice.renderImage(image, tileX, tileY, tileWidth, tileHeight, sourceArea.getX(),
            sourceArea.getY(), tileWidth, tileHeight, color, scale, centerX, centerY);

        tileX += tileWidth;
      }

      tileY += tileHeight;
    }
  }
}
