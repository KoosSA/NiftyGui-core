package de.lessvoid.nifty.spi.render;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * Nifty RenderDevice.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface RenderDevice {
  /**
   * Gives this RenderDevice access to the NiftyResourceLoader.
   *
   * @param niftyResourceLoader NiftyResourceLoader
   */
  void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader);

  /**
   * Create a new image for the renderer. This method is only called if the image is not already cached. The
   * implementation calling this method will take care for caching already loaded images as needed.
   *
   * @param filename     the filename of the image that needs to be load
   * @param filterLinear {@code true} in case a linear (and slower) rescaling filter is supposed to be applied
   * @return the created render image or {@code null} in case the image assigned to the filename is not found
   */
  @Nullable
  RenderImage createImage(@Nonnull String filename, boolean filterLinear);

  /**
   * Create a new font for the renderer. This method is only called if the font was not load before. Created fonts
   * will be cached by the implementation calling this method.
   *
   * @param filename the filename assigned to the font that needs to be load
   * @return the created render font or {@code null} in case no font was found using this name
   */
  @Nullable
  RenderFont createFont(@Nonnull String filename);

  /**
   * Get Width.
   *
   * @return width of display mode
   */
  int getWidth();

  /**
   * Get Height.
   *
   * @return height of display mode
   */
  int getHeight();

  /**
   * Called every begin frame.
   */
  void beginFrame();

  /**
   * Called every end frame.
   */
  void endFrame();

  /**
   * clear screen.
   */
  void clear();

  /**
   * Change the RenderMode to the given Mode.
   *
   * @param renderMode RenderMode
   */
  void setBlendMode(@Nonnull BlendMode renderMode);

  /**
	 * Render a quad.
	 *
	 * @param x      the x
	 * @param y      the y
	 * @param width  the width
	 * @param height the height
	 * @param color  the color
	 */
  void renderQuad(int x, int y, int width, int height, @Nonnull Color color);

  /**
	 * Render a quad with different colors at the vertices.
	 *
	 * @param x           the x
	 * @param y           the y
	 * @param width       the width
	 * @param height      the height
	 * @param topLeft     the top left
	 * @param topRight    the top right
	 * @param bottomRight the bottom right
	 * @param bottomLeft  the bottom left
	 */
  void renderQuad(
      int x,
      int y,
      int width,
      int height,
      @Nonnull Color topLeft,
      @Nonnull Color topRight,
      @Nonnull Color bottomRight,
      @Nonnull Color bottomLeft);

  /**
	 * Render the image.
	 *
	 * @param image      the image
	 * @param x          the x
	 * @param y          the y
	 * @param width      the width
	 * @param height     the height
	 * @param color      the color
	 * @param imageScale the image scale
	 */
  void renderImage(
      @Nonnull RenderImage image,
      int x,
      int y,
      int width,
      int height,
      @Nonnull Color color,
      float imageScale);

  /**
	 * Render a sub image of this image.
	 *
	 * @param image   the image
	 * @param x       the x
	 * @param y       the y
	 * @param w       the w
	 * @param h       the h
	 * @param srcX    the src X
	 * @param srcY    the src Y
	 * @param srcW    the src W
	 * @param srcH    the src H
	 * @param color   the color
	 * @param scale   the scale
	 * @param centerX the center X
	 * @param centerY the center Y
	 */
  void renderImage(
      @Nonnull RenderImage image,
      int x,
      int y,
      int w,
      int h,
      int srcX,
      int srcY,
      int srcW,
      int srcH,
      @Nonnull Color color,
      float scale,
      int centerX,
      int centerY);

  /**
	 * Render the given text at the given position.
	 *
	 * @param font      the font
	 * @param text      the text
	 * @param x         the x
	 * @param y         the y
	 * @param fontColor the font color
	 * @param sizeX     the size X
	 * @param sizeY     the size Y
	 */
  void renderFont(
      @Nonnull RenderFont font,
      @Nonnull String text,
      int x,
      int y,
      @Nonnull Color fontColor,
      float sizeX,
      float sizeY);

  /**
	 * Enable clipping to the given region.
	 *
	 * @param x0 the x 0
	 * @param y0 the y 0
	 * @param x1 the x 1
	 * @param y1 the y 1
	 */
  void enableClip(int x0, int y0, int x1, int y1);

  /**
   * Disable Clipping.
   */
  void disableClip();

  /**
	 * Create a new mouse cursor. This method is called by a implementation that
	 * takes care for caching the result of this call.
	 *
	 * @param filename the filename assigned to the mouse cursor that needs to be
	 *                 load
	 * @param hotspotX hotspot x with 0 being left of the screen
	 * @param hotspotY hotspot y with 0 being top of the screen
	 * @return the newly created mouse cursor or {@code null} in case there is no
	 *         matching cursor for this file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Nullable
  MouseCursor createMouseCursor(@Nonnull String filename, int hotspotX, int hotspotY) throws IOException;

  /**
   * Enable the given mouse cursor.
   *
   * @param mouseCursor the mouse cursor to enable
   */
  void enableMouseCursor(@Nonnull MouseCursor mouseCursor);

  /**
   * Disable the current mouse cursor.
   */
  void disableMouseCursor();
}
