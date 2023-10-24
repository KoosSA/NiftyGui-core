package de.lessvoid.nifty.effects.impl;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.image.ImageMode;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.render.image.ImageModeHelper;

/**
 * This can be applied to an image element. This will change the original image of the
 * element to the image given in the "active" attribute. When the effect gets deactivated
 * the image is being restored to the image given with the "inactive" attribute.
 *
 * @author void
 */
public class ChangeImage implements EffectImpl {
  
  /** The Constant log. */
  @Nonnull
  private static final Logger log = Logger.getLogger(ChangeImage.class.getName());
  
  /** The element. */
  @Nonnull
  private Element element;
  
  /** The active image. */
  @Nullable
  private NiftyImage activeImage;
  
  /** The inactive image. */
  @Nullable
  private NiftyImage inactiveImage;

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
    this.element = element;
    this.activeImage = loadImage("active", nifty, parameter);
    this.inactiveImage = loadImage("inactive", nifty, parameter);
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
    changeElementImage(activeImage);
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
    changeElementImage(inactiveImage);
    if (activeImage != null) {
      activeImage.dispose();
    }
    if (inactiveImage != null) {
      inactiveImage.dispose();
    }
  }

  /**
	 * Load image.
	 *
	 * @param name      the name
	 * @param nifty     the nifty
	 * @param parameter the parameter
	 * @return the nifty image
	 */
  @Nullable
  private NiftyImage loadImage(
      @Nonnull final String name,
      @Nonnull final Nifty nifty,
      @Nonnull final EffectProperties parameter) {
    NiftyImage image = createImage(name, nifty, parameter);
    if (image == null) {
      return null;
    }
    setImageMode(image, name, parameter);
    return image;
  }

  /**
	 * Creates the image.
	 *
	 * @param name      the name
	 * @param nifty     the nifty
	 * @param parameter the parameter
	 * @return the nifty image
	 */
  @Nullable
  private NiftyImage createImage(
      @Nonnull final String name,
      @Nonnull final Nifty nifty,
      @Nonnull final EffectProperties parameter) {
    return nifty.createImage(parameter.getProperty(name), false);
  }

  /**
	 * Sets the image mode.
	 *
	 * @param image     the image
	 * @param name      the name
	 * @param parameter the parameter
	 */
  private void setImageMode(
      @Nonnull final NiftyImage image,
      @Nonnull final String name,
      @Nonnull final EffectProperties parameter) {
    String areaProviderProperty = getAreaProviderProperty(name, parameter);
    String renderStrategyProperty = getRenderStrategyProperty(name, parameter);

    if ((areaProviderProperty != null) || (renderStrategyProperty != null)) {
      image.setImageMode(createImageMode(areaProviderProperty, renderStrategyProperty));
    }
  }

  /**
	 * Gets the area provider property.
	 *
	 * @param name      the name
	 * @param parameter the parameter
	 * @return the area provider property
	 */
  @Nullable
  private String getAreaProviderProperty(@Nonnull final String name, @Nonnull final EffectProperties parameter) {
    return ImageModeHelper.getAreaProviderProperty(getImageModeProperty(name, parameter));
  }

  /**
	 * Gets the render strategy property.
	 *
	 * @param name      the name
	 * @param parameter the parameter
	 * @return the render strategy property
	 */
  @Nullable
  private String getRenderStrategyProperty(@Nonnull final String name, @Nonnull final EffectProperties parameter) {
    return ImageModeHelper.getRenderStrategyProperty(getImageModeProperty(name, parameter));
  }

  /**
	 * Gets the image mode property.
	 *
	 * @param name      the name
	 * @param parameter the parameter
	 * @return the image mode property
	 */
  @Nullable
  private String getImageModeProperty(@Nonnull final String name, @Nonnull final EffectProperties parameter) {
    String imageModeProperty = null;

    if ("active".equals(name)) {
      imageModeProperty = parameter.getProperty("imageModeActive", null);
    } else if ("inactive".equals(name)) {
      imageModeProperty = parameter.getProperty("imageModeInactive", null);
    }

    if (imageModeProperty == null) {
      imageModeProperty = parameter.getProperty("imageMode", null);
    }

    return imageModeProperty;
  }

  /**
	 * Creates the image mode.
	 *
	 * @param areaProviderProperty   the area provider property
	 * @param renderStrategyProperty the render strategy property
	 * @return the image mode
	 */
  @Nonnull
  private ImageMode createImageMode(
      @Nullable final String areaProviderProperty,
      @Nullable final String renderStrategyProperty) {
    return ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty);
  }

  /**
	 * Change element image.
	 *
	 * @param image the image
	 */
  private void changeElementImage(@Nullable final NiftyImage image) {
    ImageRenderer imageRenderer = element.getRenderer(ImageRenderer.class);
    if (imageRenderer == null) {
      log.warning("this effect can only be applied to images!");
      return;
    }
    imageRenderer.setImage(image);
  }
}
