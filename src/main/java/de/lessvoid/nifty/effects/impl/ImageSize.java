package de.lessvoid.nifty.effects.impl;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.LinearInterpolator;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * ImageSize effect.
 *
 * @author void
 */
public class ImageSize implements EffectImpl {

  /** The start size. */
  private float startSize;
  
  /** The end size. */
  private float endSize;
  
  /** The image size. */
  @Nonnull
  private SizeValue imageSize = new SizeValue("100%");
  
  /** The interpolator. */
  @Nullable
  private LinearInterpolator interpolator;

  /**
	 * Activate.
	 *
	 * @param nifty     the nifty
	 * @param element   the element
	 * @param parameter the parameter
	 */
  @Override
  public final void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    // for normal mode
    startSize = Float.parseFloat(parameter.getProperty("startSize", "1.0"));
    endSize = Float.parseFloat(parameter.getProperty("endSize", "2.0"));

    // for hover mode only
    String maxSizeString = parameter.getProperty("maxSize");
    if (maxSizeString != null) {
      imageSize = new SizeValue(maxSizeString);
    }
    interpolator = parameter.getInterpolator();
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
    float scale;
    if (falloff == null) {
      if (interpolator != null) {
        scale = interpolator.getValue(normalizedTime);
      } else {
        scale = startSize + normalizedTime * (endSize - startSize);
      }
    } else {
      scale = 1.0f + falloff.getFalloffValue() * imageSize.getValue(1.0f);
    }
    r.setImageScale(scale);
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
