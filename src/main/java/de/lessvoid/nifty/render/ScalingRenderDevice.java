package de.lessvoid.nifty.render;

import java.io.IOException;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * The Class ScalingRenderDevice.
 */
public class ScalingRenderDevice implements RenderDevice {
  
  /** The render engine. */
  private final NiftyRenderEngine renderEngine;
  
  /** The internal. */
  private final RenderDevice internal;

  /**
	 * Instantiates a new scaling render device.
	 *
	 * @param renderEngine the render engine
	 * @param interal      the interal
	 */
  public ScalingRenderDevice(final NiftyRenderEngine renderEngine, final RenderDevice interal) {
    this.renderEngine = renderEngine;
    this.internal = interal;
  }

  /**
	 * Sets the resource loader.
	 *
	 * @param niftyResourceLoader the new resource loader
	 */
  @Override
  public void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader) {
  }

  /**
	 * Creates the image.
	 *
	 * @param filename     the filename
	 * @param filterLinear the filter linear
	 * @return the render image
	 */
  @Override
  public RenderImage createImage(@Nonnull String filename, boolean filterLinear) {
    return internal.createImage(filename, filterLinear);
  }

  /**
	 * Creates the font.
	 *
	 * @param filename the filename
	 * @return the render font
	 */
  @Override
  public RenderFont createFont(@Nonnull String filename) {
    return internal.createFont(filename);
  }

  /**
	 * Gets the width.
	 *
	 * @return the width
	 */
  @Override
  public int getWidth() {
    return internal.getWidth();
  }

  /**
	 * Gets the height.
	 *
	 * @return the height
	 */
  @Override
  public int getHeight() {
    return internal.getHeight();
  }

  /**
	 * Begin frame.
	 */
  @Override
  public void beginFrame() {
    internal.beginFrame();
  }

  /**
	 * End frame.
	 */
  @Override
  public void endFrame() {
    internal.endFrame();
  }

  /**
	 * Clear.
	 */
  @Override
  public void clear() {
    internal.clear();
  }

  /**
	 * Sets the blend mode.
	 *
	 * @param renderMode the new blend mode
	 */
  @Override
  public void setBlendMode(@Nonnull BlendMode renderMode) {
    internal.setBlendMode(renderMode);
  }

  /**
	 * Render quad.
	 *
	 * @param x      the x
	 * @param y      the y
	 * @param width  the width
	 * @param height the height
	 * @param color  the color
	 */
  @Override
  public void renderQuad(int x, int y, int width, int height, @Nonnull Color color) {
    internal.renderQuad(renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y),
        renderEngine.convertToNativeWidth(width), renderEngine.convertToNativeHeight(height), color);
  }

  /**
	 * Render quad.
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
  @Override
  public void renderQuad(
      int x,
      int y,
      int width,
      int height,
      @Nonnull Color topLeft,
      @Nonnull Color topRight,
      @Nonnull Color bottomRight,
      @Nonnull Color bottomLeft) {
    internal.renderQuad(renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y),
        renderEngine.convertToNativeWidth(width), renderEngine.convertToNativeHeight(height), topLeft, topRight,
        bottomRight, bottomLeft);
  }

  /**
	 * Render image.
	 *
	 * @param image      the image
	 * @param x          the x
	 * @param y          the y
	 * @param width      the width
	 * @param height     the height
	 * @param color      the color
	 * @param imageScale the image scale
	 */
  @Override
  public void renderImage(
      @Nonnull RenderImage image,
      int x,
      int y,
      int width,
      int height,
      @Nonnull Color color,
      float imageScale) {
    internal.renderImage(image, renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y),
        renderEngine.convertToNativeWidth(width), renderEngine.convertToNativeHeight(height), color, imageScale);
  }

  /**
	 * Render image.
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
  @Override
  public void renderImage(
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
      int centerY) {
    internal.renderImage(image, renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y),
        renderEngine.convertToNativeWidth(w), renderEngine.convertToNativeHeight(h), srcX, srcY, srcW, srcH, color,
        scale, renderEngine.convertToNativeX(centerX), renderEngine.convertToNativeY(centerY));
  }

  /**
	 * Render font.
	 *
	 * @param font      the font
	 * @param text      the text
	 * @param x         the x
	 * @param y         the y
	 * @param fontColor the font color
	 * @param sizeX     the size X
	 * @param sizeY     the size Y
	 */
  @Override
  public void renderFont(
      @Nonnull RenderFont font,
      @Nonnull String text,
      int x,
      int y,
      @Nonnull Color fontColor,
      float sizeX,
      float sizeY) {
    internal.renderFont(font, text, renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y), fontColor,
        renderEngine.convertToNativeTextSizeX(sizeX), renderEngine.convertToNativeTextSizeY(sizeY));
  }

  /**
	 * Enable clip.
	 *
	 * @param x0 the x 0
	 * @param y0 the y 0
	 * @param x1 the x 1
	 * @param y1 the y 1
	 */
  @Override
  public void enableClip(int x0, int y0, int x1, int y1) {
    internal.enableClip(renderEngine.convertToNativeX(x0), renderEngine.convertToNativeY(y0),
        renderEngine.convertToNativeX(x1), renderEngine.convertToNativeY(y1));
  }

  /**
	 * Disable clip.
	 */
  @Override
  public void disableClip() {
    internal.disableClip();
  }

  /**
	 * Creates the mouse cursor.
	 *
	 * @param filename the filename
	 * @param hotspotX the hotspot X
	 * @param hotspotY the hotspot Y
	 * @return the mouse cursor
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
  @Override
  public MouseCursor createMouseCursor(@Nonnull String filename, int hotspotX, int hotspotY) throws IOException {
    return internal.createMouseCursor(filename, hotspotX, hotspotY);
  }

  /**
	 * Enable mouse cursor.
	 *
	 * @param mouseCursor the mouse cursor
	 */
  @Override
  public void enableMouseCursor(@Nonnull MouseCursor mouseCursor) {
    internal.enableMouseCursor(mouseCursor);
  }

  /**
	 * Disable mouse cursor.
	 */
  @Override
  public void disableMouseCursor() {
    internal.disableMouseCursor();
  }
}
