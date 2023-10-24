package de.lessvoid.nifty.effects.impl;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * The Class AutoScroll.
 */
public class AutoScroll implements EffectImpl {
  
  /** The distance. */
  private float distance = 100;
  
  /** The start. */
  private float start = 0;

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
    int startValue = Integer.parseInt(parameter.getProperty("start", "0"));
    int endValue = Integer.parseInt(parameter.getProperty("end", "0"));
    distance = endValue - startValue;
    start = startValue;
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

    r.moveTo(0, start + normalizedTime * distance);
  }

  /**
	 * Deactivate.
	 */
  @Override
  public void deactivate() {
  }
}
