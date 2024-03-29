package de.lessvoid.nifty.effects.impl;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

/**
 * ImageSizePulsate.
 *
 * @author void
 */
public class ImageSizePulsate implements EffectImpl {

  /**
   * start size of image.
   */
  @Nonnull
  private SizeValue startSize = new SizeValue("0%");

  /**
   * end size of image.
   */
  @Nonnull
  private SizeValue endSize = new SizeValue("100%");

  /**
   * Pulsator to use.
   */
  private Pulsator pulsator;

  /**
   * flag to remember if this effect is activated or not. this is used
   * to reset the pulsator.
   */
  private boolean activated = false;

  /**
   * initialize.
   *
   * @param nifty     Nifty
   * @param element   Element
   * @param parameter Parameter
   */
  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    String startSizeString = parameter.getProperty("startSize");
    if (startSizeString != null) {
      startSize = new SizeValue(startSizeString);
    }

    String endSizeString = parameter.getProperty("endSize");
    if (endSizeString != null) {
      endSize = new SizeValue(endSizeString);
    }
    pulsator = new Pulsator(parameter, nifty.getTimeProvider());
  }

  /**
	 * execute the effect.
	 *
	 * @param element        the Element
	 * @param normalizedTime TimeInterpolator to use
	 * @param falloff        the falloff
	 * @param r              RenderDevice to use
	 */
  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (!activated && normalizedTime > 0.0f) {
      activated = true;
      pulsator.reset();
    }

    if (activated) {
      float value = pulsator.update();
      float size = startSize.getValue(1.0f) + value * (endSize.getValue(1.0f) - startSize.getValue(1.0f));
      r.setImageScale(size);
    }
  }

  /**
   * deactivate the effect.
   */
  @Override
  public void deactivate() {
    activated = true;
  }
}
