package de.lessvoid.nifty.effects.impl;


import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.render.image.ImageModeHelper;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Alpha;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * The Class ImageOverlay.
 */
public class ImageOverlay implements EffectImpl {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(ImageOverlay.class.getName());
  
  /** The image. */
  @Nullable
  private NiftyImage image;
  
  /** The alpha. */
  @Nullable
  private Alpha alpha;
  
  /** The inset. */
  @Nullable
  private SizeValue inset;
  
  /** The width. */
  @Nullable
  private SizeValue width;
  
  /** The height. */
  @Nullable
  private SizeValue height;
  
  /** The center. */
  private boolean center;
  
  /** The hide if not enough space. */
  private boolean hideIfNotEnoughSpace;
  
  /** The active before start delay. */
  private boolean activeBeforeStartDelay; // this will render the effect even when using a startDelay value so that
  // it will already render before the startDelay

  /**
	 * Activate.
	 *
	 * @param nifty     the nifty
	 * @param element   the element
	 * @param parameter the parameter
	 */
  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    Screen screen = nifty.getCurrentScreen();
    if (screen == null) {
      log.severe("Can't activate effect while there is no active current screen.");
      return;
    }
    image = nifty.getRenderEngine().createImage(screen, parameter.getProperty("filename"), false);
    if (image == null) {
      log.severe("Image not found, image overlay effect will not work.");
      return;
    }

    String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(parameter);
    String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(parameter);
    if ((areaProviderProperty != null) || (renderStrategyProperty != null)) {
      image.setImageMode(ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty,
          renderStrategyProperty));
    }

    alpha = new Alpha(parameter.getProperty("alpha", "#f"));
    inset = new SizeValue(parameter.getProperty("inset", "0px"));
    width = new SizeValue(parameter.getProperty("width", element.getWidth() + "px"));
    height = new SizeValue(parameter.getProperty("height", element.getHeight() + "px"));
    center = Boolean.valueOf(parameter.getProperty("center", "false"));
    hideIfNotEnoughSpace = Boolean.valueOf(parameter.getProperty("hideIfNotEnoughSpace", "false"));
    activeBeforeStartDelay = Boolean.valueOf(parameter.getProperty("activeBeforeStartDelay", "false"));
  }

  /**
	 * Execute.
	 *
	 * @param element        the element
	 * @param normalizedTime the normalized time
	 * @param falloff        the falloff
	 * @param r              the r
	 */
  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (image == null || inset == null || width == null || height == null || alpha == null) {
      return;
    }
    if (!activeBeforeStartDelay && normalizedTime <= 0.0) {
      return;
    }
    int insetOffset = inset.getValueAsInt(element.getWidth());
    int imageX = element.getX() + insetOffset;
    int imageY = element.getY() + insetOffset;
    int imageWidth = width.getValueAsInt(element.getWidth()) - insetOffset * 2;
    int imageHeight = height.getValueAsInt(element.getHeight()) - insetOffset * 2;
    if (hideIfNotEnoughSpace) {
      if (imageWidth > element.getWidth() || imageHeight > element.getHeight()) {
        return;
      }
    }
    r.saveStates();
    if (falloff != null) {
      r.setColorAlpha(alpha.multiply(falloff.getFalloffValue()).getAlpha());
    } else {
      if (!r.isColorAlphaChanged()) {
        r.setColorAlpha(alpha.getAlpha());
      }
    }
    if (center) {
      r.renderImage(image, element.getX() + (element.getWidth() - imageWidth) / 2,
          element.getY() + (element.getHeight() - imageHeight) / 2, imageWidth, imageHeight);
    } else {
      r.renderImage(image, imageX, imageY, imageWidth, imageHeight);
    }
    r.restoreStates();
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
    if (image != null) {
      image.dispose();
    }
  }
}
